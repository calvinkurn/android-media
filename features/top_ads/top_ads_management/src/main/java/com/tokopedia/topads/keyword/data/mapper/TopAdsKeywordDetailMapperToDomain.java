package com.tokopedia.topads.keyword.data.mapper;

import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.keyword.data.model.cloud.Datum;
import com.tokopedia.topads.keyword.domain.model.KeywordDetailDomain;

import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

public class TopAdsKeywordDetailMapperToDomain implements Func1<PageDataResponse<List<Datum>>, KeywordDetailDomain> {

    @Inject
    public TopAdsKeywordDetailMapperToDomain() {
    }

    @Override
    public KeywordDetailDomain call(PageDataResponse<List<Datum>> listPageDataResponse) {
        if (listPageDataResponse != null &&
                listPageDataResponse.getData() != null &&
                listPageDataResponse.getData().size() > 0) {
            return mapDataToDomain(listPageDataResponse.getData());
        } else {
            throw new NullPointerException("data is null");
        }
    }

    private KeywordDetailDomain mapDataToDomain(List<Datum> data) {
        Datum datum = data.get(0);
        KeywordDetailDomain keywordDetailDomain = new KeywordDetailDomain();
        keywordDetailDomain.setGroupId(datum.getGroupId());
        keywordDetailDomain.setGroupName(datum.getGroupName());
        keywordDetailDomain.setKeywordId(datum.getKeywordId());
        keywordDetailDomain.setStatusToogle(datum.getKeywordStatusToogle());
        keywordDetailDomain.setKeywordPriceBidFmt(datum.getKeywordPriceBidFmt());
        keywordDetailDomain.setKeywordStatus(datum.getKeywordStatus());
        keywordDetailDomain.setKeywordStatusDesc(datum.getKeywordStatusDesc());
        keywordDetailDomain.setKeywordTag(datum.getKeywordTag());
        keywordDetailDomain.setKeywordTypeDesc(datum.getKeywordTypeDesc());
        keywordDetailDomain.setKeywordTypeId(datum.getKeywordTypeId());
        keywordDetailDomain.setLabelPerClick(datum.getLabelPerClick());
        keywordDetailDomain.setStatAvgClick(datum.getStatAvgClick());
        keywordDetailDomain.setStatTotalClick(datum.getStatTotalClick());
        keywordDetailDomain.setStatTotalConversion(datum.getStatTotalConversion());
        keywordDetailDomain.setStatTotalCtr(datum.getStatTotalCtr());
        keywordDetailDomain.setStatTotalImpression(datum.getStatTotalImpression());
        keywordDetailDomain.setStatTotalSpent(datum.getStatTotalSpent());
        keywordDetailDomain.setGroupBid(datum.getGroupBid());
        return keywordDetailDomain;
    }
}