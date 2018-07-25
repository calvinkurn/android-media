package com.tokopedia.shop.product.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;

import java.util.List;
@Deprecated
public interface ShopProductListLimitedView extends BaseListViewListener<ShopProductBaseViewModel> {

    void onErrorRemoveFromWishList(Throwable e);

    void onSuccessRemoveFromWishList(String productId, Boolean value);

    void onErrorAddToWishList(Throwable e);

    void onSuccessAddToWishList(String productId, Boolean value);

    void hideLoadingDialog();

    void showLoadingDialog();

    void renderList(@NonNull List<ShopProductBaseViewModel> list, boolean hasNextPage, boolean hasProduct);
}
