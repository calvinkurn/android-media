package com.tokopedia.topads.dashboard.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tokopedia.base.list.seller.view.fragment.BasePresenterFragment;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.util.TopAdsComponentUtils;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditGroupPresenter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsEditGroupNameFragment extends BasePresenterFragment implements TopAdsDetailEditView {

    @Inject
    TopAdsDetailEditGroupPresenter topAdsDetailEditGroupPresenter;

    protected TextInputLayout nameInputLayout;
    protected EditText nameEditText;
    protected Button buttonNext;
    protected ProgressDialog progressDialog;

    private TopAdsDetailGroupViewModel detailAd;
    protected String adId;

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerTopAdsCreatePromoComponent.builder()
                .topAdsCreatePromoModule(new TopAdsCreatePromoModule())
                .topAdsComponent(TopAdsComponentUtils.getTopAdsComponent(this))
                .build()
                .inject(this);
        topAdsDetailEditGroupPresenter.attachView(this);
    }

    public static Fragment createInstance(String name, String adId) {
        Fragment fragment = new TopAdsEditGroupNameFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_NAME, name);
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        detailAd = new TopAdsDetailGroupViewModel();
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        nameInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_name);
        nameEditText = (EditText) view.findViewById(R.id.edit_text_name);
        buttonNext = (Button) view.findViewById(R.id.button_submit);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnifyTracking.eventTopAdsProductEditGroupName(getActivity());
                saveAd();
            }
        });
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
    }

    private void loadAd(TopAdsDetailAdViewModel topAdsDetailAdViewModel){
        this.detailAd = (TopAdsDetailGroupViewModel) topAdsDetailAdViewModel;
        nameEditText.setText(topAdsDetailAdViewModel.getTitle());
    }

    protected void saveAd() {
        showLoading();
        populateDateFromFields();
        if (TextUtils.isEmpty(nameEditText.getText().toString().trim())) {
            onSaveAdError(getString(R.string.label_top_ads_error_empty_group_name));
            return;
        }
        if (detailAd !=  null) {
            showLoading();
            topAdsDetailEditGroupPresenter.saveAd(detailAd);
        }
    }

    private void populateDateFromFields() {
        if (detailAd ==  null) {
            onSaveAdError(getString(R.string.label_top_ads_error_empty_group_name));
            return;
        }
        if(detailAd.getEndDate() != null && !detailAd.getEndDate().isEmpty()){
            detailAd.setScheduled(true);
        }
        detailAd.setTitle(nameEditText.getText().toString());
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        super.setupArguments(arguments);
        adId = arguments.getString(TopAdsExtraConstant.EXTRA_AD_ID);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_new_group_name;
    }

    @Override
    protected void setActionVar() {
        super.setActionVar();
        loadAdDetail();
    }

    protected void showLoading() {
        progressDialog.show();
    }

    protected void hideLoading() {
        progressDialog.dismiss();
    }

    private void loadAdDetail() {
        if (!TextUtils.isEmpty(adId)) {
            showLoading();
            topAdsDetailEditGroupPresenter.getDetailAd(adId);
        }
    }

    @Override
    public void onDetailAdLoaded(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        hideLoading();
        loadAd(topAdsDetailAdViewModel);
    }

    @Override
    public void onLoadDetailAdError(String errorMessage) {
        hideLoading();
        showEmptyState(new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                loadAdDetail();
            }
        });
    }

    @Override
    public void onSaveAdSuccess(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        hideLoading();
        setResultAdSaved();
        getActivity().finish();
    }

    @Override
    public void onSaveAdError(String errorMessage) {
        hideLoading();
        showSnackBarError(errorMessage);
    }

    @Override
    public void onSuggestionSuccess(GetSuggestionResponse s) {
        /* just deal with abstraction */
    }

    @Override
    public void onSuggestionError(@Nullable Throwable t) {
        /* just deal with abstraction */
    }

    @Override
    public void onSuccessLoadTopAdsProduct(TopAdsProductViewModel topAdsProductViewModel) {
        //do nothing
    }

    @Override
    public void onErrorLoadTopAdsProduct(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    protected void showEmptyState(NetworkErrorHelper.RetryClickedListener retryClickedListener) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), retryClickedListener);
    }

    private void setResultAdSaved() {
        Intent intent = new Intent();
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, true);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    protected void showSnackBarError(String errorMessage) {
        if (!TextUtils.isEmpty(errorMessage)) {
            NetworkErrorHelper.showCloseSnackbar(getActivity(), errorMessage);
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_network_error));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        topAdsDetailEditGroupPresenter.detachView();
    }
}
