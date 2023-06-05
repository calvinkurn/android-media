package com.tokopedia.recharge_component.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.recharge_component.databinding.ViewRechargeCheckBalanceDetailBinding
import com.tokopedia.recharge_component.model.check_balance.RechargeCheckBalanceDetailModel

class RechargeCheckBalanceDetailViewHolder(
    private val binding: ViewRechargeCheckBalanceDetailBinding,
    private var mListener: RechargeCheckBalanceDetailViewHolderListener?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: RechargeCheckBalanceDetailModel) {
        with(binding) {
            checkBalanceDetailTitle.text = model.title
            checkBalanceDetailSubtitle.text = model.subtitle
            checkBalanceDetailSubtitle.setTextColor(model.subtitleColor.toIntOrZero())

            if (model.applink.isNotEmpty() && model.buttonText.isNotEmpty()) {
                checkBalanceDetailBuyButton.text = model.buttonText
                checkBalanceDetailBuyButton.setOnClickListener {
                    mListener?.onClickCheckBalanceDetailBuyButton(model, absoluteAdapterPosition, "")
                    RouteManager.route(itemView.context, model.applink)
                }
                checkBalanceDetailBuyButton.show()
            } else {
                checkBalanceDetailBuyButton.hide()
            }
            mListener?.onRenderCheckBalanceDetailBuyButton(model, absoluteAdapterPosition, "")
        }
    }

    interface RechargeCheckBalanceDetailViewHolderListener {
        fun onClickCheckBalanceDetailBuyButton(
            model: RechargeCheckBalanceDetailModel,
            position: Int,
            bottomSheetTitle: String
        )
        fun onRenderCheckBalanceDetailBuyButton(
            model: RechargeCheckBalanceDetailModel,
            position: Int,
            bottomSheetTitle: String
        )
    }
}
