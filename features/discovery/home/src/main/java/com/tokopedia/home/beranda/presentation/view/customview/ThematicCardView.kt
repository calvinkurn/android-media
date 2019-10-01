package com.tokopedia.home.beranda.presentation.view.customview

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.home.R
import com.tokopedia.kotlin.extensions.view.displayTextOrHide

/**
 * Created by Lukas on 01/10/19
 */
class ThematicCardView : FrameLayout {
    private lateinit var view: View
    private val channelImage1: ImageView by lazy { view.findViewById<ImageView>(R.id.channel_image_1) }
    private val channelName: TextView by lazy { view.findViewById<TextView>(R.id.product_name) }
    private val channelPrice1: TextView by lazy { view.findViewById<TextView>(R.id.channel_price_1) }
    private val channelDiscount1: TextView by lazy { view.findViewById<TextView>(R.id.channel_discount_1) }
    private val channelCashback: TextView by lazy { view.findViewById<TextView>(R.id.channel_cashback) }
    private val channelBeforeDiscPrice1: TextView by lazy { view.findViewById<TextView>(R.id.channel_before_disc_price_1) }
    private val itemContainer1: RelativeLayout by lazy { view.findViewById<RelativeLayout>(R.id.channel_item_container_1) }

    constructor(context: Context): super(context){
        init()
    }
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        init()
    }
    constructor(context: Context, attributeSet: AttributeSet, defaultStyleAttr: Int): super(context, attributeSet, defaultStyleAttr){
        init()
    }

    private fun init(){
        view = inflate(context, R.layout.thematic_card_view, this)
    }

    fun setName(title: String) {
        channelName.displayTextOrHide(title)
    }

    fun setImageSrc(imageUrl: String){
        ImageHandler.loadImageThumbs(context, channelImage1, imageUrl)
    }

    fun setPrice(price: String){
        channelPrice1.displayTextOrHide(price)
    }

    fun setDiscount(discount: String){
        channelDiscount1.displayTextOrHide(discount)
    }

    fun setSlashedPrice(slashedPrice: String){
        channelBeforeDiscPrice1.displayTextOrHide(slashedPrice)
        channelBeforeDiscPrice1.paintFlags = channelBeforeDiscPrice1.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    fun setCashback(cashback: String){
        channelCashback.displayTextOrHide(cashback)
    }
}