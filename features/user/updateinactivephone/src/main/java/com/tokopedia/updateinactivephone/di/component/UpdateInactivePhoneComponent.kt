package com.tokopedia.updateinactivephone.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.updateinactivephone.di.UpdateInactivePhoneScope
import com.tokopedia.updateinactivephone.view.activity.ChangeInactiveFormRequestActivity
import com.tokopedia.updateinactivephone.di.module.UpdateInactivePhoneModule
import com.tokopedia.updateinactivephone.di.module.UpdateInactivePhoneQueryModule
import com.tokopedia.updateinactivephone.di.module.UpdateInactivePhoneViewModelModule
import com.tokopedia.updateinactivephone.view.activity.ChangeInactivePhoneRequestSubmittedActivity
import com.tokopedia.updateinactivephone.view.fragment.ChangeInactivePhoneFragment
import com.tokopedia.updateinactivephone.view.fragment.SelectImageNewPhoneFragment
import com.tokopedia.updateinactivephone.view.fragment.UpdateNewPhoneEmailFragment

import dagger.Component

@UpdateInactivePhoneScope
@Component(modules = [
    UpdateInactivePhoneModule::class,
    UpdateInactivePhoneViewModelModule::class,
    UpdateInactivePhoneQueryModule::class
], dependencies = [
    BaseAppComponent::class
])
interface UpdateInactivePhoneComponent {
    fun inject(changeInactivePhoneFragment: ChangeInactivePhoneFragment)
    fun inject(selectImageNewPhoneFragment: SelectImageNewPhoneFragment)
    fun inject(updateNewPhoneEmailFragment: UpdateNewPhoneEmailFragment)
    fun inject(changeInactiveFormRequestActivity: ChangeInactiveFormRequestActivity)
    fun inject(changeInactiveFormRequestSubmittedActivity: ChangeInactivePhoneRequestSubmittedActivity)
}
