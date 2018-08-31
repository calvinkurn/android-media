package com.tokopedia.digital.widget.view.listener;

import android.content.Context;

import com.tokopedia.digital.cart.presentation.listener.IBaseView;
import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.HistoryClientNumber;

/**
 * Created by Rizky on 15/01/18.
 */

public interface IDigitalWidgetView extends IBaseView {

    void renderCategory(BaseDigitalProductView digitalProductView, CategoryData categoryData,
                        HistoryClientNumber historyClientNumber);

    Context getContext();

}
