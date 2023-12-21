package com.tokopedia.epharmacy.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.epharmacy.ui.activity.EPharmacyActivity
import com.tokopedia.epharmacy.ui.activity.EPharmacyAttachPrescriptionActivity
import com.tokopedia.epharmacy.ui.activity.EPharmacyCheckoutActivity
import com.tokopedia.epharmacy.ui.activity.EPharmacyCommonBsActivity
import com.tokopedia.epharmacy.ui.activity.EPharmacyChatLoadingActivity
import com.tokopedia.epharmacy.ui.activity.EPharmacyOrderDetailActivity
import com.tokopedia.epharmacy.ui.activity.EPharmacyReminderActivity
import com.tokopedia.epharmacy.ui.activity.EPharmacyViewPrescriptionActivity
import com.tokopedia.epharmacy.ui.bottomsheet.EPharmacyChooserBottomSheet
import com.tokopedia.epharmacy.ui.bottomsheet.EPharmacyReminderScreenBottomSheet
import com.tokopedia.epharmacy.ui.bottomsheet.MiniConsultationMasterBottomSheetInfo
import com.tokopedia.epharmacy.ui.fragment.EPharmacyCheckoutFragment
import com.tokopedia.epharmacy.ui.fragment.EPharmacyChatLoadingFragment
import com.tokopedia.epharmacy.ui.fragment.EPharmacyOrderDetailFragment
import com.tokopedia.epharmacy.ui.fragment.EPharmacyPrescriptionAttachmentPageFragment
import com.tokopedia.epharmacy.ui.fragment.EPharmacyQuantityChangeFragment
import com.tokopedia.epharmacy.ui.fragment.UploadPrescriptionFragment
import dagger.Component

@EPharmacyScope
@Component(modules = [EPharmacyModule::class, EPharmacyVMModule::class], dependencies = [BaseAppComponent::class])
interface EPharmacyComponent {

    fun inject(fragment: EPharmacyCheckoutFragment)

    fun inject(fragment: EPharmacyQuantityChangeFragment)

    fun inject(ePharmacyActivity: EPharmacyActivity) {}

    fun inject(ePharmacyAttachPrescriptionActivity: EPharmacyAttachPrescriptionActivity) {}

    fun inject(ePharmacyCheckoutActivity: EPharmacyCheckoutActivity) {}

    fun inject(ePharmacyReminderActivity: EPharmacyReminderActivity) {}

    fun inject(ePharmacyChatLoadingActivity: EPharmacyChatLoadingActivity) {}

    fun inject(ePharmacyCommonBsActivity: EPharmacyCommonBsActivity) {}

    fun inject(ePharmacyViewPrescriptionActivity: EPharmacyViewPrescriptionActivity) {}

    fun inject(ePharmacyOrderDetailActivity: EPharmacyOrderDetailActivity) {}

    fun inject(uploadPrescriptionFragment: UploadPrescriptionFragment)

    fun inject(epharmacyReminderScreenBottomSheet: EPharmacyReminderScreenBottomSheet)

    fun inject(epharmacyPrescriptionAttachmentPage: EPharmacyPrescriptionAttachmentPageFragment)

    fun inject(miniConsultationMasterBottomSheetInfo: MiniConsultationMasterBottomSheetInfo)

    fun inject(chooserBottomSheet: EPharmacyChooserBottomSheet)

    fun inject(ePharmacyChatLoadingFragment: EPharmacyChatLoadingFragment)

    fun inject(ePharmacyOrderDetailFragment: EPharmacyOrderDetailFragment)

}
