package com.tokopedia.minicart.bmgm.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.tokopedia.media.loader.loadImage
import com.tokopedia.minicart.databinding.ItemBmgmMiniCartItemProductBundleBinding
import com.tokopedia.purchase_platform.common.feature.bmgm.uimodel.BmgmCommonDataUiModel
import com.tokopedia.unifycomponents.CardUnify2

/**
 * Created by @ilhamsuaib on 04/08/23.
 */

class BmgmBundledProductAdapter(
    private val items: List<BmgmCommonDataUiModel.SingleProductUiModel>
) : Adapter<BmgmBundledProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBmgmMiniCartItemProductBundleBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(
        private val binding: ItemBmgmMiniCartItemProductBundleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BmgmCommonDataUiModel.SingleProductUiModel) {
            with(binding) {
                cardBmgmProduct.cardType = CardUnify2.TYPE_SHADOW_DISABLED
                cardBmgmProduct.radius = 4f
                imgBmgmUpsellingItem.loadImage(item.productImage)
            }
        }
    }
}