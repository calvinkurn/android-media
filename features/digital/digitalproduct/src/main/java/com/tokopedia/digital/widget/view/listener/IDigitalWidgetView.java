package com.tokopedia.digital.widget.view.listener;

import android.content.Context;

import com.tokopedia.common_digital.product.presentation.model.CategoryData;
import com.tokopedia.common_digital.product.presentation.model.HistoryClientNumber;
import com.tokopedia.digital.cart.listener.IBaseView;
import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;

/**
 * Created by Rizky on 15/01/18.
 */

public interface IDigitalWidgetView extends IBaseView {

    void renderCategory(BaseDigitalProductView digitalProductView, CategoryData categoryData,
                        HistoryClientNumber historyClientNumber);

    Context getContext();

}
