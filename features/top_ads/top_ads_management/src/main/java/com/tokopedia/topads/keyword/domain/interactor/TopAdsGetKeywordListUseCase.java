package com.tokopedia.topads.keyword.domain.interactor;

import com.tokopedia.topads.common.constant.TopAdsCommonConstant;
import com.tokopedia.topads.keyword.constant.KeywordTypeDef;
import com.tokopedia.topads.keyword.domain.repository.TopAdsKeywordRepository;
import com.tokopedia.topads.keyword.view.model.BaseKeywordParam;
import com.tokopedia.topads.keyword.view.model.KeywodDashboardViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by hadi.putra on 11/05/18.
 */

public class TopAdsGetKeywordListUseCase extends UseCase<KeywodDashboardViewModel> {
    private final TopAdsKeywordRepository repository;

    @Inject
    public TopAdsGetKeywordListUseCase(TopAdsKeywordRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<KeywodDashboardViewModel> createObservable(RequestParams requestParams) {
        return repository.getDashboardKeyword(requestParams);
    }

    public static RequestParams createRequestParams(BaseKeywordParam baseKeywordParam, String shopId){
        RequestParams requestParams = RequestParams.create();
        if (baseKeywordParam.keywordTag != null) {
            requestParams.putString(KeywordTypeDef.KEYWORD, baseKeywordParam.keywordTag);
        }
        if (baseKeywordParam.startDateDesc != null) {
            requestParams.putString(KeywordTypeDef.START_DATE, baseKeywordParam.startDateDesc);
        }
        if (baseKeywordParam.endDateDesc != null) {
            requestParams.putString(KeywordTypeDef.END_DATE, baseKeywordParam.endDateDesc);
        }
        requestParams.putString(KeywordTypeDef.PAGE, Integer.toString(baseKeywordParam.page));
        requestParams.putString(KeywordTypeDef.IS_POSITIVE, Integer.toString(baseKeywordParam.isPositive()));

        if (baseKeywordParam.groupId > 0)
            requestParams.putString(KeywordTypeDef.GROUP_ID, Long.toString(baseKeywordParam.groupId));

        if (baseKeywordParam.sortingParam != null && !baseKeywordParam.sortingParam.isEmpty())
            requestParams.putString(KeywordTypeDef.SORTING, baseKeywordParam.sortingParam);

        requestParams.putString(KeywordTypeDef.KEYWORD_STATUS, Integer.toString(baseKeywordParam.keywordStatus));
        requestParams.putString(TopAdsCommonConstant.PARAM_SHOP_ID, shopId);

        return requestParams;
    }
}
