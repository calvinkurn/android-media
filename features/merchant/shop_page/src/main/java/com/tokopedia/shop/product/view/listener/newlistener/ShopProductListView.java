package com.tokopedia.shop.product.view.listener.newlistener;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.shop.product.view.model.ShopProductBaseViewModel;
import com.tokopedia.shop.product.view.model.newmodel.BaseShopProductViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductPromoViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductViewModel;

import java.util.List;

public interface ShopProductListView extends BaseListViewListener<BaseShopProductViewModel> {

    void onErrorRemoveFromWishList(Throwable e);

    void onSuccessRemoveFromWishList(String productId, Boolean value);

    void onErrorAddToWishList(Throwable e);

    void onSuccessAddToWishList(String productId, Boolean value);

    void hideLoadingDialog();

    void showLoadingDialog();

    void renderShopProductPromo(ShopProductPromoViewModel shopProductPromoViewModel);

    void renderProductList(@NonNull List<ShopProductViewModel> list, boolean hasNextPage);
}

