package com.tokopedia.gm.common.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.gm.common.R
import kotlinx.android.synthetic.main.view_gmc_shop_level.view.*

/**
 * Created By @ilhamsuaib on 20/03/21
 */

class ShopLevelView : LinearLayout {

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.view_gmc_shop_level, this)
    }

    fun show(shopLevel: Int) {
        tvSahPmShopLevel.text = shopLevel.toString()
        /*val imgLevelDrawableRes = when(shopLevel) {
            1 ->
            2 ->
            3 ->
            else ->
        }
        imgShopLevel.loadImageDrawable(imgLevelDrawableRes)*/
    }
}