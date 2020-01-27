package com.tokopedia.home_wishlist.util

/**
 * Created by Lukas on 26/08/19
 */

enum class Status{
    LOADING, ERROR, EMPTY, SUCCESS;

    override fun toString(): String {
        return when(this){
            LOADING -> "loading"
            ERROR -> "error"
            SUCCESS -> "success"
            else -> "empty"
        }
    }

    fun isLoading() = this == LOADING
    fun isError() = this == ERROR
    fun isSuccess() = this == SUCCESS
    fun isEmpty() = this == EMPTY
}