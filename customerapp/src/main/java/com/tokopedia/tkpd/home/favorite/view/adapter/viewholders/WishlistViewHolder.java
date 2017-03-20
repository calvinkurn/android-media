package com.tokopedia.tkpd.home.favorite.view.adapter.viewholders;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.adapter.WishlistRecyclerViewAdapter;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.WishlistViewModel;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * @author kulomady on 1/24/17.
 */

public class WishlistViewHolder extends AbstractViewHolder<WishlistViewModel> {

    private final WishlistRecyclerViewAdapter wishlistAdapter;

    @BindView(R.id.title)
    TextView titleTextView;
    @BindView(R.id.textview_see_all)
    TextView seeAllTextView;
    @BindView(R.id.empty_wishlist)
    RelativeLayout emptyWishlistLayout;
    @BindView(R.id.main_content)
    LinearLayout mainContentLayout;
    @BindView(R.id.wishlist_recycler_view)
    RecyclerView wishlistRecyclerView;
    @BindView(R.id.find_now)
    TextView findNowTextview;


    @LayoutRes
    public static final int LAYOUT = R.layout.child_favorite_wishlist;

    public WishlistViewHolder(View itemView) {
        super(itemView);
        Context context = itemView.getContext();
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        wishlistRecyclerView.setLayoutManager(linearLayoutManager);
        wishlistRecyclerView.setHasFixedSize(true);

        List<ProductItem> empty = Collections.emptyList();
        wishlistAdapter = new WishlistRecyclerViewAdapter(context, empty);
        wishlistRecyclerView.setAdapter(wishlistAdapter);

    }

    @Override
    public void bind(WishlistViewModel wishlistViewModel) {
        titleTextView.setText("ok");
    }


}
