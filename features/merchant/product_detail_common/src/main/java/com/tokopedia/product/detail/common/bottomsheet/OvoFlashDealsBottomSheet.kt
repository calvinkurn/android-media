package com.tokopedia.product.detail.common.bottomsheet

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.tokopedia.applink.RouteManager
import com.tokopedia.atc_common.domain.model.response.OvoValidationDataModel
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.product.detail.common.ProductTrackingCommon
import com.tokopedia.product.detail.common.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton

class OvoFlashDealsBottomSheet(val productId: String,
                               val userId: String,
                               private val ovoValidationDataModel: OvoValidationDataModel)
    : BottomSheetUnify() {

    private lateinit var childView: View
    private lateinit var titleBottomSheet: TextView
    private lateinit var descBottomSheet: TextView
    private lateinit var priceText: TextView
    private lateinit var ongkirText: TextView
    private lateinit var ovoCashAndPointText: TextView
    private lateinit var totalNeedTopup: TextView
    private lateinit var btnTopupInstant: UnifyButton
    private lateinit var btnTopupOtherMethod: UnifyButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBottomSheet()
        renderContent()
    }

    private fun initBottomSheet() {
        showCloseIcon = true
        setTitle("")
        setCloseClickListener {
            dismiss()
        }

        childView = View.inflate(requireContext(), R.layout.bottom_sheet_ovo_flash_deals, null)
        titleBottomSheet = childView.findViewById(R.id.title_ovo_bottom_sheet)
        descBottomSheet = childView.findViewById(R.id.desc_ovo_bottom_sheet)
        priceText = childView.findViewById(R.id.product_price)
        ongkirText = childView.findViewById(R.id.product_ongkir)
        ovoCashAndPointText = childView.findViewById(R.id.ovo_cash_and_point)
        totalNeedTopup = childView.findViewById(R.id.ovo_need_topup)
        btnTopupInstant = childView.findViewById(R.id.btn_topup_instant)
        btnTopupOtherMethod = childView.findViewById(R.id.btn_topup_other_method)
        setChild(childView)
    }

    private fun renderContent() {
        val ovoInsufficientBalanceModel = ovoValidationDataModel.ovoInsufficientBalance
        titleBottomSheet.text = ovoInsufficientBalanceModel.title
        descBottomSheet.text = ovoInsufficientBalanceModel.description
        priceText.text = ovoInsufficientBalanceModel.details.productPrice.getCurrencyFormatted()
        ongkirText.text = ovoInsufficientBalanceModel.details.shippingEstimation.getCurrencyFormatted()
        ovoCashAndPointText.text = "- ${ovoInsufficientBalanceModel.details.ovoBalance.getCurrencyFormatted()}"
        totalNeedTopup.text = ovoInsufficientBalanceModel.details.topupBalance.getCurrencyFormatted()

        btnTopupInstant.text = ovoInsufficientBalanceModel.buttons.topupButton.text
        btnTopupInstant.visibility = if (ovoInsufficientBalanceModel.buttons.topupButton.enable) View.VISIBLE else View.GONE
        btnTopupInstant.setOnClickListener {
            activity?.let {
                ProductTrackingCommon.eventTopupBottomSheetOvo(
                        ovoInsufficientBalanceModel.title,
                        ovoInsufficientBalanceModel.buttons.topupButton.text,
                        productId,
                        userId)
                RouteManager.route(it, ovoInsufficientBalanceModel.buttons.topupButton.applink)
                dismiss()
            }
        }

        btnTopupOtherMethod.text = ovoInsufficientBalanceModel.buttons.otherMethodButton.text
        btnTopupOtherMethod.visibility = if (ovoInsufficientBalanceModel.buttons.otherMethodButton.enable) View.VISIBLE else View.GONE
        btnTopupOtherMethod.setOnClickListener {
            activity?.let {
                ProductTrackingCommon.eventTopupBottomSheetOvo(
                        ovoInsufficientBalanceModel.title,
                        ovoInsufficientBalanceModel.buttons.otherMethodButton.text,
                        productId,
                        userId)
                RouteManager.route(it, ovoInsufficientBalanceModel.buttons.otherMethodButton.applink)
                dismiss()
            }
        }
        ProductTrackingCommon.eventSeeBottomSheetOvo(ovoInsufficientBalanceModel.title, productId, userId)
    }
}