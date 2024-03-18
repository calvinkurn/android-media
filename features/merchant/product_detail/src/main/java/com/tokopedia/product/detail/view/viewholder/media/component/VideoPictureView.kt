package com.tokopedia.product.detail.view.viewholder.media.component

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View.OnClickListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.util.lazyBind
import com.tokopedia.play.widget.liveindicator.analytic.PlayWidgetLiveIndicatorAnalytic
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.extensions.addOnPdpImpressionListener
import com.tokopedia.product.detail.common.utils.extensions.updateLayoutParams
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.MediaContainerType
import com.tokopedia.product.detail.data.model.datamodel.MediaDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMediaRecomData
import com.tokopedia.product.detail.databinding.WidgetVideoPictureBinding
import com.tokopedia.product.detail.databinding.WidgetVideoPictureLabelAnimBinding
import com.tokopedia.product.detail.databinding.WidgetVideoPictureLiveIndicatorBinding
import com.tokopedia.product.detail.view.adapter.VideoPictureAdapter
import com.tokopedia.product.detail.view.listener.ProductDetailListener
import com.tokopedia.product.detail.view.viewholder.media.model.LiveIndicatorUiModel
import kotlin.time.Duration.Companion.seconds

/**
 * Created by Yehezkiel on 23/11/20
 */
@SuppressLint("WrongConstant")
class VideoPictureView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var componentTrackDataModel: ComponentTrackDataModel? = null
    private var mListener: ProductDetailListener? = null
    private var videoPictureAdapter: VideoPictureAdapter? = null
    private var binding: WidgetVideoPictureBinding =
        WidgetVideoPictureBinding.inflate(LayoutInflater.from(context), this, true)
    private var pagerSelectedLastPosition = 0
    private var previouslyPrefetch = false
    private val overlayRecommStub by binding.txtAnimLabelRecommendationStub
        .lazyBind<WidgetVideoPictureLabelAnimBinding>()
    private val liveIndicatorStub by binding.liveIndicatorStub
        .lazyBind<WidgetVideoPictureLiveIndicatorBinding>()

    // region uiModel
    private var liveIndicatorUiModel: LiveIndicatorUiModel = LiveIndicatorUiModel()
    private var overlayRecommUiModel: ProductMediaRecomData = ProductMediaRecomData()
    // endregion

    private val shouldLiveIndicatorShow
        get() = liveIndicatorUiModel.isLive

    private val shouldOverlayRecommShow
        get() = videoPictureAdapter?.isPicture(pagerSelectedLastPosition) == true &&
            overlayRecommUiModel.shouldShow() && !shouldLiveIndicatorShow

    init {
        binding.pdpViewPager.offscreenPageLimit = VIDEO_PICTURE_PAGE_LIMIT
    }

    fun setup(
        media: List<MediaDataModel>,
        listener: ProductDetailListener,
        componentTrackDataModel: ComponentTrackDataModel?,
        initialScrollPosition: Int,
        containerType: MediaContainerType,
        recommendation: ProductMediaRecomData,
        liveIndicator: LiveIndicatorUiModel,
        isPrefetch: Boolean
    ) {
        this.mListener = listener
        this.componentTrackDataModel = componentTrackDataModel
        this.liveIndicatorUiModel = liveIndicator
        this.overlayRecommUiModel = recommendation

        if (videoPictureAdapter == null || previouslyPrefetch) {
            setupViewPagerCallback()
            setupViewPager(containerType = containerType)
        }

        updateImages(
            listOfImage = media,
            previouslyPrefetch = previouslyPrefetch,
            isLive = liveIndicator.isLive
        )
        updateMediaLabel(position = pagerSelectedLastPosition)
        setupRecommendationLabel()
        setupLiveIndicator()
        shouldShowLiveIndicatorXOverlayRecomm()
        renderVideoOnceAtPosition(position = initialScrollPosition)

        previouslyPrefetch = isPrefetch
    }

    private fun updateImages(
        listOfImage: List<MediaDataModel>?,
        previouslyPrefetch: Boolean,
        isLive: Boolean
    ) {
        val mediaList = processMedia(listOfImage)
            .map { it.copy(isLive = isLive) }
        videoPictureAdapter?.submitList(mediaList, previouslyPrefetch)
    }

    fun scrollToPosition(position: Int, smoothScroll: Boolean = false) {
        if (position == RecyclerView.NO_POSITION || pagerSelectedLastPosition == position) {
            return
        }
        pagerSelectedLastPosition = position
        binding.pdpViewPager.setCurrentItem(position, smoothScroll)
        updateMediaLabel(position)
        setupRecommendationLabel()
        shouldShowLiveIndicatorXOverlayRecomm()
    }

    private fun setupViewPager(
        containerType: MediaContainerType
    ) {
        val prefetchResource = if (previouslyPrefetch) getPreviousMediaResource() else null

        videoPictureAdapter = VideoPictureAdapter(
            listener = mListener,
            componentTrackDataModel = componentTrackDataModel,
            containerType = containerType
        ).apply {
            this.prefetchResource = prefetchResource
        }

        val viewPager = binding.pdpViewPager
        viewPager.adapter = videoPictureAdapter
        viewPager.setPageTransformer { _, _ ->
            // NO OP DONT DELETE THIS, DISABLE ITEM ANIMATOR
        }

        viewPager.updateLayoutParams<LayoutParams> {
            if (this != null) {
                dimensionRatio = containerType.ratio
            }
        }
    }

    private fun getPreviousMediaResource(): Drawable? {
        return videoPictureAdapter?.currentList?.firstOrNull()?.prefetchResource
    }

    private fun renderVideoOnceAtPosition(position: Int) {
        val checkPosition = if (position == -1) 0 else position
        binding.pdpViewPager.addOneTimeGlobalLayoutListener {
            binding.pdpViewPager.let {
                mListener?.getProductVideoCoordinator()?.onScrollChangedListener(it, checkPosition)
            }
        }
    }

    private fun setupViewPagerCallback() {
        binding.pdpViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    onMediaPageSelected(position)
                }

                override fun onPageScrollStateChanged(state: Int) {
                    if (state == RecyclerView.SCROLL_STATE_IDLE) {
                        mListener?.getProductVideoCoordinator()
                            ?.onScrollChangedListener(binding.pdpViewPager, pagerSelectedLastPosition)
                    }
                }
            })
    }

    private fun onMediaPageSelected(position: Int) {
        if (pagerSelectedLastPosition != position) {
            val selected = videoPictureAdapter?.currentList?.getOrNull(position)

            if (selected != null) {
                val url = if (selected.isVideoType()) {
                    selected.videoUrl
                } else {
                    selected.urlOriginal
                }

                mListener?.onSwipePicture(
                    type = selected.type,
                    url = url,
                    position = position + Int.ONE,
                    variantOptionId = selected.variantOptionId,
                    componentTrackDataModel = componentTrackDataModel
                )
            }

            pagerSelectedLastPosition = position
            updateMediaLabel(position)
            setupRecommendationLabel()
            shouldShowLiveIndicatorXOverlayRecomm()
        }
    }

    private fun processMedia(media: List<MediaDataModel>?): List<MediaDataModel> {
        return if (media.isNullOrEmpty()) {
            val resId = R.drawable.product_no_photo_default
            val res = context.resources
            val uriNoPhoto = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + res.getResourcePackageName(resId) +
                    '/'.toString() + res.getResourceTypeName(resId) +
                    '/'.toString() + res.getResourceEntryName(resId)
            )
            listOf(MediaDataModel(urlOriginal = uriNoPhoto.toString()))
        } else {
            media
        }
    }

    private fun updateMediaLabel(position: Int) {
        val mediaData = videoPictureAdapter?.currentList?.getOrNull(position)
        val variantName = mediaData?.mediaDescription ?: ""
        val totalMediaCount = videoPictureAdapter?.currentList?.size ?: 0
        val index = "${position + Int.ONE}/$totalMediaCount"
        val totalImageCount = videoPictureAdapter?.currentList?.count {
            !it.isVideoType()
        } ?: 0

        if (mediaData?.isVideoType() == true || totalImageCount <= HIDE_LABEL_IMAGE_COUNT_MIN) {
            binding.txtAnimLabel.showView("")
            binding.txtAnimLabel.setEmptyText()
            return
        }

        val stringLabel = when {
            variantName.isEmpty() -> {
                index
            }

            else -> {
                context.getString(R.string.pdp_media_label_builder, index, variantName)
            }
        }

        val ignoreUpdateLabel = position == RecyclerView.NO_POSITION ||
            binding.txtAnimLabel.getCurrentText() == stringLabel
        if (ignoreUpdateLabel) return

        binding.txtAnimLabel.showView(stringLabel)
    }

    private fun setupRecommendationLabel() {
        if (!shouldOverlayRecommShow) return
        val listener = mListener ?: return

        with(overlayRecommStub.binding.txtAnimLabelRecommendation) {
            setup(overlayRecommUiModel)
            setOnClickListener {
                listener.onShowProductMediaRecommendationClicked(componentTrackDataModel)
            }

            addOnPdpImpressionListener(
                holders = listener.getImpressionHolders(),
                name = overlayRecommUiModel.javaClass.simpleName
            ) {
                listener.onProductMediaRecommendationImpressed(componentTrackDataModel)
            }
        }
    }

    private fun setupLiveIndicator() {
        if (!shouldLiveIndicatorShow) return

        setupLiveIndicatorEvent()
        setupLiveIndicatorAnalytic()
    }

    private fun setupLiveIndicatorAnalytic() = with(liveIndicatorStub.binding) {
        val p1 = mListener?.getProductInfo() ?: return
        val liveIndicatorAnalyticModel = PlayWidgetLiveIndicatorAnalytic.Model(
            channelId = liveIndicatorUiModel.channelID,
            productId = p1.basic.productID,
            shopId = p1.basic.shopID
        )
        liveBadgeView.setAnalyticModel(model = liveIndicatorAnalyticModel)
        liveThumbnailView.setAnalyticModel(model = liveIndicatorAnalyticModel)
    }

    private fun setupLiveIndicatorEvent() = with(liveIndicatorStub.binding) {
        val onClick = OnClickListener { mListener?.goToApplink(url = liveIndicatorUiModel.appLink) }
        liveBadgeView.setOnClickListener(onClick)
        liveThumbnailView.setOnClickListener(onClick)
        liveThumbnailView.playUrl(
            url = liveIndicatorUiModel.mediaUrl,
            playFor = CLIP_VIDEO_DURATION
        )
    }

    private fun shouldShowLiveIndicatorXOverlayRecomm() {
        when {
            shouldLiveIndicatorShow -> {
                liveIndicatorStub.show()
                overlayRecommStub.hide()
            }

            shouldOverlayRecommShow -> {
                overlayRecommStub.show {
                    overlayRecommStub.binding.txtAnimLabelRecommendation.showView()
                }
                liveIndicatorStub.hide()
            }

            else -> {
                liveIndicatorStub.hide()
                overlayRecommStub.hide {
                    overlayRecommStub.binding.txtAnimLabelRecommendation.hideView()
                }
            }
        }
    }

    companion object {
        private const val VIDEO_PICTURE_PAGE_LIMIT = 3
        private const val HIDE_LABEL_IMAGE_COUNT_MIN = 1
        private val CLIP_VIDEO_DURATION = 3.seconds
    }
}
