package com.tokopedia.shop.product.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.view.model.BaseShopProductViewModel;
import com.tokopedia.shop.product.view.model.ShopProductPromoViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

import java.util.ArrayList;
import java.util.List;

public interface ShopProductListView extends BaseListViewListener<BaseShopProductViewModel> {

    void onErrorAddToWishList(Throwable e);

    void renderShopProductPromo(ShopProductPromoViewModel shopProductPromoViewModel);

    void renderProductList(@NonNull List<ShopProductViewModel> list, boolean hasNextPage);

    void onErrorGetProductFeature(Throwable e);

    void onSuccessGetProductFeature(@NonNull List<ShopProductViewModel> list);

    void onSuccessGetEtalaseList(List<ShopEtalaseViewModel> shopEtalaseViewModelList);

    void onErrorGetEtalaseList(Throwable e);

    void onSuccessGetEtalaseListByShop(ArrayList<ShopEtalaseModel> shopEtalaseModelList);

    void onErrorGetEtalaseListByShop(Throwable e);

    List<ShopEtalaseViewModel> getShopEtalaseViewModelList();
}

