package com.tokopedia.buyerorder.detail.revamp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.databinding.DealsQrCodeLayoutBinding
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.R as UnifyPrinciplesR
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * created by @bayazidnasir on 6/9/2022
 */

class QrDealsBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<DealsQrCodeLayoutBinding>()
    private var actionButton: ActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {arguments ->
            actionButton = arguments.getParcelable(ACTION_BUTTON_EXTRA)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DealsQrCodeLayoutBinding.inflate(LayoutInflater.from(context))
        setTitle(getString(R.string.text_redeem_voucher))
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionButton?.let { actionButton ->
            setData(actionButton)
        }
    }

    private fun setData(actionButton: ActionButton) {
        binding?.let {
            if (actionButton.headerObject.statusLabel.isNotEmpty()){
                it.redeemDialogExpiredView.visible()
                it.redeemDialogExpiredText.visible()
                it.redeemDialogExpiredText.text = actionButton.headerObject.statusLabel
            }

            it.qrCode.loadImage(actionButton.body.appURL){
                setPlaceHolder(UnifyPrinciplesR.color.Unify_N50)
                setErrorDrawable(UnifyPrinciplesR.color.Unify_N50)
            }

            it.redeemDialogShopName.text = actionButton.headerObject.poweredBy
            it.redeemDialogVoucherCode.text = actionButton.headerObject.voucherCodes

            if (actionButton.headerObject.poweredBy.isEmpty()){
                it.redeemDialogShopName.gone()
                it.redeemDialogPoweredBy.gone()
            }
        }
    }

    companion object{

        private const val ACTION_BUTTON_EXTRA = "ACTION_BUTTON_EXTRA"

        fun newInstance(actionButton: ActionButton):  QrDealsBottomSheet {
            val bottomSheet = QrDealsBottomSheet()
            val bundle = Bundle()
            bundle.putParcelable(ACTION_BUTTON_EXTRA, actionButton)
            bottomSheet.arguments = bundle
            return bottomSheet
        }
    }
}
