package com.tokopedia.digital.home.presentation.fragment

import android.os.Bundle
import android.view.View

class DigitalHomepageSearchNewFragment: DigitalHomePageSearchFragment() {

    private var searchBarRedirection: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            searchBarRedirection = it.getString(EXTRA_SEARCH_BAR_REDIRECTION, "")
        }
    }

    override fun searchCategory(searchQuery: String) {
        if (!searchQuery.isNullOrEmpty()) {
            viewModel.searchAutoComplete(viewModel.mapAutoCompleteParams(searchQuery), searchQuery)
        } else {
            viewModel.cancelAutoComplete()
        }
    }

    companion object {
        private const val EXTRA_SEARCH_BAR_REDIRECTION = "search_bar_redirection"

        fun newInstance(searchBarRedirection: String = ""): DigitalHomepageSearchNewFragment{
            val fragment = DigitalHomepageSearchNewFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_SEARCH_BAR_REDIRECTION, searchBarRedirection)
            fragment.arguments = bundle
            return fragment
        }
    }
}