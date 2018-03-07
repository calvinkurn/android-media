package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.product.ReviewActService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.mapper.ActResultMapper;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.ActResultDomain;

import java.util.Map;

import rx.Observable;

/**
 * Created by yoasfs on 18/07/17.
 */

public class CloudPostReportDataSource {

    private Context context;
    private ReviewActService reviewActService;
    private ActResultMapper actResultMapper;

    public CloudPostReportDataSource(Context context,
                                     ReviewActService reviewActService,
                                     ActResultMapper actResultMapper) {
        this.context = context;
        this.reviewActService = reviewActService;
        this.actResultMapper = actResultMapper;
    }

    public Observable<ActResultDomain> getPostReportDataSource(Map<String, String> parameters) {
        return reviewActService.getApi().reportReview(AuthUtil.generateParams(context, parameters))
                .map(actResultMapper);
    }
}
