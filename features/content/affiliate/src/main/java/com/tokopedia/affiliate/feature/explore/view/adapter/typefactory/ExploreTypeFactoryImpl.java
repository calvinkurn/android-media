package com.tokopedia.affiliate.feature.explore.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder;
import com.tokopedia.affiliate.feature.explore.view.adapter.viewholder.ExploreEmptySearchViewHolder;
import com.tokopedia.affiliate.feature.explore.view.adapter.viewholder.ExploreViewHolder;
import com.tokopedia.affiliate.feature.explore.view.adapter.viewholder.FilterViewHolder;
import com.tokopedia.affiliate.feature.explore.view.adapter.viewholder.PopularProfileViewHolder;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.EmptyExploreViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreEmptySearchViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.FilterViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.PopularProfileViewModel;

/**
 * @author by yfsx on 24/09/18.
 */
public class ExploreTypeFactoryImpl extends BaseAdapterTypeFactory implements ExploreTypeFactory {

    private ExploreContract.View mainView;

    public ExploreTypeFactoryImpl(ExploreContract.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public int type(ExploreViewModel exploreViewModel) {
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
    public int type(FilterViewModel filterViewModel) {
        return FilterViewHolder.LAYOUT;
    }

    @Override
    public int type(PopularProfileViewModel popularProfileViewModel) {
        return PopularProfileViewHolder.LAYOUT;
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
            abstractViewHolder = new FilterViewHolder(view);
        } else if (type == PopularProfileViewHolder.LAYOUT) {
            abstractViewHolder = new PopularProfileViewHolder(view);
        } else {
            abstractViewHolder = super.createViewHolder(view, type);
        }
        return abstractViewHolder;
    }
}
