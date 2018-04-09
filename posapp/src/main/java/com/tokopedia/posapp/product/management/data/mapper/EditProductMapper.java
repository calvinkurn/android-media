package com.tokopedia.posapp.product.management.data.mapper;

import com.tokopedia.posapp.base.domain.model.DataStatus;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author okasurya on 4/9/18.
 */

public class EditProductMapper implements Func1<Response, DataStatus> {
    @Inject
    EditProductMapper() {

    }

    @Override
    public DataStatus call(Response response) {
        return null;
    }
}