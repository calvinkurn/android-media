package com.tokopedia.digital.widget.listener;

import com.tokopedia.digital.product.model.CategoryData;
import com.tokopedia.digital.product.model.HistoryClientNumber;
import com.tokopedia.digital.product.model.ProductDigitalData;

/**
 * Created by Rizky on 15/01/18.
 */

public interface IDigitalWidgetView {

    void renderCategoryProductDataStyle1(CategoryData categoryData, HistoryClientNumber historyClientNumber);

    void renderCategoryProductDataStyle2(CategoryData categoryData, HistoryClientNumber historyClientNumber);

    void renderCategoryProductDataStyle3(CategoryData categoryData, HistoryClientNumber historyClientNumber);

}
