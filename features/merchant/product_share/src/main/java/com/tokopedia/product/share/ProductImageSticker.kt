package com.tokopedia.product.share

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.tokopedia.product.share.ekstensions.layoutInflater
import com.tokopedia.product.share.R

class ProductImageSticker(private val context: Context,
                          private val bitmap: Bitmap,
                          private val productData: ProductData){

    fun buildBitmapImage(): Bitmap{
        val view = RelativeLayout(context)
        context.layoutInflater.inflate(R.layout.partial_product_manage_image_sticker, view, true)

        val imageView = view.findViewById<ImageView>(R.id.img_source)
        imageView.setImageBitmap(bitmap)

        val priceTextView = view.findViewById<TextView>(R.id.tv_price)
        priceTextView.text = productData.priceText

        val nameTextView = view.findViewById<TextView>(R.id.tv_name)
        nameTextView.text = productData.productName

        val shopLinkTextView = view.findViewById<TextView>(R.id.tv_shop_link)
        shopLinkTextView.text = productData.shopUrl

        val cashbackTextView = view.findViewById<TextView>(R.id.tv_cashback)
        cashbackTextView.text = productData.cashbacktext

        with(view){
            layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT)
            measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            layout(0, 0, measuredWidth, measuredHeight)
        }

        val processedbitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        view.draw(Canvas(processedbitmap))
        return processedbitmap
    }
}