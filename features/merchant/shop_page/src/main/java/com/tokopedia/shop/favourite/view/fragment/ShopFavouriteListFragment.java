package com.tokopedia.shop.favourite.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException;
import com.tokopedia.shop.R;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.analytic.ShopPageTracking;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.favourite.di.component.DaggerShopFavouriteComponent;
import com.tokopedia.shop.favourite.di.module.ShopFavouriteModule;
import com.tokopedia.shop.favourite.view.adapter.ShopFavouriteAdapterTypeFactory;
import com.tokopedia.shop.favourite.view.listener.ShopFavouriteListView;
import com.tokopedia.shop.favourite.view.model.ShopFavouriteViewModel;
import com.tokopedia.shop.favourite.view.presenter.ShopFavouriteListPresenter;
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity;

import javax.inject.Inject;

/**
 * Created by nathan on 2/5/18.
 */

public class ShopFavouriteListFragment extends BaseListFragment<ShopFavouriteViewModel, ShopFavouriteAdapterTypeFactory> implements ShopFavouriteListView {

    private static final int REQUEST_CODE_USER_LOGIN = 100;

    public static ShopFavouriteListFragment createInstance(String shopId) {
        ShopFavouriteListFragment shopFavouriteListFragment = new ShopFavouriteListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ShopParamConstant.EXTRA_SHOP_ID, shopId);
        shopFavouriteListFragment.setArguments(bundle);
        return shopFavouriteListFragment;
    }

    @Inject
    ShopFavouriteListPresenter shopFavouriteListPresenter;
    @Inject
    ShopPageTracking shopPageTracking;
    private ShopInfo shopInfo;
    private String shopId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopId = getArguments().getString(ShopParamConstant.EXTRA_SHOP_ID);
        shopFavouriteListPresenter.attachView(this);
    }

    @Override
    public void loadData(int page) {
        shopFavouriteListPresenter.getShopInfo(shopId);
    }

    @Override
    protected ShopFavouriteAdapterTypeFactory getAdapterTypeFactory() {
        return new ShopFavouriteAdapterTypeFactory();
    }

    @Override
    public void onItemClicked(ShopFavouriteViewModel shopFavouriteViewModel) {
        if(shopInfo != null) {
            shopPageTracking.eventClickUserFavouritingShop(shopId, shopFavouriteListPresenter.isMyShop(shopId), ShopPageTracking.getShopType(shopInfo.getInfo()));
        }
        ((ShopModuleRouter) getActivity().getApplication()).goToProfileShop(getActivity(), shopFavouriteViewModel.getId());
    }

    @Override
    protected void initInjector() {
        DaggerShopFavouriteComponent
                .builder()
                .shopFavouriteModule(new ShopFavouriteModule())
                .shopComponent(getComponent(ShopComponent.class))
                .build()
                .inject(this);
    }

    @Override
    public void onSuccessGetShopInfo(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
        shopFavouriteListPresenter.getshopFavouriteList(shopId, getCurrentPage());
    }

    @Override
    public void showGetListError(Throwable throwable) {
        if (throwable instanceof UserNotLoginException) {
            Intent intent = ((ShopModuleRouter) getActivity().getApplication()).getLoginIntent(getActivity());
            startActivityForResult(intent, REQUEST_CODE_USER_LOGIN);
            return;
        }
        super.showGetListError(throwable);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_USER_LOGIN:
                if (isAdded() && shopFavouriteListPresenter != null) {
                    if (shopFavouriteListPresenter.isLoggedIn()) {
                        loadInitialData();
                    } else {
                        getActivity().finish();
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(shopInfo != null) {
            shopPageTracking.eventCloseListFavourite(shopId, shopFavouriteListPresenter.isMyShop(shopId), ShopPageTracking.getShopType(shopInfo.getInfo()));
        }
        if (shopFavouriteListPresenter != null) {
            shopFavouriteListPresenter.detachView();
        }
    }
}
