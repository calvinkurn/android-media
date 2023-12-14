package com.tokopedia.shop.common.view.customview.bannerhotspot

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.DefaultLifecycleObserver
import com.tokopedia.feedcomponent.util.util.doOnLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.common.util.ShopUtilExt.isViewRectVisibleOnScreenArea
import com.tokopedia.shop.common.view.model.ImageHotspotData
import com.tokopedia.shop.databinding.ImageHotspotViewBinding
import com.tokopedia.unifycomponents.ImageUnify

class ImageHotspotView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr), DefaultLifecycleObserver,
    HotspotTagView.Listener {

    companion object {
        private const val DEFAULT_CORNER_RADIUS = 12
        private const val DEFAULT_RATIO = "1:1"
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
    private val showIntroAnimationListener = object : ViewTreeObserver.OnScrollChangedListener {
        override fun onScrollChanged() {
            if (isViewRectVisibleOnScreenArea()) {
                listHotspot.forEach {
                    it.hotspotTagView?.showIntroAnimation()
                }
                viewTreeObserver.removeOnScrollChangedListener(this)
            }
        }
    }

    init {
        viewBinding = ImageHotspotViewBinding.inflate(LayoutInflater.from(context), this)
        setOnClickListener {
            hideAllBubbleView()
        }
    }

    fun setData(
        imageHotspotData: ImageHotspotData,
        listenerBubbleView: Listener,
        cornerRadius: Int = DEFAULT_CORNER_RADIUS,
        ratio: String? = DEFAULT_RATIO,
        isShowIntroAnimation: Boolean = false,
    ) {
        setImageBanner(imageHotspotData.imageBannerUrl, cornerRadius, ratio)
        setHotspot(imageHotspotData.listHotspot, listenerBubbleView, isShowIntroAnimation)
        setOnImageShoppingBagClicked()
    }

    private fun setOnImageShoppingBagClicked() {
        imageShoppingBag.setOnClickListener { toggleAllHotspotTagsVisibility() }
    }

    private fun hideAllHotspotTag() {
        listHotspot.forEach {
            it.hotspotTagView?.hideWithAnimation()?.start()
        }
    }

    private fun showAllHotspotTag() {
        listHotspot.forEach {
            it.hotspotTagView?.showWithAnimation()?.start()
        }
    }

    private fun setHotspot(
        listHotspot: List<ImageHotspotData.HotspotData>,
        listenerBubbleView: Listener,
        isShowIntroAnimation: Boolean
    ) {
        clearDynamicView()
        this.listHotspot = listHotspot
        imageBanner.doOnLayout {
            post {
                addHotspotTagView(listHotspot, listenerBubbleView)
                addBubbleView(listHotspot)
                if(isShowIntroAnimation) {
                    showIntroAnimation()
                }
            }
        }
    }

    private fun clearDynamicView() {
        listHotspot.forEach {
            removeView(it.hotspotTagView)
            removeView(it.bubbleView)
        }
    }

    private fun addBubbleView(listHotspot: List<ImageHotspotData.HotspotData>) {
        listHotspot.forEach {
            addView(it.bubbleView)
        }
    }

    private fun addHotspotTagView(
        listHotspot: List<ImageHotspotData.HotspotData>,
        listenerBubbleView: Listener
    ) {
        listHotspot.forEachIndexed { index, hotspotData ->
            val hotspotTagView = createHotspotTagView(hotspotData)
            createBubbleView(hotspotData, hotspotTagView, index, listenerBubbleView)
            addView(hotspotTagView)
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

    private fun setImageBanner(imageBannerUrl: String, cornerRadius: Int, ratio: String?) {
        imageBanner.apply {
            (imageBanner.layoutParams as? LayoutParams)?.dimensionRatio = ratio.takeIf { !it.isNullOrEmpty() } ?: DEFAULT_RATIO
            loadImage(imageBannerUrl)
            this.cornerRadius = cornerRadius
            setOnClickListener { toggleAllHotspotTagsVisibility() }
        }
    }

    private fun toggleAllHotspotTagsVisibility() {
        hideAllBubbleView()
        if (!isAllHotspotTagViewVisible()) {
            showAllHotspotTag()
        } else {
            hideAllHotspotTag()
        }
    }

    private fun isAllHotspotTagViewVisible(): Boolean {
        return listHotspot.all { it.hotspotTagView?.isVisible == true }
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
            it.bubbleView?.hideWithAnimation()
        }
    }

    private fun showIntroAnimation() {
        viewTreeObserver.removeOnScrollChangedListener(showIntroAnimationListener)
        viewTreeObserver.addOnScrollChangedListener(showIntroAnimationListener)
    }

}
