package com.tokopedia.common.topupbills.favoritepage.view.listener

interface FavoriteNumberDeletionListener {
    fun onSuccessDelete(operatorName: String)
    fun onFailedDelete()
}