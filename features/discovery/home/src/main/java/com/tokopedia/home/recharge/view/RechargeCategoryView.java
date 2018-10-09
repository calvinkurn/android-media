package com.tokopedia.home.recharge.view;

import com.tokopedia.digital.widget.view.model.category.Category;

import java.util.List;

/**
 *
 * @author kulomady on 7/13/2016.
 */
public interface RechargeCategoryView {

    void renderDataRechargeCategory(List<Category> rechargeCategory);

    void failedRenderDataRechargeCategory();

    void renderErrorNetwork();

    void renderErrorMessage();
}