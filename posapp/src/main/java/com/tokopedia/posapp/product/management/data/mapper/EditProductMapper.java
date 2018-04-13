package com.tokopedia.posapp.product.management.data.mapper;

import com.tokopedia.posapp.base.data.pojo.PosSimpleResponse;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.product.management.ProductManagementConstant;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author okasurya on 4/9/18.
 */

public class EditProductMapper implements Func1<Response<PosSimpleResponse<String>>, DataStatus> {
    @Inject
    EditProductMapper() {

    }

    @Override
    public DataStatus call(Response<PosSimpleResponse<String>> response) {
        if (response.isSuccessful()) {
            if (response.body().getData().getStatus().equalsIgnoreCase("ok")) {
                DataStatus dataStatus = new DataStatus();
                dataStatus.setStatus(DataStatus.OK);
                dataStatus.setMessage(response.body().getData().getMessage());
                return dataStatus;
            }
        }
        return DataStatus.defaultErrorResult(ProductManagementConstant.Message.DEFAULT_ERROR_MESSAGE);
    }
}