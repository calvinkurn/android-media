package com.tokopedia.common.topupbills.di

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

/**
 * Created by resakemal on 12/08/19.
 */
@CommonTopupBillsScope
@Component(modules = [CommonTopupBillsModule::class], dependencies = [BaseAppComponent::class])
interface CommonTopupBillsComponent {

    fun userSessionInterface(): UserSessionInterface

    fun inject(baseTopupBillsFragment: BaseTopupBillsFragment<Visitable<*>, AdapterTypeFactory>)

}