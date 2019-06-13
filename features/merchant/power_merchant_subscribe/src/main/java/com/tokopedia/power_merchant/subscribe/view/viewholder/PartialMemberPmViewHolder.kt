package com.tokopedia.power_merchant.subscribe.view.viewholder

import android.app.Activity
import android.view.View
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible

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

}