package com.tokopedia.logisticaddaddress.features.district_recommendation.adapter

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.logisticaddaddress.domain.model.Address

/**
 * Created by Irfan Khoirul on 16/11/18.
 */

class DistrictAdapterTypeFactory : BaseAdapterTypeFactory(), DistrictTypeFactory {

    override fun type(addressViewModel: Address): Int {
        return DistrictViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == DistrictViewHolder.LAYOUT) {
            DistrictViewHolder(parent)
        } else super.createViewHolder(parent, type)
    }

}
