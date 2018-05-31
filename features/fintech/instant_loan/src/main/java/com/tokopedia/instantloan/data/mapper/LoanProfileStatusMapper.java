package com.tokopedia.instantloan.data.mapper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tokopedia.instantloan.data.model.response.ResponseLoanProfileStatus;
import com.tokopedia.instantloan.domain.model.LoanProfileStatusModelDomain;

import javax.inject.Inject;

/**
 * Created by lavekush on 22/03/18.
 */

public class LoanProfileStatusMapper {

    @Inject
    public LoanProfileStatusMapper() {
    }

    @Nullable
    public LoanProfileStatusModelDomain transform(@NonNull ResponseLoanProfileStatus response) {
        return response.getData() == null
                ? null : new LoanProfileStatusModelDomain(response.getData().isSubmitted());
    }
}
