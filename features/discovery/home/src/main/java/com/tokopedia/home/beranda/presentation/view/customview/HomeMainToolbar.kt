package com.tokopedia.home.beranda.presentation.view.customview

import android.content.Context
import com.tokopedia.searchbar.MainToolbar
import com.tokopedia.searchbar.R

class HomeMainToolbar(context : Context) : MainToolbar(context) {
    override fun inflateResource(context: Context?) {
        inflate(context, R.layout.main_toolbar, this)
    }
}
