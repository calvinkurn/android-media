package com.tokopedia.product.detail.view.widget

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef
import com.tokopedia.product.detail.common.data.model.product.Picture
import com.tokopedia.product.detail.view.adapter.VideoPicturePagerAdapter
import com.tokopedia.product.detail.view.fragment.VideoPictureFragment
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import kotlinx.android.synthetic.main.widget_picture_scrolling.view.*

class PictureScrollingView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var urlTemp = ""
    val position: Int
        get() = view_pager?.currentItem ?: 0

    init {
        instantiateView()
    }

    private fun instantiateView() {
        View.inflate(context, R.layout.widget_picture_scrolling, this)
    }

    fun renderData(pictures: List<Picture>?, onPictureClickListener: ((Int) -> Unit)?, fragmentManager: FragmentManager) {
        val photoList = if (pictures== null || pictures.isEmpty()) {
            val resId = R.drawable.product_no_photo_default
            val res = context.resources
            val uriNoPhoto = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + res.getResourcePackageName(resId)
                    + '/'.toString() + res.getResourceTypeName(resId)
                    + '/'.toString() + res.getResourceEntryName(resId))
            mutableListOf(Picture(urlOriginal = uriNoPhoto.toString()))
        } else
            pictures.toMutableList()


        val urlMedia: MutableList<Pair<String, String>> = mutableListOf(
                Pair("https://pmdvod.nationalgeographic.com/NG_Video/205/467/1239312451613_1527008973501_1239317059527_mp4_video_1024x576_1632000_primary_audio_eng_3.mp4", "video"),
                Pair("https://pmdvod.nationalgeographic.com/NG_Video_DEV/986/87/deadliest-eagles-vs.mp4", "video"),
                Pair("https://ecs7.tokopedia.net/img/cache/700/product-1/2018/8/16/19829070/19829070_19948cbd-bd22-4edb-a553-d226f08e659a_960_1280.jpeg", "picture"),
                Pair("https://pmdvod.nationalgeographic.com/NG_Video/821/547/0705687ANG_0.mp4", "video"),
                Pair("https://ecs7.tokopedia.net/img/cache/700/product-1/2018/8/16/19829070/19829070_19948cbd-bd22-4edb-a553-d226f08e659a_960_1280.jpeg", "picture")
        )

        val pagerAdapter = VideoPicturePagerAdapter(context, urlMedia, urlTemp, onPictureClickListener, fragmentManager)
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

    fun renderShopStatus(shopInfo: ShopInfo, productStatus: String, productStatusTitle: String = "",
                         productStatusMessage: String = "") {
        if (shopInfo.statusInfo.shopStatus != SHOP_STATUS_ACTIVE) {
            error_product_container.visible()
            error_product_title.text = MethodChecker.fromHtml(shopInfo.statusInfo.statusTitle)
            error_product_descr.text = MethodChecker.fromHtml(shopInfo.statusInfo.statusMessage)
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