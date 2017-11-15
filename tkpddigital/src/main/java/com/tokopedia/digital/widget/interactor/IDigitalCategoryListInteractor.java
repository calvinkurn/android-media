package com.tokopedia.digital.widget.interactor;

import com.tokopedia.digital.tokocash.model.tokocashitem.TokoCashBalanceData;
import com.tokopedia.digital.widget.model.DigitalCategoryItemData;

import java.util.List;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public interface IDigitalCategoryListInteractor {

    void getDigitalCategoryItemDataList(Subscriber<List<DigitalCategoryItemData>> subscriber);

    void getTokoCashData(Subscriber<TokoCashBalanceData> subscriber);
}
