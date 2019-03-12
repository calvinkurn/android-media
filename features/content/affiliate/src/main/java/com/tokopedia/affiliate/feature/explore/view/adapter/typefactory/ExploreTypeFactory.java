package com.tokopedia.affiliate.feature.explore.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.EmptyExploreViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreEmptySearchViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.FilterViewModel;

/**
 * @author by yfsx on 24/09/18.
 */
public interface ExploreTypeFactory extends AdapterTypeFactory {

    int type(ExploreViewModel exploreViewModel);

    int type(EmptyExploreViewModel emptyExploreViewModel);

    int type(ExploreEmptySearchViewModel exploreEmptySearchViewModel);

    int type(FilterViewModel filterViewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
