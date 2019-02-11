package com.tokopedia.shop.open.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.seller.R;
import com.tokopedia.shop.open.data.model.response.isreservedomain.ResponseIsReserveDomain;
import com.tokopedia.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.shop.open.view.activity.ShopOpenDomainActivity;
import com.tokopedia.shop.open.view.activity.ShopOpenMandatoryActivity;
import com.tokopedia.shop.open.view.listener.ShopOpenCheckDomainView;
import com.tokopedia.shop.open.view.presenter.ShopCheckIsReservePresenterImpl;
import com.tokopedia.shop.open.util.ShopErrorHandler;

import javax.inject.Inject;

/**
 * Created by Hendry on 3/17/2017.
 */

public class ShopOpenRoutingFragment extends BaseDaggerFragment implements ShopOpenCheckDomainView {

    private TextView tvMessageRetry;

    public static ShopOpenRoutingFragment newInstance() {
        return new ShopOpenRoutingFragment();
    }

    private View loadingLayout;
    private View errorLayout;

    @Inject
    ShopCheckIsReservePresenterImpl shopCheckIsReservePresenter;

    @Override
    protected void initInjector() {
        ShopOpenDomainComponent component = getComponent(ShopOpenDomainComponent.class);
        component.inject(this);
        shopCheckIsReservePresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_open_routing, container, false);
        loadingLayout = view.findViewById(R.id.layout_loading);
        errorLayout = view.findViewById(R.id.layout_error);
        tvMessageRetry = view.findViewById(R.id.message_retry);
        View retryButton = view.findViewById(R.id.button_retry);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading(true);
                shopCheckIsReservePresenter.isReservingDomain();
            }
        });
        showLoading(true);
        shopCheckIsReservePresenter.isReservingDomain();
        return view;
    }

    @Override
    public void onSuccessCheckReserveDomain(ResponseIsReserveDomain responseIsReserveDomain) {
        boolean isReservingDomain = responseIsReserveDomain.isDomainAlreadyReserved();
        if (isReservingDomain) {
            goToShopOpenMandatory(responseIsReserveDomain);
        } else {
            goToShopOpenDomain();
        }
    }

    @Override
    public void onErrorCheckReserveDomain(Throwable t) {
        showLoading(false);
        String errorMessage = ShopErrorHandler.getErrorMessage(getActivity(), t);
        if (!TextUtils.isEmpty(errorMessage)) {
            tvMessageRetry.setText(errorMessage);
        }
    }

    private void showLoading(boolean show) {
        loadingLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        errorLayout.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void goToShopOpenDomain() {
        Intent intent = ShopOpenDomainActivity.getIntent(getActivity());
        startActivity(intent);
        getActivity().finish();
    }

    private void goToShopOpenMandatory(ResponseIsReserveDomain responseIsReserveDomain) {
        Intent intent = ShopOpenMandatoryActivity.getIntent(getActivity(), responseIsReserveDomain);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shopCheckIsReservePresenter != null) {
            shopCheckIsReservePresenter.detachView();
        }
    }
}