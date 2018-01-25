package com.tokopedia.digital.categorylist.data.repository;

import com.tokopedia.digital.categorylist.view.model.DigitalCategoryItemData;

import java.util.List;

import rx.Observable;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public interface IDigitalCategoryListRepository {

    Observable<List<DigitalCategoryItemData>> getDigitalCategoryItemDataList();
}
