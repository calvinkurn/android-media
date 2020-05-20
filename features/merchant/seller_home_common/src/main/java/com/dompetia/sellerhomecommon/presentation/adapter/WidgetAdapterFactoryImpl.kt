package com.dompetia.sellerhomecommon.presentation.adapter

import com.dompetia.sellerhomecommon.presentation.model.CardWidgetUiModel
import com.dompetia.sellerhomecommon.presentation.model.CarouselWidgetUiModel
import com.dompetia.sellerhomecommon.presentation.model.LineGraphWidgetUiModel
import com.dompetia.sellerhomecommon.presentation.view.viewholder.CardViewHolder
import com.dompetia.sellerhomecommon.presentation.view.viewholder.CarouselViewHolder
import com.dompetia.sellerhomecommon.presentation.view.viewholder.LineGraphViewHolder
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory

/**
 * Created By @ilhamsuaib on 19/05/20
 */

class WidgetAdapterFactoryImpl : BaseAdapterTypeFactory(), WidgetAdapterFactory {

    override fun type(cardWidget: CardWidgetUiModel): Int = CardViewHolder.RES_LAYOUT

    override fun type(carouselWidgetUiModel: CarouselWidgetUiModel): Int = CarouselViewHolder.RES_LAYOUT

    override fun type(lineGraphWidget: LineGraphWidgetUiModel): Int = LineGraphViewHolder.RES_LAYOUT
}