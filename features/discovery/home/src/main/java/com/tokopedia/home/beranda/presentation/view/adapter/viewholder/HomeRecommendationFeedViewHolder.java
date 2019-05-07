package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.collapsing.tab.layout.CollapsingTabLayout;
import com.tokopedia.design.countdown.CountDownView;
import com.tokopedia.design.image.SquareImageView;
import com.tokopedia.home.R;
import com.tokopedia.home.analytics.HomePageTracking;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.helper.DateHelper;
import com.tokopedia.home.beranda.helper.DynamicLinkHelper;
import com.tokopedia.home.beranda.helper.ViewHelper;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.listener.HomeTabFeedListener;
import com.tokopedia.home.beranda.presentation.view.adapter.HomeFeedPagerAdapter;
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.GridSpacingItemDecoration;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HomeRecommendationFeedViewModel;
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils;
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFeedFragment;
import com.tokopedia.home.beranda.presentation.view.viewmodel.FeedTabModel;
import com.tokopedia.trackingoptimizer.TrackingQueue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by henrypriyono on 22/03/18.
 */

public class HomeRecommendationFeedViewHolder extends AbstractViewHolder<HomeRecommendationFeedViewModel>
implements HomeTabFeedListener {
    @LayoutRes
    public static final int LAYOUT = R.layout.home_recommendation_feed_viewholder;
    private static final String TAG = HomeRecommendationFeedViewHolder.class.getSimpleName();
    private final ViewPager homeFeedsViewPager;
    private final CollapsingTabLayout homeFeedsTabLayout;
    private final HomeCategoryListener listener;
    private final View homeFeedsTabShadow;
    private Context context;
    private HomeFeedPagerAdapter homeFeedPagerAdapter;
    private List<FeedTabModel> feedTabModelList;
    private static final int DEFAULT_FEED_PAGER_OFFSCREEN_LIMIT = 10;
    private View container;

    public HomeRecommendationFeedViewHolder(View itemView,
                                            HomeCategoryListener listener) {
        super(itemView);
        context = itemView.getContext();
        this.listener = listener;
        homeFeedsViewPager = itemView.findViewById(R.id.view_pager_home_feeds);
        homeFeedsTabLayout = itemView.findViewById(R.id.tab_layout_home_feeds);
        homeFeedsTabShadow = itemView.findViewById(R.id.view_feed_shadow);
        container = itemView.findViewById(R.id.home_recommendation_feed_container);
    }

    @Override
    public void bind(HomeRecommendationFeedViewModel homeRecommendationFeedViewModel) {
        ViewGroup.LayoutParams layoutParams = container.getLayoutParams();

        //we must specify height for viewpager, so it can't scroll up anymore and create
        //sticky effect

        //we specify viewpager height to fill viewport below maintoolbar and tablayout, so then recyclerview reach its end scroll
        //with tab showing below maintoolbar
        //viewport - toolbar height - statusbar offset (android 19 and lower doesn't apply inset) - spacing

        int statusBarOffset = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            statusBarOffset = ViewHelper.getStatusBarHeight(context);
        }
        layoutParams.height =
                listener.getWindowHeight()
                        -listener.getHomeMainToolbarHeight()
                        -context.getResources().getDimensionPixelSize(R.dimen.tab_home_feed_max_height)
                        +statusBarOffset
                        -context.getResources().getDimensionPixelSize(R.dimen.dp_8);
        container.setLayoutParams(layoutParams);

        feedTabModelList = homeRecommendationFeedViewModel.getFeedTabModel();
        homeFeedsTabLayout.setVisibility(View.VISIBLE);
        homeFeedsViewPager.setVisibility(View.VISIBLE);

        if (homeFeedPagerAdapter == null || homeRecommendationFeedViewModel.isNewData()) {
            initViewPagerAndTablayout();
        }
    }

    private void initViewPagerAndTablayout() {
        homeFeedPagerAdapter = new HomeFeedPagerAdapter(
                listener.getEggListener(),
                this,
                listener.getChildFragmentManager(),
                feedTabModelList,
                listener.getTrackingQueue());

        homeFeedsViewPager.setOffscreenPageLimit(DEFAULT_FEED_PAGER_OFFSCREEN_LIMIT);
        homeFeedsViewPager.setAdapter(homeFeedPagerAdapter);
        homeFeedsTabLayout.setup(homeFeedsViewPager, convertToTabItemDataList(feedTabModelList));
        homeFeedsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FeedTabModel selectedFeedTabModel =
                        feedTabModelList.get(tab.getPosition());
                HomePageTracking.eventClickOnHomePageRecommendationTab(
                        context,
                        selectedFeedTabModel
                );
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                HomeFeedFragment homeFeedFragment = homeFeedPagerAdapter.getRegisteredFragment(tab.getPosition());
                if (homeFeedFragment != null) {
                    homeFeedFragment.scrollToTop();
                }
                homeFeedsTabLayout.resetCollapseState();
            }
        });
    }


    private List<CollapsingTabLayout.TabItemData> convertToTabItemDataList(List<FeedTabModel> feedTabModelList) {
        List<CollapsingTabLayout.TabItemData> tabItemDataList = new ArrayList<>();
        for (FeedTabModel feedTabModel : feedTabModelList) {
            tabItemDataList.add(new CollapsingTabLayout.TabItemData(feedTabModel.getName(), feedTabModel.getImageUrl()));
        }
        return tabItemDataList;
    }

    @Override
    public void onFeedContentScrolled(int dy, int totalScrollY) {
        homeFeedsTabLayout.adjustTabCollapseOnScrolled(dy, totalScrollY);
    }

    @Override
    public void onFeedContentScrollStateChanged(int newState) {
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            homeFeedsTabLayout.scrollActiveTabToLeftScreen();
        } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            homeFeedsTabLayout.snapCollapsingTab();
        }
    }

    public void showFeedTabShadow(Boolean show) {
        if (show) {
            homeFeedsTabShadow.setVisibility(View.VISIBLE);
        } else {
            homeFeedsTabShadow.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onViewRecycled() {
        super.onViewRecycled();
        Log.d("TokopediaDevara", "Recom Feed Section "+getAdapterPosition()+" recycled");
    }


}
