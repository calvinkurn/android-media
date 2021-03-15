package com.tokopedia.shop.score.common

import android.graphics.Color
import android.text.method.LinkMovementMethod
import com.tokopedia.shop.score.R
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography

object ShopScoreUtils {

    fun getLevelBarWhite(level: Int): Int {
        return when(level) {
            ShopScoreConstant.SHOP_SCORE_LEVEL_ONE -> R.drawable.ic_one_level_white
            ShopScoreConstant.SHOP_SCORE_LEVEL_TWO -> R.drawable.ic_two_level_white
            ShopScoreConstant.SHOP_SCORE_LEVEL_THREE -> R.drawable.ic_three_level_white
            ShopScoreConstant.SHOP_SCORE_LEVEL_FOUR -> R.drawable.ic_four_level_white
            else -> R.drawable.ic_no_level
        }
    }
}

fun Typography.setTextMakeHyperlink(text: String, onClick: () -> Unit) {
    val htmlString = HtmlLinkHelper(context, text)
    this.movementMethod =  LinkMovementMethod.getInstance()
    this.highlightColor = Color.TRANSPARENT
    this.text = htmlString.spannedString
    htmlString.urlList.getOrNull(0)?.setOnClickListener {
        onClick()
    }
}