package com.tokopedia.review.feature.reputationhistory.view.helper;

import android.view.View;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.review.R;
import com.tokopedia.unifyprinciples.Typography;

/**
 * @author normansyahputa on 3/17/17.
 */
public class ReputationViewHelper {

    private Typography textHeaderReputation;
    private ReputationView sellerReputationHeaderView;

    public ReputationViewHelper(View itemView) {
        textHeaderReputation = itemView.findViewById(R.id.text_header_reputation);
        sellerReputationHeaderView = itemView.findViewById(R.id.seller_reputation_header_view);

        textHeaderReputation.setText(R.string.shop_reputation);
    }

    public void renderData(ShopModel shopModel) {
        ReputationView.ReputationUiModel reputationUiModel = new ReputationView.ReputationUiModel();
        reputationUiModel.typeMedal = shopModel.stats.shopBadgeLevel.set;
        reputationUiModel.levelMedal = shopModel.stats.shopBadgeLevel.level;
        reputationUiModel.reputationPoints = shopModel.stats.shopReputationScore;
        reputationUiModel.stats = shopModel.stats;

        sellerReputationHeaderView.init(reputationUiModel);
    }
}
