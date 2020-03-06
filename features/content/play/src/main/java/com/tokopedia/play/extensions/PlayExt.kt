package com.tokopedia.play.extensions

import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.type.BottomInsetsType

/**
 * Created by jegul on 06/03/20
 */
val Map<BottomInsetsType, BottomInsetsState>.isAnyShown: Boolean
    get() = values.any { it is BottomInsetsState.Shown }

val Map<BottomInsetsType, BottomInsetsState>.isAnyHidden: Boolean
    get() = values.any { it is BottomInsetsState.Hidden }