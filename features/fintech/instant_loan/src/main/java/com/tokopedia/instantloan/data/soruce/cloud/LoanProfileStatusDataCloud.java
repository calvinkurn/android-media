package com.tokopedia.instantloan.data.soruce.cloud;

import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.instantloan.data.mapper.LoanProfileStatusMapper;
import com.tokopedia.instantloan.data.model.response.ResponseLoanProfileStatus;
import com.tokopedia.instantloan.data.model.response.ResponseUserProfileStatus;
import com.tokopedia.instantloan.data.model.response.UserProfileLoanEntity;
import com.tokopedia.instantloan.data.soruce.LoanProfileStatusDataStore;
import com.tokopedia.instantloan.data.soruce.cloud.api.InstantLoanApi;
import com.tokopedia.instantloan.domain.model.LoanProfileStatusModelDomain;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by lavekush on 21/03/18.
 */

public class LoanProfileStatusDataCloud implements LoanProfileStatusDataStore {

    private final InstantLoanApi mApi;

    private LoanProfileStatusMapper mMapper;

    @Inject
    public LoanProfileStatusDataCloud(InstantLoanApi api, LoanProfileStatusMapper mapper) {
        this.mApi = api;
        this.mMapper = mapper;
    }

    @Override
    public Observable<UserProfileLoanEntity> loanProfileStatus() {
        return mApi.getUserProfileStatus().map(new Func1<Response<ResponseUserProfileStatus>, UserProfileLoanEntity>() {
            @Override
            public UserProfileLoanEntity call(Response<ResponseUserProfileStatus> response) {
                if (response.isSuccessful()) {
                    ResponseUserProfileStatus responseLoanProfileStatus = response.body();
                    return mMapper.transformResponse(responseLoanProfileStatus);
                } else {
                    throw new RuntimeException(
                            ErrorNetMessage.MESSAGE_ERROR_DEFAULT
                    );
                }
            }
        });
    }
}
