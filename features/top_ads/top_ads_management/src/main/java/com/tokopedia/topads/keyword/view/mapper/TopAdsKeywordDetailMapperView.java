package com.tokopedia.topads.keyword.view.mapper;

import com.tokopedia.topads.keyword.domain.model.KeywordDetailDomain;
import com.tokopedia.topads.keyword.view.model.KeywordAd;

/**
 * Created by zulfikarrahman on 5/30/17.
 */

public class TopAdsKeywordDetailMapperView {
    public static KeywordAd mapDomainToView(KeywordDetailDomain keywordDetailDomain) {
        KeywordAd keywordAd = new KeywordAd();
        keywordAd.setKeywordTag(keywordDetailDomain.getKeywordTag());
        keywordAd.setStatTotalSpent(keywordDetailDomain.getStatTotalSpent());
        keywordAd.setStatTotalImpression(keywordDetailDomain.getStatTotalImpression());
        keywordAd.setStatTotalCtr(keywordDetailDomain.getStatTotalCtr());
        keywordAd.setStatTotalConversion(keywordDetailDomain.getStatTotalConversion());
        keywordAd.setGroupId(String.valueOf(keywordDetailDomain.getGroupId()));
        keywordAd.setId(String.valueOf(keywordDetailDomain.getKeywordId()));
        keywordAd.setKeywordTypeDesc(keywordDetailDomain.getKeywordTypeDesc());
        keywordAd.setKeywordTypeId(keywordDetailDomain.getKeywordTypeId());
        keywordAd.setLabelPerClick(keywordDetailDomain.getLabelPerClick());
        keywordAd.setPriceBidFmt(keywordDetailDomain.getKeywordPriceBidFmt());
        keywordAd.setStatAvgClick(keywordDetailDomain.getStatAvgClick());
        keywordAd.setStatTotalClick(keywordDetailDomain.getStatTotalClick());
        keywordAd.setStatus(keywordDetailDomain.getKeywordStatus());
        keywordAd.setStatusToogle(keywordDetailDomain.getStatusToogle());
        keywordAd.setStatusDesc(keywordDetailDomain.getKeywordStatusDesc());
        keywordAd.setGroupName(keywordDetailDomain.getGroupName());
        keywordAd.setGroupBid(keywordDetailDomain.getGroupBid());
        return keywordAd;
    }
}
