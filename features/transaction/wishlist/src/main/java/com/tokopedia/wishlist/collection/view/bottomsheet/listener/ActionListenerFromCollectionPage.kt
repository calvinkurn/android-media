package com.tokopedia.wishlist.collection.view.bottomsheet.listener

import com.tokopedia.wishlist.collection.data.response.CreateWishlistCollectionResponse

interface ActionListenerFromCollectionPage {
    fun onSuccessCreateNewCollection(message: CreateWishlistCollectionResponse.CreateWishlistCollection.DataCreate, newCollectionName: String)
}
