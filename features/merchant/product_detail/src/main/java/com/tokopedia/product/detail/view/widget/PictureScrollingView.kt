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
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.MediaDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMediaDataModel
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

    fun renderData(media: List<MediaDataModel>?, productMediaDataModel: ProductMediaDataModel, onPictureClickListener: ((Int) -> Unit)?, onSwipePictureListener: ((String, Int, ComponentTrackDataModel?) -> Unit), fragmentManager: FragmentManager,
                   componentTrackData: ComponentTrackDataModel? = null, onPictureClickTrackListener: ((ComponentTrackDataModel?) -> Unit)? = null,
                   lifecycle: Lifecycle) {
        setPageControl(media)
        if (!::pagerAdapter.isInitialized) {
            setPageControl(media)
            pdp_view_pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                var lastPosition = 0
                override fun onPageSelected(position: Int) {
                    val swipeDirection = if (lastPosition > position) SWIPE_LEFT_DIRECTION else SWIPE_RIGHT_DIRECTION
//                    imageSliderCounter?.setCurrentCounter(position + 1)
                    imageSliderPageControl?.setCurrentIndicator(position)
                    onSwipePictureListener.invoke(swipeDirection, position, componentTrackData)
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
            imageSliderCounter?.setView(1, mediaList.size)
            pagerAdapter = VideoPicturePagerAdapter(mediaList, onPictureClickListener, fragmentManager, componentTrackData
                    ?: ComponentTrackDataModel(), onPictureClickTrackListener, lifecycle)
            pdp_view_pager.adapter = pagerAdapter
            pdp_view_pager.setPageTransformer { page, position ->
                //NO OP DONT DELETE THIS, DISABLE ITEM ANIMATOR
            }
        }
    }

    //need to check a/b test
    fun showImageReview(shouldShow:Boolean, onReviewMediaClicked : () -> Unit) {
        imageFromUser?.shouldShowWithAction(false) {
            imageFromUser?.setOnClickListener {
                onReviewMediaClicked.invoke()
            }
            imageFromUser?.setCompoundDrawablesWithIntrinsicBounds(null, null, MethodChecker.getDrawable(context, R.drawable.ic_chevron_right_black_24dp), null)
        }
    }

    fun updateImage(listOfImage: List<MediaDataModel>?) {
        pagerAdapter.setData(listOfImage ?: listOf())
        resetViewPagerToFirstPosition(listOfImage?.size ?: 0)
    }

    private fun setPageControl(media: List<MediaDataModel>?) {
        imageSliderPageControl?.setIndicator(media?.size ?: 0)
        imageSliderPageControl?.activeColor = ContextCompat.getColor(context, R.color.Neutral_N700)
        imageSliderPageControl?.inactiveColor = ContextCompat.getColor(context, R.color.Neutral_N400)
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
        imageSliderCounter?.setView(1, countIndicator)
        pdp_view_pager.setCurrentItem(0, false)
    }

    companion object {
        private const val SWIPE_RIGHT_DIRECTION = "right"
        private const val SWIPE_LEFT_DIRECTION = "left"
    }
}