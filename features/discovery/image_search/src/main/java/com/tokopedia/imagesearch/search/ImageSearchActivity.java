package com.tokopedia.imagesearch.search;

import android.Manifest;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerEditorBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;
import com.tokopedia.imagesearch.R;
import com.tokopedia.imagesearch.analytics.ImageSearchEventTracking;
import com.tokopedia.imagesearch.di.component.DaggerImageSearchComponent;
import com.tokopedia.imagesearch.di.component.ImageSearchComponent;
import com.tokopedia.imagesearch.domain.viewmodel.ProductViewModel;
import com.tokopedia.imagesearch.search.fragment.ImageSearchProductListFragment;
import com.tokopedia.imagesearch.search.fragment.product.adapter.listener.RedirectionListener;
import com.tokopedia.permissionchecker.PermissionCheckerHelper;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.track.TrackApp;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_BRIGHTNESS;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CONTRAST;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CROP;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY;

public class ImageSearchActivity extends BaseActivity
        implements ImageSearchContract.View,
        RedirectionListener,
        PermissionCheckerHelper.PermissionCheckListener {

    private static final int REQUEST_CODE_IMAGE = 2390;
    private static final String NO_RESPONSE = "no response";
    private static final String SUCCESS = "success match found";
    private static final String KEY_IMAGE_PATH = "image_path";

    private Toolbar toolbar;
    private View backButton;
    private TextView searchTextView;
    private ImageView thumbnailImage;
    private FrameLayout container;
    protected ProgressBar loadingView;

    protected View root;

    private String imagePath = "";
    private boolean isFromCamera = false;

    @Inject
    ImageSearchPresenter searchPresenter;

    @Inject
    PermissionCheckerHelper permissionCheckerHelper;

    private String lastQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        proceed();

        initImageSearch();

        if (savedInstanceState == null) {
            checkPermissionToContinue();
        }
        else {
            restoreStateOnCreate(savedInstanceState);
        }
    }

    protected int getLayoutRes() {
        return R.layout.activity_image_search;
    }

    private void proceed() {
        initView();
        prepareView();
    }

    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.image_search_toolbar);
        backButton = findViewById(R.id.action_up_btn);
        searchTextView = findViewById(R.id.imageSearchTextView);
        thumbnailImage = findViewById(R.id.imageSearchThumbnail);
        container = (FrameLayout) findViewById(R.id.image_search_container);
        loadingView = findViewById(R.id.image_search_progressBar);
        root = findViewById(R.id.image_search_root);
    }

    protected void prepareView() {
        initToolbar();
        showLoadingView(false);
    }

    protected void showLoadingView(boolean visible) {
        loadingView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    protected void showContainer(boolean visible) {
        container.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    protected void initToolbar() {
        configureSupportActionBar();
        setSearchTextViewDrawableLeft();
        configureToolbarOnClickListener();
    }

    private void configureSupportActionBar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
        }
    }

    private void setSearchTextViewDrawableLeft() {
        Drawable iconSearch = AppCompatResources.getDrawable(this, R.drawable.image_search_ic_search);
        searchTextView.setCompoundDrawablesWithIntrinsicBounds(iconSearch, null, null, null);
    }

    private void loadThumbnailImage(String imagePath) {
        ImageHandler.loadImageCircle2(this, thumbnailImage, imagePath);
    }

    private void configureToolbarOnClickListener() {
        searchTextView.setOnClickListener(v -> moveToAutoCompletePage());
        backButton.setOnClickListener(v -> onBackPressed());
    }

    protected void setToolbarTitle(String query) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(query);
        }
    }

    protected void setLastQuery(String lastQuery) {
        this.lastQuery = lastQuery;
    }

    private void initImageSearch() {
        initInjector();
        searchPresenter.attachView(this);
    }

    private void initInjector() {
        ImageSearchComponent imageSearchComponent = DaggerImageSearchComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .build();

        imageSearchComponent.inject(this);
    }

    public BaseAppComponent getBaseAppComponent() {
        return ((BaseMainApplication)getApplication()).getBaseAppComponent();
    }

    private void checkPermissionToContinue() {
        String[] imageSearchPermissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };

        permissionCheckerHelper.checkPermissions(this, imageSearchPermissions, this, "");
    }

    private void restoreStateOnCreate(@NonNull Bundle savedInstanceState) {
        String imagePath = savedInstanceState.getString(KEY_IMAGE_PATH, "");

        if (!TextUtils.isEmpty(imagePath)) {
            onImagePickedSuccess(imagePath);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionCheckerHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionDenied(@NotNull String permissionText) {
        permissionCheckerHelper.onPermissionDenied(this, permissionText);
        finish();
    }

    @Override
    public void onNeverAskAgain(@NotNull String permissionText) {
        permissionCheckerHelper.onNeverAskAgain(this, permissionText);
        finish();
    }

    @Override
    public void onPermissionGranted() {
        boolean isImageAlreadyPicked = handleImageUri(getIntent());

        if (!isImageAlreadyPicked) {
            openImagePickerActivity();
        }
    }

    private boolean handleImageUri(Intent intent) {
        if (canHandleImageUri(intent)) {
            if (hasClipData(intent)) {
                String imagePath = getImagePathFromClipData(intent);
                processImageUri(imagePath);
                return true;
            }
            else if (hasDataWithValidMimeType(intent)) {
                String imagePath = getImagePathFromData(intent);
                processImageUri(imagePath);
                return true;
            }
        }

        return false;
    }

    private boolean canHandleImageUri(Intent intent) {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(this);

        return remoteConfig.getBoolean(RemoteConfigKey.SHOW_IMAGE_SEARCH, false)
                && intent != null;
    }

    private boolean hasClipData(Intent intent) {
        return intent.getClipData() != null && intent.getClipData().getItemCount() > 0;
    }

    private String getImagePathFromClipData(Intent intent) {
        ClipData clipData = intent.getClipData();
        Uri uri = Objects.requireNonNull(clipData).getItemAt(0).getUri();
        return uri.toString();
    }

    private boolean hasDataWithValidMimeType(Intent intent) {
        return intent.getData() != null
                && !TextUtils.isEmpty(intent.getData().toString())
                && isValidMimeType(intent.getData().toString());
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

    private String getImagePathFromData(Intent intent) {
        return Objects.requireNonNull(intent.getData()).toString();
    }

    private void processImageUri(String imagePath) {
        sendImageSearchFromGalleryGTM();
        onImagePickedSuccess(imagePath);
    }

    private void sendImageSearchFromGalleryGTM() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                ImageSearchEventTracking.Event.IMAGE_SEARCH_CLICK,
                ImageSearchEventTracking.Category.IMAGE_SEARCH,
                ImageSearchEventTracking.Action.EXTERNAL_IMAGE_SEARCH,
                "");
    }

    private void openImagePickerActivity() {
        ImagePickerEditorBuilder imagePickerEditorBuilder = new ImagePickerEditorBuilder(
                createAllowedImageEditorActions(),
                false,
                createAllowedImageRatioList()
        );

        ImagePickerBuilder builder = new ImagePickerBuilder(
                getString(com.tokopedia.imagepicker.R.string.choose_image),
                createImagePickerTabTypes(),
                GalleryType.IMAGE_ONLY,
                ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                ImagePickerBuilder.IMAGE_SEARCH_MIN_RESOLUTION,
                null,
                true,
                imagePickerEditorBuilder,
                null
        );

        Intent intent = ImageSearchImagePickerActivity.getIntent(this, builder);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
        overridePendingTransition(0, 0);
    }

    @ImageEditActionTypeDef
    private int[] createAllowedImageEditorActions() {
        return new int[] {
                ACTION_CROP,
                ACTION_BRIGHTNESS,
                ACTION_CONTRAST
        };
    }

    private ArrayList<ImageRatioTypeDef> createAllowedImageRatioList() {
        ArrayList<ImageRatioTypeDef> imageRatioTypeDefArrayList = new ArrayList<>();

        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.ORIGINAL);
        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.RATIO_1_1);
        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.RATIO_3_4);
        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.RATIO_4_3);
        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.RATIO_16_9);
        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.RATIO_9_16);

        return imageRatioTypeDefArrayList;
    }

    @ImagePickerTabTypeDef
    private int[] createImagePickerTabTypes() {
        return new int[] {
                TYPE_GALLERY,
                TYPE_CAMERA
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE) {
            handleResultFromImagePicker(resultCode, data);
        }
    }

    private void handleResultFromImagePicker(int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_CANCELED:
                cancelImageSearch();
                break;
            case RESULT_OK:
                continueToImageSearch(data);
                break;
            default:
                errorHandlingImagePicker();
        }
    }

    private void cancelImageSearch() {
        finish();
    }

    private void continueToImageSearch(Intent data) {
        if (data == null) {
            errorHandlingImagePicker();
            return;
        }

        ArrayList<String> imagePathList = data.getStringArrayListExtra(ImageSearchImagePickerActivity.PICKER_RESULT_PATHS);
        if (imagePathList == null || imagePathList.size() <= 0) {
            errorHandlingImagePicker();
            return;
        }

        isFromCamera = data.getBooleanExtra(ImageSearchImagePickerActivity.RESULT_IS_FROM_CAMERA, false);

        String imagePath = imagePathList.get(0);

        if (!TextUtils.isEmpty(imagePath)) {
            onImagePickedSuccess(imagePath);
        } else {
            errorHandlingImagePicker();
        }
    }

    private void errorHandlingImagePicker() {
        Toast.makeText(this, getString(R.string.image_search_error_gallery_valid), Toast.LENGTH_LONG).show();
        finish();
    }

    private void sendGalleryImageSearchResultGTM(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                ImageSearchEventTracking.Event.IMAGE_SEARCH_CLICK,
                ImageSearchEventTracking.Category.IMAGE_SEARCH,
                ImageSearchEventTracking.Action.GALLERY_SEARCH_RESULT,
                label);
    }


    private void sendCameraImageSearchResultGTM(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                ImageSearchEventTracking.Event.IMAGE_SEARCH_CLICK,
                ImageSearchEventTracking.Category.IMAGE_SEARCH,
                ImageSearchEventTracking.Action.CAMERA_SEARCH_RESULT,
                label);
    }

    public void onImagePickedSuccess(String imagePath) {
        setImagePath(imagePath);
        showLoadingView(true);
        showContainer(false);
        loadThumbnailImage(imagePath);
        searchPresenter.requestImageSearch(imagePath);
    }

    @Override
    public void onHandleImageResponseSearch(ProductViewModel productViewModel) {
        trackEventOnSuccessImageSearch(productViewModel);

        showImageSearchResult(productViewModel);

        showLoadingView(false);
    }

    private void trackEventOnSuccessImageSearch(ProductViewModel productViewModel) {
        if (productViewModel.getProductList() == null || productViewModel.getProductList().size() == 0) {
            return;
        }
        sendGTMEventSuccessImageSearch();
        sendAppsFlyerEventSuccessImageSearch(productViewModel);
    }

    private void sendGTMEventSuccessImageSearch() {
        if (isFromCamera) {
            sendCameraImageSearchResultGTM(SUCCESS);
        } else {
            sendGalleryImageSearchResultGTM(SUCCESS);
        }
    }

    private void sendAppsFlyerEventSuccessImageSearch(ProductViewModel productViewModel) {
        JSONArray afProdIds = new JSONArray();
        HashMap<String, String> category = new HashMap<String, String>();
        ArrayList<String> prodIdArray = new ArrayList<>();

        if (productViewModel.getProductList().size() > 0) {
            for (int i = 0; i < productViewModel.getProductList().size(); i++) {
                if (i < 3) {
                    prodIdArray.add(productViewModel.getProductList().get(i).getProductID());
                    afProdIds.put(productViewModel.getProductList().get(i).getProductID());
                } else {
                    break;
                }
                category.put(String.valueOf(productViewModel.getProductList().get(i).getCategoryID()), productViewModel.getProductList().get(i).getCategoryName());
            }
        }

        eventAppsFlyerViewListingSearch(this, afProdIds,productViewModel.getQuery(),prodIdArray);
        sendMoEngageSearchAttemptSuccessImageSearch(productViewModel.getQuery(), !productViewModel.getProductList().isEmpty(), category);
    }

    public static void eventAppsFlyerViewListingSearch(Context context,JSONArray productsId, String keyword, ArrayList<String> prodIds) {
        Map<String, Object> listViewEvent = new HashMap<>();
        listViewEvent.put("af_content_id", prodIds);
        listViewEvent.put("af_currency", "IDR");
        listViewEvent.put("af_content_type", "product");
        listViewEvent.put("af_search_string", keyword);
        if (productsId.length() > 0) {
            listViewEvent.put("af_success", "success");
        } else {
            listViewEvent.put("af_success", "fail");
        }

        TrackApp.getInstance().getAppsFlyer().sendTrackEvent("af_search", listViewEvent);
    }

    public void sendMoEngageSearchAttemptSuccessImageSearch(String keyword, boolean isResultFound, HashMap<String, String> category) {
        Map<String, Object> value = DataLayer.mapOf(
                ImageSearchEventTracking.MOENGAGE.KEYWORD, keyword,
                ImageSearchEventTracking.MOENGAGE.IS_RESULT_FOUND, isResultFound
        );
        if (category != null) {
            value.put(ImageSearchEventTracking.MOENGAGE.CATEGORY_ID_MAPPING, new JSONArray(Arrays.asList(category.keySet().toArray())));
            value.put(ImageSearchEventTracking.MOENGAGE.CATEGORY_NAME_MAPPING, new JSONArray((category.values())));
        }
        TrackApp.getInstance().getMoEngage().sendTrackEvent(value, ImageSearchEventTracking.EventMoEngage.SEARCH_ATTEMPT);
    }

    private void showImageSearchResult(ProductViewModel productViewModel) {
        if (productViewModel != null) {
            setLastQuery(productViewModel.getQuery());
            loadSection(productViewModel);
            setToolbarTitle(getString(R.string.title_image_search));
        } else {
            moveToAutoCompletePage();
        }
    }

    private void loadSection(ProductViewModel productViewModel) {
        addFragment(R.id.image_search_container, ImageSearchProductListFragment.newInstance(productViewModel));

        showContainer(true);
    }

    private void addFragment(int containerViewId, ImageSearchProductListFragment fragment) {
        if (!isFinishing() && !fragment.isAdded()) {
            FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerViewId, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onHandleInvalidImageSearchResponse() {
        showLoadingView(false);

        if (isFromCamera) {
            sendCameraImageSearchResultGTM(NO_RESPONSE);
        } else {
            sendGalleryImageSearchResultGTM(NO_RESPONSE);
        }

        Toast.makeText(this, getString(R.string.invalid_image_search_response), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void showImageNotSupportedError() {
        showLoadingView(false);

        Toast.makeText(this, getResources().getString(R.string.image_not_supported), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public Context getContext() {
        return this;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_IMAGE_PATH, imagePath);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        if(searchPresenter != null) {
            searchPresenter.detachView();
        }

        super.onDestroy();
    }

    @Override
    public void moveToAutoCompletePage() {
        if (!TextUtils.isEmpty(lastQuery)) {
            startActivityWithApplink(ApplinkConstInternalDiscovery.AUTOCOMPLETE + "?q=" + lastQuery);
        } else {
            startActivityWithApplink(ApplinkConstInternalDiscovery.AUTOCOMPLETE);
        }
    }

    private void startActivityWithApplink(String applink, String... parameter) {
        Intent intent = RouteManager.getIntent(this, applink, parameter);
        startActivity(intent);
    }
}
