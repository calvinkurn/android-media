package com.tokopedia.digital.common.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.product.data.source.CategoryDetailDataSource;
import com.tokopedia.digital.product.data.source.FavoriteListDataSource;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.widget.data.entity.category.CategoryEntity;
import com.tokopedia.digital.widget.data.entity.status.StatusEntity;
import com.tokopedia.digital.widget.view.model.DigitalNumberList;

import java.util.List;

import rx.Observable;

/**
 * Created by Rizky on 19/01/18.
 */

public class DigitalRepository implements IDigitalRepository {

    private CategoryDetailDataSource categoryDetailDataSource;
    private FavoriteListDataSource favoriteListDataSource;

    public DigitalRepository(CategoryDetailDataSource categoryDetailDataSource,
                             FavoriteListDataSource favoriteListDataSource) {
        this.categoryDetailDataSource = categoryDetailDataSource;
        this.favoriteListDataSource = favoriteListDataSource;
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
    public Observable<List<CategoryEntity>> getCategoryList() {
        return null;
    }

    @Override
    public Observable<StatusEntity> getObservableStatusNetwork() {
        return null;
    }

}
