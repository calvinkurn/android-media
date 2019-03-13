package com.tokopedia.mitratoppers.preapprove.data.source.cloud;

import com.tokopedia.abstraction.common.network.mapper.DataResponseMapper;
import com.tokopedia.mitratoppers.preapprove.data.model.response.preapprove.ResponsePreApprove;
import com.tokopedia.mitratoppers.preapprove.data.source.cloud.api.MitraToppersApi;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import rx.Observable;

public class MitraToppersPreApproveDataCloud {
    private final MitraToppersApi api;
    private final UserSessionInterface userSession;

    @Inject
    public MitraToppersPreApproveDataCloud(MitraToppersApi api,
                                           UserSessionInterface userSession) {
        this.api = api;
        this.userSession = userSession;
    }

    public Observable<ResponsePreApprove> getPreApproveBalance() {
        return api.preApproveBalance(userSession.getShopId())
                .map(new DataResponseMapper<ResponsePreApprove>());
    }

}
