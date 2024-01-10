package com.tokopedia.product.detail.view.widget

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.extensions.updateLayoutParams
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.MediaContainerType
import com.tokopedia.product.detail.data.model.datamodel.MediaDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMediaRecomData
import com.tokopedia.product.detail.databinding.WidgetVideoPictureBinding
import com.tokopedia.product.detail.view.adapter.VideoPictureAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

/**
 * Created by Yehezkiel on 23/11/20
 */
@SuppressLint("WrongConstant")
class VideoPictureView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var componentTrackDataModel: ComponentTrackDataModel? = null
    private var mListener: DynamicProductDetailListener? = null
    private var videoPictureAdapter: VideoPictureAdapter? = null
    private var binding: WidgetVideoPictureBinding =
        WidgetVideoPictureBinding.inflate(LayoutInflater.from(context))
    private var pagerSelectedLastPosition = 0
    private var previouslyPrefetch = false

    init {
        addView(binding.root)
        binding.pdpViewPager.offscreenPageLimit = VIDEO_PICTURE_PAGE_LIMIT
        setupViewPagerCallback()
        setupViewPager()
    }

    fun setup(
        media: List<MediaDataModel>,
        listener: DynamicProductDetailListener?,
        componentTrackDataModel: ComponentTrackDataModel?,
        initialScrollPosition: Int,
        containerType: MediaContainerType,
        recommendation: ProductMediaRecomData,
        isPrefetch: Boolean
    ) {
        this.mListener = listener
        this.componentTrackDataModel = componentTrackDataModel

        if (videoPictureAdapter == null || previouslyPrefetch) {
            binding.pdpViewPager.updateLayoutParams<ConstraintLayout.LayoutParams> {
                if (this != null) {
                    dimensionRatio = containerType.ratio
                }
            }
        }

        videoPictureAdapter?.containerType = containerType
        updateImages(listOfImage = media, previouslyPrefetch = previouslyPrefetch)
        updateMediaLabel(position = pagerSelectedLastPosition)
        setupRecommendationLabel(recommendation = recommendation)
        setupRecommendationLabelListener(position = pagerSelectedLastPosition)
        shouldShowRecommendationLabel(position = pagerSelectedLastPosition)
        renderVideoOnceAtPosition(position = initialScrollPosition)

        previouslyPrefetch = isPrefetch
    }

    private fun updateImages(listOfImage: List<MediaDataModel>?, previouslyPrefetch: Boolean) {
        val mediaList = processMedia(listOfImage)
        videoPictureAdapter?.submitList(mediaList, previouslyPrefetch)
    }

    fun scrollToPosition(position: Int, smoothScroll: Boolean = false) {
        if (position == RecyclerView.NO_POSITION || pagerSelectedLastPosition == position) {
            return
        }
        pagerSelectedLastPosition = position
        binding.pdpViewPager.setCurrentItem(position, smoothScroll)
        updateMediaLabel(position)
        setupRecommendationLabelListener(position)
        shouldShowRecommendationLabel(position)
    }

    private fun setupViewPager(
    ) {
        val prefetchResource = if (previouslyPrefetch) getPreviousMediaResource() else null

        videoPictureAdapter = VideoPictureAdapter(
            listener = mListener,
            componentTrackDataModel = componentTrackDataModel
        ).apply {
            this.prefetchResource = prefetchResource
        }

        val viewPager = binding.pdpViewPager
        viewPager.adapter = videoPictureAdapter
        viewPager.setPageTransformer { _, _ ->
            // NO OP DONT DELETE THIS, DISABLE ITEM ANIMATOR
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

            updateMediaLabel(position)
            setupRecommendationLabelListener(position)
            shouldShowRecommendationLabel(position)
            pagerSelectedLastPosition = position
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

    private fun setupRecommendationLabel(recommendation: ProductMediaRecomData) {
        binding.txtAnimLabelRecommendation.setup(recommendation)
    }

    private fun setupRecommendationLabelListener(position: Int) {
        if (videoPictureAdapter?.isPicture(position) == true) {
            binding.txtAnimLabelRecommendation.setOnClickListener {
                mListener?.onShowProductMediaRecommendationClicked()
            }
        } else {
            binding.txtAnimLabelRecommendation.setOnClickListener(null)
        }
    }

    private fun shouldShowRecommendationLabel(position: Int) {
        if (videoPictureAdapter?.isPicture(position) == true) {
            binding.txtAnimLabelRecommendation.showView()
        } else {
            binding.txtAnimLabelRecommendation.hideView()
        }
    }

    companion object {
        private const val VIDEO_PICTURE_PAGE_LIMIT = 3
        private const val HIDE_LABEL_IMAGE_COUNT_MIN = 1
    }
}
