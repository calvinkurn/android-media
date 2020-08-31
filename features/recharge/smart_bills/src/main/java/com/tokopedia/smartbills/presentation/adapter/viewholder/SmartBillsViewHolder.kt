package com.tokopedia.smartbills.presentation.adapter.viewholder

import android.view.View
import android.widget.CompoundButton
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.data.RechargeBills
import com.tokopedia.smartbills.data.SmartBillsItemDetail
import com.tokopedia.smartbills.presentation.widget.SmartBillsItemDetailBottomSheet
import kotlinx.android.synthetic.main.view_smart_bills_item.view.*

/**
 * @author by resakemal on 17/05/20
 */

class SmartBillsViewHolder(val view: View,
                           checkableListener: CheckableInteractionListener,
                           val detailListener: DetailListener) :
        BaseCheckableViewHolder<RechargeBills>(view, checkableListener) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_smart_bills_item
    }

    override fun bind(element: RechargeBills) {
        super.bind(element)
        with(view) {
            tv_smart_bills_item_title.text = element.productName
            val description = if (element.operatorName.isNotEmpty()) {
                String.format(getString(R.string.smart_bills_item_description),
                        element.clientNumber, element.operatorName)
            } else {
                element.clientNumber
            }
            tv_smart_bills_item_description.text = description
            tv_smart_bills_item_price.text = element.amountText
            ImageHandler.LoadImage(iv_smart_bills_item_icon, element.iconURL)

            setOnClickListener {
                toggle()
            }
            cb_smart_bills_item.isEnabled = !element.disabled

            tv_smart_bills_item_detail.setOnClickListener {
                val details = mutableListOf<SmartBillsItemDetail>()
                if (element.clientNumber.isNotEmpty()) {
                    details.add(SmartBillsItemDetail(getString(R.string.smart_bills_item_detail_label_1),
                            element.clientNumber))
                }
                if (element.billName.isNotEmpty()) {
                    details.add(SmartBillsItemDetail(getString(R.string.smart_bills_item_detail_label_2),
                            element.billName))
                }
                if (element.amountText.isNotEmpty()) {
                    details.add(SmartBillsItemDetail(getString(R.string.smart_bills_item_detail_label_3),
                            element.amountText))
                }

                val billDetailBottomSheet =
                        SmartBillsItemDetailBottomSheet.newInstance(context, element.categoryName, details)
                detailListener.onShowBillDetail(element, billDetailBottomSheet)
            }

            ticker_smart_bills_item_error.show()
            if (element.disabled) {
                smart_bills_item_disabled_overlay.show()
                ticker_smart_bills_item_error.setTextDescription(element.disabledText)
            } else {
                smart_bills_item_disabled_overlay.hide()
                if (element.errorMessage.isNotEmpty()) {
                    ticker_smart_bills_item_error.setTextDescription(element.errorMessage)
                } else {
                    ticker_smart_bills_item_error.hide()
                }
            }
        }
    }

    override fun getCheckable(): CompoundButton {
        return view.cb_smart_bills_item
    }

    interface DetailListener {
        fun onShowBillDetail(bill: RechargeBills, bottomSheet: SmartBillsItemDetailBottomSheet)
    }

}