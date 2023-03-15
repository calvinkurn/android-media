package com.tokopedia.tokochat_common.view.customview.bottomsheet

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokochat_common.databinding.TokochatConsentBottomsheetBinding
import com.tokopedia.tokochat_common.util.TokoChatUrlUtil.IMAGE_TOKOCHAT_CONSENT
import com.tokopedia.tokochat_common.util.TokoChatValueUtil
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class TokoChatConsentBottomSheet(
    private val dismissAction: (() -> Unit)
) : BottomSheetUnify() {

    private var binding by autoClearedNullable<TokochatConsentBottomsheetBinding>()
    private var needConsent = true

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
            container, false)

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
        setListener()
    }

    private fun setMainImage() {
        binding?.tokochatImageConsent?.loadImage(IMAGE_TOKOCHAT_CONSENT)
    }

    private fun loadConsentWidget() {
        binding?.tokochatWidgetConsent?.load(
            viewLifecycleOwner, this, TokoChatValueUtil.consentParam
        )
    }

    private fun setListener() {
        binding?.tokochatBtnConsent?.setOnClickListener {
            binding?.tokochatWidgetConsent?.submitConsent()
        }
        binding?.tokochatWidgetConsent?.setOnNeedConsentListener { needConsent ->
            this.needConsent = needConsent
            if (!needConsent) {
                this.dismiss()
            }
        }
        setOnDismissListener {
            if (needConsent) {
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
    }

    companion object {
        private val TAG = TokoChatConsentBottomSheet::class.simpleName
        private const val DELAY = 200L

        fun show(
            fragmentManager: FragmentManager,
            dismissAction: () -> Unit = {},
        ) {
            TokoChatConsentBottomSheet(dismissAction).show(fragmentManager, TAG)
        }
    }
}

