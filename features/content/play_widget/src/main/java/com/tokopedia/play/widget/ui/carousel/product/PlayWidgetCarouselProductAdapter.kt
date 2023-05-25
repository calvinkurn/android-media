package com.tokopedia.play.widget.ui.carousel.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.databinding.ItemPlayWidgetCarouselProductBinding
import com.tokopedia.play.widget.ui.model.PlayWidgetProduct

/**
 * Created by kenny.hadisaputra on 15/05/23
 */
class PlayWidgetCarouselProductAdapter(
    private val listener: ViewHolder.Listener,
): ListAdapter<PlayWidgetProduct, PlayWidgetCarouselProductAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<PlayWidgetProduct>() {
        override fun areItemsTheSame(
            oldItem: PlayWidgetProduct,
            newItem: PlayWidgetProduct
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: PlayWidgetProduct,
            newItem: PlayWidgetProduct
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemPlayWidgetCarouselProductBinding,
        private val listener: Listener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PlayWidgetProduct) {
            itemView.addOnImpressionListener(item.impressHolder) {
                listener.onImpressed(this, item)
            }

            binding.imgProduct.setImageUrl(item.imageUrl)
            binding.tvProductName.text = item.name
            binding.tvProductPrice.text = item.priceFmt

            binding.root.setOnClickListener {
                listener.onClicked(this, item)
            }
        }

        companion object {
            fun create(parent: ViewGroup, listener: Listener) : ViewHolder {
                return ViewHolder(
                    ItemPlayWidgetCarouselProductBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    listener,
                )
            }
        }

        interface Listener {
            fun onImpressed(viewHolder: ViewHolder, product: PlayWidgetProduct)

            fun onClicked(viewHolder: ViewHolder, product: PlayWidgetProduct)
        }
    }
}
