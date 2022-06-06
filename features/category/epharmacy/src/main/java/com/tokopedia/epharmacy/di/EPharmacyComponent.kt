package com.tokopedia.epharmacy.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.epharmacy.ui.activity.EPharmacyActivity
import com.tokopedia.epharmacy.ui.fragment.UploadPrescriptionFragment
import dagger.Component

@EPharmacyScope
@Component(modules = [EPharmacyModule::class, EPharmacyVMModule::class], dependencies = [BaseAppComponent::class])
interface EPharmacyComponent {

    @get:ApplicationContext
    val context: Context

    fun injectEPharmacyActivity(ePharmacyActivity: EPharmacyActivity)

    fun injectUploadPrescriptionFragment(uploadPrescriptionFragment: UploadPrescriptionFragment)
}
