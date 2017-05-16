package com.tokopedia.tkpd.feedplus.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.feedplus.view.viewmodel.ProductCardViewModel;
import com.tokopedia.tkpd.feedplus.view.viewmodel.PromotedShopViewModel;

import butterknife.BindView;

/**
 * Created by stevenfredian on 5/16/17.
 */

public class PromotedShopViewHolder extends AbstractViewHolder<PromotedShopViewModel>{

    @LayoutRes
    public static final int LAYOUT = R.layout.promoted_shop_layout;

//    @BindView(R.id.shop_name)
//    TextView shopName;
//
//    @BindView(R.id.gold_merchant)
//    View goldMerchant;

    private PromotedShopViewModel promotedShopViewModel;

    public PromotedShopViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(PromotedShopViewModel promotedShopViewModel) {
        this.promotedShopViewModel = promotedShopViewModel;
//        shopName.setText(promotedShopViewModel.getShopName());
//        goldMerchant.setVisibility(promotedShopViewModel.isGoldMerchant() ? View.VISIBLE : View.GONE);
    }
}
