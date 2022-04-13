package com.tokopedia.tokomember_seller_dashboard.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TokomemberCardBgAdapterListener
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardBg
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TokomemberDashCardBgVh

class TokomemberCardBgFactory(val listener: TokomemberCardBgAdapterListener) : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            TokomemberDashCardBgVh.LAYOUT_ID -> return TokomemberDashCardBgVh(parent)
        }
        return super.createViewHolder(parent, type)
    }

    fun type(tokomemberIntroHeaderItem: TokomemberCardBg): Int {
        return TokomemberDashCardBgVh.LAYOUT_ID
    }

}
