package com.tokopedia.digital.home.presentation.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingAdditionalConstant

class DigitalHomepageSearchNewFragment: DigitalHomePageSearchFragment() {

    private var searchBarRedirection: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            searchBarRedirection = it.getString(EXTRA_SEARCH_BAR_REDIRECTION, "")
            searchBarScreenName = it.getString(EXTRA_SEARCH_BAR_SCREEN_NAME, RechargeHomepageTrackingAdditionalConstant.SCREEN_NAME_TOPUP_BILLS)
            initSearchBarView(it.getString(EXTRA_SEARCH_BAR_PLACE_HOLDER, ""))
        }
    }

    private fun initSearchBarView(placeHolder: String) {
        binding.digitalHomepageSearchViewSearchBar.searchBarPlaceholder = placeHolder
    }

    companion object {
        private const val EXTRA_SEARCH_BAR_REDIRECTION = "search_bar_redirection"
        private const val EXTRA_SEARCH_BAR_PLACE_HOLDER = "search_bar_place_holder"
        private const val EXTRA_SEARCH_BAR_SCREEN_NAME = "search_bar_screen_name"

        fun newInstance(searchBarRedirection: String = "", searchBarPlaceHolder: String = "",
                        searchBarScreenName: String): DigitalHomepageSearchNewFragment{
            val fragment = DigitalHomepageSearchNewFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_SEARCH_BAR_REDIRECTION, searchBarRedirection)
            bundle.putString(EXTRA_SEARCH_BAR_PLACE_HOLDER, searchBarPlaceHolder)
            bundle.putString(EXTRA_SEARCH_BAR_SCREEN_NAME, searchBarScreenName)
            fragment.arguments = bundle
            return fragment
        }
    }
}