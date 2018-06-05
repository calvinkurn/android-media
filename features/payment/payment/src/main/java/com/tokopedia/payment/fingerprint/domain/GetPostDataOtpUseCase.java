package com.tokopedia.payment.fingerprint.domain;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/9/18.
 */

public class GetPostDataOtpUseCase extends UseCase<HashMap<String, String>> {

    public static final String TRANSACTION_ID = "transaction_id";
    private FingerprintRepository fingerprintRepository;
    private UserSession userSession;

    @Inject
    public GetPostDataOtpUseCase(FingerprintRepository fingerprintRepository, UserSession userSession) {
        this.fingerprintRepository = fingerprintRepository;
        this.userSession = userSession;
    }

    @Override
    public Observable<HashMap<String, String>> createObservable(final RequestParams requestParams) {
        TKPDMapParam<String, String> params = AuthUtil.generateParamsNetwork(userSession.getUserId(), userSession.getDeviceId(), new TKPDMapParam<String, String>());
        requestParams.putAllString(params);
        return fingerprintRepository.getPostDataOtp(requestParams.getString(TRANSACTION_ID, ""));
    }

    public RequestParams createRequestParams(String transactionId, String urlOtp) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(TRANSACTION_ID, transactionId);
        return requestParams;
    }
}
