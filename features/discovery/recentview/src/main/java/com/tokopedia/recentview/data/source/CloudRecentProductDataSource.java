package com.tokopedia.recentview.data.source;

import com.tokopedia.core.base.common.service.MojitoService;
import com.tokopedia.recentview.data.mapper.RecentProductMapper;
import com.tokopedia.recentview.domain.model.RecentViewProductDomain;
import com.tokopedia.recentview.domain.usecase.GetRecentViewUseCase;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author Kulomady on 12/9/16.
 */

public class CloudRecentProductDataSource {

    private final MojitoService mojitoService;
    private RecentProductMapper recentProductMapper;

    @Inject
    CloudRecentProductDataSource(MojitoService mojitoService,
                                 RecentProductMapper recentProductMapper) {
        this.mojitoService = mojitoService;
        this.recentProductMapper = recentProductMapper;
    }

    public Observable<List<RecentViewProductDomain>> getRecentProduct(RequestParams requestParams) {
        return mojitoService.getRecentProduct(
                String.valueOf(requestParams.getParameters()
                        .get(GetRecentViewUseCase.PARAM_USER_ID)))
                .map(recentProductMapper);
    }
}
