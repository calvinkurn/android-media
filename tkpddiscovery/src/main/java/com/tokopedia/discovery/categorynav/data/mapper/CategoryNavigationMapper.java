package com.tokopedia.discovery.categorynav.data.mapper;

import com.tokopedia.core.network.entity.categories.Data;
import com.tokopedia.discovery.categorynav.domain.model.CategoryNavDomainModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by alifa on 7/6/17.
 */

public class CategoryNavigationMapper implements Func1<Response<Data>,CategoryNavDomainModel> {

    @Override
    public CategoryNavDomainModel call(Response<Data> dataResponse) {
        return null;
    }
}
