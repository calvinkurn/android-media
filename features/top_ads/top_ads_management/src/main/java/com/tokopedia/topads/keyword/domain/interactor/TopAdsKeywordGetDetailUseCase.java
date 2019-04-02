package com.tokopedia.topads.keyword.domain.interactor;

import com.tokopedia.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.keyword.constant.KeywordTypeDef;
import com.tokopedia.topads.keyword.data.mapper.TopAdsKeywordDetailMapperToDomain;
import com.tokopedia.topads.keyword.domain.TopAdsKeywordGetDetailRepository;
import com.tokopedia.topads.keyword.domain.model.KeywordDetailDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

public class TopAdsKeywordGetDetailUseCase extends UseCase<KeywordDetailDomain> {

    private final TopAdsKeywordGetDetailRepository topAdsKeywordGetDetailRepository;
    private final TopAdsKeywordDetailMapperToDomain topAdsKeywordDetailMapperToDomain;

    @Inject
    public TopAdsKeywordGetDetailUseCase(TopAdsKeywordGetDetailRepository topAdsKeywordGetDetailRepository,
                                         TopAdsKeywordDetailMapperToDomain topAdsKeywordDetailMapperToDomain) {
        super();
        this.topAdsKeywordGetDetailRepository = topAdsKeywordGetDetailRepository;
        this.topAdsKeywordDetailMapperToDomain = topAdsKeywordDetailMapperToDomain;

    }

    @Override
    public Observable<KeywordDetailDomain> createObservable(RequestParams requestParams) {
        return topAdsKeywordGetDetailRepository.getKeywordDetail(requestParams)
                .map(topAdsKeywordDetailMapperToDomain);
    }

    public static RequestParams createRequestParams(Date startDate, Date endDate, String adId,
                                                    int isPositive, String shopId){
        String startDateText = new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(startDate);
        String endDateText = new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(endDate);

        RequestParams requestParams = RequestParams.create();
        requestParams.putString(KeywordTypeDef.IS_POSITIVE, Integer.toString(isPositive));
        requestParams.putString(TopAdsNetworkConstant.PARAM_SHOP_ID, shopId);
        requestParams.putString(TopAdsNetworkConstant.PARAM_START_DATE, startDateText);
        requestParams.putString(TopAdsNetworkConstant.PARAM_END_DATE, endDateText);
        requestParams.putString(TopAdsNetworkConstant.PARAM_KEYWORD_AD_ID, adId);
        return requestParams;
    }
}
