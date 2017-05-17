package com.tokopedia.tkpd.feedplus.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.feedplus.view.adapter.FeedProductAdapter;
import com.tokopedia.tkpd.feedplus.view.adapter.PromotedShopAdapter;
import com.tokopedia.tkpd.feedplus.view.viewmodel.ProductCardViewModel;
import com.tokopedia.tkpd.feedplus.view.viewmodel.PromotedShopViewModel;

import butterknife.BindView;

/**
 * Created by stevenfredian on 5/16/17.
 */

public class PromotedShopViewHolder extends AbstractViewHolder<PromotedShopViewModel>{

    @LayoutRes
    public static final int LAYOUT = R.layout.promoted_shop_layout;

    @BindView(R.id.shop_name)
    TextView shopName;

    @BindView(R.id.gold_merchant)
    View goldMerchant;

    @BindView(R.id.product_list)
    RecyclerView recyclerView;

    private GridLayoutManager gridLayoutManager;
    private PromotedShopAdapter adapter;


    private PromotedShopViewModel promotedShopViewModel;

    public PromotedShopViewHolder(View itemView) {
        super(itemView);

        gridLayoutManager = new GridLayoutManager(itemView.getContext(), 2, LinearLayoutManager.HORIZONTAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return getSpan(position);
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new PromotedShopAdapter(recyclerView.getWidth());
        recyclerView.setAdapter(adapter);
    }

    private int getSpan(int position) {
        if(position == 0){
            return 2;
        }else {
            return 1;
        }
    }

    @Override
    public void bind(PromotedShopViewModel promotedShopViewModel) {
        this.promotedShopViewModel = promotedShopViewModel;
        shopName.setText(promotedShopViewModel.getShopName());
        goldMerchant.setVisibility(promotedShopViewModel.isGoldMerchant() ? View.VISIBLE : View.GONE);
        adapter.setList(promotedShopViewModel.getListProduct());
    }
}
