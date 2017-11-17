package com.tokopedia.tkpd.home.recharge.view;

import com.tokopedia.digital.widget.model.category.Category;

import java.util.List;

/**
 *
 * @author kulomady on 7/13/2016.
 */
public interface RechargeCategoryView {

    void renderDataRechargeCategory(List<Category> rechargeCategory, boolean bool);

    void failedRenderDataRechargeCategory();

    void renderErrorNetwork();

    void renderErrorMessage();
}