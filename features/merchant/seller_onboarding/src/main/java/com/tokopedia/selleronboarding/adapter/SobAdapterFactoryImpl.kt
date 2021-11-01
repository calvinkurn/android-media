package com.tokopedia.selleronboarding.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.selleronboarding.adapter.viewholder.*
import com.tokopedia.selleronboarding.model.*

/**
 * Created By @ilhamsuaib on 20/07/21
 */

class SobAdapterFactoryImpl : BaseAdapterTypeFactory(), SobAdapterFactory {

    override fun type(model: SobSliderHomeUiModel): Int {
        return SliderHomeViewHolder.RES_LAYOUT
    }

    override fun type(model: SobSliderMessageUiModel): Int {
        return SliderMessageViewHolder.RES_LAYOUT
    }

    override fun type(model: SobSliderManageUiModel): Int {
        return SliderManageViewHolder.RES_LAYOUT
    }

    override fun type(model: SobSliderPromoUiModel): Int {
        return SliderPromoViewHolder.RES_LAYOUT
    }

    override fun type(model: SobSliderStatisticsUiModel): Int {
        return SliderStatisticsViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            SliderHomeViewHolder.RES_LAYOUT -> SliderHomeViewHolder(parent)
            SliderMessageViewHolder.RES_LAYOUT -> SliderMessageViewHolder(parent)
            SliderManageViewHolder.RES_LAYOUT -> SliderManageViewHolder(parent)
            SliderPromoViewHolder.RES_LAYOUT -> SliderPromoViewHolder(parent)
            SliderStatisticsViewHolder.RES_LAYOUT -> SliderStatisticsViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}