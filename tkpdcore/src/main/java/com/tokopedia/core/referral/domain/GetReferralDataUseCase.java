package com.tokopedia.core.referral.domain;

import android.content.Context;

import com.google.gson.JsonObject;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.referral.data.ReferralCodeEntity;
import com.tokopedia.core.util.SessionHandler;

import rx.Observable;

/**
 * Created by ashwanityagi on 22/01/18.
 */

public class GetReferralDataUseCase extends UseCase<ReferralCodeEntity> {

    private final ReferralRepository referralRepository;
    private Context context;
    private final String userId = "user_id";
    private final String msisdn = "msisdn";
    private final String data = "data";

    public GetReferralDataUseCase(ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread, ReferralRepository referralRepository,Context context) {
        super(threadExecutor, postExecutionThread);
        this.referralRepository = referralRepository;
        this.context = context;
    }

    @Override
    public Observable<ReferralCodeEntity> createObservable(RequestParams requestParams) {
        JsonObject requestBody = new JsonObject();
        JsonObject baseRequestBody = new JsonObject();
        requestBody.addProperty(userId, Integer.parseInt(SessionHandler.getLoginID(context)));
        requestBody.addProperty(msisdn, SessionHandler.getPhoneNumber());
        baseRequestBody.add(data, requestBody);
        return this.referralRepository.getReferralVoucherCode(baseRequestBody.toString());
    }
}
