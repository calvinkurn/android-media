package com.tokopedia.product.detail.view.widget

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.MediaDataModel
import com.tokopedia.product.detail.view.adapter.VideoPicturePagerAdapter
import com.tokopedia.product.detail.view.fragment.VideoPictureFragment
import kotlinx.android.synthetic.main.widget_picture_scrolling.view.*

class PictureScrollingView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    lateinit var pagerAdapter: VideoPicturePagerAdapter

    val position: Int
        get() = pdp_view_pager?.currentItem ?: 0
    var shouldRenderViewPager: Boolean = true

    init {
        View.inflate(context, R.layout.widget_picture_scrolling, this)
        pdp_view_pager.offscreenPageLimit = 2
    }

    fun stopVideo() {
        (pagerAdapter.getRegisteredFragment(position) as? VideoPictureFragment)?.pauseVideo()
    }

    fun renderData(media: List<MediaDataModel>?, onPictureClickListener: ((Int) -> Unit)?, onSwipePictureListener: ((String, String, Int, ComponentTrackDataModel?) -> Unit), fragmentManager: FragmentManager,
                   componentTrackData: ComponentTrackDataModel? = null, onPictureClickTrackListener: ((ComponentTrackDataModel?) -> Unit)? = null,
                   lifecycle: Lifecycle) {
        if (!::pagerAdapter.isInitialized) {
            setPageControl(media)
            pdp_view_pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                var lastPosition = 0
                override fun onPageSelected(position: Int) {
                    imageSliderPageControl?.setCurrentIndicator(position)
                    pagerAdapter.media[position].run {
                        if (position != lastPosition) {
                            onSwipePictureListener.invoke(type, urlOriginal, position, componentTrackData)
                        }
                    }
                    (pagerAdapter.getRegisteredFragment(lastPosition) as? VideoPictureFragment)?.imInvisible()
                    (pagerAdapter.getRegisteredFragment(position) as? VideoPictureFragment)?.imVisible()
                    lastPosition = position
                }

                override fun onPageScrollStateChanged(b: Int) {}
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            })
        }

        if (!::pagerAdapter.isInitialized || shouldRenderViewPager) {
            val mediaList = processMedia(media)
            pagerAdapter = VideoPicturePagerAdapter(mediaList, onPictureClickListener, fragmentManager, componentTrackData
                    ?: ComponentTrackDataModel(), onPictureClickTrackListener, lifecycle)
            pdp_view_pager.adapter = pagerAdapter
            pdp_view_pager.setPageTransformer { page, position ->
                //NO OP DONT DELETE THIS, DISABLE ITEM ANIMATOR
            }
        }
    }

    fun updateImage(listOfImage: List<MediaDataModel>?) {
        pagerAdapter.setData(listOfImage ?: listOf())
        resetViewPagerToFirstPosition(listOfImage?.size ?: 0)
    }

    private fun setPageControl(media: List<MediaDataModel>?) {
        imageSliderPageControl?.setIndicator(media?.size ?: 0)
        imageSliderPageControl?.activeColor = ContextCompat.getColor(context, R.color.product_detail_dms_page_control)
        imageSliderPageControl?.inactiveColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N400_68)
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

    private fun resetViewPagerToFirstPosition(countIndicator: Int) {
        imageSliderPageControl?.setIndicator(countIndicator)
        pdp_view_pager.setCurrentItem(0, false)
    }
}