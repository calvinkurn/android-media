package com.tokopedia.digital.widget.listener;

import com.tokopedia.digital.cart.listener.IBaseView;
import com.tokopedia.digital.tokocash.model.tokocashitem.TokoCashBalanceData;
import com.tokopedia.digital.widget.model.DigitalCategoryItemData;

import java.util.List;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public interface IDigitalCategoryListView extends IBaseView {
    void renderDigitalCategoryDataList(List<DigitalCategoryItemData> digitalCategoryItemDataList);

    void renderErrorGetDigitalCategoryList(String message);

    void renderErrorHttpGetDigitalCategoryList(String message);

    void renderErrorNoConnectionGetDigitalCategoryList(String message);

    void renderErrorTimeoutConnectionGetDigitalCategoryList(String message);

    void disableSwipeRefresh();

    void enableSwipeRefresh();

    boolean isUserLogin();

    void renderTokoCashData(TokoCashBalanceData tokoCashData);
}
