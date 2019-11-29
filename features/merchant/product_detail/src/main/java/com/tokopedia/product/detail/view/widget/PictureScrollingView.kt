package com.tokopedia.product.detail.view.widget

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef
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
        get() = view_pager?.currentItem ?: 0

    init {
        instantiateView()
    }

    fun stopVideo() {
        (pagerAdapter.getRegisteredFragment(position) as? VideoPictureFragment)?.pauseVideo()
    }

    private fun instantiateView() {
        View.inflate(context, R.layout.widget_picture_scrolling, this)
    }

    fun renderData(media: List<ProductMediaDataModel>?, onPictureClickListener: ((Int) -> Unit)?, fragmentManager: FragmentManager,
                   forceRefresh: Boolean = true) {
        val mediaList = if (media == null || media.isEmpty()) {
            val resId = R.drawable.product_no_photo_default
            val res = context.resources
            val uriNoPhoto = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + res.getResourcePackageName(resId)
                    + '/'.toString() + res.getResourceTypeName(resId)
                    + '/'.toString() + res.getResourceEntryName(resId))
            mutableListOf(ProductMediaDataModel(urlOriginal = uriNoPhoto.toString()))
        } else
            media.toMutableList()

        if (!::pagerAdapter.isInitialized || forceRefresh) {
            pagerAdapter = VideoPicturePagerAdapter(context, mediaList, onPictureClickListener, fragmentManager)
            view_pager.adapter = pagerAdapter

            view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                var lastPosition = 0
                override fun onPageSelected(position: Int) {
                    (pagerAdapter.getRegisteredFragment(lastPosition) as? VideoPictureFragment)?.imInvisible()
                    (pagerAdapter.getRegisteredFragment(position) as? VideoPictureFragment)?.imVisible()
                    lastPosition = position
                }

                override fun onPageScrollStateChanged(b: Int) {

                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }
            })

            indicator_picture.setViewPager(view_pager)
            indicator_picture.notifyDataSetChanged()
        }
    }

    fun renderShopStatus(shopInfo: ShopInfo.StatusInfo, productStatus: String, productStatusTitle: String = "",
                         productStatusMessage: String = "") {
        if (shopInfo.shopStatus != SHOP_STATUS_ACTIVE) {
            error_product_container.visible()
            error_product_title.text = MethodChecker.fromHtml(shopInfo.statusTitle)
            error_product_descr.text = MethodChecker.fromHtml(shopInfo.statusMessage)
        } else if (productStatus != ProductStatusTypeDef.ACTIVE) {
            // TODO ASK PRODUCT STATUS DETAIL
            error_product_container.visible()
            error_product_title.text = productStatusTitle
            error_product_descr.text = productStatusMessage
        } else {
            error_product_container.gone()
        }
    }

    companion object {
        private const val SHOP_STATUS_ACTIVE = 1
    }

}