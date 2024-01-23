package com.tokopedia.home_component.widget.lego3auto

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.PaintDrawable
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
import com.tokopedia.home_component.model.ChannelGridImage
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.loadImage
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.unifyprinciples.ColorMode
import com.tokopedia.unifyprinciples.modeAware
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.unifycomponents.R as unifycomponentsR

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
            private const val DIMENSION_RATIO = "1:1"
        }

        fun bind(
            channelModel: ChannelModel,
            channelGrid: ChannelGrid,
            position: Int,
        ) {
            setClickListener(channelModel, channelGrid, position)
            channelGrid.imageList.forEach { renderImage(it) }
        }

        private fun renderImage(channelGridImage: ChannelGridImage) {
            val containerWidth = DeviceScreenInfo.getScreenWidth(parentView.context)/SPAN
            val leftPadding = (containerWidth * (channelGridImage.leftPadding/100)).toInt()
            val rightPadding = (containerWidth * (channelGridImage.rightPadding/100)).toInt()
            val lParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
            ).apply {
                verticalBias = VERTICAL_BIAS_CENTER
                dimensionRatio = DIMENSION_RATIO
                marginStart = leftPadding
                marginEnd = rightPadding
            }

            if(channelGridImage.type == TYPE_PRODUCT) {
                parentView.addBackgroundView(lParams)
            }
            parentView.addImageView(lParams, channelGridImage)
        }

        private fun ViewGroup.addImageView(
            lParams: ViewGroup.LayoutParams,
            channelGridImage: ChannelGridImage
        ) {
            ImageView(parentView.context).apply {
                id = ViewCompat.generateViewId()
            }.also {
                addView(it, lParams)
                setContraints(it.id)

                val cornerRadius = if(channelGridImage.type == TYPE_PRODUCT) {
                    parentView.context.resources.getDimensionPixelSize(home_componentR.dimen.home_lego_3_auto_product_image_radius).toFloat()
                } else {
                    RADIUS_DEFAULT
                }
                it.loadImage(channelGridImage.imageUrl) {
                    setRoundedRadius(cornerRadius)
                    fitCenter()
                }
            }
        }

        @SuppressLint("ResourceType")
        private fun ViewGroup.addBackgroundView(lParams: ViewGroup.LayoutParams) {
            View(parentView.context).apply {
                id = ViewCompat.generateViewId()
                val shape = PaintDrawable(Color.parseColor(context.modeAware(ColorMode.LIGHT_MODE)?.resources?.getString(unifycomponentsR.color.Unify_NN50)))
                shape.setCornerRadius(parentView.context.resources.getDimensionPixelSize(home_componentR.dimen.home_lego_3_auto_product_image_radius).toFloat())
                background = shape
            }.also {
                addView(it, lParams)
                setContraints(it.id)
            }
        }

        private fun setContraints(viewId: Int) {
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
            position: Int,
        ) {
            parentView.setOnClickListener {
                listener.onClickGridThreeImage(channelModel, channelGrid, position, channelModel.verticalPosition)
            }
        }
    }
}
