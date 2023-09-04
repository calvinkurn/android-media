package com.tokopedia.catalogcommon.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetItemDoubleBannerImageBinding
import com.tokopedia.catalogcommon.databinding.WidgetItemDoubleBannerImageContentBinding
import com.tokopedia.catalogcommon.uimodel.DoubleBannerCatalogUiModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding

class DoubleBannerViewHolder(itemView: View) : AbstractViewHolder<DoubleBannerCatalogUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_item_double_banner_image
    }

    private val binding by viewBinding<WidgetItemDoubleBannerImageBinding>()

    override fun bind(element: DoubleBannerCatalogUiModel) {
        binding?.rvImages?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = DoubleImageAdapter(element.imageUrls.zipWithNext())
        }
    }

    class DoubleImageAdapter(
        private var items: List<Pair<String, String>> = listOf()
    ): RecyclerView.Adapter<DoubleImageViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoubleImageViewHolder {
            val rootView = DoubleImageViewHolder.createRootView(parent)
            return DoubleImageViewHolder(rootView)
        }

        override fun onBindViewHolder(holder: DoubleImageViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount() = items.size
    }

    class DoubleImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            fun createRootView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
                .inflate(R.layout.widget_item_double_banner_image_content, parent, false)
        }

        private val binding: WidgetItemDoubleBannerImageContentBinding? by viewBinding()

        fun bind(item: Pair<String, String>) {
            binding?.apply {
                contentLeft.loadImage(item.first)
                contentRight.loadImage(item.second)
            }
        }
    }
}
