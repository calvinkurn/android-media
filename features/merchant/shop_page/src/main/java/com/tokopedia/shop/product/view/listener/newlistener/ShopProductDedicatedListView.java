package com.tokopedia.shop.product.view.listener.newlistener;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.view.model.newmodel.BaseShopProductViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductPromoViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductViewModel;

import java.util.List;

public interface ShopProductDedicatedListView extends BaseListViewListener<BaseShopProductViewModel> {

    void onErrorRemoveFromWishList(Throwable e);

    void onSuccessRemoveFromWishList(String productId, Boolean value);

    void onErrorAddToWishList(Throwable e);

    void onSuccessAddToWishList(String productId, Boolean value);

    void renderProductList(@NonNull List<ShopProductViewModel> list, boolean hasNextPage);

    void onSuccessGetEtalaseList(List<ShopEtalaseViewModel> shopEtalaseViewModelList);

    void onErrorGetEtalaseList(Throwable e);

    void onSuccessGetShopInfo(ShopInfo shopInfo);

    void onErrorGetShopInfo(Throwable e);

    void onSuccessGetEtalaseName(String etalaseId, String etalaseName);


}

