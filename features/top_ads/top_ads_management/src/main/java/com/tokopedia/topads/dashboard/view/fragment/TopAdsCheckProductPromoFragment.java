package com.tokopedia.topads.dashboard.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.topads.dashboard.view.activity.TopAdsDetailProductActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupNewPromoActivity;
import com.tokopedia.topads.dashboard.view.listener.TopAdsCheckProductPromoView;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsCheckProductPromoPresenter;
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceTaggingConstant;

import javax.inject.Inject;

/**
 * Created by hadi.putra on 17/04/18.
 */

public class TopAdsCheckProductPromoFragment extends BaseDaggerFragment
        implements TopAdsCheckProductPromoView {

    @Inject TopAdsCheckProductPromoPresenter presenter;
    String shopId;
    private String itemId;
    String source;

    private TextView tvMessageRetry;
    private View loadingLayout;
    private View errorLayout;

    public static TopAdsCheckProductPromoFragment createInstance(String shopId, String itemId){
        TopAdsCheckProductPromoFragment fragment = new TopAdsCheckProductPromoFragment();
        Bundle bundle = new Bundle();

        bundle.putString(TopAdsSourceTaggingConstant.PARAM_EXTRA_SHOP_ID, shopId);
        bundle.putString(TopAdsSourceTaggingConstant.PARAM_EXTRA_ITEM_ID, itemId);

        fragment.setArguments(bundle);
        return fragment;
    }

    public static TopAdsCheckProductPromoFragment createInstance(String shopId, String itemId, String source){
        TopAdsCheckProductPromoFragment fragment = new TopAdsCheckProductPromoFragment();
        Bundle bundle = new Bundle();

        bundle.putString(TopAdsSourceTaggingConstant.PARAM_EXTRA_SHOP_ID, shopId);
        bundle.putString(TopAdsSourceTaggingConstant.PARAM_EXTRA_ITEM_ID, itemId);
        bundle.putString(TopAdsSourceTaggingConstant.PARAM_KEY_SOURCE, source);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return getClass().getSimpleName();
    }

    @Override
    protected void initInjector() {
        DaggerTopAdsCreatePromoComponent.builder()
                .topAdsCreatePromoModule(new TopAdsCreatePromoModule())
                .topAdsComponent(getComponent(TopAdsComponent.class))
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter.attachView(this);
        return inflater.inflate(R.layout.fragment_top_ads_check_product_promo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initialVar();
    }

    protected void initView(View view) {
        loadingLayout = view.findViewById(R.id.layout_loading);
        errorLayout = view.findViewById(R.id.layout_error);
        tvMessageRetry = view.findViewById(R.id.message_retry);
        View retryButton = view.findViewById(R.id.button_retry);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadData();
            }
        });
    }

    protected void initialVar() {
        shopId = getArguments().getString(TopAdsSourceTaggingConstant.PARAM_EXTRA_SHOP_ID, "");
        itemId = getArguments().getString(TopAdsSourceTaggingConstant.PARAM_EXTRA_ITEM_ID, "");
        source = getArguments().getString(TopAdsSourceTaggingConstant.PARAM_KEY_SOURCE, "");
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
        loadingLayout.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
    }

    @Override
    public void finishLoadingProgress() {
        loadingLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void renderErrorView(Throwable throwable) {
        String errorMessage = ErrorHandler.getErrorMessage(getActivity(), throwable);
        if (!TextUtils.isEmpty(errorMessage)) {
            tvMessageRetry.setText(errorMessage);
        }
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
    public void moveToCreateAds() {
        startActivity(TopAdsGroupNewPromoActivity.createIntent(getActivity(), itemId, null));
        getActivity().finish();
    }

    @Override
    public void moveToAdsDetail(String adsId) {
        Intent intent = TopAdsDetailProductActivity.getCallingIntent(getActivity(), adsId);
        intent.putExtra(TopAdsNewScheduleNewGroupFragment.EXTRA_IS_ENOUGH_DEPOSIT, true);
        startActivity(intent);
        getActivity().finish();
    }
}
