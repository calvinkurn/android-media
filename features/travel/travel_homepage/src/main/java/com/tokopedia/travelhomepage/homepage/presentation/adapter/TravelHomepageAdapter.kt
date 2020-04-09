package com.tokopedia.travelhomepage.homepage.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageItemModel
import com.tokopedia.travelhomepage.homepage.presentation.adapter.factory.TravelHomepageTypeFactory

/**
 * @author by jessica on 2020-03-12
 */

class TravelHomepageAdapter(typeFactory: TravelHomepageTypeFactory): BaseListAdapter<TravelHomepageItemModel, TravelHomepageTypeFactory>(typeFactory) {

    override fun getItemId(position: Int): Long {
        return if (position > 0) data[position].layoutData.position.toLong() else position.toLong()
    }
}