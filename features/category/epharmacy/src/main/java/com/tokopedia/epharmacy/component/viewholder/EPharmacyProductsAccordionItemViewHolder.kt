package com.tokopedia.epharmacy.component.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class EPharmacyProductsAccordionItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(product: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product?) {
        view.findViewById<Typography>(com.tokopedia.epharmacy.R.id.product_name).text = product?.name
        view.findViewById<Typography>(com.tokopedia.epharmacy.R.id.product_quantity).text = product?.quantityString
        view.findViewById<ImageUnify>(com.tokopedia.epharmacy.R.id.product_image).loadImage(product?.productImage)
    }
}
