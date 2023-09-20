package com.tokopedia.epharmacy.ui.bottomsheet

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.common_epharmacy.EPHARMACY_MINI_CONSULTATION_REQUEST_CODE
import com.tokopedia.common_epharmacy.EPHARMACY_UPLOAD_REQUEST_CODE
import com.tokopedia.epharmacy.databinding.EpharmacyChooserBottomSheetBinding
import com.tokopedia.epharmacy.di.DaggerEPharmacyComponent
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.utils.*
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class EPharmacyChooserBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<EpharmacyChooserBottomSheetBinding>()
    private var enableImageURL = ""
    private var groupId = ""
    private var enablerName = ""
    private var consultationSourceId = 0L
    private var tokoConsultationId = ""
    private var duration = ""
    private var price = ""
    companion object {
        fun newInstance(
            enableImageURL: String,
            groupId: String,
            enablerName: String,
            consultationSourceId: Long,
            tokoConsultationId : String,
            price: String?, duration: String?
        ): EPharmacyChooserBottomSheet {
            return EPharmacyChooserBottomSheet().apply {
                showCloseIcon = false
                showHeader = false
                clearContentPadding = true
                isDragable = true
                isHideable = true
                customPeekHeight = 800
                arguments = Bundle().apply {
                    putString(ENABLER_IMAGE_URL, enableImageURL)
                    putString(EPHARMACY_GROUP_ID, groupId)
                    putString(EPHARMACY_ENABLER_NAME, enablerName)
                    putLong(EPHARMACY_ENABLER_ID, consultationSourceId)
                    putString(EPHARMACY_CONSULTATION_SOURCE_ID, tokoConsultationId)
                    putString(EPHARMACY_CONS_PRICE, price)
                    putString(EPHARMACY_CONS_DURATION, duration)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EpharmacyChooserBottomSheetBinding.inflate(
            inflater,
            container,
            false
        )
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        EPharmacyMiniConsultationAnalytics.viewAttachPrescriptionsOptionsPage(enablerName, groupId)
    }

    private fun init() {
        extractArguments()
        binding?.let {
            with(it) {
                closeIcon.setOnClickListener {
                    closeBottomSheet()
                }
                setOnDismissListener {
                    activity?.finish()
                }
            }
        }
        setupBottomSheetUiData()
    }

    private fun extractArguments() {
        enableImageURL = arguments?.getString(ENABLER_IMAGE_URL) ?: ""
        groupId = arguments?.getString(EPHARMACY_GROUP_ID) ?: ""
        enablerName = arguments?.getString(EPHARMACY_ENABLER_NAME) ?: ""
        consultationSourceId = arguments?.getLong(EPHARMACY_ENABLER_ID) ?: 0L
        tokoConsultationId = arguments?.getString(EPHARMACY_CONSULTATION_SOURCE_ID) ?: ""
        price = arguments?.getString(EPHARMACY_CONS_PRICE) ?: ""
        duration = arguments?.getString(EPHARMACY_CONS_DURATION) ?: ""
    }

    private fun setupBottomSheetUiData() {
        binding?.run {
            context?.resources?.let { res ->
                chooserUpload.lblPAPTittleOptionBottomsheet.text = res.getString(com.tokopedia.epharmacy.R.string.epharmacy_upload_resep_dokter_chooser_title)
                chooserUpload.lblPAPDescriptionOptionBottomsheet.text = res.getString(com.tokopedia.epharmacy.R.string.epharmacy_upload_resep_dokter_chooser_subtitle)
                chooserMiniConsultation.lblPAPTittleOptionBottomsheet.text = res.getString(com.tokopedia.epharmacy.R.string.epharmacy_mini_consult_chooser_title)
                chooserMiniConsultation.lblPAPDescriptionOptionBottomsheet.text = res.getString(com.tokopedia.epharmacy.R.string.eepharmacy_mini_consult_chooser_subtitle)
                chooserUpload.stepIcon.loadImage(UPLOAD_CHOOSER_IMAGE_URL)
                chooserMiniConsultation.stepIcon.loadImage(MINI_CONS_CHOOSER_IMAGE_URL)
                chooserMiniConsultation.baruLabel.show()
                if (enableImageURL.isNotBlank()) {
                    bottomImageLogo.show()
                    bottomImageLogo.loadImage(enableImageURL)
                } else {
                    bottomImageLogo.hide()
                }

                chooserUpload.parent.setOnClickListener {
                    uploadResepAction()
                }
                chooserMiniConsultation.parent.setOnClickListener {
                    miniConsultationAction()
                }
                if(duration.isNotBlank() || price.isNotBlank()){
                    chooserMiniConsultation.payGroup.show()
                    chooserMiniConsultation.durationValue.text = duration
                    chooserMiniConsultation.feeValue.text = price
                }
            }
        }
    }

    private fun closeBottomSheet() {
        dismiss()
        activity?.finish()
    }

    private fun uploadResepAction() {
        activity?.setResult(
            EPHARMACY_UPLOAD_REQUEST_CODE,
            Intent().apply {
                putExtra(EPHARMACY_GROUP_ID, groupId)
                putExtra(EPHARMACY_ENABLER_NAME, enablerName)
            }
        )
        closeBottomSheet()
    }

    private fun miniConsultationAction() {
        activity?.setResult(
            EPHARMACY_MINI_CONSULTATION_REQUEST_CODE,
            Intent().apply {
                putExtra(EPHARMACY_GROUP_ID, groupId)
                putExtra(EPHARMACY_ENABLER_NAME, enablerName)
                putExtra(EPHARMACY_ENABLER_ID, consultationSourceId)
                putExtra(EPHARMACY_CONSULTATION_SOURCE_ID, tokoConsultationId)
            }
        )
        closeBottomSheet()
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
