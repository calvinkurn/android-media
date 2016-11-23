package com.tokopedia.home.recharge.view;

import com.tokopedia.core.database.model.category.CategoryData;

/**
 *
 * @author kulomady on 7/13/2016.
 */
public interface RechargeCategoryView {

    void renderDataRechargeCategory(CategoryData rechargeCategory);

    void failedRenderDataRechargeCategory();

    void renderErrorNetwork();
}
