package com.tokopedia.topads.dashboard.view.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.base.view.fragment.BasePresenterFragment;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.constant.TopAdsConstant;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.topads.dashboard.view.activity.TopAdsDetailProductActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupNewPromoActivity;
import com.tokopedia.topads.dashboard.view.listener.TopAdsCheckProductPromoView;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsCheckProductPromoPresenter;

import javax.inject.Inject;

/**
 * Created by hadi.putra on 17/04/18.
 */

public class TopAdsCheckProductPromoFragment extends BasePresenterFragment<TopAdsCheckProductPromoPresenter>
        implements TopAdsCheckProductPromoView {
    @Inject TopAdsCheckProductPromoPresenter presenter;
    String shopId;
    private String itemId;
    String source;

    TkpdProgressDialog progressDialog;

    public static TopAdsCheckProductPromoFragment createInstance(String shopId, String itemId){
        TopAdsCheckProductPromoFragment fragment = new TopAdsCheckProductPromoFragment();
        Bundle bundle = new Bundle();

        bundle.putString(TopAdsConstant.PARAM_EXTRA_SHOP_ID, shopId);
        bundle.putString(TopAdsConstant.PARAM_EXTRA_ITEM_ID, itemId);

        fragment.setArguments(bundle);
        return fragment;
    }

    public static TopAdsCheckProductPromoFragment createInstance(String shopId, String itemId, String source){
        TopAdsCheckProductPromoFragment fragment = new TopAdsCheckProductPromoFragment();
        Bundle bundle = new Bundle();

        bundle.putString(TopAdsConstant.PARAM_EXTRA_SHOP_ID, shopId);
        bundle.putString(TopAdsConstant.PARAM_EXTRA_ITEM_ID, itemId);
        bundle.putString(TopAdsConstant.PARAM_KEY_SOURCE, source);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return getClass().getSimpleName();
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerTopAdsCreatePromoComponent.builder()
                .topAdsCreatePromoModule(new TopAdsCreatePromoModule())
                .topAdsComponent(getComponent(TopAdsComponent.class))
                .build()
                .inject(this);
    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        presenter.attachView(this);
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        shopId = getArguments().getString(TopAdsConstant.PARAM_EXTRA_SHOP_ID, "");
        itemId = getArguments().getString(TopAdsConstant.PARAM_EXTRA_ITEM_ID, "");
        source = getArguments().getString(TopAdsConstant.PARAM_KEY_SOURCE, "");
        if (TextUtils.isEmpty(itemId)){
            moveToCreateAds();
        } else {
            if (TextUtils.isEmpty(source)){
                presenter.checkAndSaveSource();
            } else {
                presenter.save(source);
            }
            reloadData();
        }

    }

    @Override
    public void showLoadingProgress() {
        if (progressDialog == null && getActivity() != null)
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);

        if (progressDialog != null && getActivity() != null)
            progressDialog.showDialog();
    }

    @Override
    public void finishLoadingProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void renderErrorView(Throwable throwable) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(),
                ErrorHandler.getErrorMessage(getActivity(), throwable), null);
    }

    @Override
    public void renderRetryRefresh() {
        NetworkErrorHelper.showEmptyState(
                getActivity(),
                getView(),
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        reloadData();
                    }
                });
    }

    private void reloadData() {
        showLoadingProgress();
        presenter.checkPromoAds(shopId, itemId, SessionHandler.getLoginID(getActivity()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_base_list;
    }

    @Override
    public void moveToCreateAds() {
        startActivity(TopAdsGroupNewPromoActivity.createIntent(getActivity(), itemId, null));
        getActivity().finish();
    }

    @Override
    public void moveToAdsDetail(String adsId) {
        startActivity(TopAdsDetailProductActivity.getCallingIntent(getActivity(), adsId));
        getActivity().finish();
    }
}
