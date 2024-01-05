package com.tokopedia.epharmacy.ui.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.epharmacy.databinding.EpharmacyReminderScreenBottomSheetBinding
import com.tokopedia.epharmacy.di.DaggerEPharmacyComponent
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.network.request.EpharmacyUserReminderParam
import com.tokopedia.epharmacy.utils.CLOSE_TIME
import com.tokopedia.epharmacy.utils.CONSULTATION_SOURCE_ID
import com.tokopedia.epharmacy.utils.EPHARMACY_ENABLER_NAME
import com.tokopedia.epharmacy.utils.EPHARMACY_GROUP_ID
import com.tokopedia.epharmacy.utils.EPharmacyMiniConsultationAnalytics
import com.tokopedia.epharmacy.utils.EPharmacyUtils
import com.tokopedia.epharmacy.utils.IS_OUTSIDE_WORKING_HOURS
import com.tokopedia.epharmacy.utils.LabelKeys.Companion.IN_WORKING_HOURS
import com.tokopedia.epharmacy.utils.LabelKeys.Companion.OUTSIDE_WORKING_HOURS
import com.tokopedia.epharmacy.utils.OPEN_TIME
import com.tokopedia.epharmacy.utils.OUTSIDE_WORKING_HOURS_SOURCE
import com.tokopedia.epharmacy.utils.REMINDER_ILLUSTRATION_IMAGE
import com.tokopedia.epharmacy.utils.REMINDER_TYPE
import com.tokopedia.epharmacy.utils.WORKING_HOURS_SOURCE
import com.tokopedia.epharmacy.viewmodel.EPharmacyReminderBsViewModel
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageFitCenter
import com.tokopedia.picker.common.basecomponent.utils.rootCurrentView
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject
import com.tokopedia.epharmacy.R as epharmacyR

class EPharmacyReminderScreenBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<EpharmacyReminderScreenBottomSheetBinding>()
    private var openTime = String.EMPTY
    private var closeTime = String.EMPTY
    private var isOutsideWorkingHours = false
    private val APPLNK_HOME = "tokopedia://home"

    companion object {
        fun newInstance(
            isOutsideWorkingHours: Boolean = false,
            openTime: String,
            closeTime: String,
            reminderType: Int,
            consultationSourceId: Long,
            groupId: String?,
            enablerName: String?
        ): EPharmacyReminderScreenBottomSheet {
            return EPharmacyReminderScreenBottomSheet().apply {
                showCloseIcon = false
                showHeader = false
                clearContentPadding = true
                isDragable = true
                isHideable = true
                customPeekHeight = 800
                arguments = Bundle().apply {
                    putBoolean(IS_OUTSIDE_WORKING_HOURS, isOutsideWorkingHours)
                    putString(OPEN_TIME, openTime)
                    putString(CLOSE_TIME, closeTime)
                    putInt(REMINDER_TYPE, reminderType)
                    putString(EPHARMACY_GROUP_ID, groupId)
                    putString(EPHARMACY_ENABLER_NAME, enablerName)
                    putLong(CONSULTATION_SOURCE_ID, consultationSourceId)
                }
            }
        }
    }

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null

    private val viewModel by lazy {
        viewModelFactory?.let {
            ViewModelProvider(this, it).get(
                EPharmacyReminderBsViewModel::class.java
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EpharmacyReminderScreenBottomSheetBinding.inflate(
            inflater,
            container,
            false
        )
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        extractArguments()
        sendViewEvent()
        setupBottomSheetUiData()
        setupObservers()
    }

    private fun extractArguments() {
        openTime = arguments?.getString(OPEN_TIME).orEmpty()
        closeTime = arguments?.getString(CLOSE_TIME).orEmpty()
        isOutsideWorkingHours = arguments?.getBoolean(IS_OUTSIDE_WORKING_HOURS, false).orFalse()
    }

    private fun sendViewEvent() {
        EPharmacyMiniConsultationAnalytics.viewNoDoctorScreen(
            arguments?.getString(EPHARMACY_GROUP_ID).orEmpty(),
            arguments?.getString(EPHARMACY_ENABLER_NAME).orEmpty(),
            if (isOutsideWorkingHours) { OUTSIDE_WORKING_HOURS } else IN_WORKING_HOURS
        )
    }

    private fun setupObservers() {
        viewModel?.reminderLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if (it.data.data?.isSuccess == true) {
                        context?.resources?.let { res ->
                            showToast(
                                Toaster.TYPE_NORMAL,
                                res.getString(epharmacyR.string.epharmacy_reminder_success)
                            )
                        }
                    } else {
                        context?.resources?.let { res ->
                            if (it.data.data?.error.isNullOrBlank()) {
                                showToast(Toaster.TYPE_ERROR, it.data.data?.error.orEmpty())
                            } else {
                                showToast(Toaster.TYPE_ERROR, context?.resources?.getString(epharmacyR.string.epharmacy_reminder_fail).orEmpty())
                            }
                        }
                    }
                }
                is Fail -> {
                    when (it.throwable) {
                        is UnknownHostException, is SocketTimeoutException -> showToast(
                            Toaster.TYPE_ERROR,
                            context?.resources?.getString(epharmacyR.string.epharmacy_internet_error).orEmpty()
                        )
                        else -> showToast(
                            Toaster.TYPE_ERROR,
                            context?.resources?.getString(epharmacyR.string.epharmacy_reminder_fail).orEmpty()
                        )
                    }
                }
            }
            dismiss()
        }
    }

    private fun setupBottomSheetUiData() {
        binding?.let {
            with(it) {
                epharmacyGlobalError.errorIllustration.loadImageFitCenter(REMINDER_ILLUSTRATION_IMAGE)
                epharmacyGlobalError.errorTitle.text = getString(epharmacyR.string.epharmacy_reminder_title)
                epharmacyGlobalError.errorDescription.text = getMessageString()
                epharmacyGlobalError.errorAction.text = getString(epharmacyR.string.epharmacy_reminder_button_text)
                epharmacyGlobalError.errorAction.show()
                epharmacyGlobalError.setActionClickListener {
                    requestParams()?.let { it1 ->
                        viewModel?.setForReminder(it1)
                        EPharmacyMiniConsultationAnalytics.clickIngatkanSaya(
                            arguments?.getString(EPHARMACY_GROUP_ID).orEmpty(),
                            arguments?.getString(EPHARMACY_ENABLER_NAME).orEmpty(),
                            if (isOutsideWorkingHours) { OUTSIDE_WORKING_HOURS } else IN_WORKING_HOURS
                        )
                    }
                }
                epharmacyGlobalError.errorSecondaryAction.text = getString(epharmacyR.string.epharmacy_reminder_back_text)
                epharmacyGlobalError.errorSecondaryAction.show()
                epharmacyGlobalError.setSecondaryActionClickListener {
                    activity?.finish()
                    RouteManager.route(context, APPLNK_HOME)
                }
            }
        }
    }

    private fun getMessageString(): String {
        val openTimeLocal: Date? = EPharmacyUtils.formatDateToLocal(dateString = arguments?.getString(OPEN_TIME).orEmpty())
        val closeTimeLocal: Date? = EPharmacyUtils.formatDateToLocal(dateString = arguments?.getString(CLOSE_TIME).orEmpty())
        return getString(
            if (isOutsideWorkingHours) {
                epharmacyR.string.epharmacy_reminder_description_outside
            } else {
                epharmacyR.string.epharmacy_reminder_description
            },
            EPharmacyUtils.getTimeFromDate(openTimeLocal),
            EPharmacyUtils.getTimeFromDate(closeTimeLocal)
        )
    }

    private fun requestParams(): EpharmacyUserReminderParam? {
        val reminderType = arguments?.getInt(REMINDER_TYPE)
        val consultationSourceId = arguments?.getLong(CONSULTATION_SOURCE_ID)
        return if (reminderType != null) {
            EpharmacyUserReminderParam(
                input = EpharmacyUserReminderParam.Input(
                    reminderType = reminderType,
                    EpharmacyUserReminderParam.Input.EpharmacyConsultationInfoParams(
                        consultationSourceId = consultationSourceId,
                        if (isOutsideWorkingHours)OUTSIDE_WORKING_HOURS_SOURCE else WORKING_HOURS_SOURCE
                    )
                )
            )
        } else {
            context?.resources?.let { res ->
                showToast(Toaster.TYPE_ERROR, res.getString(epharmacyR.string.epharmacy_reminder_fail))
            }
            return null
        }
    }

    private fun showToast(toasterType: Int = Toaster.TYPE_NORMAL, message: String) {
        activity?.rootCurrentView()?.let { it ->
            Toaster.build(it, message, Toaster.LENGTH_LONG, toasterType).show()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        initInject()
        return super.onCreateDialog(savedInstanceState)
    }

    private fun initInject() {
        getComponent().inject(this)
    }

    private fun getComponent(): EPharmacyComponent =
        DaggerEPharmacyComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()
}
