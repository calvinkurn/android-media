package com.tokopedia.epharmacy.ui.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
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
    private var checkoutId = ""
    private var consultationWebLink = ""
    companion object {
        fun newInstance(
            enableImageURL: String,
            checkoutId: String,
            consultationWebLink : String
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
                    putString(EXTRA_CHECKOUT_ID_STRING, checkoutId)
                    putString(EXTRA_CONSULTATION_WEB_LINK_STRING, consultationWebLink)
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
        checkoutId = arguments?.getString(EXTRA_CHECKOUT_ID_STRING) ?: ""
        consultationWebLink = arguments?.getString(EXTRA_CONSULTATION_WEB_LINK_STRING) ?: ""
    }

    private fun setupBottomSheetUiData() {
        binding?.run {
            context?.resources?.let { res ->
                chooserUpload.headingTitle.text = res.getString(com.tokopedia.epharmacy.R.string.epharmacy_upload_resep_dokter_chooser_title)
                chooserUpload.paraSubtitle.text = res.getString(com.tokopedia.epharmacy.R.string.epharmacy_upload_resep_dokter_chooser_subtitle)
                chooserMiniConsultation.headingTitle.text = res.getString(com.tokopedia.epharmacy.R.string.epharmacy_mini_consult_chooser_title)
                chooserMiniConsultation.paraSubtitle.text = res.getString(com.tokopedia.epharmacy.R.string.eepharmacy_mini_consult_chooser_subtitle)
                chooserUpload.stepIcon.loadImage(UPLOAD_CHOOSER_IMAGE_URL)
                chooserMiniConsultation.stepIcon.loadImage(MINI_CONS_CHOOSER_IMAGE_URL)
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
            }
        }
    }

    private fun closeBottomSheet() {
        dismiss()
        activity?.finish()
    }

    private fun uploadResepAction() {
        RouteManager.getIntent(activity, EPHARMACY_APPLINK).apply {
            putExtra(EXTRA_CHECKOUT_ID_STRING, checkoutId)
        }.also {
            startActivityForResult(it, EPHARMACY_UPLOAD_REQUEST_CODE)
        }
    }

    private fun miniConsultationAction() {
        if(consultationWebLink.isNotBlank()){
            activity?.let { safeContext ->
                startActivityForResult(RouteManager.getIntent(context,
                    "tokopedia://webview?url=https://accounts-staging.tokopedia.com/oauth/sandbox/in"),
                    EPHARMACY_MINI_CONSULTATION_REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            EPHARMACY_UPLOAD_REQUEST_CODE -> {
                Intent().apply {
                    putStringArrayListExtra(EPHARMACY_PRESCRIPTION_IDS, arrayListOf<String?>().apply {
                        add("1")
                        add("2")
                        add("3")
                    })
                    putExtra(EPHARMACY_GROUP_ID,checkoutId)
                    activity?.setResult(EPHARMACY_UPLOAD_REQUEST_CODE,this)
                    activity?.finish()
                }
            }

            EPHARMACY_MINI_CONSULTATION_REQUEST_CODE -> {
                activity?.setResult(EPHARMACY_MINI_CONSULTATION_REQUEST_CODE,null)
            }
        }
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
