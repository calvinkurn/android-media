package com.tokopedia.tkpd.recharge.view;
import com.tokopedia.tkpd.recharge.model.category.CategoryData;

/**
 *
 * @author kulomady on 7/13/2016.
 */
public interface RechargeCategoryView {

    void renderDataRechargeCategory(CategoryData rechargeCategory);

    void failedRenderDataRechargeCategory();
}
