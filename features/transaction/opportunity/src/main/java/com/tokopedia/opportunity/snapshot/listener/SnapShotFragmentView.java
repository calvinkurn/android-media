package com.tokopedia.opportunity.snapshot.listener;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;

/**
 * Created by hangnadi on 3/1/17.
 */
public interface SnapShotFragmentView extends CustomerView{
    void onProductDetailLoaded(ProductDetailData productData);

    void onProductPictureClicked(Bundle bundle);

    void showProgressLoading();

    void hideProgressLoading();

    void showProductDetailRetry();

    void onNullData();

    void showToastMessage(String message);

    void closeView();

    void showFullScreenError();

    void renderTempProductData(ProductPass productPass);

    void onProductShopNameClicked(String shopId);

    void onProductShopRatingClicked(String shopId);

    void onProductShopAvatarClicked(String shopId);
}
