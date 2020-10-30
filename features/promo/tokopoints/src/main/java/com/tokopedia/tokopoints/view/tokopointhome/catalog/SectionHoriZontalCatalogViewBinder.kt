package com.tokopedia.tokopoints.view.tokopointhome.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.tokopointhome.SectionItemViewBinder
import com.tokopedia.tokopoints.view.tokopointhome.TokoPointsHomeViewModel

class SectionHoriZontalCatalogViewBinder( val mPresenter: TokoPointsHomeViewModel)
    : SectionItemViewBinder<SectionContent, SectionHorizontalCatalogVH>(
        SectionContent::class.java) {
    override fun createViewHolder(parent: ViewGroup): SectionHorizontalCatalogVH {
        return SectionHorizontalCatalogVH(
                LayoutInflater.from(parent.context).inflate(getSectionItemType(), parent, false), mPresenter)
    }

    override fun bindViewHolder(model: SectionContent, viewHolder: SectionHorizontalCatalogVH) {
        viewHolder.bind(model)
    }

    override fun getSectionItemType() = R.layout.tp_layout_generic_carousal_catalog

}