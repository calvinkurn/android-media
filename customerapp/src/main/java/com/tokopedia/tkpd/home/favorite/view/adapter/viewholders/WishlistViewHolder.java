package com.tokopedia.tkpd.home.favorite.view.adapter.viewholders;

import android.app.Activity;
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
import com.tokopedia.tkpd.home.ParentIndexHome;
import com.tokopedia.tkpd.home.SimpleHomeActivity;
import com.tokopedia.tkpd.home.favorite.view.adapter.WishlistAdapter;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.WishlistViewModel;

import static com.tokopedia.tkpd.home.adapter.ProductFeedAdapter.HOTLIST_TAB;

/**
 * @author kulomady on 1/24/17.
 */

public class WishlistViewHolder extends AbstractViewHolder<WishlistViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.child_favorite_wishlist;

    private final WishlistAdapter wishlistAdapter;
    private Context context;
    private TextView titleTextView;
    private TextView seeAllTextView;
    private RelativeLayout emptyWishlistLayout;
    private LinearLayout mainContentLayout;
    private RecyclerView wishlistRecyclerView;
    private TextView findNowTextview;

    public WishlistViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        initView(itemView);
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        wishlistRecyclerView.setLayoutManager(linearLayoutManager);
        wishlistRecyclerView.setHasFixedSize(true);
        wishlistAdapter = new WishlistAdapter();
        wishlistRecyclerView.setAdapter(wishlistAdapter);

    }

    private void initView(View itemView) {
        titleTextView = itemView.findViewById(R.id.title);
        seeAllTextView = itemView.findViewById(R.id.textview_see_all);
        emptyWishlistLayout = itemView.findViewById(R.id.empty_wishlist);
        mainContentLayout = itemView.findViewById(R.id.main_content);
        wishlistRecyclerView = itemView.findViewById(R.id.wishlist_recycler_view);
        findNowTextview = itemView.findViewById(R.id.find_now);
        seeAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSeeAllClicked();
            }
        });

        findNowTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFindNowClicked();
            }
        });
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

    private void onSeeAllClicked() {
        UnifyTracking.eventWishlistAll();
        Intent intent = new Intent(context, SimpleHomeActivity.class);
        intent.putExtra(SimpleHomeActivity.FRAGMENT_TYPE, SimpleHomeActivity.WISHLIST_FRAGMENT);
        if(context instanceof Activity){
            ((Activity) context).startActivityForResult(intent, ParentIndexHome.WISHLIST_REQUEST);
        } else {
            context.startActivity(intent);
        }
    }

    private void onFindNowClicked() {
        ParentIndexHome.ChangeTabListener listener = ((ParentIndexHome) context).changeTabListener();
        listener.onChangeTab(HOTLIST_TAB);
    }
}
