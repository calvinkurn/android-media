package com.tokopedia.shop.score.view.mapper;

import com.tokopedia.shop.score.domain.model.ShopScoreDetailDomainModel;
import com.tokopedia.shop.score.domain.model.ShopScoreDetailSummaryDomainModel;
import com.tokopedia.shop.score.view.model.ShopScoreDetailSummaryViewModel;

/**
 * @author sebastianuskh on 3/2/17.
 */
public class ShopScoreDetailSummaryViewModelMapper {
    public static ShopScoreDetailSummaryViewModel map(ShopScoreDetailDomainModel domainModels) {
        ShopScoreDetailSummaryViewModel viewModel = new ShopScoreDetailSummaryViewModel();
        ShopScoreDetailSummaryDomainModel summaryModel = domainModels.getSummaryModel();

        viewModel.setColor(summaryModel.getColor());
        viewModel.setValue(summaryModel.getValue());
        viewModel.setText(summaryModel.getText());

        return viewModel;
    }
}
