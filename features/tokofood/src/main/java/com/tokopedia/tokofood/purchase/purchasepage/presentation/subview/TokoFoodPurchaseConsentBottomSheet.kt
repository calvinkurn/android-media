package com.tokopedia.tokofood.purchase.purchasepage.presentation.subview

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.tokofood.databinding.LayoutBottomSheetPurchaseConsentBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

class TokoFoodPurchaseConsentBottomSheet(val listener: Listener?) : BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(title: String,
                           desc: String,
                           tnc: String,
                           imageUrl: String,
                           listener: Listener): TokoFoodPurchaseConsentBottomSheet {
            return TokoFoodPurchaseConsentBottomSheet(listener).apply {
                arguments = Bundle().apply {
                    putString(PARAM_TITLE, title)
                    putString(PARAM_DESC, desc)
                    putString(PARAM_TNC, tnc)
                    putString(PARAM_IMAGE_URL, imageUrl)
                }
            }
        }

        private const val PARAM_TITLE = "title"
        private const val PARAM_DESC = "desc"
        private const val PARAM_TNC = "tnc"
        private const val PARAM_IMAGE_URL = "image_url"

        private const val TAG = "TokoFoodPurchaseConsentBottomSheet"
    }

    private val title by lazy {
        arguments?.getString(PARAM_TITLE).orEmpty()
    }
    private val desc by lazy {
        arguments?.getString(PARAM_DESC).orEmpty()
    }
    private val imageUrl by lazy {
        arguments?.getString(PARAM_IMAGE_URL).orEmpty()
    }
    private val tnc by lazy {
        arguments?.getString(PARAM_TNC).orEmpty()
    }

    private var viewBinding: LayoutBottomSheetPurchaseConsentBinding? = null

    interface Listener {
        fun onSuccessAgreeConsent()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initializeView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.let {
            renderBottomSheet(it)
        }
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun initializeView(): LayoutBottomSheetPurchaseConsentBinding {
        val viewBinding = LayoutBottomSheetPurchaseConsentBinding.inflate(LayoutInflater.from(context))
        this.viewBinding = viewBinding
        initializeBottomSheet(viewBinding)
        return viewBinding
    }

    private fun initializeBottomSheet(viewBinding: LayoutBottomSheetPurchaseConsentBinding) {
        showCloseIcon = true
        isDragable = true
        isHideable = true
        clearContentPadding = true
        customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
        setChild(viewBinding.root)
    }

    private fun renderBottomSheet(viewBinding: LayoutBottomSheetPurchaseConsentBinding) {
        with(viewBinding) {
            imageConsent.setImageUrl(imageUrl)
            textConsentTitle.text = title
            textConsentDescription.text = desc
            checkboxConsentAgreement.setOnCheckedChangeListener { _, isChecked ->
                buttonContinue.isEnabled = isChecked
            }
            textConsentAgreement.run {
                text = tnc
                setOnClickListener {
                    checkboxConsentAgreement.isChecked = !checkboxConsentAgreement.isChecked
                }
            }
            checkboxConsentAgreement.isChecked = false
            buttonContinue.run {
                isEnabled = false
                setOnClickListener {
                    // TODO: Load agree consent and redirect to payment
                    listener?.onSuccessAgreeConsent()
                }
            }
        }
    }
}