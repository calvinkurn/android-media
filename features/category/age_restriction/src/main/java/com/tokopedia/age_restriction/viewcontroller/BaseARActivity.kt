package com.tokopedia.age_restriction.viewcontroller

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import com.tokopedia.tradein_common.viewcontrollers.BaseViewModelActivity
import com.tokopedia.tradein_common.viewmodel.BaseViewModel

abstract class BaseARActivity<T : BaseViewModel> : BaseViewModelActivity<T>() {

    companion object {
        val LOGIN_REQUEST = 514
        val VERIFICATION_REQUEST = 515
        val RESULT_IS_ADULT = 980
        val RESULT_IS_NOT_ADULT = 180
        val PARAM_EXTRA_DOB = "VERIFY DOB"
        private val CATEGORYPAGE = "category page"
        private val PDP = "product detail page"
        private val SEARCHPAGE = "search result page"
        private val CLICKCATEGORY = "clickCategory"
        private val CLICKPDP = "clickPDP"
        private val CLICKSEARCH = "clickSearchResult"
        private val VIEWPDP = "viewPDP"
        private val VIEWCATEGORY = "viewCategory"
        private val VIEWSEARCH = "viewSearchResult"
        var event = CATEGORYPAGE
        var eventView = VIEWPDP
        var eventClick = CLICKPDP
        var origin = 1
        var destinationUrlGtm = ""


    }

    override fun getVMFactory(): ViewModelProvider.NewInstanceFactory {
        return ViewModelProvider.NewInstanceFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        origin = intent.getIntExtra("ORIGIN", 1)
        destinationUrlGtm = intent.getStringExtra("DESTINATION_GTM")
        when (origin) {
            1 -> {
                event = CATEGORYPAGE
                eventView = VIEWCATEGORY
                eventClick = CLICKCATEGORY

            }
            2 -> {
                event = PDP
                eventView = VIEWPDP
                eventClick = CLICKPDP
            }
            3 -> {
                event = SEARCHPAGE
                eventView = VIEWSEARCH
                eventClick = CLICKSEARCH
            }
        }
    }
}