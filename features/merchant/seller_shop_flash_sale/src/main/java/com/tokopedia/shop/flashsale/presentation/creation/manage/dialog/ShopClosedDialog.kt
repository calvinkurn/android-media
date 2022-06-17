package com.tokopedia.shop.flashsale.presentation.creation.manage.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsDialogShopClosedBinding
import com.tokopedia.shop.flashsale.common.customcomponent.ModalBottomSheet
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ShopClosedDialog(
    private var primaryCTAAction: (() -> Unit)? = null,
    private var secondaryCTAAction: (() -> Unit)? = null
) : ModalBottomSheet() {

    companion object {
        const val TAG = "ShopClosedDialog"
        const val CUSTOM_MODAL_WIDTH_RATIO = 0.9
        private const val SHOP_CLOSED_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/flash-sale-toko/creation_2_shop_close_illustration.png"
    }


    init {
        setCloseClickListener {
            dismiss()
        }
    }

    private var binding by autoClearedNullable<SsfsDialogShopClosedBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsfsDialogShopClosedBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initContent()
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this, TAG)
        }
    }

    private fun initContent() {
        overlayClickDismiss = true
        modalWidthRatio = CUSTOM_MODAL_WIDTH_RATIO
        showCloseIcon = false
        binding?.run {
            iuShopCloseIllustration.setImageUrl(SHOP_CLOSED_IMAGE_URL)
            btnBukaToko.setOnClickListener {
                primaryCTAAction?.invoke()
            }
            btnBatal.setOnClickListener {
                secondaryCTAAction?.invoke() ?: dismiss()
            }
        }
    }
}