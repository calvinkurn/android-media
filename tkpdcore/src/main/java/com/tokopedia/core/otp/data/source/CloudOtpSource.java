package com.tokopedia.core.otp.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.otp.data.RequestOtpModel;
import com.tokopedia.core.otp.data.ValidateOtpModel;
import com.tokopedia.core.otp.data.mapper.RequestOtpMapper;
import com.tokopedia.core.otp.data.mapper.ValidateOtpMapper;
import com.tokopedia.core.util.SessionHandler;

import rx.Observable;

/**
 * Created by nisie on 3/7/17.
 */

public class CloudOtpSource {

    private final Context context;
    private AccountsService accountsService;
    private RequestOtpMapper requestOtpMapper;
    private ValidateOtpMapper validateOtpMapper;

    public CloudOtpSource(Context context, AccountsService accountsService,
                          RequestOtpMapper requestOtpMapper, ValidateOtpMapper validateOtpMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.requestOtpMapper = requestOtpMapper;
        this.validateOtpMapper = validateOtpMapper;
    }

    public Observable<RequestOtpModel> requestOtp(TKPDMapParam<String, Object> params) {
        String id = SessionHandler.getLoginID(context);
        if(id==null || id.isEmpty()){
            id = SessionHandler.getTempLoginSession(context);
        }
        return accountsService.getApi()
                .requestOtp(id, params).map(requestOtpMapper);
    }

    public Observable<ValidateOtpModel> validateOtp(TKPDMapParam<String, Object> params) {
        return accountsService.getApi()
                .validateOtp(params).map(validateOtpMapper);
    }
}
