package com.tokopedia.tokomember_seller_dashboard.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.*
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.*

class TokomemberIntroFactory(val listener: TmIntroButtonVh.TokomemberIntroButtonListener) : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            TmIntroButtonVh.LAYOUT_ID -> return TmIntroButtonVh(listener,parent)
            TmIntroTextVh.LAYOUT_ID -> return TmIntroTextVh(parent)
            TmIntroBenefitImageVh.LAYOUT_ID -> return TmIntroBenefitImageVh(parent)
        }
        return super.createViewHolder(parent, type)
    }

    fun type(tokomemberIntroHeaderItem: TokomemberIntroButtonItem): Int {
        return TmIntroButtonVh.LAYOUT_ID
    }

    fun type(tokomemberIntroHeaderItem: TokomemberIntroTextItem): Int {
        return TmIntroTextVh.LAYOUT_ID
    }

    fun type(tokomemberIntroBenefitImage: TokomemberIntroBenefitImageItem): Int {
        return TmIntroBenefitImageVh.LAYOUT_ID
    }

}
