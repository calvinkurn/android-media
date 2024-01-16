package com.tokopedia.home_component.widget.special_release

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl

internal open class SpecialReleaseRevampItemTypeFactoryImpl(
    private val listener: SpecialReleaseRevampListener,
    private val commonCarouselTypeFactory: CommonCarouselProductCardTypeFactory = CommonCarouselProductCardTypeFactoryImpl(listener = listener)
): SpecialReleaseRevampItemTypeFactory,
    CommonCarouselProductCardTypeFactory by commonCarouselTypeFactory {

    override fun type(dataModel: SpecialReleaseRevampItemDataModel): Int {
        return SpecialReleaseRevampItemViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when(viewType) {
            SpecialReleaseRevampItemViewHolder.LAYOUT -> {
                SpecialReleaseRevampItemViewHolder(view, listener)
            }
            else -> commonCarouselTypeFactory.createViewHolder(view, viewType)
        }
    }
}
