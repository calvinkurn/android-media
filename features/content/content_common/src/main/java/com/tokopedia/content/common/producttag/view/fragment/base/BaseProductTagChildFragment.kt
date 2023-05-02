package com.tokopedia.content.common.producttag.view.fragment.base

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.content.common.producttag.analytic.product.ContentProductTagAnalytic

/**
 * Created By : Jonathan Darwin on May 10, 2022
 */
@Suppress("LateinitUsage")
open class BaseProductTagChildFragment : TkpdBaseV4Fragment() {

    protected lateinit var viewModelProvider: ViewModelProvider

    protected var mAnalytic: ContentProductTagAnalytic? = null
        private set

    override fun getScreenName(): String = "BaseProductTagChildFragment"

    fun createViewModelProvider(viewModelProvider: ViewModelProvider) {
        this.viewModelProvider = viewModelProvider
    }

    fun setAnalytic(analytic: ContentProductTagAnalytic?) {
        mAnalytic = analytic
    }
}
