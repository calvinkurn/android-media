package com.tokopedia.catalogcommon.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.uimodel.DummyUiModel
import com.tokopedia.catalogcommon.uimodel.HeroBannerUiModel
import com.tokopedia.catalogcommon.uimodel.SliderImageTextUiModel
import com.tokopedia.catalogcommon.uimodel.TopFeaturesUiModel
import com.tokopedia.catalogcommon.viewholder.DummyViewHolder
import com.tokopedia.catalogcommon.viewholder.HeroBannerViewHolder
import com.tokopedia.catalogcommon.viewholder.SliderImageTextViewHolder
import com.tokopedia.catalogcommon.viewholder.TopFeatureViewHolder

class CatalogAdapterFactoryImpl : BaseAdapterTypeFactory(), CatalogAdapterFactory {

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            TopFeatureViewHolder.LAYOUT -> TopFeatureViewHolder(view)
            HeroBannerViewHolder.LAYOUT -> HeroBannerViewHolder(view)
            DummyViewHolder.LAYOUT -> DummyViewHolder(view)
            SliderImageTextViewHolder.LAYOUT -> SliderImageTextViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }

    override fun type(uiModel: TopFeaturesUiModel): Int {
        return TopFeatureViewHolder.LAYOUT
    }

    override fun type(uiModel: HeroBannerUiModel): Int {
        return HeroBannerViewHolder.LAYOUT
    }

    override fun type(uiModel: DummyUiModel): Int {
        return DummyViewHolder.LAYOUT
    }

    override fun type(uiModel: SliderImageTextUiModel): Int {
        return SliderImageTextViewHolder.LAYOUT
    }
}
