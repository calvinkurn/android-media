package com.tokopedia.tokomember_seller_dashboard.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TokomemberIntroAdapterListener
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroBenefitImageItem
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroHeaderItem
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroTextItem
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroVideoItem
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TokomemberIntroBenefitImageVh
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TokomemberIntroHeaderVh
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TokomemberIntroTextVh
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TokomemberIntroVideoVh

class TokomemberIntroFactory(val listener: TokomemberIntroAdapterListener) : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            TokomemberIntroHeaderVh.LAYOUT_ID -> return TokomemberIntroHeaderVh(parent)
            TokomemberIntroTextVh.LAYOUT_ID -> return TokomemberIntroTextVh(listener,parent)
            TokomemberIntroVideoVh.LAYOUT_ID -> return TokomemberIntroVideoVh(parent)
            TokomemberIntroBenefitImageVh.LAYOUT_ID -> return TokomemberIntroBenefitImageVh(parent)
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

    fun type(tokomemberIntroBenefitImage: TokomemberIntroBenefitImageItem): Int {
        return TokomemberIntroBenefitImageVh.LAYOUT_ID
    }

}
