package com.tokopedia.promotionstarget

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

//todo Rahul check exsistence of this class
class ViewModelFactory<T>(val creator: () -> T) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return creator() as T
    }
}