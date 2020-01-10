package com.tokopedia.tokopoints.view.util

sealed class Resources<T>
class Loading<T> : Resources<T>()
data class Success<T>(val data : T) : Resources<T>()
data class ErrorMessage<T>(val data : String) : Resources<T>()



//sealed class ListResources<T>
//class ShowLoader<T> : ListResources<T>()
//data class StartListLoading<T>(val data : String) : Resources<T>()
//class EmptyList<T> : ListResources<T>()
//class HideLoader<T> : ListResources<T>()
//data class ListErrorMessage<T>(val data : String) : ListResources<T>()