package com.tokopedia.product.detail.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.MediaDataModel
import com.tokopedia.product.detail.view.adapter.VideoPictureAdapter
import kotlinx.android.synthetic.main.widget_picture_scrolling.view.*

/**
 * Created by Yehezkiel on 23/11/20
 */
class VideoPictureView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var shouldRenderViewPager: Boolean = true
    private var videoPictureAdapter: VideoPictureAdapter? = null

    init {
        View.inflate(context, R.layout.widget_picture_scrolling, this)
        pdp_view_pager.offscreenPageLimit = 2
    }

    fun setup(media: List<MediaDataModel>, productVideoCoordinator: ProductVideoCoordinator) {
        if (videoPictureAdapter == null) {
            setupViewPagerCallback(productVideoCoordinator)
            setPageControl(media)
        }

        if (videoPictureAdapter == null || shouldRenderViewPager) {
            setupViewPager(media, productVideoCoordinator)
        }
    }

    fun updateImage(listOfImage: List<MediaDataModel>?, productVideoCoordinator: ProductVideoCoordinator?) {
        val shouldNotifyItemInserted = videoPictureAdapter?.mediaData?.size != listOfImage?.size
        videoPictureAdapter?.mediaData = listOfImage ?: listOf()

        if (shouldNotifyItemInserted) {
            videoPictureAdapter?.notifyItemInserted(0)
            imageSliderPageControl?.setIndicator(listOfImage?.size ?: 0)
        } else {
            videoPictureAdapter?.notifyItemChanged(0)
        }

        resetViewPagerToFirstPosition()
        productVideoCoordinator?.onScrollChangedListener(pdp_view_pager, 0)
        productVideoCoordinator?.onStop()
    }

    private fun setupViewPager(media: List<MediaDataModel>, productVideoCoordinator: ProductVideoCoordinator) {
        videoPictureAdapter = VideoPictureAdapter(productVideoCoordinator)
        pdp_view_pager?.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        pdp_view_pager?.adapter = videoPictureAdapter
        videoPictureAdapter?.mediaData = media

        (pdp_view_pager?.getChildAt(0) as RecyclerView).addOneTimeGlobalLayoutListener {
            productVideoCoordinator.onScrollChangedListener(pdp_view_pager, 0)
        }

        pdp_view_pager.setPageTransformer { _, _ ->
            //NO OP DONT DELETE THIS, DISABLE ITEM ANIMATOR
        }
    }

    private fun setupViewPagerCallback(productVideoCoordinator: ProductVideoCoordinator) {
        (pdp_view_pager.getChildAt(0) as RecyclerView)
        pdp_view_pager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            var lastPosition = 0
            override fun onPageSelected(position: Int) {
                imageSliderPageControl?.setCurrentIndicator(position)
                lastPosition = position
            }

            override fun onPageScrollStateChanged(b: Int) {
                if (b == RecyclerView.SCROLL_STATE_IDLE) {
                    productVideoCoordinator.onScrollChangedListener(pdp_view_pager, lastPosition)
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        })
    }

    private fun setPageControl(media: List<MediaDataModel>?) {
        imageSliderPageControl?.setIndicator(media?.size ?: 0)
        imageSliderPageControl?.activeColor = ContextCompat.getColor(context, R.color.product_detail_dms_page_control)
        imageSliderPageControl?.inactiveColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N400_68)
    }

    private fun resetViewPagerToFirstPosition() {
        pdp_view_pager?.postDelayed({
            pdp_view_pager?.setCurrentItem(0, false)
        }, 100)
    }
}