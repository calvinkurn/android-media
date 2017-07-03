package com.tokopedia.digital.widget.listener;

import com.tokopedia.digital.cart.listener.IBaseView;
import com.tokopedia.digital.widget.data.entity.DigitalCategoryItemData;

import java.util.List;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public interface IDigitalCategoryListView extends IBaseView {
    void renderDigitalCategoryDataList(List<DigitalCategoryItemData> digitalCategoryItemDataList);
}
