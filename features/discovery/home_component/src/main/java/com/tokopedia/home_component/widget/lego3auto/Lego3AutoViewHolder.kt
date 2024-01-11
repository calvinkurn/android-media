package com.tokopedia.home_component.widget.lego3auto

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
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
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.toPx
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

                override fun onVisibilityChanged(isVisible: Boolean) {
                    val margin = itemView.context.resources.getDimensionPixelSize(home_componentR.dimen.home_lego_3_auto_vertical_padding)
                    if(isVisible) {
                        binding?.rvLego3Auto?.setMargin(0, 0, 0, margin)
                    } else {
                        binding?.rvLego3Auto?.setMargin(0, margin, 0, margin)
                    }
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
            val view = ConstraintLayout(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
            }
            return Lego3AutoItemViewHolder(view, listener)
        }

        override fun onBindViewHolder(holder: Lego3AutoItemViewHolder, position: Int) {
            try {
                val channel = model?.channelModel ?: return
                val grid = channel.channelGrids.getOrNull(position) ?: return
                holder.bind(channel, grid, position)
            } catch (_: Exception) { }
        }

        fun setData(model: Lego3AutoModel) {
            this.model = model
        }

        override fun getItemCount(): Int {
            return SPAN
        }
    }

    class Lego3AutoItemViewHolder(
        private val parentView: ConstraintLayout,
        private val listener: DynamicLegoBannerListener,
    ) : RecyclerView.ViewHolder(parentView) {

        companion object {
            private const val TYPE_PRODUCT = "product"
            private const val VERTICAL_BIAS_CENTER = 0.5f
            private const val RADIUS_DEFAULT = 0f
            private const val RADIUS_PRODUCT = 8f
            private const val DIMENSION_RATIO = "1:1"
        }

        fun bind(
            channelModel: ChannelModel,
            channelGrid: ChannelGrid,
            position: Int
        ) {
            setClickListener(channelModel, channelGrid, position)
            channelGrid.imageList.forEach {
                val imageUrl = it.imageUrl
                val containerWidth = DeviceScreenInfo.getScreenWidth(parentView.context)/SPAN
                val leftPadding = (containerWidth * (it.leftPadding/100)).toInt()
                val rightPadding = (containerWidth * (it.rightPadding/100)).toInt()
                val lParams: ConstraintLayout.LayoutParams
                var cornerRadius = RADIUS_DEFAULT
                val imageView = ImageView(parentView.context).apply {
                    id = ViewCompat.generateViewId()
                    if(it.type == TYPE_PRODUCT) {
                        lParams = ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                            ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                        )
                        cornerRadius = RADIUS_PRODUCT.toPx()
                    } else {
                        lParams = ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                            ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                        )
                    }
                    lParams.verticalBias = VERTICAL_BIAS_CENTER
                    lParams.dimensionRatio = DIMENSION_RATIO
                    lParams.marginStart = leftPadding
                    lParams.marginEnd = rightPadding
                    layoutParams = lParams
                }
                parentView.addView(imageView)
                setConstraints(imageView.id)
                imageView.loadImage(imageUrl) {
                    setRoundedRadius(cornerRadius)
                    fitCenter()
                }
            }
        }

        private fun setConstraints(viewId: Int) {
            val constraintSet = ConstraintSet()
            constraintSet.clone(parentView)
            constraintSet.connect(viewId, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            constraintSet.connect(viewId, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            constraintSet.connect(viewId, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            constraintSet.connect(viewId, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            constraintSet.applyTo(parentView)
        }

        private fun setClickListener(
            channelModel: ChannelModel,
            channelGrid: ChannelGrid,
            position: Int
        ) {
            parentView.setOnClickListener {
                listener.onClickGridThreeImage(channelModel, channelGrid, position, channelModel.verticalPosition)
            }
        }
    }
}
