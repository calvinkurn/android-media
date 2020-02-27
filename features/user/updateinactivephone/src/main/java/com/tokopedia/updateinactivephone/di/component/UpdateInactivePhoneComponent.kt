package com.tokopedia.updateinactivephone.di.component

import com.tokopedia.core.base.di.component.AppComponent
import com.tokopedia.updateinactivephone.di.scope.UpdateInactivePhoneScope
import com.tokopedia.updateinactivephone.view.activity.ChangeInactiveFormRequestActivity
import com.tokopedia.updateinactivephone.di.module.UpdateInactivePhoneModule
import com.tokopedia.updateinactivephone.view.fragment.ChangeInactivePhoneFragment
import com.tokopedia.updateinactivephone.view.fragment.SelectImageNewPhoneFragment

import dagger.Component

@UpdateInactivePhoneScope
@Component(modules = [UpdateInactivePhoneModule::class], dependencies = [AppComponent::class])
interface UpdateInactivePhoneComponent {

    fun inject(changeInactivePhoneFragment: ChangeInactivePhoneFragment)

    fun inject(selectImageNewPhoneFragment: SelectImageNewPhoneFragment)

    fun inject(changeInactiveFormRequestActivity: ChangeInactiveFormRequestActivity)
}
