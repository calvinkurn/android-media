package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.PdpRecomWidgetDataModel
import com.tokopedia.product.detail.databinding.ItemRecomViewHolderBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel on 27/03/23
 */
class PdpRecomWidgetViewHolder(
    private val view: View,
    private val listener: DynamicProductDetailListener
) : AbstractViewHolder<PdpRecomWidgetDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_recom_view_holder
    }

    private val binding : ItemRecomViewHolderBinding? by viewBinding()

    init {
        //todo
    }

    override fun bind(element: PdpRecomWidgetDataModel) {
        element.recomWidgetModel.let {
            binding?.recomWidget?.bind(it)
        }
    }
}
