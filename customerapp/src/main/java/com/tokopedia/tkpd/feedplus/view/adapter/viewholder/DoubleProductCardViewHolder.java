package com.tokopedia.tkpd.feedplus.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.feedplus.FeedPlus;
import com.tokopedia.tkpd.feedplus.view.adapter.FeedProductAdapter;
import com.tokopedia.tkpd.feedplus.view.viewmodel.ProductCardViewModel;

import butterknife.BindView;

/**
 * @author by nisie on 5/16/17.
 */

public class DoubleProductCardViewHolder extends ProductCardViewHolder<ProductCardViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.list_feed_product_double;

    @BindView(R.id.product_list)
    RecyclerView recyclerView;

    private GridLayoutManager gridLayoutManager;
    private FeedProductAdapter adapter;

    public DoubleProductCardViewHolder(View itemView, FeedPlus.View viewListener) {
        super(itemView, viewListener);
        gridLayoutManager = new GridLayoutManager(
                itemView.getContext(),
                2,
                LinearLayoutManager.VERTICAL,
                false);
        adapter = new FeedProductAdapter();
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(ProductCardViewModel productCardViewModel) {
        super.bind(productCardViewModel);
        adapter.setList(productCardViewModel.getListProduct());
    }
}
