package com.tokopedia.review.feature.reputationhistory.di;

import com.tokopedia.review.common.di.ReviewComponent;
import com.tokopedia.review.feature.reputationhistory.view.fragment.SellerReputationFragment;

import dagger.Component;

/**
 * Created by normansyahputa on 2/13/18.
 */
@SellerReputationScope
@Component(modules = {SellerReputationModule.class}, dependencies = {ReviewComponent.class})
public interface SellerReputationComponent {
    void inject(SellerReputationFragment sellerReputationFragment);
}