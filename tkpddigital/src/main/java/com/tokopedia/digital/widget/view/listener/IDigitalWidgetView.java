package com.tokopedia.digital.widget.view.listener;

import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.HistoryClientNumber;

/**
 * Created by Rizky on 15/01/18.
 */

public interface IDigitalWidgetView {

    void renderCategory(BaseDigitalProductView digitalProductView, CategoryData categoryData, HistoryClientNumber historyClientNumber);

}
