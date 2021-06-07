package com.tokopedia.product.detail.view.viewholder

import android.text.Html
import android.view.View
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofItemDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofItemType
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class ProductMiniSocialProofTextDividerViewHolder(
        private val view: View
) : ProductMiniSocialProofTypeBaseViewHolder(view) {
    override fun bind(socialProof: ProductMiniSocialProofItemDataModel, componentTrackDataModel: ComponentTrackDataModel?) {

        if (socialProof.type != ProductMiniSocialProofItemType.ProductMiniSocialProofTextDivider) return

        val firstSocialProofTxt = view.findViewById<Typography>(R.id.social_proof_first_text)
        firstSocialProofTxt.apply {
            val htmlText = view.context.getString(R.string.label_stock_builder, socialProof.formattedCount)
            text = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
            } else Html.fromHtml(htmlText)
            setPadding(8.toPx(), 7.toPx(), 8.toPx(), 7.toPx())
        }
    }
}