package com.tokopedia.wishlistcollection.view.bottomsheet.listener

import com.tokopedia.wishlistcollection.data.response.AddWishlistCollectionItemsResponse

interface ActionListenerFromPdp {
    fun onSuccessSaveToNewCollection(message: AddWishlistCollectionItemsResponse.AddWishlistCollectionItems.DataItem)
    fun onFailedSaveToNewCollection(errorMessage: String?)
}