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
    companion object {
        fun newInstance( enableImageURL: String, checkoutId : String
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
            inflater, container, false
        )
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
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
    }

    private fun setupBottomSheetUiData() {
        binding?.run {
            context?.resources?.let { res ->
                chooserUpload.headingTitle.text = res.getString(com.tokopedia.epharmacy.R.string.epharmacy_upload_resep_dokter_chooser_title)
                chooserUpload.paraSubtitle.text = res.getString(com.tokopedia.epharmacy.R.string.epharmacy_upload_resep_dokter_chooser_subtitle)
                chooserMiniConsultation.headingTitle.text = res.getString(com.tokopedia.epharmacy.R.string.epharmacy_mini_consult_chooser_title)
                chooserMiniConsultation.paraSubtitle.text = res.getString(com.tokopedia.epharmacy.R.string.eepharmacy_mini_consult_chooser_subtitle)
                chooserUpload.stepIcon.let {
                    it.loadImage(UPLOAD_CHOOSER_IMAGE_URL)
                }
                chooserMiniConsultation.stepIcon.let {
                    it.loadImage(MINI_CONS_CHOOSER_IMAGE_URL)
                }
                if(enableImageURL.isNotBlank()){
                    bottomImageLogo.show()
                    bottomImageLogo.loadImage(enableImageURL)
                }else {
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

    private fun closeBottomSheet(){
        dismiss()
        activity?.finish()
    }

    private fun uploadResepAction(){
        RouteManager.getIntent(activity,EPHARMACY_APPLINK).apply {
            putExtra(EXTRA_CHECKOUT_ID_STRING,checkoutId)
        }.also {
            startActivityForResult(it, EPHARMACY_REQUEST_CODE)
        }
    }

    private fun miniConsultationAction(){
        RouteManager.getIntent(activity,EPHARMACY_ATTACH_PRESCRIPTION_APPLINK).apply {
            putExtra(EXTRA_CHECKOUT_ID_STRING,checkoutId)
        }.also {
            startActivityForResult(it, EPHARMACY_MINI_CONSULTATION__CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            EPHARMACY_REQUEST_CODE -> {

            }
            EPHARMACY_MINI_CONSULTATION__CODE -> {

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
