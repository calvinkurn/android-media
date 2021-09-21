package com.tokopedia.product.detail.view.viewholder

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

    private val firstSocialProofTxt: Typography? = view.findViewById(R.id.social_proof_first_text)

    override fun bind(socialProof: ProductMiniSocialProofItemDataModel, componentTrackDataModel: ComponentTrackDataModel?) {

        if (socialProof.type != ProductMiniSocialProofItemType.ProductMiniSocialProofTextDivider) return

        firstSocialProofTxt?.apply {
            text = view.context.getString(R.string.label_stock_builder, socialProof.formattedCount)
            setPadding(8.toPx(), 7.toPx(), 8.toPx(), 7.toPx())
        }
    }
}