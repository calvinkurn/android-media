package com.tokopedia.wishlistcollection.view.bottomsheet.listener

interface ActionListenerFromPdp {
    fun onSuccessSaveToNewCollection(message: String)
    fun onFailedSaveToNewCollection(errorMessage: String?)
}