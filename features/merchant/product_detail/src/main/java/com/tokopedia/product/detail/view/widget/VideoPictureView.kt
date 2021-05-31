package com.tokopedia.product.detail.view.widget

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.MediaDataModel
import com.tokopedia.product.detail.view.adapter.VideoPictureAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.widget_video_picture.view.*

/**
 * Created by Yehezkiel on 23/11/20
 */
class VideoPictureView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var shouldRenderViewPager: Boolean = true
    private var componentTrackDataModel: ComponentTrackDataModel? = null
    private var mListener: DynamicProductDetailListener? = null
    private var videoPictureAdapter: VideoPictureAdapter? = null

    init {
        View.inflate(context, R.layout.widget_video_picture, this)
        pdp_view_pager.offscreenPageLimit = VIDEO_PICTURE_PAGE_LIMIT
    }

    fun setup(media: List<MediaDataModel>, listener: DynamicProductDetailListener?,
              componentTrackDataModel: ComponentTrackDataModel?) {
        this.mListener = listener
        this.componentTrackDataModel = componentTrackDataModel

        if (videoPictureAdapter == null) {
            setupViewPagerCallback()
        }

        if (videoPictureAdapter == null || shouldRenderViewPager) {
            //Media changed, so reset video
            listener?.getProductVideoCoordinator()?.onDestroy()
            setupViewPager(media)
            renderVideoAtFirstPosition()
            setPageControl(media)
        }
    }

    fun updateImage(listOfImage: List<MediaDataModel>?, listener: DynamicProductDetailListener?) {
        val shouldNotifyItemInserted = videoPictureAdapter?.mediaData?.size != listOfImage?.size
        videoPictureAdapter?.mediaData = listOfImage ?: listOf()

        if (shouldNotifyItemInserted) {
            videoPictureAdapter?.notifyItemInserted(0)
            imageSliderPageControl?.setIndicator(listOfImage?.size ?: 0)
        } else {
            videoPictureAdapter?.notifyItemChanged(0)
        }

        resetViewPagerToFirstPosition()
        listener?.getProductVideoCoordinator()?.pauseVideoAndSaveLastPosition()
    }

    private fun setupViewPager(media: List<MediaDataModel>) {
        val mediaList = processMedia(media)
        videoPictureAdapter = VideoPictureAdapter(mListener?.getProductVideoCoordinator(), mListener, componentTrackDataModel)

        pdp_view_pager?.adapter = videoPictureAdapter
        videoPictureAdapter?.mediaData = mediaList
        pdp_view_pager.setPageTransformer { _, _ ->
            //NO OP DONT DELETE THIS, DISABLE ITEM ANIMATOR
        }
    }

    private fun renderVideoAtFirstPosition() {
        pdp_view_pager?.addOneTimeGlobalLayoutListener {
            pdp_view_pager?.let {
                mListener?.getProductVideoCoordinator()?.onScrollChangedListener(it, 0)
            }
        }
    }

    private fun setupViewPagerCallback() {
        pdp_view_pager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            var lastPosition = 0
            override fun onPageSelected(position: Int) {
                if (lastPosition != position) {
                    videoPictureAdapter?.mediaData?.getOrNull(position)?.run {
                        mListener?.onSwipePicture(type, if (isVideoType()) videoUrl else urlOriginal, position + 1, componentTrackDataModel)
                    }
                    imageSliderPageControl?.setCurrentIndicator(position)
                    lastPosition = position
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == RecyclerView.SCROLL_STATE_IDLE) {
                    mListener?.getProductVideoCoordinator()?.onScrollChangedListener(pdp_view_pager, lastPosition)
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        })
    }

    private fun setPageControl(media: List<MediaDataModel>?) {
        imageSliderPageControl?.setIndicator(media?.size ?: 0)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            imageSliderPageControl?.activeColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700)
        } else {
            imageSliderPageControl?.activeColor = ContextCompat.getColor(context, R.color.product_detail_dms_page_control)
        }
        imageSliderPageControl?.inactiveColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N400_68)
    }

    private fun resetViewPagerToFirstPosition() {
        pdp_view_pager?.postDelayed({
            pdp_view_pager?.setCurrentItem(0, false)
        }, 100)
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