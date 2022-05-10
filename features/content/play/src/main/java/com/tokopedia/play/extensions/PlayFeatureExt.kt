package com.tokopedia.play.extensions

import android.view.View
import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.uimodel.state.KebabMenuType

/**
 * Created by jegul on 06/03/20
 */
val Map<BottomInsetsType, BottomInsetsState>.isAnyShown: Boolean
    get() = values.any { it is BottomInsetsState.Shown }

val Map<BottomInsetsType, BottomInsetsState>.isKeyboardShown: Boolean
    get() = this[BottomInsetsType.Keyboard]?.isShown == true

val Map<BottomInsetsType, BottomInsetsState>.isAnyBottomSheetsShown: Boolean
    get() = this[BottomInsetsType.VariantSheet]?.isShown == true ||
            this[BottomInsetsType.ProductSheet]?.isShown == true ||
            this[BottomInsetsType.LeaderboardSheet]?.isShown == true ||
            this[BottomInsetsType.CouponSheet]?.isShown == true

val Map<BottomInsetsType, BottomInsetsState>.isProductSheetsShown: Boolean
    get() = this[BottomInsetsType.ProductSheet]?.isShown == true

val Map<BottomInsetsType, BottomInsetsState>.isLeaderboardSheetShown: Boolean
    get() = this[BottomInsetsType.LeaderboardSheet]?.isShown == true

val Map<BottomInsetsType, BottomInsetsState>.isCouponSheetsShown: Boolean
    get() = this[BottomInsetsType.CouponSheet]?.isShown == true

val View.isFullAlpha: Boolean
    get() = alpha == 0.0f

val View.isFullSolid: Boolean
    get() = alpha == 1.0f

val View.hasAlpha: Boolean
    get() = !isFullSolid
