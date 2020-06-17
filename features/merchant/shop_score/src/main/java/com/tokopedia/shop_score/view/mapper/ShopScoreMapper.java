package com.tokopedia.shop_score.view.mapper;

import com.tokopedia.shop_score.domain.model.ShopScoreMainDomainModel;
import com.tokopedia.shop_score.domain.model.ShopScoreSummaryDomainModelData;
import com.tokopedia.shop_score.view.model.ShopScoreViewModel;
import com.tokopedia.shop_score.view.model.ShopScoreViewModelData;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class ShopScoreMapper {
    public static ShopScoreViewModel map(ShopScoreMainDomainModel domainModel) {

        ShopScoreViewModel viewModel = new ShopScoreViewModel();
        viewModel.setBadgeScore(domainModel.getBadgeScore());

        ShopScoreViewModelData data = new ShopScoreViewModelData();
        ShopScoreSummaryDomainModelData domainModelData = domainModel.getData();
        data.setTitle(domainModelData.getTitle());
        data.setValue(domainModelData.getValue());
        data.setDescription(domainModelData.getDescription());
        data.setProgressBarColor(domainModelData.getProgressBarColor());
        viewModel.setData(data);

        return viewModel;
    }
}
