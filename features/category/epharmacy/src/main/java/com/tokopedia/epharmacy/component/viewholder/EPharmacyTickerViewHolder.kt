package com.tokopedia.epharmacy.component.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.component.model.EPharmacyTickerDataModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class EPharmacyTickerViewHolder(
    val view: View
) : AbstractViewHolder<EPharmacyTickerDataModel>(view) {
    companion object {
        val LAYOUT = R.layout.epharmacy_ticker_view_item
    }

    override fun bind(element: EPharmacyTickerDataModel) {
        view.findViewById<ImageUnify>(R.id.ticker_icon).loadImage(element.tickerLogo)
        view.findViewById<Typography>(R.id.ticker_text).text = element.tickerText
    }
}
