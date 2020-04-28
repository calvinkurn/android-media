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
import kotlinx.android.synthetic.main.view_smart_bills_item.view.*

class SmartBillsViewHolder(val view: View, listener: CheckableInteractionListener):
        BaseCheckableViewHolder<RechargeBills>(view, listener) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_smart_bills_item
    }

    override fun bind(element: RechargeBills) {
        super.bind(element)
        with (view) {
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

            if (element.errorMessage.isNotEmpty()) {
                tv_smart_bills_item_error.show()
                tv_smart_bills_item_error.text = element.errorMessage
            } else {
                tv_smart_bills_item_error.hide()
            }
        }
    }

    override fun getCheckable(): CompoundButton {
        return view.cb_smart_bills_item
    }

}