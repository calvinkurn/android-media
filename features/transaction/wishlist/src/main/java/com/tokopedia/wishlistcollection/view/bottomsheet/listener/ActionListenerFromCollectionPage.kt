package com.tokopedia.wishlistcollection.view.bottomsheet.listener

import com.tokopedia.wishlistcollection.data.response.CreateWishlistCollectionResponse

interface ActionListenerFromCollectionPage {
    fun onSuccessCreateNewCollection(message: CreateWishlistCollectionResponse.CreateWishlistCollection.DataCreate, newCollectionName: String)
}