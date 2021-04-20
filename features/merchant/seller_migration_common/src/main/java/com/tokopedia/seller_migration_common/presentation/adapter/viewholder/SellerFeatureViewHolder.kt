package com.tokopedia.seller_migration_common.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel
import com.tokopedia.unifycomponents.setImage
import kotlinx.android.synthetic.main.item_seller_feature.view.*

open class SellerFeatureViewHolder(view: View?) : AbstractViewHolder<SellerFeatureUiModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_seller_feature
    }

    override fun bind(element: SellerFeatureUiModel) {
        with(itemView) {
            itemSellerFeatureContainer.setPadding(
                    getDimens(com.tokopedia.unifycomponents.R.dimen.layout_lvl2),
                    getDimens(com.tokopedia.unifycomponents.R.dimen.layout_lvl2),
                    getDimens(com.tokopedia.unifycomponents.R.dimen.layout_lvl2),
                    getDimens(com.tokopedia.unifycomponents.R.dimen.layout_lvl2)
            )
            ivSellerFeature.setImage(element.imageUrl, 0f)
            tvSellerFeatureTitle.text = getString(element.titleId)
            tvSellerFeatureDescription.text = getString(element.descriptionId)
        }
    }
}
