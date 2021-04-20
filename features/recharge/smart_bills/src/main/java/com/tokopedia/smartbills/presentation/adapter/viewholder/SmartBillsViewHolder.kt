package com.tokopedia.smartbills.presentation.adapter.viewholder

import android.content.Context
import android.view.View
import android.widget.CompoundButton
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.data.RechargeBills
import com.tokopedia.smartbills.data.SmartBillsItemDetail
import com.tokopedia.smartbills.presentation.fragment.SmartBillsFragment.Companion.ACTION_TYPE
import com.tokopedia.smartbills.presentation.fragment.SmartBillsFragment.Companion.PAID_TYPE
import com.tokopedia.smartbills.presentation.widget.SmartBillsItemDetailBottomSheet
import com.tokopedia.smartbills.util.RechargeSmartBillsAccordionView.disableView
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.view_smart_bills_item.view.*


/**
 * @author by resakemal on 17/05/20
 */

class SmartBillsViewHolder(val view: View,
                           checkableListener: CheckableInteractionListener,
                           private val detailListener: DetailListener,
                           private val accordionType: Int = 0
) : BaseCheckableViewHolder<RechargeBills>(view, checkableListener) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_smart_bills_item
    }

    override fun bind(element: RechargeBills) {
        super.bind(element)
        with(view) {

            if(accordionType == ACTION_TYPE){
                //showing overlay white
                smart_bills_view_disable.show()

                //disabling view to cannot clicked
                disableView()
                tv_smart_bills_item_detail.gone()
                cb_smart_bills_item.disableView()
                tv_smart_bills_item_title.disableView()
                tv_smart_bills_item_description_bill_name.disableView()
                tv_smart_bills_item_description_number.disableView()
                tv_smart_bills_item_price.disableView()
                tv_due_message.disableView()
                tv_due_date_label.disableView()
                tv_smart_bills_item_detail.disableView()
                cb_smart_bills_item.gone()
                cb_smart_bills_item_accordion.show()
                cb_smart_bills_item_accordion.disableView()
            } else if(accordionType == PAID_TYPE){
                //remove checkbox in paid type
                cb_smart_bills_item.gone()
            }


            val title = when {
                (element.categoryName.isNotEmpty() && element.productName.isNotEmpty()) -> String.format("%s - %s", element.categoryName, element.productName)
                (element.categoryName.isNullOrEmpty() && element.productName.isNotEmpty()) -> element.productName
                (element.categoryName.isNotEmpty() && element.productName.isNullOrEmpty()) -> element.categoryName
                else -> ""
            }

            tv_smart_bills_item_title.apply {
                if (title.isNotEmpty()) text = title
                else gone()
            }

            val description = when{
                element.billName.isNotEmpty() && element.flag -> String.format(getString(R.string.smart_bills_item_description), element.clientNumber)
                element.clientNumber.isNotEmpty() && !element.flag -> String.format(getString(R.string.smart_bills_item_description), element.operatorName)
                element.flag -> element.clientNumber
                !element.flag -> element.operatorName
                else -> ""
            }

            val titleDesc = when{
                element.billName.isNotEmpty() && element.flag -> element.billName
                element.clientNumber.isNotEmpty() && !element.flag -> element.clientNumber
                else -> ""
            }

            tv_smart_bills_item_description_bill_name.apply {
                if (titleDesc.isNotEmpty()){
                    this.show()
                    text = titleDesc
                } else this.gone()
            }

            tv_smart_bills_item_description_number.apply {
                if (description.isNotEmpty()) {
                    this.show()
                    text = description
                } else this.gone()
            }

            tv_smart_bills_item_price.text = if(accordionType != ACTION_TYPE)
                element.amountText else getString(R.string.smart_bills_clustering_price)

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

            if (!element.dueMessage.text.isNullOrEmpty() && element.dueMessage.type != 0) {
                tv_due_message.apply {
                    show()
                    text = element.dueMessage.text
                    setTextColor(getDueUrgencyColor(element.dueMessage.type, context))
                }
            } else {
                tv_due_message.gone()
            }

            if (!element.dueDateLabel.text.isNullOrEmpty() && element.dueDateLabel.type != 0) {
                tv_due_date_label.apply {
                    show()
                    text = element.dueDateLabel.text
                    setTextColor(getDueUrgencyColor(element.dueDateLabel.type, context))
                    setWeight(Typography.BOLD)
                }

                iv_urgency_icon.apply {
                    show()
                    setImageResource(getDueUrgencyIcon(element.dueDateLabel.type))
                }
            } else {
                tv_due_date_label.gone()
                iv_urgency_icon.gone()
            }
        }
    }

    override fun getCheckable(): CompoundButton {
        return view.cb_smart_bills_item
    }

    interface DetailListener {
        fun onShowBillDetail(bill: RechargeBills, bottomSheet: SmartBillsItemDetailBottomSheet)
    }

    private fun getDueUrgencyColor(type: Int, context: Context): Int {
        return when (type) {
            1 -> ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
            2 -> ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Y400)
            3 -> ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_R500)
            4 -> ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_T600)
            else -> ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
        }
    }

    private fun getDueUrgencyIcon(type: Int): Int {
        return when (type) {
            1 -> R.drawable.ic_countdown_black
            2 -> R.drawable.ic_countdown_yellow
            3 -> R.drawable.ic_countdown_red
            else -> R.drawable.ic_countdown_black
        }
    }

}