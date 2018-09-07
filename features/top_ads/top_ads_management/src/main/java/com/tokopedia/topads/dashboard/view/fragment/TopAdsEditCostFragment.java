package com.tokopedia.topads.dashboard.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.topads.R;
import com.tokopedia.seller.base.view.model.StepperModel;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.topads.dashboard.view.model.Ad;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditPresenter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/15/17.
 */

public abstract class TopAdsEditCostFragment<T extends TopAdsDetailEditPresenter, V extends TopAdsDetailAdViewModel, U extends Ad>
        extends TopAdsNewCostFragment<StepperModel, V> implements TopAdsDetailEditView {

    @Inject
    T daggerPresenter;

    protected String adId;
    protected U adFromIntent;

    @Override
    protected void setupArguments(Bundle arguments) {
        super.setupArguments(arguments);
        adId = arguments.getString(TopAdsExtraConstant.EXTRA_AD_ID);
        adFromIntent = arguments.getParcelable(TopAdsExtraConstant.EXTRA_AD);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        headerText.setVisibility(View.GONE);
        titleCost.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        submitButton.setText(getString(R.string.label_top_ads_save));
    }

    @Override
    protected void setActionVar() {
        super.setActionVar();
        loadAdDetail();
    }

    private void loadAdDetail() {
        if (!TextUtils.isEmpty(adId)) {
            showLoading();
            daggerPresenter.getDetailAd(adId);
        }
    }

    @Override
    public void onDetailAdLoaded(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        hideLoading();
        loadAd((V) topAdsDetailAdViewModel);
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
    protected void populateDataFromFields() {
        super.populateDataFromFields();
        if(detailAd.getEndDate() != null && !detailAd.getEndDate().isEmpty()){
            detailAd.setScheduled(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        daggerPresenter.detachView();
    }
}
