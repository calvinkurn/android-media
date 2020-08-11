package com.tokopedia.shop.score.data.mapper;

import com.tokopedia.shop.score.data.model.summary.Data;
import com.tokopedia.shop.score.data.model.summary.DetailData;
import com.tokopedia.shop.score.data.model.summary.ShopScoreSummaryServiceModel;
import com.tokopedia.shop.score.domain.model.ShopScoreMainDomainModel;
import com.tokopedia.shop.score.domain.model.ShopScoreSummaryDomainModelData;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class ShopScoreSummaryMapper implements Func1<ShopScoreSummaryServiceModel, ShopScoreMainDomainModel> {
    @Override
    public ShopScoreMainDomainModel call(ShopScoreSummaryServiceModel serviceModel) {
        Data serviceModelData = serviceModel.getData();

        ShopScoreMainDomainModel domainModel = new ShopScoreMainDomainModel();
        domainModel.setBadgeScore(serviceModelData.getBadgeScore());

        ShopScoreSummaryDomainModelData data = new ShopScoreSummaryDomainModelData();
        DetailData detailData = serviceModelData.getData();
        data.setTitle(detailData.getTitle());
        data.setValue(detailData.getValue());
        data.setProgressBarColor(ColorUtil.formatColorWithAlpha(detailData.getColor()));
        data.setDescription(detailData.getDescription());

        domainModel.setData(data);

        return domainModel;
    }
}
