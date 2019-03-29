package com.tokopedia.product.manage.item.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.product.manage.item.main.base.data.model.ProductPictureViewModel;
import com.tokopedia.product.manage.item.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.product.manage.item.variant.data.model.variantbyprd.ProductVariantViewModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public interface ProductEditModuleRouter {
    ProductComponent getProductComponent();
    Intent getLoginIntent(Context context);

    void goToGMSubscribe(Activity activity);

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
