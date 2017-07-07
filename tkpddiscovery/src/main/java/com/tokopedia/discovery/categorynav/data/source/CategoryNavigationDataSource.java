package com.tokopedia.discovery.categorynav.data.source;

import android.content.Context;
import com.tokopedia.core.network.apiservices.hades.apis.HadesApi;
import com.tokopedia.discovery.categorynav.data.mapper.CategoryNavigationMapper;
import com.tokopedia.discovery.categorynav.domain.model.CategoryNavDomainModel;

import rx.Observable;

/**
 * @author by alifa on 7/6/17.
 */

public class CategoryNavigationDataSource {

    private final Context context;
    private final HadesApi hadesApi;
    private final CategoryNavigationMapper mapper;


    public CategoryNavigationDataSource(Context context, HadesApi hadesApi, CategoryNavigationMapper mapper) {
        this.context = context;
        this.hadesApi = hadesApi;
        this.mapper = mapper;
    }

    public Observable<CategoryNavDomainModel> getRootCategory(String categoryId) {

        return hadesApi.getNavigationCategoriesRoot(categoryId).map(mapper);

    }
}
