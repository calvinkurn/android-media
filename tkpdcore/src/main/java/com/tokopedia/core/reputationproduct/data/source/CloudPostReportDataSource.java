package com.tokopedia.core.reputationproduct.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.product.ReviewActService;
import com.tokopedia.core.network.apiservices.shop.ReputationActService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.reputationproduct.data.mapper.ActResultMapper;
import com.tokopedia.core.reputationproduct.domain.model.ActResultDomain;

import java.util.Map;

import rx.Observable;

/**
 * Created by yoasfs on 18/07/17.
 */

public class CloudPostReportDataSource {

    private Context context;
    private ReputationActService reputationActService;
    private ActResultMapper actResultMapper;

    public CloudPostReportDataSource(Context context,
                                     ReputationActService reputationActService,
                                     ActResultMapper actResultMapper) {
        this.context = context;
        this.reputationActService = reputationActService;
        this.actResultMapper = actResultMapper;
    }

    public Observable<ActResultDomain> getPostReportDataSource(Map<String, String> parameters) {
        return reputationActService.getApi().deleteRepReviewResponse(AuthUtil.generateParams(context,parameters))
                .map(actResultMapper);
    }
}
