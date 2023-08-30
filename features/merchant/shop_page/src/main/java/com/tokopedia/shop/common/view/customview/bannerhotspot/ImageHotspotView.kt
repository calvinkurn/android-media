package com.tokopedia.shop.common.view.customview.bannerhotspot

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.DefaultLifecycleObserver
import com.tokopedia.feedcomponent.util.util.doOnLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.common.view.model.ImageHotspotData
import com.tokopedia.shop.databinding.ImageHotspotViewBinding
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.dpToPx

class ImageHotspotView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr), DefaultLifecycleObserver,
    HotspotTagView.Listener {

    companion object {
        private val DEFAULT_CORNER_RADIUS = 4f.dpToPx().toInt()
    }

    interface Listener {
        fun onBubbleViewClicked(
            hotspotData: ImageHotspotData.HotspotData,
            view: View,
            index: Int
        )
    }

    private val viewBinding: ImageHotspotViewBinding
    private val imageBanner: ImageUnify
        get() = viewBinding.imageBanner
    private val imageShoppingBag: IconUnify
        get() = viewBinding.imageShoppingBag
    private var listHotspot: List<ImageHotspotData.HotspotData> = listOf()
    private var isAllHotspotTagViewVisible = true

    init {
        viewBinding = ImageHotspotViewBinding.inflate(LayoutInflater.from(context), this)
    }

    fun setData(
        imageHotspotData: ImageHotspotData,
        listenerBubbleView: Listener,
        cornerRadius: Int = DEFAULT_CORNER_RADIUS
    ) {
        setImageBanner(imageHotspotData.imageBannerUrl, cornerRadius)
        setHotspot(imageHotspotData.listHotspot, listenerBubbleView)
        setOnImageShoppingBagClicked()
    }

    private fun setOnImageShoppingBagClicked() {
        imageShoppingBag.setOnClickListener {
            hideAllBubbleView()
            isAllHotspotTagViewVisible = !isAllHotspotTagViewVisible
            if (isAllHotspotTagViewVisible) {
                showAllHotspotTag()
            } else {
                hideAllHotspotTag()
            }
        }
    }

    private fun hideAllHotspotTag() {
        listHotspot.forEach {
            it.hotspotTagView?.hideWithAnimation()
        }
    }

    private fun showAllHotspotTag() {
        listHotspot.forEach {
            it.hotspotTagView?.showWithAnimation()
        }
    }

    private fun setHotspot(
        listHotspot: List<ImageHotspotData.HotspotData>,
        listenerBubbleView: Listener
    ) {
        this.listHotspot = listHotspot
        imageBanner.doOnLayout {
            listHotspot.forEachIndexed { index, hotspotData ->
                val hotspotTagView = createHotspotTagView(hotspotData)
                val bubbleView =
                    createBubbleView(hotspotData, hotspotTagView, index, listenerBubbleView)
                addView(hotspotTagView)
                addView(bubbleView)
            }
        }
    }

    private fun createBubbleView(
        hotspotData: ImageHotspotData.HotspotData,
        hotspotTagView: HotspotTagView,
        index: Int,
        listenerBubbleView: Listener
    ): HotspotBubbleView {
        val hotspotBubbleView = HotspotBubbleView(context)
        hotspotBubbleView.bindData(
            hotspotData,
            imageBanner.width,
            imageBanner.height,
            hotspotTagView,
            listenerBubbleView,
            index
        )
        hotspotData.bubbleView = hotspotBubbleView
        return hotspotBubbleView
    }

    private fun createHotspotTagView(hotspotData: ImageHotspotData.HotspotData): HotspotTagView {
        val hotspotTagView = HotspotTagView(context)
        hotspotTagView.bindData(hotspotData, imageBanner.width, imageBanner.height, this)
        hotspotData.hotspotTagView = hotspotTagView
        return hotspotTagView
    }

    private fun setImageBanner(imageBannerUrl: String, cornerRadius: Int) {
        imageBanner.apply {
            loadImage(imageBannerUrl)
            this.cornerRadius = cornerRadius
        }
    }

    override fun onHotspotTagClicked(
        hotspotData: ImageHotspotData.HotspotData,
        view: View
    ) {
        listHotspot.onEach {
            if (it.bubbleView != hotspotData.bubbleView) {
                it.bubbleView?.hideWithAnimation()
            }
        }
        if (hotspotData.bubbleView?.isVisibleWithAlpha() == true) {
            hotspotData.bubbleView?.hideWithAnimation()
        } else {
            hotspotData.bubbleView?.showWithAnimation()
        }
    }

    private fun hideAllBubbleView() {
        listHotspot.forEach {
//            it.bubbleView?.hideWithAlpha()
            it.bubbleView?.hideWithAnimation()
        }
    }

}
