package com.tokopedia.home_component.widget.lego3auto

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.core.view.marginLeft
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.HomeComponentLegoBanner3AutoBinding
import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.loadImage
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.home_component.R as home_componentR

/**
 * Created by frenzel
 */
class Lego3AutoViewHolder(
    itemView: View,
    private val legoListener: DynamicLegoBannerListener,
) : AbstractViewHolder<Lego3AutoModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = home_componentR.layout.home_component_lego_banner_3_auto
        private const val SPAN = 3
    }

    private val binding: HomeComponentLegoBanner3AutoBinding? by viewBinding()
    private val adapter by lazy { Lego3AutoAdapter(legoListener) }

    override fun bind(element: Lego3AutoModel) {
        binding?.let {
            initAdapter()
            setData(element)
            setupImpression(element.channelModel)
            setHeaderComponent(element.channelModel)
            setChannelDivider(element.channelModel)
        }
    }

    override fun bind(element: Lego3AutoModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun initAdapter() {
        val layoutManager = GridLayoutManager(
            itemView.context,
            SPAN,
            GridLayoutManager.VERTICAL,
            false
        )
        binding?.rvLego3Auto?.apply {
            setLayoutManager(layoutManager)
            this.adapter = this@Lego3AutoViewHolder.adapter
        }
    }

    private fun setupImpression(channelModel: ChannelModel) {
        itemView.addOnImpressionListener(channelModel) {
            legoListener.onViewportImpression(channelModel)
        }
    }

    private fun setChannelDivider(channelModel: ChannelModel) {
        binding?.let {
            ChannelWidgetUtil.validateHomeComponentDivider(
                channelModel = channelModel,
                dividerTop = it.homeComponentDividerHeader,
                dividerBottom = it.homeComponentDividerFooter
            )
        }
    }

    private fun setHeaderComponent(channelModel: ChannelModel) {
        binding?.homeComponentHeaderView?.setChannel(
            channelModel,
            object: HeaderListener {
                override fun onSeeAllClick(link: String) {
                    legoListener.onSeeAllThreemage(channelModel, channelModel.verticalPosition)
                }
            }
        )
    }

    private fun setData(model: Lego3AutoModel) {
        adapter.setData(model)
    }

    class Lego3AutoAdapter(
        private val listener: DynamicLegoBannerListener,
    ) : RecyclerView.Adapter<Lego3AutoItemViewHolder>() {

        private var model: Lego3AutoModel? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Lego3AutoItemViewHolder {
            val view = FrameLayout(parent.context)
            return Lego3AutoItemViewHolder(view, listener)
        }

        override fun onBindViewHolder(holder: Lego3AutoItemViewHolder, position: Int) {
            try {
                val channel = model?.channelModel ?: return
                val grid = channel.channelGrids.getOrNull(position) ?: return
                holder.bind(channel, grid, position)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun setData(model: Lego3AutoModel) {
            this.model = model
        }

        override fun getItemCount(): Int {
            return SPAN
        }
    }

    class Lego3AutoItemViewHolder(
        private val view: FrameLayout,
        private val listener: DynamicLegoBannerListener,
    ) : RecyclerView.ViewHolder(view) {

        fun bind(
            channelModel: ChannelModel,
            channelGrid: ChannelGrid,
            position: Int
        ) {
            setClickListener(channelModel, channelGrid, position)
            channelGrid.imageList.forEach {
                val imageUrl = it.imageUrl
                val imageView = ImageView(view.context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT,
                    )
                    loadImage(imageUrl)
                    scaleType = ImageView.ScaleType.FIT_CENTER
                    val leftPadding = (view.width * (it.leftPadding/100)).toInt()
                    val rightPadding = (view.width * (it.rightPadding/100)).toInt()
                    updatePadding(left = leftPadding, right = rightPadding)
                }
                view.addView(imageView)
            }
        }

        private fun setClickListener(
            channelModel: ChannelModel,
            channelGrid: ChannelGrid,
            position: Int
        ) {
            listener.onClickGridThreeImage(channelModel, channelGrid, position, channelModel.verticalPosition)
        }
    }
}
