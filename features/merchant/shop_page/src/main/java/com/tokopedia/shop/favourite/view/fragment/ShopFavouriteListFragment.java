package com.tokopedia.shop.favourite.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.shop.R;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer;
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.favourite.di.component.DaggerShopFavouriteComponent;
import com.tokopedia.shop.favourite.di.module.ShopFavouriteModule;
import com.tokopedia.shop.favourite.view.adapter.ShopFavouriteAdapterTypeFactory;
import com.tokopedia.shop.favourite.view.listener.ShopFavouriteListView;
import com.tokopedia.shop.favourite.view.model.ShopFavouriteViewModel;
import com.tokopedia.shop.favourite.view.presenter.ShopFavouriteListPresenter;
import com.tokopedia.trackingoptimizer.TrackingQueue;

import javax.inject.Inject;

/**
 * Created by nathan on 2/5/18.
 */

public class ShopFavouriteListFragment extends BaseListFragment<ShopFavouriteViewModel, ShopFavouriteAdapterTypeFactory> implements ShopFavouriteListView, BaseEmptyViewHolder.Callback {

    private static final int DEFAULT_INITIAL_PAGE = 1;
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

    ShopPageTrackingBuyer shopPageTracking;
    private ShopInfo shopInfo;
    private String shopId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopPageTracking = new ShopPageTrackingBuyer(
                new TrackingQueue(getContext()));
        shopId = getArguments().getString(ShopParamConstant.EXTRA_SHOP_ID);
        shopFavouriteListPresenter.attachView(this);
    }

    @Override
    public void loadData(int page) {
        if (this.shopInfo == null) {
            shopFavouriteListPresenter.getShopInfo(shopId);
        } else {
            shopFavouriteListPresenter.getShopFavouriteList(shopId, page);
        }
    }

    @Override
    protected ShopFavouriteAdapterTypeFactory getAdapterTypeFactory() {
        return new ShopFavouriteAdapterTypeFactory(this);
    }

    @Override
    public void onItemClicked(ShopFavouriteViewModel shopFavouriteViewModel) {
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
        shopFavouriteListPresenter.getShopFavouriteList(shopId, getCurrentPage());
    }

    @Override
    public void onErrorToggleFavourite(Throwable throwable) {
        if (!shopFavouriteListPresenter.isLoggedIn()) {
            Intent intent = ((ShopModuleRouter) getActivity().getApplication()).getLoginIntent(getContext());
            startActivityForResult(intent, REQUEST_CODE_USER_LOGIN);
            return;
        }
        NetworkErrorHelper.showRedCloseSnackbar(getView(), ErrorHandler.getErrorMessage(getContext(), throwable));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_USER_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                shopFavouriteListPresenter.toggleFavouriteShop(shopId);
            }
        }
    }

    @Override
    public void onSuccessToggleFavourite(boolean successValue) {
        loadInitialData();
        getActivity().setResult(Activity.RESULT_OK);
    }

    @Override
    protected Visitable getEmptyDataViewModel() {
        EmptyModel emptyModel = new EmptyModel();
        emptyModel.setIconRes(R.drawable.ic_empty_state);
        if (shopFavouriteListPresenter.isMyShop(shopId)) {
            emptyModel.setTitle(getString(R.string.shop_product_my_empty_follower_title));
            emptyModel.setContent("");
            emptyModel.setButtonTitle("");
        } else {
            emptyModel.setTitle(getString(R.string.shop_product_empty_follower_title));
            emptyModel.setContent(getString(R.string.shop_product_empty_product_title_desc, shopInfo.getInfo().getShopName()));
            emptyModel.setButtonTitle(getString(R.string.shop_page_label_follow));
            if (shopInfo != null) {
                shopPageTracking.impressionFollowFromZeroFollower(CustomDimensionShopPage.create(shopInfo));
            }
        }
        return emptyModel;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public int getDefaultInitialPage() {
        return DEFAULT_INITIAL_PAGE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shopFavouriteListPresenter != null) {
            shopFavouriteListPresenter.detachView();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        shopPageTracking.sendAllTrackingQueue();
    }

    @Override
    public void onEmptyContentItemTextClicked() {
        // no-op
    }

    @Override
    public void onEmptyButtonClicked() {
        if (shopInfo != null) {
            shopPageTracking.followFromZeroFollower(CustomDimensionShopPage.create(shopInfo));
            shopFavouriteListPresenter.toggleFavouriteShop(shopId);
        }
    }

}
