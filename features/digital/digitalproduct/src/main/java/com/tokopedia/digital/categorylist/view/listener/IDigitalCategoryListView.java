package com.tokopedia.digital.categorylist.view.listener;

import android.content.Context;

import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.digital.cart.presentation.listener.IBaseView;
import com.tokopedia.digital.categorylist.view.model.DigitalCategoryItemData;

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

    void renderTokoCashData(TokoCashData tokoCashData);

    Context getAppContext();
}
