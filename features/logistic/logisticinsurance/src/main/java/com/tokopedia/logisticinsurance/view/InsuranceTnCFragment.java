package com.tokopedia.logisticinsurance.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.abstraction.base.view.webview.TkpdWebView;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.logisticinsurance.R;
import com.tokopedia.logisticinsurance.di.DaggerInsuranceTnCComponent;
import com.tokopedia.logisticinsurance.di.InsuranceTnCComponent;
import com.tokopedia.logisticinsurance.di.InsuranceTnCModule;

import javax.inject.Inject;

/**
 * Created by Irfan Khoirul on 11/12/17.
 */

public class InsuranceTnCFragment extends TkpdBaseV4Fragment
        implements InsuranceTnCContract.View {

    private TkpdWebView webViewTermsAndCondition;
    private ProgressBar pbLoading;

    @Inject
    InsuranceTnCContract.Presenter presenter;


    public static Fragment createInstance() {
        return new InsuranceTnCFragment();
    }


    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initialVar();
        setViewListener();
        setActionVar();
        presenter.loadWebViewData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(isRetainInstance());
        initInjector();
        if (getArguments() != null) {
            setupArguments(getArguments());
        }
        initialPresenter();
    }

    private void initInjector() {
        BaseAppComponent appComponent = ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent();
        InsuranceTnCComponent insuranceTnCComponent = DaggerInsuranceTnCComponent.builder()
                .baseAppComponent(appComponent)
                .insuranceTnCModule(new InsuranceTnCModule())
                .build();
        insuranceTnCComponent.inject(this);
    }

    protected void initialPresenter() {

        presenter.attachView(this);
    }

    protected void setupArguments(Bundle arguments) {

    }


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_insurance_tnc, container, false);
    }


    protected void initView(View view) {
        webViewTermsAndCondition = view.findViewById(R.id.web_view_terms_and_condition);
        pbLoading = view.findViewById(R.id.pb_loading);
    }


    protected void setViewListener() {

    }


    protected void initialVar() {

    }


    protected void setActionVar() {

    }


    @Override
    public void showWebView(String webViewData) {
        webViewTermsAndCondition.setWebViewClient(new TermsAndConditionsWebViewClient());
        webViewTermsAndCondition.setWebChromeClient(new WebChromeClient());
        webViewTermsAndCondition.loadData(webViewData, "text/html", "UTF-8");
    }

    @Override
    public void showLoading() {
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (pbLoading != null) {
            pbLoading.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    private class TermsAndConditionsWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            hideLoading();
        }
    }

}
