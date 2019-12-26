package com.tokopedia.shop.open.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.shop.open.data.model.response.isreservedomain.ResponseIsReserveDomain;
import com.tokopedia.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.shop.open.util.ShopErrorHandler;
import com.tokopedia.shop.open.view.activity.ShopOpenDomainActivity;
import com.tokopedia.shop.open.view.listener.ShopOpenCheckDomainView;

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

    @Override
    protected void initInjector() {
        ShopOpenDomainComponent component = getComponent(ShopOpenDomainComponent.class);
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(com.tokopedia.seller.R.layout.fragment_shop_open_routing, container, false);
        loadingLayout = view.findViewById(com.tokopedia.seller.R.id.layout_loading);
        errorLayout = view.findViewById(com.tokopedia.seller.R.id.layout_error);
        tvMessageRetry = view.findViewById(com.tokopedia.abstraction.R.id.message_retry);
        View retryButton = view.findViewById(com.tokopedia.abstraction.R.id.button_retry);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading(true);
                goToShopOpenDomain();
            }
        });
        showLoading(true);
        goToShopOpenDomain();
        return view;
    }

    @Override
    public void onSuccessCheckReserveDomain(ResponseIsReserveDomain responseIsReserveDomain) {
        goToShopOpenDomain();
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
        if (getActivity() == null) {
            return;
        }

        Intent intent;
        if (SessionHandler.isV4Login(getActivity()) && !SessionHandler.isUserHasShop(getActivity())) {
            intent = ShopOpenDomainActivity.getIntent(getActivity());
        } else {
            intent = RouteManager.getIntent(getActivity(), ApplinkConst.HOME);
        }

        startActivity(intent);
        getActivity().finish();
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}