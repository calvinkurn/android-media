package com.tokopedia.digital.common.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.common.data.source.CategoryDetailDataSource;
import com.tokopedia.digital.common.data.source.CategoryListDataSource;
import com.tokopedia.digital.common.data.source.FavoriteListDataSource;
import com.tokopedia.digital.common.data.source.StatusDataSource;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.widget.view.model.DigitalNumberList;
import com.tokopedia.digital.widget.view.model.category.Category;
import com.tokopedia.digital.widget.view.model.status.Status;

import java.util.List;

import rx.Observable;

/**
 * Created by Rizky on 19/01/18.
 */

public class DigitalRepository implements IDigitalRepository {

    private CategoryDetailDataSource categoryDetailDataSource;
    private FavoriteListDataSource favoriteListDataSource;
    private StatusDataSource statusDataSource;
    private CategoryListDataSource categoryListDataSource;

    public DigitalRepository(CategoryDetailDataSource categoryDetailDataSource,
                             FavoriteListDataSource favoriteListDataSource,
                             StatusDataSource statusDataSource,
                             CategoryListDataSource categoryListDataSource) {
        this.categoryDetailDataSource = categoryDetailDataSource;
        this.favoriteListDataSource = favoriteListDataSource;
        this.statusDataSource = statusDataSource;
        this.categoryListDataSource = categoryListDataSource;
    }

    @Override
    public Observable<CategoryData> getCategory(String categoryId, TKPDMapParam<String, String> param) {
        return categoryDetailDataSource.getCategory(categoryId, param);
    }

    @Override
    public Observable<DigitalNumberList> getFavoriteList(TKPDMapParam<String, String> param) {
        return favoriteListDataSource.getFavoriteList(param);
    }

    @Override
    public Observable<List<Category>> getCategoryList() {
        return categoryListDataSource.getCategoryList();
    }

    @Override
    public Observable<Status> getObservableStatusNetwork() {
        return statusDataSource.getStatus();
    }

}
