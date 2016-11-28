package com.tokopedia.sellerapp.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.model.shopmodel.ShopModel;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by normansyahputa on 8/29/16.
 */

public class TransactionView extends FrameLayout implements BaseView<ShopModel> {

    @Bind(R.id.reputation_view)
    ReputationView reputationView;

    @Bind(R.id.shop_statistic_transaction_view)
    ShopStatisticTransactionView shopStatisticTransactionView;
    private ShopModel shopModel;

    public TransactionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.reputation_item_layout, this);
        ButterKnife.bind(this);
    }

    @Override
    public void init(ShopModel data) {
        shopModel = data;

        ReputationView.ReputationViewModel reputationViewModel = new ReputationView.ReputationViewModel();
        reputationViewModel.typeMedal = data.stats.shopBadgeLevel.set;
        reputationViewModel.levelMedal = data.stats.shopBadgeLevel.level;
        reputationViewModel.reputationPoints = data.stats.shopReputationScore;
        reputationViewModel.stats = data.stats;

        reputationView.init(reputationViewModel);

        shopStatisticTransactionView.init(shopModel.shopTxStats);
    }
}
