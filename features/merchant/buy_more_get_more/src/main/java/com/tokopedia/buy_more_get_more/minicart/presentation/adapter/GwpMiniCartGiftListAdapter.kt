package com.tokopedia.buy_more_get_more.minicart.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.bmsm_widget.presentation.adapter.diffutil.ProductGiftDiffUtilCallback
import com.tokopedia.bmsm_widget.presentation.model.ProductGiftUiModel
import com.tokopedia.buy_more_get_more.databinding.ItemBmgmMiniCartProductGiftBinding
import com.tokopedia.media.loader.loadImage

/**
 * Created by @ilhamsuaib on 07/12/23.
 */

class GwpMiniCartGiftListAdapter(
    private val listener: BmgmMiniCartAdapter.Listener
) : ListAdapter<ProductGiftUiModel, GwpMiniCartGiftListAdapter.ViewHolder>(
    ProductGiftDiffUtilCallback.createDiffUtil()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context.applicationContext)
        val binding = ItemBmgmMiniCartProductGiftBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: ItemBmgmMiniCartProductGiftBinding,
        private val listener: BmgmMiniCartAdapter.Listener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ProductGiftUiModel) {
            with(binding) {
                imgBmgmSingleProduct.loadImage(model.imageUrl)
                root.setOnClickListener {
                    listener.setOnItemClickedListener()
                }
            }
        }
    }
}