package com.tokopedia.shop.score.common.presentation

import com.tokopedia.unifycomponents.BottomSheetUnify

abstract class BaseBottomSheetShopScore: BottomSheetUnify() {

    abstract fun getLayoutResId(): Int
}