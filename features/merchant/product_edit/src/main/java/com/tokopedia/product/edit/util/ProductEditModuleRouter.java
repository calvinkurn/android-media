package com.tokopedia.product.edit.util;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.product.common.di.component.ProductComponent;

public interface ProductEditModuleRouter {
    ProductComponent getProductComponent();
    Intent getLoginIntent(Context context);
}
