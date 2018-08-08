package com.tokopedia.discovery.categorynav.data.source;

import android.content.Context;
import com.tokopedia.core.network.apiservices.hades.apis.HadesApi;
import com.tokopedia.discovery.categorynav.data.mapper.CategoryChildrenNavigationMapper;
import com.tokopedia.discovery.categorynav.data.mapper.CategoryNavigationMapper;
import com.tokopedia.discovery.categorynav.domain.model.Category;
import com.tokopedia.discovery.categorynav.domain.model.CategoryNavDomainModel;
import com.tokopedia.discovery.categorynav.domain.model.ChildCategory;

import java.util.List;

import rx.Observable;

/**
 * @author by alifa on 7/6/17.
 */

public class CategoryNavigationDataSource {

    private final Context context;
    private final HadesApi hadesApi;
    private final CategoryNavigationMapper mapper;
    private final CategoryChildrenNavigationMapper childrenMapper;


    public CategoryNavigationDataSource(Context context, HadesApi hadesApi, CategoryNavigationMapper mapper, CategoryChildrenNavigationMapper childrenMapper) {
        this.context = context;
        this.hadesApi = hadesApi;
        this.mapper = mapper;
        this.childrenMapper = childrenMapper;
    }

    public Observable<CategoryNavDomainModel> getRootCategory(String categoryId) {

        return hadesApi.getNavigationCategoriesRoot(categoryId).map(mapper);

    }

    public Observable<List<Category>> getCategoryChildren(String categoryId) {

        return hadesApi.getNavigationCategories(categoryId).map(childrenMapper);

    }
}
