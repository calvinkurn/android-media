package com.tokopedia.epharmacy.component.viewholder

import android.os.Build
import android.text.Html
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

    override fun bind(data: EPharmacyTickerDataModel) {
        view.findViewById<ImageUnify>(R.id.ticker_icon).loadImage(data.tickerLogo)
        view.findViewById<ImageUnify>(R.id.ticker_background_image).loadImage(data.tickerBackground)
        view.findViewById<Typography>(R.id.ticker_text).run {
            text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(data.tickerText, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(data.tickerText)
            }
        }
    }
}
