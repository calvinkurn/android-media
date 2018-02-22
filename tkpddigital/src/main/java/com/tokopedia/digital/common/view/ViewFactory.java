package com.tokopedia.digital.common.view;

import android.content.Context;

import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.common.view.compoundview.CategoryProductStyle1View;
import com.tokopedia.digital.common.view.compoundview.CategoryProductStyle2View;
import com.tokopedia.digital.common.view.compoundview.CategoryProductStyle3View;
import com.tokopedia.digital.common.view.compoundview.CategoryProductStyle99View;
import com.tokopedia.digital.product.view.model.CategoryData;

/**
 * Created by Rizky on 24/01/18.
 */

public class ViewFactory {

    public static BaseDigitalProductView renderCategoryDataAndBannerToView(Context context,
                                                                           String operatorStyle) {
        switch (operatorStyle) {
            case CategoryData.STYLE_PRODUCT_CATEGORY_1:
                return new CategoryProductStyle1View(context);
            case CategoryData.STYLE_PRODUCT_CATEGORY_2:
                return new CategoryProductStyle2View(context);
            case CategoryData.STYLE_PRODUCT_CATEGORY_3:
            case CategoryData.STYLE_PRODUCT_CATEGORY_4:
            case CategoryData.STYLE_PRODUCT_CATEGORY_5:
                return new CategoryProductStyle3View(context);
            case CategoryData.STYLE_PRODUCT_CATEGORY_99:
                return new CategoryProductStyle99View(context);

        }
        return null;
    }

}
