package com.tokopedia.instantloan.view.mapper;

import android.support.annotation.NonNull;

import com.tokopedia.instantloan.domain.model.LoanProfileStatusModelDomain;
import com.tokopedia.instantloan.view.model.LoanProfileStatusViewModel;

import javax.inject.Inject;

/**
 * Created by lavekush on 22/03/18.
 */

public class LoanStatusMapper {

    @Inject
    public LoanStatusMapper() {
    }

    public LoanProfileStatusViewModel transform(@NonNull LoanProfileStatusModelDomain domainModel) {
        return new LoanProfileStatusViewModel(domainModel.isSubmitted());
    }
}
