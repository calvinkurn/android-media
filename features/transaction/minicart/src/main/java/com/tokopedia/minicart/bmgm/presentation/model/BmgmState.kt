package com.tokopedia.minicart.bmgm.presentation.model

/**
 * Created by @ilhamsuaib on 21/08/23.
 */
sealed interface BmgmState<out T> {

    object None : BmgmState<Nothing>
    object Loading : BmgmState<Nothing>
    data class Success<T>(val data: T) : BmgmState<T>
    data class Error(val t: Throwable) : BmgmState<Nothing>
}