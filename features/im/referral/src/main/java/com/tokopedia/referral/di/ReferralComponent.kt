package com.tokopedia.referral.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.referral.view.fragment.FragmentReferral
import com.tokopedia.referral.view.fragment.FragmentReferralFriendsWelcome

import dagger.Component

/**
 * Created by ashwanityagi on 22/01/18.
 */

@ReferralScope
@Component(modules = arrayOf(ReferralModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface ReferralComponent {
    fun inject(fragmentReferral: FragmentReferral)
    fun inject(fragmentReferral: FragmentReferralFriendsWelcome)
}
