package com.tokopedia.digital.common.data.repository;

import com.tokopedia.digital.widget.view.model.category.Category;
import com.tokopedia.digital.widget.view.model.status.Status;

import java.util.List;

import rx.Observable;

/**
 * Created by Rizky on 22/01/18.
 */

public interface IDigitalWidgetRepository {

    Observable<Status> getObservableStatus();

    Observable<List<Category>> getObservableCategoryData();

}
