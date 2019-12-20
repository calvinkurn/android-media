package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.apiservices.user.FaveShopActService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.FaveShopMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.FavoriteShopDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import rx.Observable;

/**
 * @author by nisie on 9/26/17.
 */

public class CloudFaveShopDataSource {
    private final FaveShopActService faveShopActService;
    private final FaveShopMapper faveShopMapper;
    private UserSessionInterface userSession;

    public CloudFaveShopDataSource(FaveShopActService faveShopActService, FaveShopMapper
            faveShopMapper, UserSessionInterface userSession) {
        this.faveShopActService = faveShopActService;
        this.faveShopMapper = faveShopMapper;
        this.userSession = userSession;
    }


    public Observable<FavoriteShopDomain> favoriteShop(RequestParams requestParams) {
        return faveShopActService.getApi()
                .faveShop(AuthHelper.generateParamsNetwork(
                        userSession.getUserId(),
                        userSession.getDeviceId(),
                        requestParams.getParamsAllValueInString()
                ))
                .map(faveShopMapper);
    }
}
