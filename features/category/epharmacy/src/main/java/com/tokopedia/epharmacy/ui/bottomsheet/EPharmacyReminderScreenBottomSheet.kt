package com.tokopedia.epharmacy.ui.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.epharmacy.databinding.EpharmacyReminderScreenBottomSheetBinding
import com.tokopedia.epharmacy.di.DaggerEPharmacyComponent
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.network.request.EPharmacyReminderScreenParam
import com.tokopedia.epharmacy.utils.*
import com.tokopedia.epharmacy.viewmodel.EPharmacyReminderBsViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.media.loader.loadImageFitCenter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class EPharmacyReminderScreenBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<EpharmacyReminderScreenBottomSheetBinding>()

    companion object {
        fun newInstance(openTime : String? , closeTime : String?, reminderType: Long, consultationSourceId: String? = null): EPharmacyReminderScreenBottomSheet {
            return EPharmacyReminderScreenBottomSheet().apply {
                showCloseIcon = false
                showHeader = false
                clearContentPadding = true
                isDragable = true
                isHideable = true
                customPeekHeight = 800
                arguments = Bundle().apply {
                    putString(OPEN_TIME,openTime)
                    putString(CLOSE_TIME,closeTime)
                    putLong(REMINDER_TYPE, reminderType)
                    if (consultationSourceId != null) {
                        putString(CONSULTATION_SOURCE_ID, consultationSourceId)
                    }
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
        setupBottomSheetUiData()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel?.reminderLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    context?.resources?.let { res ->
                        showToast(res.getString(com.tokopedia.epharmacy.R.string.epharmacy_reminder_success))
                    }
                }
                is Fail -> {
                    context?.resources?.let { res ->
                        showToast(res.getString(com.tokopedia.epharmacy.R.string.epharmacy_reminder_fail))
                    }
                }
            }
        }
    }

    private fun setupBottomSheetUiData() {
        binding?.let {
            with(it) {
                reminderParentView.errorIllustration.loadImageFitCenter("https://images.tokopedia.net/img/pharmacy-illustration.png")
                reminderParentView.errorTitle.text = getString(com.tokopedia.epharmacy.R.string.epharmacy_reminder_title)
                reminderParentView.errorDescription.text = getString(com.tokopedia.epharmacy.R.string.epharmacy_reminder_description)
                reminderParentView.errorSecondaryAction.text = getString(com.tokopedia.epharmacy.R.string.epharmacy_reminder_button_text)
                reminderParentView.setSecondaryActionClickListener {
                    requestParams()?.let { it1 -> viewModel?.setForReminder(it1) }
                }
                reminderParentView.setButtonFull(true)
                reminderParentView.errorAction.hide()
            }
        }
    }

    private fun requestParams(): EPharmacyReminderScreenParam? {
        val reminderType = arguments?.getLong(REMINDER_TYPE)
        val consultationSourceId = arguments?.getString(CONSULTATION_SOURCE_ID)
        return if (reminderType != null) {
            EPharmacyReminderScreenParam(
                input = EPharmacyReminderScreenParam.Input(
                    reminderType = reminderType,
                    EPharmacyReminderScreenParam.Input.EpharmacyConsultationInfoParams(
                        consultationSourceId = consultationSourceId
                    )
                )
            )
        } else {
            context?.resources?.let { res ->
                showToast(res.getString(com.tokopedia.epharmacy.R.string.epharmacy_reminder_fail))
            }
            return null
        }
    }

    private fun showToast(message: String) {
        view?.let { it ->
            Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
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
