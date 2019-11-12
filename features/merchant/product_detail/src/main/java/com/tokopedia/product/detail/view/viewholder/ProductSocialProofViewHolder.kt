package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.data.model.datamodel.ProductSocialProofDataModel
import com.tokopedia.product.detail.view.adapter.dynamicadapter.SocialProofAdapter
import kotlinx.android.synthetic.main.item_dynamic_pdp_social_proof.view.*

class ProductSocialProofViewHolder(itemView: View) : AbstractViewHolder<ProductSocialProofDataModel>(itemView) {

    private val socialProofAdapter by lazy {
        SocialProofAdapter()
    }

    override fun bind(element: ProductSocialProofDataModel) {
        itemView.rv_social_proof.apply {
            layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
            adapter = socialProofAdapter
        }

        socialProofAdapter.updateSocialProofData(element)
    }

}