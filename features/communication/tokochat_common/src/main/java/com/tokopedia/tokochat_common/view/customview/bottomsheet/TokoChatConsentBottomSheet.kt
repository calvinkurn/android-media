package com.tokopedia.tokochat_common.view.customview.bottomsheet

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokochat_common.databinding.TokochatConsentBottomsheetBinding
import com.tokopedia.tokochat_common.util.TokoChatUrlUtil.IMAGE_TOKOCHAT_CONSENT
import com.tokopedia.tokochat_common.util.TokoChatValueUtil
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import timber.log.Timber

class TokoChatConsentBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<TokochatConsentBottomsheetBinding>()
    private var needConsent = true

    private var submitAction: (() -> Unit) = {}
    private var dismissAction: (() -> Unit) = {}

    init {
        setupBottomSheetVariant(VariantType.CLOSE)
        this.clearContentPadding = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            com.tokopedia.tokochat_common.R.layout.tokochat_consent_bottomsheet,
            container,
            false
        )

        binding = TokochatConsentBottomsheetBinding.bind(view)
        setChild(view)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setMainImage()
        setMainButton(false) // False when first show up
        setListener()
    }

    private fun setMainImage() {
        binding?.tokochatImageConsent?.loadImage(IMAGE_TOKOCHAT_CONSENT)
    }

    private fun setMainButton(isEnabled: Boolean) {
        binding?.tokochatBtnConsent?.isEnabled = isEnabled
    }

    private fun loadConsentWidget() {
        binding?.tokochatWidgetConsent?.load(
            TokoChatValueUtil.consentParam
        )
    }

    private fun setListener() {
        binding?.tokochatBtnConsent?.setOnClickListener {
            binding?.tokochatWidgetConsent?.submitConsent()
        }
        binding?.tokochatWidgetConsent?.setOnDetailConsentListener { needConsent, consentType ->
            this.needConsent = needConsent
            if (!this.needConsent) { // if no need for consent, load tokochat
                dismissAndLoadTokoChatData()
            }
        }
        setOnDismissListener {
            if (needConsent) { // finish page if need consent but bottomsheet dismissed
                dismissAction()
            }
        }
        setCloseClickListener {
            dismissAction()
        }
        setShowListener {
            Handler(Looper.getMainLooper()).postDelayed({
                loadConsentWidget()
            }, DELAY)
        }
        binding?.tokochatWidgetConsent?.setOnFailedGetCollectionListener {
            this.dismiss()
        }
        binding?.tokochatWidgetConsent?.setSubmitResultListener(
            onSuccess = {
                dismissAndLoadTokoChatData()
            },
            onError = {
                dismissAndLoadTokoChatData()
                Timber.d(it)
            },
            onLoading = {}
        )
        binding?.tokochatWidgetConsent?.setOnCheckedChangeListener {
            setMainButton(it) // Change button enable based on checkbox
        }
    }

    private fun dismissAndLoadTokoChatData() {
        needConsent = false
        this.dismiss()
        submitAction()
    }

    fun setConsentListener(
        submitAction: (() -> Unit),
        dismissAction: (() -> Unit)
    ) {
        this.submitAction = submitAction
        this.dismissAction = dismissAction
    }

    companion object {
        val TAG = TokoChatConsentBottomSheet::class.simpleName
        private const val DELAY = 200L
    }
}
