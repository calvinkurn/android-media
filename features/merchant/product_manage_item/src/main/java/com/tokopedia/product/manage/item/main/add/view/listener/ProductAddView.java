package com.tokopedia.product.manage.item.main.add.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.product.manage.item.variant.data.model.variantbycat.ProductVariantByCatModel;

import java.util.List;

/**
 * @author sebastianuskh on 4/13/17.
 */

public interface ProductAddView extends CustomerView {

    void onSuccessStoreProductToDraft(long productId, boolean isUploading);

    void onErrorStoreProductToDraftWhenUpload(String errorMessage);

    void onErrorStoreProductToDraftWhenBackPressed(String errorMessage);

    void onSuccessLoadShopInfo(boolean goldMerchant, boolean freeReturn, boolean officialStore);

    void onErrorLoadShopInfo(String errorMessage);

    void onSuccessGetProductVariantCat(List<ProductVariantByCatModel> productVariantByCatModelList);

    void onErrorGetProductVariantByCat(Throwable throwable);

    long getProductDraftId();

}