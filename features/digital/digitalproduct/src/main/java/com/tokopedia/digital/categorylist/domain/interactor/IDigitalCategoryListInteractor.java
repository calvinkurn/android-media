package com.tokopedia.digital.categorylist.domain.interactor;

import android.content.Context;

import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.digital.categorylist.view.model.DigitalCategoryItemData;

import java.util.List;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public interface IDigitalCategoryListInteractor {

    void getDigitalCategoryItemDataList(Subscriber<List<DigitalCategoryItemData>> subscriber);

    void getTokoCashData(Subscriber<TokoCashData> subscriber, Context context);
}
