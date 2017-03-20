package com.tokopedia.tkpd.home.favorite.view.adapter.viewholders;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.SimpleHomeActivity;
import com.tokopedia.tkpd.home.favorite.view.adapter.WishlistAdapter;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.WishlistViewModel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author kulomady on 1/24/17.
 */

public class WishlistViewHolder extends AbstractViewHolder<WishlistViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.child_favorite_wishlist;

    private final WishlistAdapter wishlistAdapter;
    private Context mContext;
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

    public WishlistViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        wishlistRecyclerView.setLayoutManager(linearLayoutManager);
        wishlistRecyclerView.setHasFixedSize(true);
        wishlistAdapter = new WishlistAdapter();
        wishlistRecyclerView.setAdapter(wishlistAdapter);

    }

    @Override
    public void bind(WishlistViewModel wishlistViewModel) {
        if (wishlistViewModel.getWishlistItems().size() > 0) {
            wishlistRecyclerView.setVisibility(View.VISIBLE);
            titleTextView.setVisibility(View.VISIBLE);
            seeAllTextView.setVisibility(View.VISIBLE);
            emptyWishlistLayout.setVisibility(View.GONE);
            wishlistAdapter.setData(wishlistViewModel.getWishlistItems());
        } else {
            wishlistRecyclerView.setVisibility(View.GONE);
            titleTextView.setVisibility(View.GONE);
            seeAllTextView.setVisibility(View.GONE);
            emptyWishlistLayout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.textview_see_all)
    public void onClick() {
        UnifyTracking.eventWishlistAll();
        Intent intent = new Intent(mContext, SimpleHomeActivity.class);
        intent.putExtra(SimpleHomeActivity.FRAGMENT_TYPE, SimpleHomeActivity.WISHLIST_FRAGMENT);
        mContext.startActivity(intent);
    }
}
