package com.tokopedia.shop.flashsale.presentation.creation.rule.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsDialogCreateCampaignConfirmationBinding
import com.tokopedia.shop.flashsale.common.customcomponent.ModalBottomSheet
import com.tokopedia.shop.flashsale.common.extension.setFragmentToUnifyBgColor
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CreateCampaignConfirmationDialog : ModalBottomSheet() {
    companion object {
        const val TAG = "CreateCampaignConfirmationDialog"
        const val CUSTOM_MODAL_WIDTH_RATIO = 0.9
        private const val CAMPAIGN_CONFIRMATION_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/flash-sale-toko/ic_create_campaign_confirmation.png"
    }

    private var binding by autoClearedNullable<SsfsDialogCreateCampaignConfirmationBinding>()

    private var listener: CreateCampaignConfirmationListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpDialogProperties()
        binding = SsfsDialogCreateCampaignConfirmationBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setUpDialogProperties() {
        showCloseIcon = false
        overlayClickDismiss = true
        modalWidthRatio = CUSTOM_MODAL_WIDTH_RATIO
        setCloseClickListener {
            dismiss()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentToUnifyBgColor()
        initContent()
    }

    override fun onDestroyView() {
        listener = null
        super.onDestroyView()
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this, TAG)
        }
    }

    private fun initContent() {
        val binding = binding ?: return
        binding.imgIllustration.setImageUrl(CAMPAIGN_CONFIRMATION_IMAGE_URL)
        binding.btnPrimaryCta.setOnClickListener {
            listener?.onCreateCampaignButtonClicked()
            dismiss()
        }
        binding.btnSecondaryCta.setOnClickListener {
            dismiss()
        }
    }

    fun setCreateCampaignConfirmationListener(listener: CreateCampaignConfirmationListener) {
        this.listener = listener
    }

    interface CreateCampaignConfirmationListener {
        fun onCreateCampaignButtonClicked()
    }
}
