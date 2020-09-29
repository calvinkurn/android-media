package com.tokopedia.tkpd.tkpdreputation.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.ReviewProductFragment;

import dagger.Component;

/**
 * @author by nisie on 8/11/17.
 */

@ReputationScope
@Component(modules = {ReputationModule.class}, dependencies = BaseAppComponent.class)
public interface ReputationComponent {

    void inject(ReviewProductFragment productReviewFragment);

}
