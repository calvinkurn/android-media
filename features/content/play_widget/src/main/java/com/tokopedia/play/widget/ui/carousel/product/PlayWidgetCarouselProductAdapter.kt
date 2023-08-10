package com.tokopedia.play.widget.ui.carousel.product

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.play.widget.databinding.ItemPlayWidgetCarouselProductBinding
import com.tokopedia.play.widget.ui.model.PlayWidgetProduct

/**
 * Created by kenny.hadisaputra on 15/05/23
 */
class PlayWidgetCarouselProductAdapter(
    private val listener: ViewHolder.Listener,
): RecyclerView.Adapter<PlayWidgetCarouselProductAdapter.ViewHolder>() {

    private var itemList = emptyList<PlayWidgetProduct>()

    @SuppressLint("NotifyDataSetChanged")
    fun setNewItems(newItems: List<PlayWidgetProduct>) {
        itemList = newItems
        //need to use notifyDataSetChanged to support smooth transition on infinite carousel
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent, listener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    class ViewHolder(
        private val binding: ItemPlayWidgetCarouselProductBinding,
        private val listener: Listener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PlayWidgetProduct) {
            itemView.addOnImpressionListener(item.impressHolder) {
                listener.onImpressed(this, item)
            }

            binding.imgProduct.loadImage(item.imageUrl)
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
