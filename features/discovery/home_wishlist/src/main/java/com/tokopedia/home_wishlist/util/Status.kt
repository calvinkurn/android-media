package com.tokopedia.home_wishlist.util

/**
 * Created by Lukas on 26/08/19
 */

enum class Status{
    LOADING, SUCCESS, ERROR, EMPTY, LOAD_MORE;

    companion object {
        fun fromString(state: String) : Status = when(state){
            "loading" -> LOADING
            "error" -> ERROR
            "success" -> SUCCESS
            "load_more" -> LOAD_MORE
            else -> EMPTY
        }
    }

    override fun toString(): String {
        return when(this){
            LOADING -> "loading"
            ERROR -> "error"
            SUCCESS -> "success"
            LOAD_MORE -> "load_more"
            else -> "empty"
        }
    }

    fun isSuccess() = this == SUCCESS
    fun isLoading() = this == LOADING
    fun isError() = this == ERROR
    fun isEmpty() = this == EMPTY
    fun isLoadMore() = this == LOAD_MORE
}