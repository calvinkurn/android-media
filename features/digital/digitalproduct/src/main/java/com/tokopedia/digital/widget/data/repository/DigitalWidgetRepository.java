package com.tokopedia.digital.widget.data.repository;

import com.tokopedia.digital.common.data.source.CategoryListDataSource;
import com.tokopedia.digital.common.data.source.StatusDataSource;
import com.tokopedia.digital.widget.data.source.RecommendationListDataSource;
import com.tokopedia.digital.widget.view.model.Recommendation;
import com.tokopedia.digital.widget.view.model.category.Category;
import com.tokopedia.digital.widget.view.model.status.Status;

import java.util.List;

import rx.Observable;

/**
 * @author rizkyfadillah on 22/01/18.
 */

public class DigitalWidgetRepository implements IDigitalWidgetRepository {

    private StatusDataSource statusDataSource;
    private CategoryListDataSource categoryListDataSource;
    private RecommendationListDataSource recommendationListDataSource;

    public DigitalWidgetRepository(StatusDataSource statusDataSource,
                                   CategoryListDataSource categoryListDataSource,
                                   RecommendationListDataSource recommendationListDataSource) {
        this.statusDataSource = statusDataSource;
        this.categoryListDataSource = categoryListDataSource;
        this.recommendationListDataSource = recommendationListDataSource;
    }

    @Override
    public Observable<Status> getObservableStatus() {
        return statusDataSource.getStatus();
    }

    @Override
    public Observable<List<Category>> getObservableCategoryList() {
        return categoryListDataSource.getCategoryList();
    }

    @Override
    public Observable<List<Recommendation>> getRecommendationList(int deviceId) {
        return recommendationListDataSource.getRecommendationList(deviceId);
    }

}
