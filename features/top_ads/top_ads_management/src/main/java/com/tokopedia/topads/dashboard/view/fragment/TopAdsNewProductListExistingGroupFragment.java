package com.tokopedia.topads.dashboard.view.fragment;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.util.TopAdsComponentUtils;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.response.TopAdsDepositResponse;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.topads.dashboard.domain.model.MinimumBidDomain;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailNewGroupView;
import com.tokopedia.topads.dashboard.view.model.TopAdsCreatePromoExistingGroupModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailShopViewModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailNewGroupPresenter;

/**
 * Created by zulfikarrahman on 8/7/17.
 */

public class TopAdsNewProductListExistingGroupFragment extends TopAdsNewProductListFragment<TopAdsCreatePromoExistingGroupModel, TopAdsDetailNewGroupPresenter> implements TopAdsDetailNewGroupView {

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerTopAdsCreatePromoComponent.builder()
                .topAdsCreatePromoModule(new TopAdsCreatePromoModule())
                .topAdsComponent(TopAdsComponentUtils.getTopAdsComponent(this))
                .build()
                .inject(this);
        daggerPresenter.attachView(this);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        buttonNext.setText(getString(R.string.label_top_ads_save));
    }

    @Override
    protected void initiateStepperModel() {
        stepperModel = new TopAdsCreatePromoExistingGroupModel();
    }

    @Override
    protected void goToNextPage() {
        UnifyTracking.eventTopAdsProductAddPromoExistingGroupStep1(getActivity());
        daggerPresenter.getBalance(getResources());
    }


    @Override
    public void onDetailAdLoaded(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        // do nothing
    }

    @Override
    public void onLoadDetailAdError(String errorMessage) {
        // do nothing
    }

    @Override
    public void onSaveAdSuccess(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        hideLoading();

        Intent intent = new Intent();
        if(topAdsDetailAdViewModel != null && topAdsDetailAdViewModel instanceof TopAdsDetailShopViewModel){
            intent.putExtra(TopAdsNewScheduleNewGroupFragment.EXTRA_IS_ENOUGH_DEPOSIT, ((TopAdsDetailShopViewModel)topAdsDetailAdViewModel).isEnoughDeposit());
            intent.putExtra(TopAdsNewScheduleNewGroupFragment.EXTRA_NEW_GROUP_ID, (Long.valueOf(((TopAdsDetailShopViewModel) topAdsDetailAdViewModel).getGroupId())));
        }
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, true);
        getActivity().setResult(Activity.RESULT_OK, intent);

        if (stepperListener != null) {
            stepperListener.finishPage();
        }
    }

    @Override
    public void onBalanceCheck(TopAdsDepositResponse.Data topAdsDepositResponse) {
        daggerPresenter.saveAdExisting(stepperModel.getGroupId(), stepperModel.getTopAdsProductViewModels(), stepperModel.getSource(),topAdsDepositResponse);
    }

    @Override
    public void onSaveAdError(String errorMessage) {
        hideLoading();
        showSnackBarError(errorMessage);
    }

    @Override
    public void onBidInfoSuccess(MinimumBidDomain.TopadsBidInfo bidInfo) {

    }

    @Override
    public void onBidInfoError(@Nullable Throwable t) {

    }

    @Override
    public void goToGroupDetail(String groupId) {
    }

    protected void showSnackBarError(String errorMessage) {
        if (!TextUtils.isEmpty(errorMessage)) {
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_network_error));
        }
    }
}
