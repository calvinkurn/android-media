package com.tokopedia.tkpd.tkpdreputation.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.fragment.CreateReviewFragment;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.ImageUploadPreviewFragment;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationDetailFragment;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationFilterFragment;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationFormFragment;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationFragment;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationReportFragment;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.ReviewProductFragment;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.ReviewShopFragment;

import dagger.Component;

/**
 * @author by nisie on 8/11/17.
 */

@ReputationScope
@Component(modules = {ReputationModule.class, ViewModelModule.class}, dependencies = BaseAppComponent.class)
public interface ReputationComponent {

    void inject(InboxReputationFragment inboxReputationFragment);

    void inject(InboxReputationDetailFragment inboxReputationDetailFragment);

    void inject(InboxReputationFormFragment inboxReputationFormFragment);

    void inject(InboxReputationReportFragment inboxReputationReportFragment);

    void inject(ReviewProductFragment productReviewFragment);

    void inject(ReviewShopFragment shopReviewFragment);

    void inject(CreateReviewFragment createReviewFragment);

    void inject(InboxReputationFilterFragment inboxReputationFilterFragment);

    void inject(ImageUploadPreviewFragment imageUploadPreviewFragment);

}
