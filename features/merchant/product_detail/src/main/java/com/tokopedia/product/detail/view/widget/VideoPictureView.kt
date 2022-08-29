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
    private var animator: ThumbnailAnimator? = null
    private var binding: WidgetVideoPictureBinding =
        WidgetVideoPictureBinding.inflate(LayoutInflater.from(context))
    private var lastPosition = 0
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
            animator?.animateShow()
        }
    }

    fun setup(
        media: List<MediaDataModel>,
        listener: DynamicProductDetailListener?,
        componentTrackDataModel: ComponentTrackDataModel?,
        initialScrollPosition: Int,
        shouldAnimateLabel: Boolean,
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
            setupThumbnailRv(media)
        }

        updateInitialThumbnail(media)
        updateImages(media)
        updateMediaLabel(lastPosition, shouldAnimateLabel)
        scrollToPosition(initialScrollPosition)
        renderVideoOnceAtPosition(initialScrollPosition)
    }

    private fun updateInitialThumbnail(media: List<MediaDataModel>) {
        val mediaList = processMedia(media)

        if (hideThumbnail(mediaList)) {
            return
        }

        thumbnailAdapter?.submitList(mediaList.mapIndexed { index, data ->
            val isSelected = lastPosition == index
            ThumbnailDataModel(data, isSelected)
        })
    }

    override fun onThumbnailClicked(element: ThumbnailDataModel) {
        val selectedPosition = thumbnailAdapter?.currentList?.indexOfFirst {
            it.media.id == element.media.id
        } ?: Int.ZERO

        updateThumbnail(selectedPosition)
        scrollToPosition(selectedPosition, true)
        renderVideoOnceAtPosition(selectedPosition)
    }

    private fun updateImages(listOfImage: List<MediaDataModel>?) {
        val mediaList = processMedia(listOfImage)
        videoPictureAdapter?.submitList(mediaList)
    }

    private fun updateThumbnail(selectedPosition: Int) {
        showThumbnail()
        thumbnailAdapter?.submitList(
            thumbnailAdapter?.currentList?.toMutableList()?.mapIndexed { index, data ->
                val isSelected = selectedPosition == index
                data.copy(isSelected = isSelected)
            } ?: listOf())

        binding.pdpMainThumbnailRv.addOneTimeGlobalLayoutListener {
            smoothScroller.scrollThumbnail(selectedPosition)
        }
    }

    fun scrollToPosition(position: Int, smoothScroll: Boolean = false) {
        if (position == RecyclerView.NO_POSITION || lastPosition == position) {
            return
        }
        lastPosition = position
        binding.pdpViewPager.setCurrentItem(position, smoothScroll)
        updateMediaLabel(position)
        updateThumbnail(position)
    }

    private fun setupThumbnailRv(media: List<MediaDataModel>) {
        binding.pdpMainThumbnailRv.layoutParams.height = 0

        if (hideThumbnail(media)) {
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
        animator = ThumbnailAnimator(binding.pdpMainThumbnailRv)
    }

    private fun hideThumbnail(media: List<MediaDataModel>): Boolean {
        if (media.size < MIN_MEDIA_TO_SHOW_THUMBNAIL
            || mListener?.showThumbnailImage() == false
        ) {
            thumbnailAdapter = null
            animator = null
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
            mListener,
            componentTrackDataModel
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
                if (lastPosition != position) {
                    videoPictureAdapter?.currentList?.getOrNull(position)?.run {
                        mListener?.onSwipePicture(
                            type,
                            if (isVideoType()) videoUrl else urlOriginal,
                            position + 1,
                            componentTrackDataModel
                        )
                    }
                    updateMediaLabel(position)
                    updateThumbnail(position)
                    lastPosition = position
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == RecyclerView.SCROLL_STATE_IDLE) {
                    mListener?.getProductVideoCoordinator()
                        ?.onScrollChangedListener(binding.pdpViewPager, lastPosition)
                }
            }
        })
    }

    private fun processMedia(media: List<MediaDataModel>?): List<MediaDataModel> {
        return if (media == null || media.isEmpty()) {
            val resId = R.drawable.product_no_photo_default
            val res = context.resources
            val uriNoPhoto = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://" + res.getResourcePackageName(resId)
                        + '/'.toString() + res.getResourceTypeName(resId)
                        + '/'.toString() + res.getResourceEntryName(resId)
            )
            mutableListOf(MediaDataModel(urlOriginal = uriNoPhoto.toString()))
        } else
            media.toMutableList()
    }

    private fun updateMediaLabel(
        position: Int,
        shouldAnimateLabel: Boolean = false
    ) {
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

        binding.txtAnimLabel.showView(stringLabel, shouldAnimateLabel)
    }

    companion object {
        private const val VIDEO_PICTURE_PAGE_LIMIT = 3
        private const val HIDE_LABEL_IMAGE_COUNT_MIN = 1
        private const val MIN_MEDIA_TO_SHOW_THUMBNAIL = 4
    }
}