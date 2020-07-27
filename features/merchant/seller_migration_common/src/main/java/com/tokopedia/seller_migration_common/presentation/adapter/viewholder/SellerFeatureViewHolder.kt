package com.tokopedia.seller_migration_common.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel
import com.tokopedia.unifycomponents.setImage
import kotlinx.android.synthetic.main.item_seller_feature.view.*

class SellerFeatureViewHolder(view: View?) : AbstractViewHolder<SellerFeatureUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_seller_feature
    }

    override fun bind(element: SellerFeatureUiModel) {
        with(itemView) {
            ivSellerFeature.setImage(element.imageId, 0f)
            tvSellerFeatureTitle.text = getString(element.titleId)
            tvSellerFeatureDescription.text = getString(element.descriptionId)
        }
    }
}
