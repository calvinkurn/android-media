package com.tokopedia.wishlist.collection.view.bottomsheet.listener

import com.tokopedia.wishlist.collection.data.response.AddWishlistCollectionItemsResponse

interface ActionListenerFromPdp {
    fun onSuccessSaveToNewCollection(message: AddWishlistCollectionItemsResponse.AddWishlistCollectionItems.DataItem)
    fun onFailedSaveToNewCollection(errorMessage: String?)
}
