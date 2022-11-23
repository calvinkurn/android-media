package com.tokopedia.privacycenter.sharingwishlist.ui

sealed class SharingWishlistStateResult<T> {
    class Loading<T>: SharingWishlistStateResult<T>()
    data class RenderCollection<T>(val data: T) : SharingWishlistStateResult<T>()
    data class CollectionEmpty<T>(val url: String) : SharingWishlistStateResult<T>()
    data class Fail<T>(val error: Throwable) : SharingWishlistStateResult<T>()
}
