package com.tokopedia.instantloan.data.mapper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tokopedia.instantloan.data.model.response.ResponsePhoneData;
import com.tokopedia.instantloan.domain.model.PhoneDataModelDomain;

import javax.inject.Inject;

/**
 * Created by lavekush on 22/03/18.
 */

public class PhoneDataMapper {

    @Inject
    public PhoneDataMapper() {
    }

    @Nullable
    public PhoneDataModelDomain transform(@NonNull ResponsePhoneData response) {
        return response.getData() == null
                ? null : new PhoneDataModelDomain(response.getData().getMobileDeviceId());
    }
}
