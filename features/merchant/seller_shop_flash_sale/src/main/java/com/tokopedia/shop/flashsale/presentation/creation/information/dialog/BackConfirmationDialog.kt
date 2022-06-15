package com.tokopedia.shop.flashsale.presentation.creation.information.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsDialogBackConfirmationBinding
import com.tokopedia.shop.flashsale.common.customcomponent.ModalBottomSheet
import com.tokopedia.utils.lifecycle.autoClearedNullable

class BackConfirmationDialog : ModalBottomSheet() {

    init {
        overlayClickDismiss = true
        modalWidthRatio = CUSTOM_MODAL_WIDTH_RATIO
        modalMarginPercentage = MODAL_MARGIN_PERCENTAGE
        showCloseIcon = false
        setCloseClickListener { dismiss() }
    }

    companion object {
        private const val MODAL_MARGIN_PERCENTAGE = 0.3f
        private const val CUSTOM_MODAL_WIDTH_RATIO = 0.75
    }

    private var onPrimaryActionClick: () -> Unit = {}
    private var onSecondaryActionClick: () -> Unit = {}
    private var onThirdActionClick: () -> Unit = {}

    private var binding by autoClearedNullable<SsfsDialogBackConfirmationBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsfsDialogBackConfirmationBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        binding?.run {
            btnYes.setOnClickListener {
                onPrimaryActionClick()
                dismiss()
            }
            btnNo.setOnClickListener {
                onSecondaryActionClick()
                dismiss()
            }
            btnSaveAsDraft.setOnClickListener {
                onThirdActionClick()
                dismiss()
            }
        }
    }

    fun setOnPrimaryActionClick(onPrimaryActionClick: () -> Unit) {
        this.onPrimaryActionClick = onPrimaryActionClick
    }

    fun setOnSecondaryActionClick(onSecondaryActionClick: () -> Unit) {
        this.onSecondaryActionClick = onSecondaryActionClick
    }

    fun setOnThirdActionClick(onThirdActionClick: () -> Unit) {
        this.onThirdActionClick = onThirdActionClick
    }
}