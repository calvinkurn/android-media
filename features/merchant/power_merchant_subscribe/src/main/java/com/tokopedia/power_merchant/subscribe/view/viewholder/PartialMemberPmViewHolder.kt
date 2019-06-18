package com.tokopedia.power_merchant.subscribe.view.viewholder

import android.view.View
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.partial_member_power_merchant.view.*

class PartialMemberPmViewHolder private constructor(private val view: View,
                                                    private val clickListener: View.OnClickListener):
        View.OnClickListener by clickListener  {

    companion object {
        fun build(_view: View, _clickListener: View.OnClickListener) = PartialMemberPmViewHolder(_view, _clickListener)
    }

    fun renderPartialMember(shopStatusModel: ShopStatusModel, isAutoExtend: Boolean) {
        if (shopStatusModel.isPowerMerchantActive() or shopStatusModel.isPowerMerchantPending() or shopStatusModel.isPowerMerchantIdle()) {
            showCancellationButton(isAutoExtend)
        } else if (shopStatusModel.isPowerMerchantInactive()){
            view.hide()
        }
        view.member_cancellation_button.setOnClickListener(this@PartialMemberPmViewHolder)

    }

    private fun showCancellationButton(isAutoExtend: Boolean){
        if (isAutoExtend) {
            view.member_cancellation_button.visibility = View.VISIBLE
            view.hide()
        } else {
            view.show()
            view.member_cancellation_button.visibility = View.GONE

        }
    }
}