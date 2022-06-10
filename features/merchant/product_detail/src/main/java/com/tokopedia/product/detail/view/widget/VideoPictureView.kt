package com.tokopedia.product.detail.view.widget

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
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
    private var binding: WidgetVideoPictureBinding = WidgetVideoPictureBinding.inflate(LayoutInflater.from(context))
    private var lastPosition = 0
    private var smoothScroller = ThumbnailSmoothScroller(
            context,
            binding.pdpMainThumbnailRv
    )

    init {
        addView(binding.root)
        binding.pdpViewPager.offscreenPageLimit = VIDEO_PICTURE_PAGE_LIMIT
        measureScreenHeight(binding)
    }

    private fun measureScreenHeight(binding: WidgetVideoPictureBinding) {
        val screenWidth = binding.root.resources.displayMetrics.widthPixels
        binding.viewPagerContainer.layoutParams.height = screenWidth
    }

    private fun showThumbnail() {
        if (binding.pdpMainThumbnailRv.visibility == View.GONE) {
            animator?.animateShow()
        }
    }

    fun setup(media: List<MediaDataModel>,
              listener: DynamicProductDetailListener?,
              initialScrollPosition: Int,
              componentTrackDataModel: ComponentTrackDataModel?) {
        this.mListener = listener
        this.componentTrackDataModel = componentTrackDataModel

        if (videoPictureAdapter == null) {
            setupViewPagerCallback()
            setupViewPager()
            setupThumbnailRv()
            //If first position is video and selected: process the video
        }
        setPageControl(media, initialScrollPosition)
        updateImages(media)
        updateMediaLabel(lastPosition)
        scrollToPosition(initialScrollPosition)
        renderVideoOnceAtPosition(initialScrollPosition)
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
        thumbnailAdapter?.submitList(mediaList.mapIndexed { index, data ->
            val isSelected = lastPosition == index
            ThumbnailDataModel(data, isSelected)
        })
        animator = ThumbnailAnimator(binding.pdpMainThumbnailRv)
    }

    private fun updateThumbnail(selectedPosition: Int) {
        showThumbnail()
        thumbnailAdapter?.submitList(thumbnailAdapter?.currentList?.toMutableList()?.mapIndexed { index, data ->
            val isSelected = selectedPosition == index
            data.copy(isSelected = isSelected)
        } ?: listOf())

        binding.pdpMainThumbnailRv.addOneTimeGlobalLayoutListener {
            smoothScroller.scrollThumbnail(selectedPosition)
        }
    }

    fun scrollToPosition(position: Int, smoothScroll: Boolean = false) {
        if (position == -1) {
            return
        }
        lastPosition = position
        binding.pdpViewPager.setCurrentItem(position, smoothScroll)
        binding.imageSliderPageControl.setCurrentIndicator(position)
        updateMediaLabel(position)
        updateThumbnail(position)
    }

    private fun setupThumbnailRv() {
        binding.pdpMainThumbnailRv.layoutParams.height = 0

        thumbnailAdapter = ProductMainThumbnailAdapter(
                this,
                mListener,
                componentTrackDataModel
        )

        binding.pdpMainThumbnailRv.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL,
                false)
        binding.pdpMainThumbnailRv.adapter = thumbnailAdapter
    }

    private fun setupViewPager() {
        videoPictureAdapter = VideoPictureAdapter(
                mListener,
                componentTrackDataModel)

        binding.pdpViewPager.adapter = videoPictureAdapter
        binding.pdpViewPager.setPageTransformer { _, _ ->
            //NO OP DONT DELETE THIS, DISABLE ITEM ANIMATOR
        }
    }

    private fun renderVideoOnceAtPosition(position: Int) {
        if (position == -1) {
            return
        }

        binding.pdpViewPager.addOneTimeGlobalLayoutListener {
            binding.pdpViewPager.let {
                mListener?.getProductVideoCoordinator()?.onScrollChangedListener(it, position)
            }
        }
    }

    private fun setupViewPagerCallback() {
        binding.pdpViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (lastPosition != position) {
                    videoPictureAdapter?.currentList?.getOrNull(position)?.run {
                        mListener?.onSwipePicture(type, if (isVideoType()) videoUrl else urlOriginal, position + 1, componentTrackDataModel)
                    }
                    binding.imageSliderPageControl.setCurrentIndicator(position)
                    updateMediaLabel(position)
                    updateThumbnail(position)
                    lastPosition = position
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == RecyclerView.SCROLL_STATE_IDLE) {
                    mListener?.getProductVideoCoordinator()?.onScrollChangedListener(binding.pdpViewPager, lastPosition)
                }
            }
        })
    }

    private fun setPageControl(media: List<MediaDataModel>?, initialScrollPosition: Int) {
        if (binding.imageSliderPageControl.indicatorCount == media?.size) {
            return
        }

        binding.imageSliderPageControl.setIndicator(media?.size ?: 0)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            binding.imageSliderPageControl.activeColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700)
        } else {
            binding.imageSliderPageControl.activeColor = ContextCompat.getColor(context, R.color.product_detail_dms_page_control)
        }
        binding.imageSliderPageControl.inactiveColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N400_68)
        binding.imageSliderPageControl.setCurrentIndicator(initialScrollPosition.takeIf { it > -1 }
                ?: 0)
    }

    private fun processMedia(media: List<MediaDataModel>?): List<MediaDataModel> {
        return if (media == null || media.isEmpty()) {
            val resId = R.drawable.product_no_photo_default
            val res = context.resources
            val uriNoPhoto = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + res.getResourcePackageName(resId)
                    + '/'.toString() + res.getResourceTypeName(resId)
                    + '/'.toString() + res.getResourceEntryName(resId))
            mutableListOf(MediaDataModel(urlOriginal = uriNoPhoto.toString()))
        } else
            media.toMutableList()
    }

    private fun updateMediaLabel(position: Int) {
        val mediaData = videoPictureAdapter?.currentList?.getOrNull(position)
        val variantName = mediaData?.mediaDescription ?: ""
        val totalMediaCount = videoPictureAdapter?.currentList?.size ?: 0
        val index = "${position + 1} / $totalMediaCount"

        val stringLabel = when {
            mediaData?.isVideoType() == true -> ""
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
    }
}