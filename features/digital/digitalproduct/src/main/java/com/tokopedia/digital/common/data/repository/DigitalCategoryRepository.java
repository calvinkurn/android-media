package com.tokopedia.digital.common.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.common.data.source.CategoryDetailDataSource;
import com.tokopedia.digital.common.data.source.FavoriteListDataSource;
import com.tokopedia.digital.common.domain.IDigitalCategoryRepository;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.widget.view.model.DigitalNumberList;

import rx.Observable;

/**
 * @author rizkyfadillah on 19/01/18.
 */

public class DigitalCategoryRepository implements IDigitalCategoryRepository {

    private CategoryDetailDataSource categoryDetailDataSource;
    private FavoriteListDataSource favoriteListDataSource;

    public DigitalCategoryRepository(CategoryDetailDataSource categoryDetailDataSource,
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
    public Observable<String> getHelpUrl(String categoryId) {
        return categoryDetailDataSource.getHelpUrl(categoryId);
    }

}
