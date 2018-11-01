package com.tokopedia.flashsale.management.product.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.flashsale.management.R
import kotlinx.android.synthetic.main.widget_flash_sale_product_widget.view.*

/**
 * Created by hendry on 31/10/18.
 */
class FlashSaleProductWidget @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    var imageWidth:Int = 0
    var hideDetail:Boolean = true

    init{
        applyAttrs(attrs)
        LayoutInflater.from(context).inflate(R.layout.widget_flash_sale_product_widget,
                this, true)
        setData()
    }

    private fun applyAttrs(attrs: AttributeSet?){
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.FlashSaleProductWidget)
        try {
            imageWidth = styledAttributes.getDimensionPixelOffset(R.styleable.FlashSaleProductWidget_fspw_image_width, 0)
            hideDetail = styledAttributes.getBoolean(R.styleable.FlashSaleProductWidget_fspw_hide_detail, false)
        } finally {
            styledAttributes.recycle()
        }
    }

    fun setData(){
        //TODO set model here
        tvPercentOff.setText(context.getString(R.string.x_percent_off, "12.5"))
        ivProduct.setImageResource(R.drawable.ic_empty_box)
    }
}