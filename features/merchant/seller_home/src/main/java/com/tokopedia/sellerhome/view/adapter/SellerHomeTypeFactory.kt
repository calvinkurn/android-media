package com.tokopedia.sellerhome.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.view.model.CardWidgetUiModel
import com.tokopedia.sellerhome.view.model.CarouselWidgetUiModel
import com.tokopedia.sellerhome.view.model.LineGraphWidgetUiModel

/**
 * Created By @faisalramd on 2020-01-22
 */

interface SellerHomeTypeFactory {

    fun type(cardWidget: CardWidgetUiModel): Int

    fun type(lineGraphWidget: LineGraphWidgetUiModel): Int

    fun type(carouselWidgetUiModel: CarouselWidgetUiModel): Int

    fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<*>
}