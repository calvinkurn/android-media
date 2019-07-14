package com.tokopedia.discovery.autocomplete.presentation.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.common.utils.RequestPermissionUtil;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.autocomplete.presentation.AutoCompleteContract;
import com.tokopedia.discovery.autocomplete.presentation.presenter.AutoCompletePresenter;
import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking;
import com.tokopedia.discovery.newdiscovery.base.DiscoveryActivity;
import com.tokopedia.discovery.newdiscovery.constant.SearchEventTracking;
import com.tokopedia.discovery.newdiscovery.di.component.DaggerSearchComponent;
import com.tokopedia.discovery.newdiscovery.di.component.SearchComponent;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;
import com.tokopedia.discovery.util.AnimationUtil;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.permissionchecker.PermissionCheckerHelper;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.track.TrackApp;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import static com.tokopedia.discovery.common.constants.SearchConstant.DEEP_LINK_URI;
import static com.tokopedia.discovery.common.constants.SearchConstant.EXTRA_SEARCH_PARAMETER_MODEL;
import static com.tokopedia.discovery.common.constants.SearchConstant.FROM_APP_SHORTCUTS;

public class AutoCompleteActivity extends DiscoveryActivity
        implements AutoCompleteContract.View {

    public static Intent newInstance(Context context) {
        return new Intent(context, AutoCompleteActivity.class);
    }

    @DeepLink(ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE)
    public static Intent getCallingApplinkAutoCompleteSearchIntent(Context context, Bundle bundle) {
        return createIntentToAutoCompleteActivityFromBundle(context, bundle);
    }

    private static Intent createIntentToAutoCompleteActivityFromBundle(Context context, Bundle bundle) {
        SearchParameter searchParameter = createSearchParameterFromBundle(bundle);

        Intent intent = new Intent(context, AutoCompleteActivity.class);
        intent.putExtra(EXTRA_SEARCH_PARAMETER_MODEL, searchParameter);

        return intent;
    }

    private static SearchParameter createSearchParameterFromBundle(Bundle bundle) {
        String deepLinkURI = bundle.getString(DEEP_LINK_URI);
        return new SearchParameter(deepLinkURI == null ? "" : deepLinkURI);
    }

    @Inject
    AutoCompletePresenter autoCompletePresenter;
    @Inject
    SearchTracking searchTracking;
    @Inject
    PermissionCheckerHelper permissionCheckerHelper;

    private SearchComponent searchComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityOnCreate(savedInstanceState);

        handleIntent(getIntent());
    }

    private void initActivityOnCreate(Bundle savedInstanceState) {
        GraphqlClient.init(this);
        initInjector();
    }

    private void initInjector() {
        searchComponent =
                DaggerSearchComponent.builder()
                        .appComponent(getApplicationComponent())
                        .build();
        searchComponent.inject(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        initPresenter();

        SearchParameter searchParameter = getSearchParameterFromIntent(intent);

        handleIntentAutoComplete(searchParameter);

        if (intent.getBooleanExtra(FROM_APP_SHORTCUTS, false)) {
            searchTracking.eventSearchShortcut();
        }

        handleImageUri(intent);
    }

    private void initPresenter() {
        setPresenter(autoCompletePresenter);
        autoCompletePresenter.attachView(this);
        autoCompletePresenter.setDiscoveryView(this);
    }

    private SearchParameter getSearchParameterFromIntent(Intent intent) {
        SearchParameter searchParameter = intent.getParcelableExtra(EXTRA_SEARCH_PARAMETER_MODEL);

        if (searchParameter == null) {
            searchParameter = new SearchParameter();
        }

        return searchParameter;
    }

    private void handleIntentAutoComplete(SearchParameter searchParameter) {
        searchView.showSearch(true, false, searchParameter);

        animateEnterActivityTransition();
    }

    private void animateEnterActivityTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            root.setVisibility(View.INVISIBLE);

            ViewTreeObserver viewTreeObserver = root.getViewTreeObserver();
            addOnGlobalLayoutListenerForAnimationIfAlive(viewTreeObserver, () -> AnimationUtil.reveal(root, getEmptyAnimationListener()));
        } else {
            root.setVisibility(View.VISIBLE);
        }
    }

    private void addOnGlobalLayoutListenerForAnimationIfAlive(ViewTreeObserver viewTreeObserver, AnimationCallback animationCallback) {
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    animationCallback.doAnimation();
                    root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }

    private AnimationUtil.AnimationListener getEmptyAnimationListener() {
        return new AnimationUtil.AnimationListener() {
            @Override
            public boolean onAnimationStart(View view) {
                return false;
            }

            @Override
            public boolean onAnimationEnd(View view) {
                return false;
            }

            @Override
            public boolean onAnimationCancel(View view) {
                return false;
            }
        };
    }

    private void handleImageUri(Intent intent) {

        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(this);

        if (remoteConfig.getBoolean(RemoteConfigKey.SHOW_IMAGE_SEARCH, false) &&
                intent != null) {

            if (intent.getClipData() != null &&
                    intent.getClipData().getItemCount() > 0) {

                searchView.hideShowCaseDialog(true);
                sendImageSearchFromGalleryGTM("");
                ClipData clipData = intent.getClipData();
                Uri uri = clipData.getItemAt(0).getUri();
                onImageSuccess(uri.toString());
            } else if (intent.getData() != null &&
                    !TextUtils.isEmpty(intent.getData().toString()) &&
                    isValidMimeType(intent.getData().toString())) {
                searchView.hideShowCaseDialog(true);
                sendImageSearchFromGalleryGTM("");
                onImageSuccess(intent.getData().toString());
            }
        }
    }

    private void sendImageSearchFromGalleryGTM(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                SearchEventTracking.Event.IMAGE_SEARCH_CLICK,
                SearchEventTracking.Category.IMAGE_SEARCH,
                SearchEventTracking.Action.EXTERNAL_IMAGE_SEARCH,
                "");
    }

    private boolean isValidMimeType(String url) {
        String mimeType = getMimeTypeUri(Uri.parse(url));

        return mimeType != null &&
                (mimeType.equalsIgnoreCase("image/jpg") ||
                        mimeType.equalsIgnoreCase("image/png") ||
                        mimeType.equalsIgnoreCase("image/jpeg"));

    }

    private String getMimeTypeUri(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    @Override
    protected void onResume() {
        super.onResume();
        unregisterShake();
    }

    private void forceShowKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_auto_complete;
    }

    public void onImageSuccess(String uri) {
        permissionCheckerHelper.checkPermission(
                getActivityContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE,
                new PermissionCheckerHelper.PermissionCheckListener() {
                    @Override
                    public void onPermissionDenied(@NotNull String permissionText) {
                        RequestPermissionUtil.onPermissionDenied(getActivityContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                    }

                    @Override
                    public void onNeverAskAgain(@NotNull String permissionText) {
                        RequestPermissionUtil.onNeverAskAgain(getActivityContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                    }

                    @Override
                    public void onPermissionGranted() {
                        onImagePickedSuccess(uri);
                    }
                },
                ""
        );
    }

    private Activity getActivityContext() {
        return AutoCompleteActivity.this;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionCheckerHelper.onRequestPermissionsResult(getActivityContext(), requestCode, permissions, grantResults);
    }

    private interface AnimationCallback {
        void doAnimation();
    }
}
