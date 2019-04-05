package com.tokopedia.kol.feature.createpost.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseWebViewFragment;
import com.tokopedia.abstraction.common.utils.network.URLGenerator;
import com.tokopedia.kol.KolComponentInstance;
import com.tokopedia.kol.common.di.DaggerKolComponent;
import com.tokopedia.kol.common.di.KolComponent;
import com.tokopedia.kol.feature.createpost.di.CreatePostModule;
import com.tokopedia.kol.feature.createpost.di.DaggerCreatePostComponent;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

/**
 * @author by yfsx on 07/06/18.
 */
public class CreatePostWebviewFragment extends BaseWebViewFragment {

    public static final String FORM_URL = "form_url";
    public static final String SUCCESS_URL_PATH = "/content/new?success=true";

    private UserSessionInterface userSession;

    public static CreatePostWebviewFragment newInstance(Bundle bundle) {
        CreatePostWebviewFragment fragment = new CreatePostWebviewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSession = new UserSession(getActivity());
    }

    @Override
    protected void initInjector() {
        KolComponent kolComponent = DaggerKolComponent.builder().baseAppComponent(
                ((BaseMainApplication)getActivity().getApplicationContext()).getBaseAppComponent())
                .build();
        DaggerCreatePostComponent.builder().kolComponent(kolComponent).build();
        DaggerCreatePostComponent.builder()
                .kolComponent(KolComponentInstance.getKolComponent(getActivity().getApplication()))
                .createPostModule(new CreatePostModule())
                .build()
                .inject(this);
    }

    @Override
    protected boolean shouldOverrideUrlLoading(WebView webView, String url) {
        if (url.contains(SUCCESS_URL_PATH)) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
            return true;
        }
        return false;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected String getUrl() {
        return URLGenerator.generateURLSessionLogin(
                getArguments().getString(FORM_URL), userSession.getDeviceId(), userSession.getUserId());
    }

    @Nullable
    @Override
    protected String getUserIdForHeader() {
        return userSession.getUserId();
    }

    @Nullable
    @Override
    protected String getAccessToken() {
        return userSession.getAccessToken();
    }
}
