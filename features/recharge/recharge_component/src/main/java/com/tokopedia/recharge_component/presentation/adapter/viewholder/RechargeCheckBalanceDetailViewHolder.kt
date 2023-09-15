package com.tokopedia.recharge_component.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
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
            val color = when (model.subtitleColor) {
                CHECK_BALANCE_WARNING -> com.tokopedia.unifyprinciples.R.color.Unify_YN500
                CHECK_BALANCE_CRITICAL -> com.tokopedia.unifyprinciples.R.color.Unify_RN500
                CHECK_BALANCE_INFORMATION -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
                else -> com.tokopedia.unifyprinciples.R.color.Unify_NN600
            }
            checkBalanceDetailSubtitle.setTextColor(MethodChecker.getColor(itemView.context, color))

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

    companion object {
        private const val CHECK_BALANCE_INFORMATION = "information"
        private const val CHECK_BALANCE_CRITICAL = "critical"
        private const val CHECK_BALANCE_WARNING = "warning"
    }
}
