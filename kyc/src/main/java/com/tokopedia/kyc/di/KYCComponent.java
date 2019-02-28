package com.tokopedia.kyc.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.kyc.view.fragment.ErrorKycConfirmation;
import com.tokopedia.kyc.view.fragment.FragmentCardIDUpload;
import com.tokopedia.kyc.view.fragment.FragmentErrorKyc;
import com.tokopedia.kyc.view.fragment.FragmentFollowupCustomerCare;
import com.tokopedia.kyc.view.fragment.FragmentIntroToOvoUpgradeSteps;
import com.tokopedia.kyc.view.fragment.FragmentSelfieIdPreviewAndUpload;
import com.tokopedia.kyc.view.fragment.FragmentTermsAndConditions;
import com.tokopedia.kyc.view.fragment.FragmentUpgradeToOvo;
import com.tokopedia.kyc.view.fragment.FragmentVerificationFailure;
import com.tokopedia.kyc.view.fragment.FragmentVerificationSuccess;

import dagger.Component;

@KYCScope
@Component(modules = KYCModule.class, dependencies = BaseAppComponent.class)
public interface KYCComponent {
    void inject(ErrorKycConfirmation fragmentUpgradeToOvo);
    void inject(FragmentUpgradeToOvo fragmentUpgradeToOvo);
    void inject(FragmentIntroToOvoUpgradeSteps upgradeToOvoFragment);
    void inject(FragmentCardIDUpload fragmentCardIDUpload);
    void inject(FragmentErrorKyc fragmentErrorUpload);
    void inject(FragmentFollowupCustomerCare fragmentFollowupCustomerCare);
    void inject(FragmentSelfieIdPreviewAndUpload fragmentSelfieIdPreviewAndUpload);
    void inject(FragmentTermsAndConditions fragmentTermsAndConditions);
    void inject(FragmentVerificationFailure fragmentVerificationFailure);
    void inject(FragmentVerificationSuccess fragmentVerificationSuccess);
}
