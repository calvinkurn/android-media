package com.tokopedia.wishlistcollection.view.bottomsheet.listener

import android.view.View

interface ActionListenerBottomSheetMenu {
    fun onEditCollection(collectionId: String, collectionName: String, actionText: String)
    fun onDeleteCollection(collectionId: String, collectionName: String, actionText: String)
    fun onShareCollection(
        collectionId: String,
        collectionName: String,
        actionText: String,
        _collectionIndicatorTitle: String
    )
    fun onManageItemsInCollection(actionText: String)
    fun onShareItemShown(anchorView: View)
}
