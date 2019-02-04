package com.tokopedia.product.detail.view.widget

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.product.Picture
import com.tokopedia.product.detail.data.model.product.ProductParams
import com.tokopedia.product.detail.view.adapter.PicturePagerAdapter
import kotlinx.android.synthetic.main.widget_picture_scrolling.view.*

class PictureScrollingView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr){
    private var urlTemp = ""
    init {
        instatiateView()
    }

    private fun instatiateView() {
        View.inflate(context, R.layout.widget_picture_scrolling, this)
    }

    fun renderDataTemp(productParams: ProductParams){
        val pagerAdapter = PicturePagerAdapter(context,
                mutableListOf(Picture(urlOriginal = productParams.productImage!!)))
        view_pager.adapter = pagerAdapter
        indicator_picture.setViewPager(view_pager)
        indicator_picture.notifyDataSetChanged()
        urlTemp = productParams.productImage!!
    }

    fun renderData(pictures: List<Picture>) {
        val photoList = if (pictures.isEmpty()){
            val resId = R.drawable.product_no_photo_default
            val res = context.resources
            val uriNoPhoto = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + res.getResourcePackageName(resId)
                    + '/'.toString() + res.getResourceTypeName(resId)
                    + '/'.toString() + res.getResourceEntryName(resId))
            mutableListOf(Picture(urlOriginal = uriNoPhoto.toString()))
        } else
            pictures.toMutableList()

        val pagerAdapter = PicturePagerAdapter(context, photoList, urlTemp)
        view_pager.adapter = pagerAdapter
        indicator_picture.setViewPager(view_pager)
        indicator_picture.notifyDataSetChanged()
    }
}