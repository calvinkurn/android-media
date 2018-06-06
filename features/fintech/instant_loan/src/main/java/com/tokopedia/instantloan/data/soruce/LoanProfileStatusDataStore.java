package com.tokopedia.instantloan.data.soruce;

import com.tokopedia.instantloan.data.model.response.UserProfileLoanEntity;
import com.tokopedia.instantloan.domain.model.LoanProfileStatusModelDomain;

import rx.Observable;

/**
 * Created by lavekush on 22/03/18.
 */

public interface LoanProfileStatusDataStore {

    Observable<UserProfileLoanEntity> loanProfileStatus();
}
