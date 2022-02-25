package com.tokopedia.common.topupbills.favorite.view.listener

interface FavoriteNumberDeletionListener {
    fun onSuccessDelete(operatorName: String)
    fun onFailedDelete()
}