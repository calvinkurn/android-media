package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.collapsing.tab.layout.CollapsingTabLayout;
import com.tokopedia.home.R;
import com.tokopedia.home.analytics.HomePageTracking;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.listener.HomeTabFeedListener;
import com.tokopedia.home.beranda.presentation.view.adapter.HomeFeedPagerAdapter;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HomeRecommendationFeedViewModel;
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFeedFragment;
import com.tokopedia.home.beranda.presentation.view.viewmodel.FeedTabModel;

import java.util.ArrayList;
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

        layoutParams.height =
                listener.getWindowHeight()
                        -listener.getHomeMainToolbarHeight()
                        +context.getResources().getDimensionPixelSize(R.dimen.dp_8);
        container.setLayoutParams(layoutParams);

        feedTabModelList = homeRecommendationFeedViewModel.getFeedTabModel();
        homeFeedsTabLayout.setVisibility(View.VISIBLE);
        homeFeedsViewPager.setVisibility(View.VISIBLE);

        if (homeFeedPagerAdapter == null || homeRecommendationFeedViewModel.isNewData()) {
            initViewPagerAndTablayout();
            homeRecommendationFeedViewModel.setNewData(false);
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
}
