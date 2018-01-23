package com.tokopedia.core.referral.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.drawer2.domain.TokoCashRepository;
import com.tokopedia.core.referral.domain.ReferralRepository;
import com.tokopedia.core.referral.fragment.FragmentReferral;
import com.tokopedia.core.util.SessionHandler;

import dagger.Component;

/**
 * Created by ashwanityagi on 22/01/18.
 */

@ReferralScope
@Component(modules =ReferralModule.class, dependencies = AppComponent.class)
public interface ReferralComponent {
    SessionHandler sessionHandler();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    TokoCashRepository getTokoCashRepository();

    ReferralRepository referralRepository();

    void inject(FragmentReferral fragmentReferral);

}
