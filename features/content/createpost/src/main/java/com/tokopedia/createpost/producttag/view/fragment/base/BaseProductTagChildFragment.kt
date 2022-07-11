package com.tokopedia.createpost.producttag.view.fragment.base

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment

/**
 * Created By : Jonathan Darwin on May 10, 2022
 */
open class BaseProductTagChildFragment : TkpdBaseV4Fragment() {

    protected lateinit var viewModelProvider: ViewModelProvider

    override fun getScreenName(): String = "BaseProductTagChildFragment"

    fun createViewModelProvider(viewModelProvider: ViewModelProvider) {
        this.viewModelProvider = viewModelProvider
    }
}