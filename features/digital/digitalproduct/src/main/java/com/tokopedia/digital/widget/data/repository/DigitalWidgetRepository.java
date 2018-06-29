package com.tokopedia.digital.widget.data.repository;

import com.tokopedia.digital.common.data.source.CategoryListDataSource;
import com.tokopedia.digital.common.data.source.StatusDataSource;
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

    public DigitalWidgetRepository(StatusDataSource statusDataSource,
                                   CategoryListDataSource categoryListDataSource) {
        this.statusDataSource = statusDataSource;
        this.categoryListDataSource = categoryListDataSource;
    }

    @Override
    public Observable<Status> getObservableStatus() {
        return statusDataSource.getStatus();
    }

    @Override
    public Observable<List<Category>> getObservableCategoryList() {
        return categoryListDataSource.getCategoryList();
    }

}
