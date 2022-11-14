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
import com.tokopedia.epharmacy.viewmodel.EpharmacyReminderBsViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.media.loader.loadImageFitCenter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class EpharmacyReminderScreenBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<EpharmacyReminderScreenBottomSheetBinding>()

    companion object {
        fun newInstance(): EpharmacyReminderScreenBottomSheet {
            return EpharmacyReminderScreenBottomSheet().apply {
                showCloseIcon = false
                showHeader = false
                clearContentPadding = true
                isDragable = true
                isHideable = true
                customPeekHeight = 800
            }
        }
    }

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null

    private val viewModel by lazy {
        viewModelFactory?.let {
            ViewModelProvider(this, it).get(
                EpharmacyReminderBsViewModel::class.java
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
                }
                is Fail -> {
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
                reminderParentView.setButtonFull(true)
                reminderParentView.errorAction.hide()
            }
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
