package com.tokopedia.power_merchant.subscribe.view.viewholder

import android.app.Activity
import android.view.View
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.partial_member_power_merchant.view.*

class PartialMemberPmViewHolder private constructor(private val view: View,
                                                    private val activity: Activity? = null) {

    companion object {
        fun build(_view: View, _activity: Activity?) = PartialMemberPmViewHolder(_view, _activity)
    }

    fun renderPartialMember(shopStatusModel: ShopStatusModel) {
        if (shopStatusModel.powerMerchant.status == "activate" || shopStatusModel.powerMerchant.status == "inactive") {
            view.hide()
        } else {
            view.visible()
        }
    }

    fun showCancellationButton(isAutoExtend: Boolean){
        if (isAutoExtend) {
            view.member_cancellation_button.visibility = View.VISIBLE
        } else {
            view.member_cancellation_button.visibility = View.GONE

        }
    }

}