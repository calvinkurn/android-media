package com.tokopedia.affiliate.feature.explore.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.EmptyExploreViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreEmptySearchViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreProductViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.FilterViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.PopularProfileViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.RecommendationViewModel;

/**
 * @author by yfsx on 24/09/18.
 */
public interface ExploreTypeFactory extends AdapterTypeFactory {

    int type(ExploreProductViewModel exploreProductViewModel);

    int type(EmptyExploreViewModel emptyExploreViewModel);

    int type(ExploreEmptySearchViewModel exploreEmptySearchViewModel);

    int type(FilterViewModel filterViewModel);

    int type(PopularProfileViewModel popularProfileViewModel);

    int type(RecommendationViewModel recommendationViewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
