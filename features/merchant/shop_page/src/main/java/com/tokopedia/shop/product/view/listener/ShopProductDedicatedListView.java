package com.tokopedia.shop.product.view.listener;

import androidx.annotation.NonNull;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.view.model.BaseShopProductViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

import java.util.ArrayList;
import java.util.List;

public interface ShopProductDedicatedListView extends BaseListViewListener<BaseShopProductViewModel> {

    void onErrorAddToWishList(Throwable e);

    void renderProductList(@NonNull List<ShopProductViewModel> list, boolean hasNextPage);

    void onSuccessGetEtalaseList(List<ShopEtalaseViewModel> shopEtalaseViewModelList);

    void onErrorGetEtalaseList(Throwable e);

    void onSuccessGetShopInfo(ShopInfo shopInfo);

    void onErrorGetShopInfo(Throwable e);

}

