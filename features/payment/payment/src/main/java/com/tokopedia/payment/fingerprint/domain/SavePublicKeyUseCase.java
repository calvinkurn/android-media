package com.tokopedia.payment.fingerprint.domain;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/27/18.
 */

public class SavePublicKeyUseCase extends UseCase<Boolean> {

    public static final String USER_ID = "user_id";
    public static final String PUBLIC_KEY = "public_key";
    private FingerprintRepository fingerprintRepository;
    private UserSession userSession;

    @Inject
    public SavePublicKeyUseCase(FingerprintRepository fingerprintRepository, UserSession userSession) {
        this.fingerprintRepository = fingerprintRepository;
        this.userSession = userSession;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        Map<String, String> params = AuthUtil.generateParamsNetwork(
                userSession.getUserId(), userSession.getDeviceId(), new TKPDMapParam<>()
        );
        requestParams.putAllString(params);
        return fingerprintRepository.savePublicKey(requestParams.getParamsAllValueInString());
    }

    public RequestParams createRequestParams(String userId, String publicKey) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(USER_ID, userId);
        requestParams.putString(PUBLIC_KEY, publicKey);
        return requestParams;
    }
}
