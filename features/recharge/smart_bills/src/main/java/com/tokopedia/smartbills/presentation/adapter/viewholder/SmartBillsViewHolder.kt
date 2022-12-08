package com.tokopedia.smartbills.presentation.adapter.viewholder

import android.content.Context
import android.graphics.Paint
import android.view.View
import android.widget.CompoundButton
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.data.RechargeBills
import com.tokopedia.smartbills.data.SmartBillsItemDetail
import com.tokopedia.smartbills.databinding.ViewSmartBillsItemBinding
import com.tokopedia.smartbills.presentation.fragment.SmartBillsFragment.Companion.ACTION_TYPE
import com.tokopedia.smartbills.presentation.fragment.SmartBillsFragment.Companion.PAID_TYPE
import com.tokopedia.smartbills.presentation.widget.SmartBillsItemDetailBottomSheet
import com.tokopedia.smartbills.util.RechargeSmartBillsAccordionView.disableView
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

/**
 * @author by resakemal on 17/05/20
 */

class SmartBillsViewHolder(
    val view: View,
    checkableListener: CheckableInteractionListener,
    private val detailListener: DetailListener,
    private val accordionType: Int = 0
) : BaseCheckableViewHolder<RechargeBills>(view, checkableListener) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_smart_bills_item
        const val ZERO_PERCENT = 0
    }

    private val binding: ViewSmartBillsItemBinding? by viewBinding()

    override fun bind(element: RechargeBills) {
        super.bind(element)
        binding?.run {
            if (accordionType == ACTION_TYPE) {
                // showing overlay white
                smartBillsViewDisable.show()

                // disabling view to cannot clicked
                root.disableView()
                tvSmartBillsItemDetail.gone()
                cbSmartBillsItem.disableView()
                tvSmartBillsItemTitle.disableView()
                tvSmartBillsItemDescriptionBillName.disableView()
                tvSmartBillsItemDescriptionNumber.disableView()
                tvSmartBillsItemPrice.disableView()
                tvSmartBillsPercentageLabel.disableView()
                tvSmartBillsPercentageAmount.disableView()
                tvDueMessage.disableView()
                tvDueDateLabel.disableView()
                tvSmartBillsItemDetail.disableView()
                cbSmartBillsItem.gone()
                cbSmartBillsItemAccordion.show()
                cbSmartBillsItemAccordion.disableView()
            } else if (accordionType == PAID_TYPE) {
                // remove checkbox in paid type
                root.disableView()
                cbSmartBillsItem.gone()
                tvSmartBillsItemPrice.gone()
            }

            val title = when {
                (element.categoryName.isNotEmpty() && element.productName.isNotEmpty()) -> String.format("%s - %s", element.categoryName, element.productName)
                (element.categoryName.isNullOrEmpty() && element.productName.isNotEmpty()) -> element.productName
                (element.categoryName.isNotEmpty() && element.productName.isNullOrEmpty()) -> element.categoryName
                else -> ""
            }

            tvSmartBillsItemTitle.apply {
                if (title.isNotEmpty()) {
                    text = title
                } else {
                    gone()
                }
            }

            val description = when {
                element.billName.isNotEmpty() && element.flag -> String.format(getString(R.string.smart_bills_item_description), element.clientNumber)
                element.clientNumber.isNotEmpty() && !element.flag -> String.format(getString(R.string.smart_bills_item_description), element.operatorName)
                element.flag -> element.clientNumber
                !element.flag -> element.operatorName
                else -> ""
            }

            val titleDesc = when {
                element.billName.isNotEmpty() && element.flag -> element.billName
                element.clientNumber.isNotEmpty() && !element.flag -> element.clientNumber
                else -> ""
            }

            tvSmartBillsItemDescriptionBillName.apply {
                if (titleDesc.isNotEmpty()) {
                    this.show()
                    text = titleDesc
                } else {
                    this.gone()
                }
            }

            tvSmartBillsItemDescriptionNumber.apply {
                if (description.isNotEmpty()) {
                    this.show()
                    text = description
                } else {
                    this.gone()
                }
            }

            if (!element.amountText.isNullOrEmpty() && accordionType != PAID_TYPE) {
                tvSmartBillsItemPrice.show()
                tvSmartBillsItemPrice.text = if (accordionType != ACTION_TYPE) {
                    element.amountText
                } else {
                    getString(R.string.smart_bills_clustering_price)
                }
            } else {
                tvSmartBillsItemPrice.gone()
            }

            ivSmartBillsItemIcon.loadImage(element.iconURL)

            root.setOnClickListener {
                toggle()
            }
            cbSmartBillsItem.isEnabled = !element.disabled

            tvSmartBillsItemDetail.setOnClickListener {
                val details = mutableListOf<SmartBillsItemDetail>()
                if (element.clientNumber.isNotEmpty()) {
                    details.add(
                        SmartBillsItemDetail(
                            getString(R.string.smart_bills_item_detail_label_1),
                            element.clientNumber
                        )
                    )
                }
                if (element.billName.isNotEmpty()) {
                    details.add(
                        SmartBillsItemDetail(
                            getString(R.string.smart_bills_item_detail_label_2),
                            element.billName
                        )
                    )
                }
                if (element.amountText.isNotEmpty()) {
                    details.add(
                        SmartBillsItemDetail(
                            getString(R.string.smart_bills_item_detail_label_3),
                            element.amountText
                        )
                    )
                }

                val billDetailBottomSheet =
                    SmartBillsItemDetailBottomSheet.newInstance(element.categoryName, details)
                detailListener.onShowBillDetail(element, billDetailBottomSheet)
            }

            tickerSmartBillsItemError.show()
            if (element.disabled) {
                smartBillsItemDisabledOverlay.show()
                tickerSmartBillsItemError.setTextDescription(element.disabledText)
            } else {
                smartBillsItemDisabledOverlay.hide()
                if (element.errorMessage.isNotEmpty()) {
                    tickerSmartBillsItemError.setTextDescription(element.errorMessage)
                } else {
                    tickerSmartBillsItemError.hide()
                }
            }

            if (!element.dueMessage.text.isNullOrEmpty() && element.dueMessage.type != 0) {
                tvDueMessage.apply {
                    show()
                    text = element.dueMessage.text
                    setTextColor(getDueUrgencyColor(element.dueMessage.type, context))
                }
            } else {
                tvDueMessage.gone()
            }

            if (!element.dueDateLabel.text.isNullOrEmpty() && element.dueDateLabel.type != 0) {
                tvDueDateLabel.apply {
                    show()
                    text = element.dueDateLabel.text
                    setTextColor(getDueUrgencyColor(element.dueDateLabel.type, context))
                    setWeight(Typography.BOLD)
                }

                ivUrgencyIcon.apply {
                    show()
                    setImageResource(getDueUrgencyIcon(element.dueDateLabel.type))
                }
            } else {
                tvDueDateLabel.gone()
                ivUrgencyIcon.gone()
            }

            if (element.promo.percentage == ZERO_PERCENT) {
                tvSmartBillsPercentageLabel.gone()
                tvSmartBillsPercentageAmount.gone()
            } else {
                tvSmartBillsPercentageLabel.apply {
                    show()
                    text = String.format("%s%%", element.promo.percentage.toString())
                }

                tvSmartBillsPercentageAmount.apply {
                    show()
                    text = element.promo.slashAmountText
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            }

            if (element.newBillLabel.isNewLabel && element.newBillLabel.text.isNotEmpty()) {
                iconMenuSbmDelete.apply {
                    show()
                    setOnClickListener {
                        detailListener.onDeleteClicked(element)
                    }
                }
            } else {
                iconMenuSbmDelete.gone()
            }
        }
    }

    override fun getCheckable(): CompoundButton? {
        return binding?.cbSmartBillsItem
    }

    interface DetailListener {
        fun onShowBillDetail(bill: RechargeBills, bottomSheet: SmartBillsItemDetailBottomSheet)
        fun onDeleteClicked(bill: RechargeBills)
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
