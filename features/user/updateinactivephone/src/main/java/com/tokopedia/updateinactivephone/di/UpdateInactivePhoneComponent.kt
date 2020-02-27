package com.tokopedia.updateinactivephone.di

import com.tokopedia.core.base.di.component.AppComponent
import com.tokopedia.updateinactivephone.activity.ChangeInactiveFormRequestActivity
import com.tokopedia.updateinactivephone.di.module.UpdateInactivePhoneModule
import com.tokopedia.updateinactivephone.fragment.ChangeInactivePhoneFragment
import com.tokopedia.updateinactivephone.fragment.SelectImageNewPhoneFragment

import dagger.Component

@UpdateInactivePhoneScope
@Component(modules = [UpdateInactivePhoneModule::class], dependencies = [AppComponent::class])
interface UpdateInactivePhoneComponent {

    fun inject(changeInactivePhoneFragment: ChangeInactivePhoneFragment)

    fun inject(selectImageNewPhoneFragment: SelectImageNewPhoneFragment)

    fun inject(changeInactiveFormRequestActivity: ChangeInactiveFormRequestActivity)
}
