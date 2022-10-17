package com.tokopedia.tokomember_seller_dashboard.util


class InfiniteListResult<T>{
    var status = InfiniteState.NOT_LOADING
    var list:List<T>?=null

    private constructor(status:InfiniteState,list:List<T>?){
        this.status = status
        this.list = list
    }

    enum class InfiniteState{
        NOT_LOADING,LOADING,JUST_STOPPED
    }

    companion object{
        fun <T> notLoading(list:List<T>? = null) = InfiniteListResult(InfiniteState.NOT_LOADING,list)
        fun <T> infiniteLoading(list:List<T>? = null) = InfiniteListResult(InfiniteState.LOADING,list)
        fun <T> loadingStopped(list:List<T>? = null) = InfiniteListResult(InfiniteState.JUST_STOPPED,list)
    }
}

