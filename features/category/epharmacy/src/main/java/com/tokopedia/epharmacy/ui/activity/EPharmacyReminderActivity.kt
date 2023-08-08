package com.tokopedia.epharmacy.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.di.DaggerEPharmacyComponent
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.ui.bottomsheet.EPharmacyReminderScreenBottomSheet
import com.tokopedia.epharmacy.ui.fragment.EPharmacyCheckoutFragment
import com.tokopedia.epharmacy.utils.DEFAULT_CLOSE_TIME
import com.tokopedia.epharmacy.utils.DEFAULT_OPEN_TIME
import com.tokopedia.epharmacy.utils.EPHARMACY_ENABLER_ID
import com.tokopedia.epharmacy.utils.EPHARMACY_GROUP_ID
import com.tokopedia.epharmacy.utils.EPHARMACY_TOKO_CONSULTATION_ID
import com.tokopedia.epharmacy.utils.OUTSIDE_WORKING_HOURS_SOURCE
import com.tokopedia.epharmacy.utils.TYPE_OUTSIDE_WORKING_HOURS_REMINDER
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class EPharmacyReminderActivity : BaseSimpleActivity(), HasComponent<EPharmacyComponent> {

    private val ePharmacyComponent: EPharmacyComponent by lazy(LazyThreadSafetyMode.NONE) { initInjector() }

    override fun onCreate(savedInstanceState: Bundle?) {
        ePharmacyComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutRes() = R.layout.epharmacy_activity
    override fun getToolbarResourceID() = R.id.e_pharmacy_header
    override fun getParentViewResourceID(): Int = R.id.e_pharmacy_parent_view

    override fun getNewFragment(): Fragment {
        return EPharmacyReminderScreenBottomSheet.newInstance(
            true,
            DEFAULT_OPEN_TIME,DEFAULT_CLOSE_TIME,
            TYPE_OUTSIDE_WORKING_HOURS_REMINDER,
            0L,"","")
    }

    override fun getComponent() = ePharmacyComponent

    private fun initInjector() = DaggerEPharmacyComponent.builder()
        .baseAppComponent(
            (applicationContext as BaseMainApplication)
                .baseAppComponent
        ).build()
}
