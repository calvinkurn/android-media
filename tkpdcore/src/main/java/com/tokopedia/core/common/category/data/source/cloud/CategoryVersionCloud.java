package com.tokopedia.core.common.category.data.source.cloud;

import com.tokopedia.core.base.common.util.GetData;
import com.tokopedia.core.common.category.data.source.cloud.api.HadesCategoryApi;
import com.tokopedia.core.common.category.data.source.cloud.model.CategoryVersionServiceModel;
import com.tokopedia.core.common.mapper.SimpleResponseMapper;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 3/8/17.
 */
public class CategoryVersionCloud {
    private final HadesCategoryApi api;

    @Inject
    public CategoryVersionCloud(HadesCategoryApi api) {
        this.api = api;
    }

    public Observable<CategoryVersionServiceModel> checkVersion() {
        return api
                .checkVersion()
                .map(new SimpleResponseMapper<CategoryVersionServiceModel>());
    }
}
