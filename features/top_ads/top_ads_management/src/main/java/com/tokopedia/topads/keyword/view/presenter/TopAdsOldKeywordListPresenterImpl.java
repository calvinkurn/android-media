package com.tokopedia.topads.keyword.view.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.common.utils.view.DateFormatUtils;
import com.tokopedia.base.list.seller.view.listener.BaseListViewListener;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsAdListPresenter;
import com.tokopedia.topads.keyword.constant.KeywordTypeDef;
import com.tokopedia.topads.keyword.domain.interactor.KeywordDashboardUseCase;
import com.tokopedia.topads.keyword.domain.model.Datum;
import com.tokopedia.topads.keyword.domain.model.KeywordDashboardDomain;
import com.tokopedia.topads.keyword.view.model.BaseKeywordParam;
import com.tokopedia.topads.keyword.view.model.KeywodDashboardViewModel;
import com.tokopedia.topads.keyword.view.model.KeywordAd;
import com.tokopedia.topads.keyword.view.model.KeywordNegativeParam;
import com.tokopedia.topads.keyword.view.model.KeywordPositiveParam;
import com.tokopedia.topads.keyword.view.model.NegativeKeywordAd;
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsAddSourceTaggingUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author normansyahputa on 5/17/17.
 */

public class TopAdsOldKeywordListPresenterImpl extends
        TopAdsOldKeywordListPresenter<BaseListViewListener> implements TopAdsAdListPresenter<GroupAd> {

    public static final String KEYWORD_DATE_FORMAT = "yyyy-MM-dd";
    private static final String TAG = "TopAdsKeywordListPresen";

    private KeywordDashboardUseCase keywordDashboardUseCase;
    private TopAdsAddSourceTaggingUseCase topAdsAddSourceTaggingUseCase;

    @Inject
    public TopAdsOldKeywordListPresenterImpl(KeywordDashboardUseCase keywordDashboardUseCase,
                                             TopAdsAddSourceTaggingUseCase topAdsAddSourceTaggingUseCase) {
        this.keywordDashboardUseCase = keywordDashboardUseCase;
        this.topAdsAddSourceTaggingUseCase = topAdsAddSourceTaggingUseCase;
    }

    public void saveSourceTagging(@TopAdsSourceOption String source){
        topAdsAddSourceTaggingUseCase.execute(TopAdsAddSourceTaggingUseCase
                .createRequestParams(source), new Subscriber<Void>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Void aVoid) {
                //do nothing
            }
        });
    }

    public void fetchKeyword(final BaseKeywordParam baseKeywordParam) {
        keywordDashboardUseCase.execute(generateParam(baseKeywordParam), new Subscriber<KeywordDashboardDomain>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable t) {
                if (!isViewAttached()) {
                    return;
                }
                getView().onLoadSearchError(t);
            }

            @Override
            public void onNext(KeywordDashboardDomain keywordDashboardDomain) {
                revealData(getKeywordAds(keywordDashboardDomain, baseKeywordParam.isPositive));
            }
        });
    }

    private KeywodDashboardViewModel getKeywordAds(KeywordDashboardDomain keywordDashboardDomain, boolean isPositive) {
        KeywodDashboardViewModel keywodDashboardViewModel = new KeywodDashboardViewModel();
        keywodDashboardViewModel.setPage(keywordDashboardDomain.getPage());
        List<KeywordAd> keywordAds = new ArrayList<>();
        for (Datum datum : keywordDashboardDomain.getData()) {
            keywordAds.add(getKeywordAd(datum, isPositive));
        }
        keywodDashboardViewModel.setData(keywordAds);
        return keywodDashboardViewModel;
    }

    @NonNull
    private KeywordAd getKeywordAd(Datum datum, boolean isPositive) {
        KeywordAd keywordAd = isPositive ? new KeywordAd() : new NegativeKeywordAd();
        keywordAd.setId(Integer.toString(datum.getKeywordId()));
        keywordAd.setGroupId(Integer.toString(datum.getGroupId()));
        keywordAd.setKeywordTypeId(datum.getKeywordTypeId());
        keywordAd.setGroupName(datum.getGroupName());
        keywordAd.setKeywordTag(datum.getKeywordTag());
        keywordAd.setStatus(datum.getKeywordStatus());
        keywordAd.setStatusDesc(datum.getKeywordStatusDesc());
        keywordAd.setStatusToogle(datum.getKeywordStatusToogle());
        keywordAd.setStatAvgClick(datum.getStatAvgClick());
        keywordAd.setStatTotalSpent(datum.getStatTotalSpent());
        keywordAd.setStatTotalImpression(datum.getStatTotalImpression());
        keywordAd.setStatTotalClick(datum.getStatTotalClick());
        keywordAd.setStatTotalCtr(datum.getStatTotalCtr());
        keywordAd.setStatTotalConversion(datum.getStatTotalConversion());
        keywordAd.setPriceBidFmt(datum.getKeywordPriceBidFmt());
        keywordAd.setLabelPerClick(datum.getLabelPerClick());
        keywordAd.setKeywordTypeDesc(datum.getKeywordTypeDesc());
        keywordAd.setGroupBid(datum.getGroupBid());
        return keywordAd;
    }

    private void revealData(KeywodDashboardViewModel keywordDashboardViewModel) {
        if (isViewAttached()) {
            getView().onSearchLoaded(
                    keywordDashboardViewModel.getData(),
                    keywordDashboardViewModel.getPage().getTotal()
            );
        }
    }

    private String formatDate(long date) {
        return DateFormatUtils.getFormattedDate(date, KEYWORD_DATE_FORMAT);
    }

    @Override
    public void unSubscribe() {

    }

    private RequestParams generateParam(BaseKeywordParam baseKeywordParam) {
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

//        requestParams.putString("keyword_id", Long.toString(baseKeywordParam.keywordId));
        if (baseKeywordParam.groupId > 0)
            requestParams.putString(KeywordTypeDef.GROUP_ID, Long.toString(baseKeywordParam.groupId));

        if (baseKeywordParam.sortingParam != null && !baseKeywordParam.sortingParam.isEmpty())
            requestParams.putString(KeywordTypeDef.SORTING, baseKeywordParam.sortingParam);

        requestParams.putString(KeywordTypeDef.KEYWORD_STATUS, Integer.toString(baseKeywordParam.keywordStatus));
//        requestParams.putString("keyword_type_id", Integer.toString(baseKeywordParam.keywordTypeId));
        return requestParams;
    }

    public KeywordPositiveParam generateKeywordPositiveParam() {
        return new KeywordPositiveParam();
    }

    private KeywordNegativeParam generateKeywordNegativeParam() {
        return new KeywordNegativeParam();
    }

    private BaseKeywordParam generateParam(String query, int page, boolean isPositive
    ) {
        BaseKeywordParam baseKeywordParam = isPositive ? generateKeywordPositiveParam() : generateKeywordNegativeParam();
        if (query != null)
            baseKeywordParam.keywordTag = query;
        baseKeywordParam.page = page;

        return baseKeywordParam;
    }

    public BaseKeywordParam generateParam(String query, int page, boolean isPositive, long startDate, long endDate
    ) {
        BaseKeywordParam baseKeywordParam = generateParam(query, page, isPositive);
        baseKeywordParam.startDate = startDate;
        baseKeywordParam.endDate = endDate;

        baseKeywordParam.startDateDesc = formatDate(startDate);
        baseKeywordParam.endDateDesc = formatDate(endDate);

        return baseKeywordParam;
    }
}