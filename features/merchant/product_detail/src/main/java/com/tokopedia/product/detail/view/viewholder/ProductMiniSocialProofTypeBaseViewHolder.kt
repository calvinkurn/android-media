package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofItemDataModel

abstract class ProductMiniSocialProofTypeBaseViewHolder(
        view: View
) : RecyclerView.ViewHolder(view) {
    abstract fun bind(
            socialProof: ProductMiniSocialProofItemDataModel,
            componentTrackDataModel: ComponentTrackDataModel?
    )
}