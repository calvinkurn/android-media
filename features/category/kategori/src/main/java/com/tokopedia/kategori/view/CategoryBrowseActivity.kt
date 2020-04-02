package com.tokopedia.kategori.view

import com.tokopedia.kategori.R


class CategoryBrowseActivity : BaseCategoryBrowseActivity() {

    private val launchSource: String = "Belanja/Home"

    override fun getCategoryLaunchSource(): String {
        return launchSource
    }

    override fun getScreenName(): String = getString(R.string.belanja_screen_name)
}
