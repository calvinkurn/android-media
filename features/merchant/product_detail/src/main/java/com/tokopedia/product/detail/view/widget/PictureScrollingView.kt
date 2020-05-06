package com.tokopedia.product.detail.view.widget

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMediaDataModel
import com.tokopedia.product.detail.view.adapter.VideoPicturePagerAdapter
import com.tokopedia.product.detail.view.fragment.VideoPictureFragment
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import kotlinx.android.synthetic.main.widget_picture_scrolling.view.*

class PictureScrollingView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    lateinit var pagerAdapter: VideoPicturePagerAdapter

    val position: Int
        get() = pdp_view_pager?.currentItem ?: 0
    var shouldRenderViewPager: Boolean = true

    init {
        instantiateView()
        pdp_view_pager.offscreenPageLimit = 2
    }

    fun stopVideo() {
        (pagerAdapter.getRegisteredFragment(position) as? VideoPictureFragment)?.pauseVideo()
    }

    fun renderData(media: List<ProductMediaDataModel>?, onPictureClickListener: ((Int) -> Unit)?, onSwipePictureListener: ((String, Int, ComponentTrackDataModel?) -> Unit), fragmentManager: FragmentManager,
                   componentTrackData: ComponentTrackDataModel? = null, onPictureClickTrackListener: ((ComponentTrackDataModel?) -> Unit)? = null,
                   lifecycle: Lifecycle) {
        val mediaList = processMedia(media)

        if (!::pagerAdapter.isInitialized) {
            pdp_view_pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                var lastPosition = 0
                override fun onPageSelected(position: Int) {
                    pdp_page_control.setCurrentIndicator(position)
                    val swipeDirection = if (lastPosition > position) SWIPE_LEFT_DIRECTION else SWIPE_RIGHT_DIRECTION
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
            pdp_page_control.setIndicator(mediaList.size)
            pagerAdapter = VideoPicturePagerAdapter(mediaList, onPictureClickListener, fragmentManager, componentTrackData
                    ?: ComponentTrackDataModel(), onPictureClickTrackListener, lifecycle)
            pdp_view_pager.adapter = pagerAdapter
            pdp_view_pager.setPageTransformer { page, position ->
                //NO OP DONT DELETE THIS, DISABLE ITEM ANIMATOR
            }
        }
    }

    fun updateImage(listOfImage: List<ProductMediaDataModel>?) {
        pagerAdapter.setData(listOfImage ?: listOf())
        resetViewPagerToFirstPosition(listOfImage?.size ?: 0)
    }

    fun renderShopStatus(shopInfo: ShopInfo.StatusInfo, productStatus: String, productStatusTitle: String = "",
                         productStatusMessage: String = "") {
        when {
            shopInfo.shopStatus != SHOP_STATUS_ACTIVE -> {
                error_product_container.visible()
                error_product_title.text = MethodChecker.fromHtml(shopInfo.statusTitle)
                error_product_descr.text = MethodChecker.fromHtml(shopInfo.statusMessage)
            }
            productStatus != ProductStatusTypeDef.ACTIVE -> {
                error_product_container.visible()
                error_product_title.text = productStatusTitle
                error_product_descr.text = productStatusMessage
            }
            else -> {
                error_product_container.gone()
            }
        }
    }

    fun renderShopStatusDynamicPdp(shopStatus: Int, statusTitle: String, statusMessage: String, productStatus: String, productStatusTitle: String = "", productStatusMessage: String = "") {
        when {
            shopStatus != SHOP_STATUS_ACTIVE -> {
                error_product_container.visible()
                error_product_title.text = MethodChecker.fromHtml(statusTitle)
                error_product_descr.text = MethodChecker.fromHtml(statusMessage)
            }
            productStatus != ProductStatusTypeDef.ACTIVE -> {
                error_product_container.showWithCondition(productStatus != ProductStatusTypeDef.WAREHOUSE)
                error_product_title.text = productStatusTitle
                error_product_descr.text = productStatusMessage
            }
            else -> error_product_container.gone()
        }
    }

    private fun instantiateView() {
        View.inflate(context, R.layout.widget_picture_scrolling, this)
    }

    private fun processMedia(media: List<ProductMediaDataModel>?): List<ProductMediaDataModel> {
        return if (media == null || media.isEmpty()) {
            val resId = R.drawable.product_no_photo_default
            val res = context.resources
            val uriNoPhoto = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + res.getResourcePackageName(resId)
                    + '/'.toString() + res.getResourceTypeName(resId)
                    + '/'.toString() + res.getResourceEntryName(resId))
            mutableListOf(ProductMediaDataModel(urlOriginal = uriNoPhoto.toString()))
        } else
            media.toMutableList()
    }

    private fun resetViewPagerToFirstPosition(countIndicator: Int) {
        pdp_page_control.setIndicator(countIndicator)
        pdp_page_control.setCurrentIndicator(0)
        pdp_view_pager.setCurrentItem(0, false)
    }

    companion object {
        private const val SHOP_STATUS_ACTIVE = 1
        private const val SWIPE_RIGHT_DIRECTION = "right"
        private const val SWIPE_LEFT_DIRECTION = "left"
    }
}