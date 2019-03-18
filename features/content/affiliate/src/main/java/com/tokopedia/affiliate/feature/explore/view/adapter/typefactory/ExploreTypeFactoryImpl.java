package com.tokopedia.affiliate.feature.explore.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder;
import com.tokopedia.affiliate.feature.explore.view.adapter.FilterAdapter;
import com.tokopedia.affiliate.feature.explore.view.adapter.viewholder.ExploreEmptySearchViewHolder;
import com.tokopedia.affiliate.feature.explore.view.adapter.viewholder.ExploreViewHolder;
import com.tokopedia.affiliate.feature.explore.view.adapter.viewholder.FilterViewHolder;
import com.tokopedia.affiliate.feature.explore.view.adapter.viewholder.banner.ExploreBannerViewHolder;
import com.tokopedia.affiliate.feature.explore.view.adapter.viewholder.profile.PopularProfileViewHolder;
import com.tokopedia.affiliate.feature.explore.view.adapter.viewholder.recommendation.RecommendationViewHolder;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.EmptyExploreViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreBannerViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreEmptySearchViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreProductViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.FilterListViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.PopularProfileViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.RecommendationViewModel;

/**
 * @author by yfsx on 24/09/18.
 */
public class ExploreTypeFactoryImpl extends BaseAdapterTypeFactory implements ExploreTypeFactory {

    private ExploreContract.View mainView;
    private FilterAdapter.OnFilterClickedListener filterClickedListener;

    public ExploreTypeFactoryImpl(ExploreContract.View mainView,
                                  FilterAdapter.OnFilterClickedListener filterClickedListener) {
        this.mainView = mainView;
        this.filterClickedListener = filterClickedListener;
    }

    @Override
    public int type(ExploreProductViewModel exploreProductViewModel) {
        return ExploreViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptyExploreViewModel emptyExploreViewModel) {
        return EmptyViewHolder.LAYOUT;
    }

    @Override
    public int type(ExploreEmptySearchViewModel exploreEmptySearchViewModel) {
        return ExploreEmptySearchViewHolder.LAYOUT;
    }

    @Override
    public int type(FilterListViewModel filterViewModel) {
        return FilterViewHolder.LAYOUT;
    }

    @Override
    public int type(PopularProfileViewModel popularProfileViewModel) {
        return PopularProfileViewHolder.LAYOUT;
    }

    @Override
    public int type(RecommendationViewModel recommendationViewModel) {
        return RecommendationViewHolder.LAYOUT;
    }

    @Override
    public int type(ExploreBannerViewModel exploreBannerViewModel) {
        return ExploreBannerViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {
        AbstractViewHolder abstractViewHolder;
        if (type == ExploreViewHolder.LAYOUT) {
            abstractViewHolder = new ExploreViewHolder(view, mainView);
        } else if (type == EmptyViewHolder.LAYOUT) {
            abstractViewHolder = new EmptyViewHolder(view);
        } else if (type == ExploreEmptySearchViewHolder.LAYOUT) {
            abstractViewHolder = new ExploreEmptySearchViewHolder(view, mainView);
        } else if (type == FilterViewHolder.LAYOUT) {
            abstractViewHolder = new FilterViewHolder(view, filterClickedListener);
        } else if (type == PopularProfileViewHolder.LAYOUT) {
            abstractViewHolder = new PopularProfileViewHolder(view);
        } else if (type == RecommendationViewHolder.LAYOUT) {
            abstractViewHolder = new RecommendationViewHolder(view);
        } else if (type == ExploreBannerViewHolder.LAYOUT) {
            abstractViewHolder = new ExploreBannerViewHolder(view);
        } else {
            abstractViewHolder = super.createViewHolder(view, type);
        }
        return abstractViewHolder;
    }
}
