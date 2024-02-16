package com.tokopedia.home.beranda.presentation.view.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.listener.HomeEggListener;
import com.tokopedia.home.beranda.listener.HomeTabFeedListener;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.RecommendationTabDataModel;
import com.tokopedia.home.beranda.presentation.view.fragment.BaseRecommendationFragment;
import com.tokopedia.home.beranda.presentation.view.fragment.HomeGlobalRecommendationFragment;
import com.tokopedia.home.beranda.presentation.view.fragment.HomeRecommendationFragment;
import com.tokopedia.home.beranda.presentation.view.helper.HomeRecommendationController;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;

import java.util.ArrayList;
import java.util.List;

public class HomeFeedPagerAdapter extends FragmentStatePagerAdapter {

    private final RecyclerView.RecycledViewPool parentPool;
    private final HomeCategoryListener homeCategoryListener;
    private final SparseArrayCompat<BaseRecommendationFragment> registeredFragments = new SparseArrayCompat<>();
    private final HomeEggListener homeEggListener;
    private final HomeTabFeedListener homeTabFeedListener;
    private final List<RecommendationTabDataModel> recommendationTabDataModelList = new ArrayList<>();
    private boolean shouldUseGlobalForYouComponent = false;

    public HomeFeedPagerAdapter(HomeCategoryListener homeCategoryListener,
                                HomeEggListener homeEggListener,
                                HomeTabFeedListener homeTabFeedListener,
                                FragmentManager fragmentManager,
                                List<RecommendationTabDataModel> recommendationTabDataModelList,
                                RecyclerView.RecycledViewPool parentPool,
                                RemoteConfig remoteConfig) {
        super(fragmentManager);
        this.homeEggListener = homeEggListener;
        this.homeTabFeedListener = homeTabFeedListener;
        this.parentPool = parentPool;
        this.homeCategoryListener = homeCategoryListener;
        fetchRemoteConfig(remoteConfig);
        updateData(recommendationTabDataModelList);
    }

    public void updateData(List<RecommendationTabDataModel> recommendationTabDataModelList) {
        this.recommendationTabDataModelList.clear();
        this.recommendationTabDataModelList.addAll(recommendationTabDataModelList);
        registeredFragments.clear();
        notifyDataSetChanged();
    }

    private void fetchRemoteConfig(RemoteConfig remoteConfig) {
        shouldUseGlobalForYouComponent = remoteConfig
                .getBoolean(RemoteConfigKey.HOME_GLOBAL_COMPONENT_FALLBACK, false);
    }

    @Override
    public Fragment getItem(int position) {
        if (shouldUseGlobalForYouComponent && HomeRecommendationController.INSTANCE.isUsingRecommendationCard()) {
            HomeGlobalRecommendationFragment homeFeedFragment = HomeGlobalRecommendationFragment.Companion.newInstance(
                    position,
                    Integer.parseInt(recommendationTabDataModelList.get(position).getId()),
                    recommendationTabDataModelList.get(position).getName(),
                    recommendationTabDataModelList.get(position).getSourceType()
            );
            homeFeedFragment.setListener(homeCategoryListener, homeEggListener, homeTabFeedListener);
            homeFeedFragment.setParentPool(parentPool);
            return homeFeedFragment;
        } else {
            HomeRecommendationFragment homeFeedFragment = HomeRecommendationFragment.Companion.newInstance(
                    position,
                    Integer.parseInt(recommendationTabDataModelList.get(position).getId()),
                    recommendationTabDataModelList.get(position).getName(),
                    recommendationTabDataModelList.get(position).getSourceType()
            );
            homeFeedFragment.setListener(homeCategoryListener, homeEggListener, homeTabFeedListener);
            homeFeedFragment.setParentPool(parentPool);
            return homeFeedFragment;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object o = super.instantiateItem(container, position);
        if (shouldUseGlobalForYouComponent && HomeRecommendationController.INSTANCE.isUsingRecommendationCard()) {
            HomeGlobalRecommendationFragment homeFeedFragment = (HomeGlobalRecommendationFragment) o;
            homeFeedFragment.setListener(homeCategoryListener, homeEggListener, homeTabFeedListener);
            homeFeedFragment.setParentPool(parentPool);
            registeredFragments.put(position, homeFeedFragment);
        } else {
            HomeRecommendationFragment homeFeedFragment = (HomeRecommendationFragment) o;
            homeFeedFragment.setListener(homeCategoryListener, homeEggListener, homeTabFeedListener);
            homeFeedFragment.setParentPool(parentPool);
            registeredFragments.put(position, homeFeedFragment);
        }
        return o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public BaseRecommendationFragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public int getCount() {
        return recommendationTabDataModelList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return recommendationTabDataModelList.get(position).getName();
    }

    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
    }
}
