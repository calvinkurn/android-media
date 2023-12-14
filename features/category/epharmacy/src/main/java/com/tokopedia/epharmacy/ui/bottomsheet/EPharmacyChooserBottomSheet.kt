package com.tokopedia.epharmacy.ui.bottomsheet

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.common_epharmacy.EPHARMACY_MINI_CONSULTATION_REQUEST_CODE
import com.tokopedia.common_epharmacy.EPHARMACY_UPLOAD_REQUEST_CODE
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.databinding.EpharmacyChooserBottomSheetBinding
import com.tokopedia.epharmacy.di.DaggerEPharmacyComponent
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.utils.ENABLER_IMAGE_URL
import com.tokopedia.epharmacy.utils.EPHARMACY_CONS_DURATION
import com.tokopedia.epharmacy.utils.EPHARMACY_CONS_PRICE
import com.tokopedia.epharmacy.utils.EPHARMACY_ENABLER_NAME
import com.tokopedia.epharmacy.utils.EPHARMACY_GROUP_ID
import com.tokopedia.epharmacy.utils.EPHARMACY_IS_ONLY_CONSULT
import com.tokopedia.epharmacy.utils.EPHARMACY_IS_OUTSIDE_WORKING_HOURS
import com.tokopedia.epharmacy.utils.EPHARMACY_NOTE
import com.tokopedia.epharmacy.utils.EPharmacyMiniConsultationAnalytics
import com.tokopedia.epharmacy.utils.EPharmacyUtils
import com.tokopedia.epharmacy.utils.MINI_CONS_CHOOSER_IMAGE_URL
import com.tokopedia.epharmacy.utils.MINI_CONS_CHOOSER_IMAGE_URL_DISABLED
import com.tokopedia.epharmacy.utils.UPLOAD_CHOOSER_IMAGE_URL
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.webview.ext.decode
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class EPharmacyChooserBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<EpharmacyChooserBottomSheetBinding>()
    private var enableImageURL = String.EMPTY
    private var groupId = String.EMPTY
    private var enablerName = String.EMPTY
    private var duration = String.EMPTY
    private var price = String.EMPTY
    private var note = String.EMPTY
    private var isOutsideWorkingHours = false
    private var isOnlyConsultation = false

    companion object {
        fun newInstance(
            enableImageURL: String,
            groupId: String,
            enablerName: String,
            price: String?,
            duration: String?,
            note: String?,
            isOutsideWorkingHours: Boolean,
            isOnlyConsult: Boolean
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
                    putString(EPHARMACY_CONS_PRICE, price)
                    putString(EPHARMACY_CONS_DURATION, duration)
                    putString(EPHARMACY_NOTE, note)
                    putBoolean(EPHARMACY_IS_OUTSIDE_WORKING_HOURS, isOutsideWorkingHours)
                    putBoolean(EPHARMACY_IS_ONLY_CONSULT, isOnlyConsult)
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
        enableImageURL = arguments?.getString(ENABLER_IMAGE_URL)?.decode().orEmpty()
        groupId = arguments?.getString(EPHARMACY_GROUP_ID).orEmpty()
        enablerName = arguments?.getString(EPHARMACY_ENABLER_NAME).orEmpty()
        price = arguments?.getString(EPHARMACY_CONS_PRICE).orEmpty()
        duration = arguments?.getString(EPHARMACY_CONS_DURATION).orEmpty()
        note = arguments?.getString(EPHARMACY_NOTE).orEmpty()
        isOutsideWorkingHours = arguments?.getBoolean(EPHARMACY_IS_OUTSIDE_WORKING_HOURS).orFalse()
        isOnlyConsultation = arguments?.getBoolean(EPHARMACY_IS_ONLY_CONSULT).orFalse()
    }

    private fun setupBottomSheetUiData() {
        binding?.run {
            context?.resources?.let { res ->
                if (isOnlyConsultation) {
                    lblPAPTitleBottomsheet.text = res.getString(R.string.epharmacy_mini_consult_chooser_title)
                    chooserUpload.parent.hide()
                } else {
                    chooserUpload.parent.show()
                    chooserUpload.lblPAPTittleOptionBottomsheet.text = res.getString(R.string.epharmacy_upload_resep_dokter_chooser_title)
                    chooserUpload.lblPAPDescriptionOptionBottomsheet.text = res.getString(R.string.epharmacy_upload_resep_dokter_chooser_subtitle)
                    chooserUpload.stepIcon.loadImage(UPLOAD_CHOOSER_IMAGE_URL)
                    chooserUpload.divider.hide()
                    chooserUpload.parent.setOnClickListener {
                        uploadResepAction()
                    }
                }
                chooserMiniConsultation.lblPAPTittleOptionBottomsheet.text = res.getString(R.string.epharmacy_mini_consult_chooser_title)
                chooserMiniConsultation.lblPAPDescriptionOptionBottomsheet.text = res.getString(R.string.eepharmacy_mini_consult_chooser_subtitle)
                chooserMiniConsultation.stepIcon.loadImage(MINI_CONS_CHOOSER_IMAGE_URL)
                if (enableImageURL.isNotBlank()) {
                    bottomImageLogo.show()
                    bottomImageLogo.loadImage(enableImageURL)
                } else {
                    bottomImageLogo.hide()
                }

                if (isOutsideWorkingHours) {
                    renderClosingHours()
                    chooserMiniConsultation.parent.setOnClickListener(null)
                } else {
                    chooserMiniConsultation.parent.setOnClickListener {
                        miniConsultationAction()
                    }
                }

                renderDuration(duration)
                renderPrice(price)
                renderNote(note)
                btnPelajari.setOnClickListener {
                    routePelajari()
                }
            }
        }
    }

    private fun routePelajari() {
        RouteManager.route(context, "https://www.tokopedia.com/help/article/apa-itu-chat-dokter-di-tokopedia")
    }

    private fun renderDuration(durationText: String?) {
        binding?.chooserMiniConsultation?.apply {
            if (durationText?.isNotBlank().orFalse()) {
                lblDuration.show()
                lblChatDoctorDuration.show()
                lblChatDoctorDuration.text = durationText
            }
        }
    }

    private fun renderPrice(priceText: String?) {
        binding?.chooserMiniConsultation?.apply {
            if (priceText?.isNotBlank().orFalse()) {
                lblBiayaChatDokter.show()
                lblChatDoctorFee.show()
                lblChatDoctorFee.text = priceText
            }
        }
    }

    private fun renderClosingHours() {
        binding?.chooserMiniConsultation?.let {
            with(it) {
                val disableColor = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN400)
                lblPAPTittleOptionBottomsheet.setTextColor(disableColor)
                lblPAPDescriptionOptionBottomsheet.setTextColor(disableColor)
                baruLabel.setLabelType(Label.HIGHLIGHT_LIGHT_GREY)
                lblBiayaChatDokter.setTextColor(disableColor)
                lblDuration.setTextColor(disableColor)
                lblChatDoctorFee.setTextColor(disableColor)
                lblChatDoctorDuration.setTextColor(disableColor)
                chevron.isEnabled = false
                stepIcon.loadImage(MINI_CONS_CHOOSER_IMAGE_URL_DISABLED)
            }
        }
    }

    private fun renderNote(note: String) {
        if (note.isNotBlank()) {
            context?.let {
                binding?.chooserMiniConsultation?.noteLl?.show()
                context?.let {
                    binding?.chooserMiniConsultation?.lblClosingSoon?.text = EPharmacyUtils.getTextFromHtml(note)
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
