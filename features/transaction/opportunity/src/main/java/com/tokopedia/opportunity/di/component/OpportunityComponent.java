package com.tokopedia.opportunity.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.opportunity.di.module.OpportunityModule;
import com.tokopedia.opportunity.di.scope.OpportunityScope;
import com.tokopedia.opportunity.fragment.OpportunityDetailFragment;
import com.tokopedia.opportunity.fragment.OpportunityListFragment;
import com.tokopedia.opportunity.fragment.OpportunityTncFragment;
import com.tokopedia.opportunity.snapshot.fragment.SnapShotFragment;

import dagger.Component;

/**
 * Created by normansyahputa on 1/10/18.
 */
@OpportunityScope
@Component(modules = OpportunityModule.class, dependencies = AppComponent.class)
public interface OpportunityComponent {
    void inject(OpportunityListFragment opportunityListFragment);

    void inject(OpportunityDetailFragment opportunityDetailFragment);

    void inject(OpportunityTncFragment opportunityTncFragment);

    void inject(SnapShotFragment snapShotFragment);
}
