package com.tokopedia.tokomember_seller_dashboard.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TokomemberCardColorAdapterListener
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardColor
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TmDashCardColorVh

class TmCardColorFactory(val listener: TokomemberCardColorAdapterListener) : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            TmDashCardColorVh.LAYOUT_ID -> return TmDashCardColorVh(parent , listener)
        }
        return super.createViewHolder(parent, type)
    }

    fun type(tokomemberIntroHeaderItem: TokomemberCardColor): Int {
        return TmDashCardColorVh.LAYOUT_ID
    }

}
