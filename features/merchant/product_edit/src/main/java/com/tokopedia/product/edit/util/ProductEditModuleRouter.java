package com.tokopedia.product.edit.util;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.product.edit.common.di.component.ProductComponent;
import com.tokopedia.product.edit.common.model.edit.ProductPictureViewModel;
import com.tokopedia.product.edit.common.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.product.edit.common.model.variantbyprd.ProductVariantViewModel;
import com.tokopedia.product.edit.view.service.UploadProductService;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public interface ProductEditModuleRouter {
    ProductComponent getProductComponent();
    Intent getLoginIntent(Context context);

    void goToGMSubscribe(Context context);

    @Nullable
    Intent createIntentProductVariant(Context context, ArrayList<ProductVariantByCatModel> productVariantByCatModelList,
                                      ProductVariantViewModel productVariant, int productPriceCurrency, double productPrice,
                                      int productStock, boolean officialStore, String productSku,
                                      boolean needRetainImage, ProductPictureViewModel productSizeChart, boolean hasOriginalVariantLevel1,
                                      boolean hasOriginalVariantLevel2, boolean hasWholesale);

    Intent getManageProductIntent(Context context);

    Intent createIntentProductEtalase(Context context, int etalaseId);

    Intent getCategoryPickerIntent(Context context, int categoryId);
}
