package com.tokopedia.epharmacy.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.epharmacy.ui.activity.EPharmacyActivity
import com.tokopedia.epharmacy.ui.activity.EPharmacyAttachPrescriptionActivity
import com.tokopedia.epharmacy.ui.bottomsheet.EPharmacyChooserBottomSheet
import com.tokopedia.epharmacy.ui.bottomsheet.EPharmacyReminderScreenBottomSheet
import com.tokopedia.epharmacy.ui.bottomsheet.MiniConsultationMasterBottomSheetInfo
import com.tokopedia.epharmacy.ui.fragment.EPharmacyPrescriptionAttachmentPageFragment
import com.tokopedia.epharmacy.ui.fragment.UploadPrescriptionFragment
import dagger.Component

@EPharmacyScope
@Component(modules = [EPharmacyModule::class, EPharmacyVMModule::class], dependencies = [BaseAppComponent::class])
interface EPharmacyComponent {

    fun inject(ePharmacyActivity: EPharmacyActivity) {}

    fun inject(ePharmacyAttachPrescriptionActivity: EPharmacyAttachPrescriptionActivity) {}

    fun inject(uploadPrescriptionFragment: UploadPrescriptionFragment)

    fun inject(epharmacyReminderScreenBottomSheet: EPharmacyReminderScreenBottomSheet)

    fun inject(epharmacyPrescriptionAttachmentPage: EPharmacyPrescriptionAttachmentPageFragment)

    fun inject(miniConsultationMasterBottomSheetInfo: MiniConsultationMasterBottomSheetInfo)

    fun inject(chooserBottomSheet: EPharmacyChooserBottomSheet)
}
