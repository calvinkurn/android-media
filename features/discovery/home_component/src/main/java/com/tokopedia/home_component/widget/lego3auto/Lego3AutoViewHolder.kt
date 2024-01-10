package com.tokopedia.home_component.widget.lego3auto

import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.marginLeft
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.HomeComponentLegoBanner3AutoBinding
import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.loadImage
import com.tokopedia.home_component.util.loadImageRounded
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.unifycomponents.toPx
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
        private const val TYPE_PRODUCT = "product"
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
            val view = FrameLayout(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
            }
            return Lego3AutoItemViewHolder(view, listener)
        }

        override fun onBindViewHolder(holder: Lego3AutoItemViewHolder, position: Int) {
            try {
                val channel = model?.channelModel ?: return
                val grid = channel.channelGrids.getOrNull(position) ?: return
                holder.bind(channel, grid, position)
            } catch (e: Exception) {
                Log.e("kvilvi", "onBindViewHolder: ", e)
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
            channelGrid.imageList.forEachIndexed { index, it ->
                val imageUrl = it.imageUrl
                val containerWidth = DeviceScreenInfo.getScreenWidth(view.context)/3
                val leftPadding = (containerWidth * (it.leftPadding/100)).toInt()
                val rightPadding = (containerWidth * (it.rightPadding/100)).toInt()
                val imageView = ImageView(view.context).apply {
                    val lParams = if(it.type == TYPE_PRODUCT) {
                        FrameLayout.LayoutParams(
                            (containerWidth - leftPadding - rightPadding),
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                        )
                    } else {
                        FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT,
                        )
                    }
                    if(it.type == TYPE_PRODUCT) {
                        loadImage(imageUrl) {
                            setRoundedRadius(view.context.resources.getDimensionPixelSize(home_componentR.dimen.home_lego_3_auto_product_radius).toFloat())
                            fitCenter()
                        }
                    } else {
                        loadImage(imageUrl) {
                            fitCenter()
                        }
                    }
                    lParams.gravity = Gravity.CENTER_VERTICAL
                    layoutParams = lParams
                    setMargin(leftPadding, 0, rightPadding, 0)
                }
                view.addView(imageView)
            }
        }

        private fun setClickListener(
            channelModel: ChannelModel,
            channelGrid: ChannelGrid,
            position: Int
        ) {
            view.setOnClickListener {
                listener.onClickGridThreeImage(channelModel, channelGrid, position, channelModel.verticalPosition)
            }
        }
    }
}
