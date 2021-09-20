package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.power_merchant.subscribe.R

class BenefitPackageLoaderViewHolder(view: View) : AbstractViewHolder<LoadingModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_benefit_package_loading
    }

    override fun bind(element: LoadingModel?) {}
}