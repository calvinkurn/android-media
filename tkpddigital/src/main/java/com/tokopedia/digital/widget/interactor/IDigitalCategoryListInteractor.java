package com.tokopedia.digital.widget.interactor;

import com.tokopedia.core.drawer.model.topcastItem.TopCashItem;
import com.tokopedia.digital.widget.data.entity.DigitalCategoryItemData;

import java.util.List;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public interface IDigitalCategoryListInteractor {

    void getDigitalCategoryItemDataList(Subscriber<List<DigitalCategoryItemData>> subscriber);

    void getTokoCashData(Subscriber<TopCashItem> subscriber);
}
