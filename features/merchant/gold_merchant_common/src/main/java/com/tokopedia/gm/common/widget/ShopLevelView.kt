package com.tokopedia.gm.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.gm.common.R
import com.tokopedia.gm.common.constant.ShopLevel
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
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
        val levelLbl = context.getString(R.string.gmc_level, shopLevel.toString())
        tvGmcPmShopLevel.text = levelLbl

        val imgLevelDrawableRes = when (shopLevel) {
            ShopLevel.ONE -> R.drawable.ic_gmc_shop_level_1
            ShopLevel.TWO -> R.drawable.ic_gmc_shop_level_2
            ShopLevel.THREE -> R.drawable.ic_gmc_shop_level_3
            ShopLevel.FOUR -> R.drawable.ic_gmc_shop_level_4
            else -> R.drawable.ic_gmc_shop_level_0
        }
        imgGmcPmShopLevel.loadImageDrawable(imgLevelDrawableRes)
    }
}