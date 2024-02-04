package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.visitable.CouponWidgetDataModel

class CouponWidgetViewHolder(view: View) : AbstractViewHolder<CouponWidgetDataModel>(view) {

    override fun bind(element: CouponWidgetDataModel?) {

    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_coupon_widget
    }
}
