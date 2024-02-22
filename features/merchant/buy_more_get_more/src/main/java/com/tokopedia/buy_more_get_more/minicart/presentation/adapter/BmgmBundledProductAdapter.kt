package com.tokopedia.buy_more_get_more.minicart.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.tokopedia.buy_more_get_more.databinding.ItemBmgmMiniCartItemProductBundleBinding
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.media.loader.loadImage
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.unifycomponents.CardUnify2

/**
 * Created by @ilhamsuaib on 04/08/23.
 */

class BmgmBundledProductAdapter(
    private val items: List<BmgmMiniCartVisitable.ProductUiModel>,
    private val listener: BmgmMiniCartAdapter.Listener
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

        fun bind(item: BmgmMiniCartVisitable.ProductUiModel) {
            with(binding) {
                cardBmgmProduct.cardType = CardUnify2.TYPE_SHADOW_DISABLED
                cardBmgmProduct.radius = root.context.dpToPx(2)
                imgBmgmUpsellingItem.loadImage(item.productImage)

                root.setOnClickListener { listener.setOnItemClickedListener() }
            }
        }
    }
}