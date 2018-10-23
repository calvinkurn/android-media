package com.tokopedia.gm.subscribe.membership.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.gm.subscribe.membership.analytic.GmSubscribeMembershipTracking
import com.tokopedia.gm.subscribe.membership.view.fragment.GmMembershipFragment
import com.tokopedia.gm.subscribe.membership.view.fragment.GmMembershipProductFragment
import dagger.Component

@GmSubscribeMembershipScope
@Component(modules = [GmSubscribeMembershipModule::class], dependencies = [BaseAppComponent::class])
interface GmSubscribeMembershipComponent {
    fun inject(fragment: GmMembershipFragment)
    fun inject(fragment: GmMembershipProductFragment)
}