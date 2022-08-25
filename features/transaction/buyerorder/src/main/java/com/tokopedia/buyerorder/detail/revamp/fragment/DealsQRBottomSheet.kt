package com.tokopedia.buyerorder.detail.revamp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.databinding.DealsQrCodeLayoutBinding
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * created by @bayazidnasir on 25/8/2022
 */

class DealsQRBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<DealsQrCodeLayoutBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DealsQrCodeLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setChild(binding?.root)
        setTitle(getString(R.string.text_redeem_voucher))

        setCloseClickListener {
            dismiss()
        }
    }

    fun renderView(actionButton: ActionButton){
        binding?.let {
            if (actionButton.headerObject.statusLabel.isNotEmpty()){
                it.redeemDialogExpiredView.visible()
                it.redeemDialogExpiredText.visible()
                it.redeemDialogExpiredText.text = actionButton.headerObject.statusLabel
            }

            it.qrCode.loadImage(actionButton.body.appURL){
                setPlaceHolder(com.tokopedia.unifyprinciples.R.color.Unify_N50)
                setErrorDrawable(com.tokopedia.unifyprinciples.R.color.Unify_N50)
            }

            it.redeemDialogShopName.text = actionButton.headerObject.poweredBy
            it.redeemDialogVoucherCode.text = actionButton.headerObject.voucherCodes

            if (actionButton.headerObject.poweredBy.isEmpty()){
                it.redeemDialogShopName.gone()
                it.redeemDialogPoweredBy.gone()
            }
        }
    }

    fun show(fragmentManager: FragmentManager){
        show(fragmentManager, TAG)
    }

    private companion object{
        const val TAG = "deals_qr_code_bottom_sheet"
    }

}