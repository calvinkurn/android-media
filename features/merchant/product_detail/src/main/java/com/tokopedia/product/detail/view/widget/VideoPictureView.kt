package com.tokopedia.product.detail.view.widget

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.extensions.updateLayoutParams
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.MediaContainerType
import com.tokopedia.product.detail.data.model.datamodel.MediaDataModel
import com.tokopedia.product.detail.data.model.datamodel.ThumbnailDataModel
import com.tokopedia.product.detail.databinding.WidgetVideoPictureBinding
import com.tokopedia.product.detail.view.adapter.ProductMainThumbnailAdapter
import com.tokopedia.product.detail.view.adapter.ProductMainThumbnailListener
import com.tokopedia.product.detail.view.adapter.VideoPictureAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.ThumbnailSmoothScroller
import com.tokopedia.product.detail.view.util.animateCollapse
import com.tokopedia.product.detail.view.util.animateExpand

/**
 * Created by Yehezkiel on 23/11/20
 */
@SuppressLint("WrongConstant")
class VideoPictureView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ProductMainThumbnailListener {

    private var componentTrackDataModel: ComponentTrackDataModel? = null
    private var mListener: DynamicProductDetailListener? = null
    private var videoPictureAdapter: VideoPictureAdapter? = null
    private var thumbnailAdapter: ProductMainThumbnailAdapter? = null
    private var binding: WidgetVideoPictureBinding =
        WidgetVideoPictureBinding.inflate(LayoutInflater.from(context))
    private var pagerSelectedLastPosition = 0
    private var smoothScroller = ThumbnailSmoothScroller(
        context,
        binding.pdpMainThumbnailRv
    )

    init {
        addView(binding.root)
        binding.pdpViewPager.offscreenPageLimit = VIDEO_PICTURE_PAGE_LIMIT
    }

    private fun showThumbnail() {
        if (binding.pdpMainThumbnailRv.visibility == View.GONE) {
            binding.pdpMainThumbnailRv.animateExpand(duration = THUMBNAIL_ANIMATION_DURATION)
        }
    }

    private fun hideThumbnail() {
        if (binding.pdpMainThumbnailRv.visibility == View.VISIBLE) {
            binding.pdpMainThumbnailRv.animateCollapse(duration = THUMBNAIL_ANIMATION_DURATION)
        }
    }

    fun setup(
        media: List<MediaDataModel>,
        listener: DynamicProductDetailListener?,
        componentTrackDataModel: ComponentTrackDataModel?,
        initialScrollPosition: Int,
        containerType: MediaContainerType
    ) {
        this.mListener = listener
        this.componentTrackDataModel = componentTrackDataModel

        if (videoPictureAdapter == null) {
            setupViewPagerCallback()
            setupViewPager(containerType = containerType)
            //If first position is video and selected: process the video
        }

        if (thumbnailAdapter == null) {
            setupThumbnailRv()
        }

        updateInitialThumbnail(media = media)
        updateImages(listOfImage = media)
        updateMediaLabel(position = pagerSelectedLastPosition)
        scrollToPosition(position = initialScrollPosition)
        renderVideoOnceAtPosition(position = initialScrollPosition)
    }

    private fun updateInitialThumbnail(media: List<MediaDataModel>) {
        if (isRollenceHideThumbnail()) {
            return
        }

        val mediaSelected = videoPictureAdapter?.currentList?.getOrNull(pagerSelectedLastPosition)
        val thumbList = getThumbnailVariantOnly(media = media)
            .mapIndexed { index, data ->
                val isMediaSame = data.id == mediaSelected?.id
                val thumbPosition = if (isMediaSame) index else -Int.ONE
                val isSelected = thumbPosition == index
                ThumbnailDataModel(data, isSelected)
            }
        thumbnailAdapter?.submitList(thumbList)
    }

    override fun onThumbnailClicked(element: ThumbnailDataModel) {
        val pagerSelectedPosition = videoPictureAdapter?.currentList?.indexOfFirst {
            it.id == element.media.id
        }.orZero()

        updateThumbnail(pagerSelectedPosition)
        scrollToPosition(pagerSelectedPosition, true)
        renderVideoOnceAtPosition(pagerSelectedPosition)
    }

    private fun updateImages(listOfImage: List<MediaDataModel>?) {
        val mediaList = processMedia(listOfImage)
        videoPictureAdapter?.submitList(mediaList)
    }

    private fun updateThumbnail(pagerPosition: Int) {
        val mediaSelected = videoPictureAdapter?.currentList?.getOrNull(pagerPosition)
        var thumbSelectedPosition = -Int.ZERO

        thumbnailAdapter?.currentList.orEmpty()
            .mapIndexed { index, data ->
                val isMediaSame = data.media.id == mediaSelected?.id
                val thumbPosition = if (isMediaSame) {
                    thumbSelectedPosition = index
                    index
                } else -Int.ONE
                val isSelected = thumbPosition == index

                data.copy(isSelected = isSelected)
            }.also {
                thumbnailAdapter?.submitList(it)
            }

        binding.pdpMainThumbnailRv.addOneTimeGlobalLayoutListener {
            if (thumbSelectedPosition >= Int.ZERO) {
                smoothScroller.scrollThumbnail(thumbSelectedPosition)
            }
        }

        thumbnailVisibilityState(mediaSelected = mediaSelected)
    }

    fun scrollToPosition(position: Int, smoothScroll: Boolean = false) {
        if (position == RecyclerView.NO_POSITION || pagerSelectedLastPosition == position) {
            return
        }
        pagerSelectedLastPosition = position
        binding.pdpViewPager.setCurrentItem(position, smoothScroll)
        updateMediaLabel(position)
        updateThumbnail(position)
    }

    private fun setupThumbnailRv() {
        binding.pdpMainThumbnailRv.layoutParams.height = 0

        if (isRollenceHideThumbnail()) {
            return
        }
        thumbnailAdapter = ProductMainThumbnailAdapter(
            this,
            mListener,
            componentTrackDataModel
        )

        binding.pdpMainThumbnailRv.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.pdpMainThumbnailRv.adapter = thumbnailAdapter
    }

    private fun isRollenceHideThumbnail(): Boolean {
        if (mListener?.showThumbnailImage() == false) {
            thumbnailAdapter = null
            binding.pdpMainThumbnailRv.layoutParams.height = 0
            binding.pdpMainThumbnailRv.hide()
            return true
        }

        return false
    }

    private fun setupViewPager(
        containerType: MediaContainerType
    ) {
        videoPictureAdapter = VideoPictureAdapter(
            listener = mListener,
            componentTrackDataModel = componentTrackDataModel,
            containerType = containerType
        )

        val viewPager = binding.pdpViewPager
        viewPager.adapter = videoPictureAdapter
        viewPager.setPageTransformer { _, _ ->
            //NO OP DONT DELETE THIS, DISABLE ITEM ANIMATOR
        }

        viewPager.updateLayoutParams<ConstraintLayout.LayoutParams> {
            if (this != null) {
                dimensionRatio = containerType.ratio
            }
        }
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
                    componentTrackDataModel = componentTrackDataModel
                )
            }

            updateMediaLabel(position)
            updateThumbnail(position)
            pagerSelectedLastPosition = position
        }
    }

    private fun thumbnailVisibilityState(mediaSelected: MediaDataModel?) {
        if (!isRollenceHideThumbnail()) {
            if (mediaSelected?.variantOptionId.toIntOrZero() > Int.ZERO) {
                showThumbnail()
            } else {
                hideThumbnail()
            }
        }
    }

    private fun processMedia(media: List<MediaDataModel>?): List<MediaDataModel> {
        return if (media.isNullOrEmpty()) {
            val resId = R.drawable.product_no_photo_default
            val res = context.resources
            val uriNoPhoto = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + res.getResourcePackageName(resId)
                    + '/'.toString() + res.getResourceTypeName(resId)
                    + '/'.toString() + res.getResourceEntryName(resId)
            )
            listOf(MediaDataModel(urlOriginal = uriNoPhoto.toString()))
        } else media
    }

    private fun getThumbnailVariantOnly(media: List<MediaDataModel>?): List<MediaDataModel> {
        return processMedia(media = media)
            .filter {
                it.variantOptionId.toIntOrZero() > Int.ZERO
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

        val ignoreUpdateLabel = position == RecyclerView.NO_POSITION
            || binding.txtAnimLabel.getCurrentText() == stringLabel
        if (ignoreUpdateLabel) return

        binding.txtAnimLabel.showView(stringLabel)
    }

    companion object {
        private const val VIDEO_PICTURE_PAGE_LIMIT = 3
        private const val HIDE_LABEL_IMAGE_COUNT_MIN = 1
        private const val THUMBNAIL_ANIMATION_DURATION = 300L
    }
}
