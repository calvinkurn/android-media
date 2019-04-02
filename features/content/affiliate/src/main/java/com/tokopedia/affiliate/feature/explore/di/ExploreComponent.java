package com.tokopedia.affiliate.feature.explore.di;

import com.tokopedia.affiliate.common.di.AffiliateComponent;
import com.tokopedia.affiliate.feature.explore.view.fragment.ExploreFragment;

import dagger.Component;

/**
 * @author by yfsx on 03/10/18.
 */
@ExploreScope
@Component(modules = ExploreModule.class, dependencies = AffiliateComponent.class)
public interface ExploreComponent {

    void inject(ExploreFragment exploreFragment);
}
