package com.tokopedia.digital.widget.view.listener;

import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.HistoryClientNumber;

/**
 * Created by Rizky on 15/01/18.
 */

public interface IDigitalWidgetView {

    void renderCategoryProductDataStyle1(CategoryData categoryData, HistoryClientNumber historyClientNumber);

    void renderCategoryProductDataStyle2(CategoryData categoryData, HistoryClientNumber historyClientNumber);

    void renderCategoryProductDataStyle3(CategoryData categoryData, HistoryClientNumber historyClientNumber);

}
