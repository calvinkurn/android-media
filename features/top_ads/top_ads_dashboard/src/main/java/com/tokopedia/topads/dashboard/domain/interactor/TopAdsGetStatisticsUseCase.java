package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.topads.common.constant.TopAdsCommonConstant;
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant;
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType;
import com.tokopedia.topads.dashboard.data.model.DataStatistic;
import com.tokopedia.topads.dashboard.domain.repository.TopAdsDashboardRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by hadi.putra on 25/04/18.
 */

public class TopAdsGetStatisticsUseCase extends UseCase<DataStatistic> {

    private final TopAdsDashboardRepository topAdsDashboardRepository;

    @Inject
    public TopAdsGetStatisticsUseCase(TopAdsDashboardRepository topAdsDashboardRepository) {
        this.topAdsDashboardRepository = topAdsDashboardRepository;
    }

    @Override
    public Observable<DataStatistic> createObservable(RequestParams requestParams) {
        return topAdsDashboardRepository.getStatistics(requestParams);
    }

    public static RequestParams createRequestParams(Date startDate, Date endDate,
                                                    @TopAdsStatisticsType int type, String shopId){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TopAdsCommonConstant.PARAM_SHOP_ID, shopId);
        requestParams.putString(TopAdsDashboardConstant.PARAM_TYPE, String.valueOf(type));
        requestParams.putObject(TopAdsDashboardConstant.PARAM_START_DATE,
                new SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(startDate));
        requestParams.putObject(TopAdsDashboardConstant.PARAM_END_DATE,
                new SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(endDate));
        return requestParams;
    }
}
