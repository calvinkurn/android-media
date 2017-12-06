package com.tokopedia.tkpd.beranda.data.source;

import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.beranda.data.mapper.HomeCategoryMapper;
import com.tokopedia.tkpd.beranda.domain.model.category.HomeCategoryResponseModel;

import rx.Observable;


/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomeCategoryDataSource {

    private final MojitoApi mojitoApi;
    private final HomeCategoryMapper homeCategoryMapper;
    private final SessionHandler sessionHandler;

    public HomeCategoryDataSource(MojitoApi mojitoApi, HomeCategoryMapper homeCategoryMapper, SessionHandler sessionHandler) {
        this.mojitoApi = mojitoApi;
        this.homeCategoryMapper = homeCategoryMapper;
        this.sessionHandler = sessionHandler;
    }

    public Observable<HomeCategoryResponseModel> getHomeCategory() {
        return mojitoApi.getHomeCategoryMenuV2(sessionHandler.getLoginID(), GlobalConfig.getPackageApplicationName())
                .map(homeCategoryMapper);
    }
}
