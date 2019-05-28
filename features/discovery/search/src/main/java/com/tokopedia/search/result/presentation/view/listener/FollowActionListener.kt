package com.tokopedia.search.result.presentation.view.listener

interface FollowActionListener {
    fun onSuccessToggleFollow(adapterPosition : Int, enable : Boolean)
    fun onErrorToggleFollow()
    fun onErrorToggleFollow(errorMessage : String)
    fun getString(resId: Int): String
}