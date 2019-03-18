package com.tokopedia.product.detail.view.widget

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.Picture
import com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef
import com.tokopedia.product.detail.data.model.shop.ShopInfo
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.adapter.PicturePagerAdapter
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

    fun renderData(pictures: List<Picture>?, onPictureClickListener: ((Int) -> Unit)?) {
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

        val pagerAdapter = PicturePagerAdapter(context, photoList, urlTemp, onPictureClickListener)
        view_pager.adapter = pagerAdapter
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