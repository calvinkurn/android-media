package com.tokopedia.tokomember_seller_dashboard.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.*
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.*

class TokomemberIntroFactory(val listener: TokomemberIntroButtonVh.TokomemberIntroButtonListener) : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            TokomemberIntroButtonVh.LAYOUT_ID -> return TokomemberIntroButtonVh(listener,parent)
            TokomemberIntroTextVh.LAYOUT_ID -> return TokomemberIntroTextVh(parent)
            TokomemberIntroBenefitImageVh.LAYOUT_ID -> return TokomemberIntroBenefitImageVh(parent)
        }
        return super.createViewHolder(parent, type)
    }

    fun type(tokomemberIntroHeaderItem: TokomemberIntroButtonItem): Int {
        return TokomemberIntroButtonVh.LAYOUT_ID
    }

    fun type(tokomemberIntroHeaderItem: TokomemberIntroTextItem): Int {
        return TokomemberIntroTextVh.LAYOUT_ID
    }

    fun type(tokomemberIntroBenefitImage: TokomemberIntroBenefitImageItem): Int {
        return TokomemberIntroBenefitImageVh.LAYOUT_ID
    }

}
