package com.tokopedia.dilayanitokopedia.common.constant

import androidx.annotation.IntDef

/**
 * Created by irpan on 12/09/22.
 */
@Retention(AnnotationRetention.SOURCE)
@IntDef(
    DtLayoutState.IDLE,
    DtLayoutState.SHOW,
    DtLayoutState.LOADING,
    DtLayoutState.HIDE,
    DtLayoutState.LOAD_MORE,
    DtLayoutState.LOADED,
    DtLayoutState.EMPTY,
    DtLayoutState.UPDATE
)
annotation class DtLayoutState {
    companion object {
        const val IDLE = -1
        const val SHOW = 1
        const val LOADING = 2
        const val HIDE = 3
        const val LOAD_MORE = 4
        const val LOADED = 5
        const val EMPTY = 6
        const val UPDATE = 7
    }
}
