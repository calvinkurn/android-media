package com.tokopedia.tokomember_seller_dashboard.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroHeaderItem
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroTextItem
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroVideoItem
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TokomemberIntroHeaderVh
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TokomemberIntroTextVh
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TokomemberIntroVideoVh

class TokomemberIntroFactory : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            TokomemberIntroHeaderVh.LAYOUT_ID -> return TokomemberIntroHeaderVh(parent)
            TokomemberIntroTextVh.LAYOUT_ID -> return TokomemberIntroTextVh(parent)
            TokomemberIntroVideoVh.LAYOUT_ID -> return TokomemberIntroVideoVh(parent)
        }
        return super.createViewHolder(parent, type)
    }

    fun type(tokomemberIntroHeaderItem: TokomemberIntroHeaderItem): Int {
        return TokomemberIntroHeaderVh.LAYOUT_ID
    }

    fun type(tokomemberIntroHeaderItem: TokomemberIntroTextItem): Int {
        return TokomemberIntroTextVh.LAYOUT_ID
    }

    fun type(tokomemberIntroHeaderItem: TokomemberIntroVideoItem): Int {
        return TokomemberIntroVideoVh.LAYOUT_ID
    }

}
