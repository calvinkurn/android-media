package com.tokopedia.recharge_component.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_component.databinding.ViewRechargeCheckBalanceUnitBinding
import com.tokopedia.recharge_component.model.check_balance.RechargeCheckBalanceUnitModel

class RechargeCheckBalanceUnitViewHolder(
   private val binding: ViewRechargeCheckBalanceUnitBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(model: RechargeCheckBalanceUnitModel) {
        with(binding) {
            checkBalanceUnitKey.text = model.key
            checkBalanceUnitValue.text = MethodChecker.fromHtml(model.value)
            checkBalanceUnitIcon.loadImage(model.iconUrl)
        }
    }
}
