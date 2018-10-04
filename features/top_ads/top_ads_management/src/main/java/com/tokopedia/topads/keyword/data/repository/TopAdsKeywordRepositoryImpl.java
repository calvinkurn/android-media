package com.tokopedia.topads.keyword.data.repository;

import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.topads.dashboard.data.model.data.Page;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.keyword.constant.KeywordTypeDef;
import com.tokopedia.topads.keyword.data.model.cloud.Datum;
import com.tokopedia.topads.keyword.data.model.cloud.bulkkeyword.DataBulkKeyword;
import com.tokopedia.topads.keyword.data.source.TopAdsKeywordDataSource;
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModel;
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModelDatum;
import com.tokopedia.topads.keyword.domain.repository.TopAdsKeywordRepository;
import com.tokopedia.topads.keyword.view.model.KeywodDashboardViewModel;
import com.tokopedia.topads.keyword.view.model.KeywordAd;
import com.tokopedia.topads.keyword.view.model.NegativeKeywordAd;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by hadi.putra on 11/05/18.
 */

public class TopAdsKeywordRepositoryImpl implements TopAdsKeywordRepository {
    private static final int POSITIVE_VALUE = 1;
    private final TopAdsKeywordDataSource dataSource;

    public TopAdsKeywordRepositoryImpl(TopAdsKeywordDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Observable<KeywodDashboardViewModel> getDashboardKeyword(final RequestParams requestParams) {
        return dataSource.searchKeyword(requestParams)
                .map(new Func1<PageDataResponse<List<Datum>>, KeywodDashboardViewModel>() {
                    @Override
                    public KeywodDashboardViewModel call(PageDataResponse<List<Datum>> listPageDataResponse) {
                        KeywodDashboardViewModel keywodDashboardViewModel = new KeywodDashboardViewModel();
                        keywodDashboardViewModel.setPage(convertPage(listPageDataResponse.getPage()));
                        List<KeywordAd> keywordAds = new ArrayList<>();
                        for (Datum datum : listPageDataResponse.getData()){
                            keywordAds.add(convertToKeywordAd(datum, Integer.parseInt(requestParams
                                    .getString(KeywordTypeDef.IS_POSITIVE, "1"))));
                        }
                        keywodDashboardViewModel.setData(keywordAds);
                        return keywodDashboardViewModel;
                    }
                });
    }

    @Override
    public Observable<PageDataResponse<DataBulkKeyword>> bulkActionKeyword(DataRequest<DataBulkKeyword> dataBulkKeyword){
        return dataSource.bulkActionKeyword(dataBulkKeyword);
    }

    @Override
    public Observable<AddKeywordDomainModel> addKeywords(List<AddKeywordDomainModelDatum> keywords, String source) {
        for (AddKeywordDomainModelDatum keyword : keywords) {
            keyword.setSource(source);
        }
        AddKeywordDomainModel addKeywordDomainModel = new AddKeywordDomainModel(keywords);
        return dataSource.addKeywords(addKeywordDomainModel);
    }

    private KeywordAd convertToKeywordAd(Datum datum, int isPositive) {
        KeywordAd keywordAd = isPositive == POSITIVE_VALUE ? new KeywordAd() : new NegativeKeywordAd();
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

    private com.tokopedia.topads.keyword.domain.model.Page convertPage(Page page) {
        com.tokopedia.topads.keyword.domain.model.Page res =
                new com.tokopedia.topads.keyword.domain.model.Page();
        res.setCurrent(page.getCurrent());
        res.setMax(page.getMax());
        res.setMin(page.getMin());
        res.setPerPage(page.getPerPage());
        res.setTotal(page.getTotal());
        return res;
    }
}
