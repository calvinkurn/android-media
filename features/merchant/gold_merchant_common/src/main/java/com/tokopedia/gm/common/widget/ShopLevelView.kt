package com.tokopedia.gm.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.gm.common.R
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created By @ilhamsuaib on 20/03/21
 */

class ShopLevelView : LinearLayout {

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var tvGmcPmShopLevel: Typography? = null
    private var imgGmcPmShopLevel: ImageUnify? = null

    init {
        View.inflate(context, R.layout.view_gmc_shop_level, this).run {
            imgGmcPmShopLevel = findViewById(R.id.imgGmcPmShopLevel)
            tvGmcPmShopLevel = findViewById(R.id.tvGmcPmShopLevel)
        }
    }

    fun show(levelLbl: String, shopLevel: Int) {
        tvGmcPmShopLevel?.text = levelLbl

        val imgLevelDrawableRes = when (shopLevel) {
            PMConstant.ShopLevel.ONE -> R.drawable.ic_gmc_shop_level_1
            PMConstant.ShopLevel.TWO -> R.drawable.ic_gmc_shop_level_2
            PMConstant.ShopLevel.THREE -> R.drawable.ic_gmc_shop_level_3
            PMConstant.ShopLevel.FOUR -> R.drawable.ic_gmc_shop_level_4
            else -> R.drawable.ic_gmc_shop_level_0
        }
        imgGmcPmShopLevel?.loadImageDrawable(imgLevelDrawableRes)
    }
}