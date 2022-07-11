package com.tokopedia.product.detail.view.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ThumbnailDataModel
import com.tokopedia.product.detail.databinding.ItemImgThumbnailViewHolderBinding
import com.tokopedia.product.detail.view.adapter.ProductMainThumbnailAdapter.Companion.PAYLOAD_ACTIVATED
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder

class ProductMainThumbnailAdapter(val listener: ProductMainThumbnailListener?,
                                  val pdpListener: DynamicProductDetailListener?,
                                  val componentTrackDataModel: ComponentTrackDataModel?)
    : RecyclerView.Adapter<AbstractViewHolder<ThumbnailDataModel>>() {

    companion object {
        val VIEW_HOLDER_LAYOUT = R.layout.item_img_thumbnail_view_holder
        const val PAYLOAD_ACTIVATED = "activated"
    }

    val currentList: MutableList<ThumbnailDataModel> = mutableListOf()

    fun submitList(newList: List<ThumbnailDataModel>) {
        val diffCallback = ProductMainThumbnailDiffutil(currentList.toMutableList(), newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        currentList.clear()
        currentList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<ThumbnailDataModel> {
        return ProductMainThumbnailViewHolder(LayoutInflater.from(parent.context)
                .inflate(VIEW_HOLDER_LAYOUT, parent, false),
                listener)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<ThumbnailDataModel>, position: Int) {
        (holder as? ProductMainThumbnailViewHolder)?.bind(currentList[position], position)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<ThumbnailDataModel>,
                                  position: Int,
                                  payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            (holder as? ProductMainThumbnailViewHolder)?.bind(currentList[position], position, payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemCount(): Int = currentList.size

    inner class ProductMainThumbnailViewHolder(val view: View,
                                               private val listener: ProductMainThumbnailListener?)
        : AbstractViewHolder<ThumbnailDataModel>(view) {

        private val binding = ItemImgThumbnailViewHolderBinding.bind(view)

        fun bind(element: ThumbnailDataModel, position: Int) {

            view.addOnImpressionListener(element.impressHolder) {
                pdpListener?.onThumbnailImpress(position,
                        element.media,
                        componentTrackDataModel)
            }

            if (element.media.isVideoType()) {
                binding.pdpVideoThumbnail.show()
            } else {
                binding.pdpVideoThumbnail.hide()
            }

            binding.pdpImgThumbnail.loadImage(element.media.urlOriginal)
            setupBackground(element)
            setupClickThumbnail(position, element)
        }

        override fun bind(element: ThumbnailDataModel) {
        }

        fun bind(element: ThumbnailDataModel, position: Int, payloads: MutableList<Any>) {
            if (payloads.isEmpty()) {
                return
            }

            if ((payloads[0] as? Bundle)?.getInt(PAYLOAD_ACTIVATED) == Int.ONE) {
                setupBackground(element)
                setupClickThumbnail(position, element)
            }
        }

        private fun setupBackground(element: ThumbnailDataModel) {
            if (element.isSelected) {
                binding.pdpThumbnailContainer.background =
                        ContextCompat.getDrawable(view.context,
                                R.drawable.pdp_thumbnail_active_bg)
            } else {
                binding.pdpThumbnailContainer.background =
                        ContextCompat.getDrawable(view.context,
                                R.drawable.pdp_thumbnail_inactive_bg)
            }
        }

        private fun setupClickThumbnail(position: Int, element: ThumbnailDataModel) {
            view.setOnClickListener {
                if (!element.isSelected) {
                    listener?.onThumbnailClicked(element)
                    pdpListener?.trackThumbnailClicked(position,
                            element.media,
                            componentTrackDataModel)
                }
            }
        }
    }
}

interface ProductMainThumbnailListener {
    fun onThumbnailClicked(element: ThumbnailDataModel)
}

class ProductMainThumbnailDiffutil(
        private val oldList: List<ThumbnailDataModel>,
        private val newList: List<ThumbnailDataModel>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].media.id == newList[newItemPosition].media.id
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].media == newList[newItemPosition].media &&
                oldList[oldItemPosition].isSelected == newList[newItemPosition].isSelected
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return if (oldList[oldItemPosition].media == newList[newItemPosition].media &&
                oldList[oldItemPosition].isSelected != newList[newItemPosition].isSelected) {
            Bundle().apply {
                putInt(PAYLOAD_ACTIVATED, Int.ONE)
            }
        } else null
    }
}