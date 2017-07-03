package com.tokopedia.digital.widget.domain;

import com.tokopedia.digital.widget.data.entity.DigitalCategoryItemData;

import java.util.List;

import rx.Observable;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public interface IDigitalCategoryListRepository {

    Observable<List<DigitalCategoryItemData>> getDigitalCategoryItemDataList();
}
