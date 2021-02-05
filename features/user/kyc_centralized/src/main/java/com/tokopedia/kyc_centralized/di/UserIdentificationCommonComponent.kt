package com.tokopedia.kyc_centralized.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.kyc_centralized.view.fragment.BaseUserIdentificationStepperFragment
import com.tokopedia.kyc_centralized.view.fragment.UserIdentificationFormFinalFragment
import com.tokopedia.kyc_centralized.view.fragment.UserIdentificationInfoFragment
import com.tokopedia.kyc_centralized.view.model.UserIdentificationStepperModel
import dagger.Component

/**
 * @author by nisie on 13/11/18.
 */
@UserIdentificationCommonScope
@Component(modules = [UserIdentificationCommonModule::class, KycCentralizedViewModelModule::class, KycUploadImageModule::class], dependencies = [BaseAppComponent::class])
interface UserIdentificationCommonComponent {
    fun inject(fragment: BaseUserIdentificationStepperFragment<UserIdentificationStepperModel>?)
    fun inject(fragment: UserIdentificationFormFinalFragment?)
    fun inject(fragment: UserIdentificationInfoFragment?)
}