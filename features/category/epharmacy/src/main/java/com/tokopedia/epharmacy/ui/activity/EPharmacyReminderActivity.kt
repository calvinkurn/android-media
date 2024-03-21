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
import com.tokopedia.epharmacy.utils.DEFAULT_CLOSE_TIME
import com.tokopedia.epharmacy.utils.DEFAULT_OPEN_TIME
import com.tokopedia.epharmacy.utils.TYPE_DOCTOR_NOT_AVAILABLE_REMINDER
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kotlin.extensions.view.toLongOrZero

class EPharmacyReminderActivity : BaseSimpleActivity(), HasComponent<EPharmacyComponent> {

    private var isClosingTime: Boolean = true
    private var openTime: String = DEFAULT_OPEN_TIME
    private var closeTime: String = DEFAULT_CLOSE_TIME
    private var reminderType: Int = TYPE_DOCTOR_NOT_AVAILABLE_REMINDER
    private var consultationSourceId: Long = 0L
    private var groupId = String.EMPTY
    private var enablerName = String.EMPTY
    private val ePharmacyComponent: EPharmacyComponent by lazy(LazyThreadSafetyMode.NONE) { initInjector() }
    override fun onCreate(savedInstanceState: Bundle?) {
        ePharmacyComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutRes() = R.layout.epharmacy_activity
    override fun getToolbarResourceID() = R.id.e_pharmacy_header
    override fun getParentViewResourceID(): Int = R.id.e_pharmacy_parent_view

    override fun getNewFragment(): Fragment {
        extractArguments()
        updateTitle(String.EMPTY)
        return EPharmacyReminderScreenBottomSheet.newInstance(
            isClosingTime,
            openTime,
            closeTime,
            reminderType,
            consultationSourceId,
            groupId,
            enablerName
        )
    }

    private fun extractArguments() {
        intent?.data?.let {
            isClosingTime = it.getBooleanQueryParameter(REMINDER_IS_CLOSING_HOURS, false)
            openTime = it.getQueryParameter(REMINDER_OPEN_TIME_KEY).orEmpty()
            closeTime = it.getQueryParameter(REMINDER_CLOSE_TIME_KEY).orEmpty()
            reminderType = it.getQueryParameter(REMINDER_TYPE_KEY).toIntSafely()
            consultationSourceId = it.getQueryParameter(REMINDER_C_ID_KEY).toLongOrZero()
            groupId = it.getQueryParameter(REMINDER_GROUP_ID_KEY).orEmpty()
            enablerName = it.getQueryParameter(REMINDER_ENABLER_NAME_KEY).orEmpty()
        }
    }

    override fun getComponent() = ePharmacyComponent

    private fun initInjector() = DaggerEPharmacyComponent.builder()
        .baseAppComponent(
            (applicationContext as BaseMainApplication)
                .baseAppComponent
        ).build()

    companion object {
        const val REMINDER_IS_CLOSING_HOURS = "is_closing_hour"
        const val REMINDER_OPEN_TIME_KEY = "open_time"
        const val REMINDER_CLOSE_TIME_KEY = "close_time"
        const val REMINDER_TYPE_KEY = "reminder_type"
        const val REMINDER_C_ID_KEY = "consultation_source_id"
        const val REMINDER_GROUP_ID_KEY = "group_id"
        const val REMINDER_ENABLER_NAME_KEY = "enabler_name"
    }
}
