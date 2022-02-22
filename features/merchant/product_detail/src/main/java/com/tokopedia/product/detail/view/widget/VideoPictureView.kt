package com.tokopedia.product.detail.view.widget

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.MediaDataModel
import com.tokopedia.product.detail.databinding.WidgetVideoPictureBinding
import com.tokopedia.product.detail.view.adapter.VideoPictureAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

/**
 * Created by Yehezkiel on 23/11/20
 */
class VideoPictureView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var componentTrackDataModel: ComponentTrackDataModel? = null
    private var mListener: DynamicProductDetailListener? = null
    private var videoPictureAdapter: VideoPictureAdapter? = null

    private var binding: WidgetVideoPictureBinding = WidgetVideoPictureBinding.inflate(LayoutInflater.from(context))
    private var lastPosition = 0

    init {
        addView(binding.root)
        binding.pdpViewPager.offscreenPageLimit = VIDEO_PICTURE_PAGE_LIMIT
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
            //If first position is video and selected: process the video
        }
        setPageControl(media, initialScrollPosition)
        updateImages(media)
        scrollToPosition(initialScrollPosition)
        renderVideoOnceAtPosition(initialScrollPosition)
    }

    private fun updateImages(listOfImage: List<MediaDataModel>?) {
        val mediaList = processMedia(listOfImage)
        videoPictureAdapter?.submitList(mediaList)
    }

    fun scrollToPosition(position: Int) {
        if (position == -1) {
            return
        }
        binding.pdpViewPager.setCurrentItem(position, false)
        binding.imageSliderPageControl.setCurrentIndicator(position)
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
                    lastPosition = position
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == RecyclerView.SCROLL_STATE_IDLE) {
                    mListener?.getProductVideoCoordinator()?.onScrollChangedListener(binding.pdpViewPager, lastPosition)
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
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
        binding.imageSliderPageControl.setCurrentIndicator(initialScrollPosition)
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

    companion object {
        private const val VIDEO_PICTURE_PAGE_LIMIT = 3
    }
}