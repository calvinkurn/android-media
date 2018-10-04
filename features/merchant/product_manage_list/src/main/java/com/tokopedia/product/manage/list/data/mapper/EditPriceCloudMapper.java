package com.tokopedia.product.manage.list.data.mapper;

import com.tokopedia.product.manage.list.data.model.ResponseEditPriceData;

import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public class EditPriceCloudMapper implements Func1<ResponseEditPriceData, Boolean> {
    @Override
    public Boolean call(ResponseEditPriceData responseEditPriceData) {
        return responseEditPriceData != null && responseEditPriceData.getIs_success() == 1;
    }
}
