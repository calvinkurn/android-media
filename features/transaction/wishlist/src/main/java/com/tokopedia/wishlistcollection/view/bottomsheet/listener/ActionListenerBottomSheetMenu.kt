package com.tokopedia.wishlistcollection.view.bottomsheet.listener

interface ActionListenerBottomSheetMenu {
    fun onEditCollection(collectionId: String, collectionName: String, actionText: String)
    fun onDeleteCollection(collectionId: String, collectionName: String, actionText: String)
    fun onShareCollection(collectionId: String, actionText: String)
    fun onManageItemsInCollection(actionText: String)
}
