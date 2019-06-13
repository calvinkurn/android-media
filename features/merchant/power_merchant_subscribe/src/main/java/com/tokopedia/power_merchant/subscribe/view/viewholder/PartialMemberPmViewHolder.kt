package com.tokopedia.power_merchant.subscribe.view.viewholder

import android.app.Activity
import android.view.View
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel

class PartialMemberPmViewHolder private constructor(private val view: View,
                                                    private val activity: Activity? = null) {

    companion object {
        fun build(_view: View, _activity: Activity?) = PartialMemberPmViewHolder(_view, _activity)
    }


    fun renderPartialMember(shopStatusModel: ShopStatusModel) {

    }

}