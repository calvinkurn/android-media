package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.topads.common.constant.TopAdsCommonConstant;
import com.tokopedia.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.constant.TopAdsStatisticsType;
import com.tokopedia.topads.dashboard.data.model.data.Cell;
import com.tokopedia.topads.dashboard.domain.repository.TopAdsDashboardRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by hadi.putra on 25/04/18.
 */

public class TopAdsGetStatisticsUseCase extends UseCase<List<Cell>> {

    private final TopAdsDashboardRepository topAdsDashboardRepository;

    @Inject
    public TopAdsGetStatisticsUseCase(TopAdsDashboardRepository topAdsDashboardRepository) {
        this.topAdsDashboardRepository = topAdsDashboardRepository;
    }

    @Override
    public Observable<List<Cell>> createObservable(RequestParams requestParams) {
        return topAdsDashboardRepository.getStatistics(requestParams);
    }

    public static RequestParams createRequestParams(Date startDate, Date endDate,
                                                    @TopAdsStatisticsType int type, String shopId){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TopAdsCommonConstant.PARAM_SHOP_ID, shopId);
        requestParams.putString(TopAdsNetworkConstant.PARAM_TYPE, String.valueOf(type));
        requestParams.putObject(TopAdsNetworkConstant.PARAM_START_DATE,
                new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(startDate));
        requestParams.putObject(TopAdsNetworkConstant.PARAM_END_DATE,
                new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(endDate));
        return requestParams;
    }
}
