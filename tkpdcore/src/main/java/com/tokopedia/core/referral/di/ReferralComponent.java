package com.tokopedia.core.referral.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.drawer2.domain.TokoCashRepository;
import com.tokopedia.core.referral.domain.ReferralRepository;
import com.tokopedia.core.referral.fragment.FragmentReferral;

import dagger.Component;

/**
 * Created by ashwanityagi on 22/01/18.
 */

@ReferralScope
@Component(modules =ReferralModule.class, dependencies = AppComponent.class)
public interface ReferralComponent {

    TokoCashRepository getTokoCashRepository();

    ReferralRepository referralRepository();

    void inject(FragmentReferral fragmentReferral);

}
