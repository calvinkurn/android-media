package com.tokopedia.instantloan.view.mapper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tokopedia.instantloan.domain.model.PhoneDataModelDomain;
import com.tokopedia.instantloan.view.model.PhoneDataViewModel;

import javax.inject.Inject;

/**
 * Created by lavekush on 22/03/18.
 */

public class PhoneDataMapper {

    @Inject
    public PhoneDataMapper() {
    }

    @Nullable
    public PhoneDataViewModel transform(@NonNull PhoneDataModelDomain domainModel) {
        return new PhoneDataViewModel(domainModel.getMobileDeviceId());
    }
}
