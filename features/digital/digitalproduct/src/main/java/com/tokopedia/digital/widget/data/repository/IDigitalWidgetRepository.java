package com.tokopedia.digital.widget.data.repository;

import com.tokopedia.digital.widget.view.model.Recommendation;
import com.tokopedia.digital.widget.view.model.category.Category;
import com.tokopedia.digital.widget.view.model.status.Status;

import java.util.List;

import rx.Observable;

/**
 * @author rizkyfadillah on 22/01/18.
 */

public interface IDigitalWidgetRepository {

    Observable<Status> getObservableStatus();

    Observable<List<Category>> getObservableCategoryList();

    Observable<List<Recommendation>> getRecommendationList(int deviceId);

}
