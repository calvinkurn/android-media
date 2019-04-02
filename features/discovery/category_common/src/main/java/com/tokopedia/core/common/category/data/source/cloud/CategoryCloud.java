package com.tokopedia.core.common.category.data.source.cloud;

import com.tokopedia.core.common.category.data.mapper.SimpleResponseMapper;
import com.tokopedia.core.common.category.data.source.cloud.api.HadesCategoryApi;
import com.tokopedia.core.common.category.data.source.cloud.model.CategoryServiceModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/4/17.
 */
public class CategoryCloud {

    private final HadesCategoryApi api;

    @Inject
    public CategoryCloud(HadesCategoryApi api) {
        this.api = api;
    }

    public Observable<CategoryServiceModel> fetchDataFromNetwork() {
        return api.fetchCategory()
                .map(new SimpleResponseMapper<>());
    }
}
