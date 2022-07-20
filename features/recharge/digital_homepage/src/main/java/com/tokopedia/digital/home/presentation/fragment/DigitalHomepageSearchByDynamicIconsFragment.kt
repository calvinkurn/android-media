package com.tokopedia.digital.home.presentation.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.digital.home.analytics.RechargeHomepageTrackingAdditionalConstant.SCREEN_NAME_TOPUP_BILLS
import com.tokopedia.kotlin.extensions.view.toIntOrZero

/**
 * @author by jessica on 21/01/21
 */
class DigitalHomepageSearchByDynamicIconsFragment : DigitalHomePageSearchFragment() {

    private var platformId: Int = 0
    private var enablePersonalized: Boolean = true
    private var sectionId: List<Int> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            platformId = it.getInt(EXTRA_PLATFORM_ID, 0)
            enablePersonalized = it.getBoolean(EXTRA_ENABLE_PERSONALIZE, true)
            sectionId = ((it.getStringArrayList(EXTRA_SECTION_ID)
                    ?: arrayListOf()).map { sectionId ->
                sectionId.toIntOrZero()
            })
            searchBarScreenName = it.getString(EXTRA_SEARCH_BAR_SCREEN_NAME, SCREEN_NAME_TOPUP_BILLS)
            initSearchBarView(it.getString(EXTRA_SEARCH_BAR_PLACE_HOLDER, ""))
        }
    }

    override fun searchCategory(searchQuery: String) {
        if (!searchQuery.isNullOrEmpty()) {
            viewModel.searchByDynamicIconsCategory(searchQuery, platformId, sectionId, enablePersonalized)
        } else {
            viewModel.cancelAutoComplete()
        }
    }

    private fun initSearchBarView(placeHolder: String) {
        binding.digitalHomepageSearchViewSearchBar.searchBarPlaceholder = placeHolder
    }

    companion object {
        private const val EXTRA_PLATFORM_ID = "platform_id"
        private const val EXTRA_ENABLE_PERSONALIZE = "personalize"
        private const val EXTRA_SECTION_ID = "section_id"
        private const val EXTRA_SEARCH_BAR_PLACE_HOLDER = "search_bar_place_holder"
        private const val EXTRA_SEARCH_BAR_SCREEN_NAME = "search_bar_screen_name"

        fun newInstance(platformId: Int, enablePersonalize: Boolean = false,
                        sectionId: ArrayList<String>,
                        searchBarPlaceHolder: String = "",
                        searchBarScreenName: String)
                : DigitalHomepageSearchByDynamicIconsFragment {
            val fragment = DigitalHomepageSearchByDynamicIconsFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_PLATFORM_ID, platformId)
            bundle.putBoolean(EXTRA_ENABLE_PERSONALIZE, enablePersonalize)
            bundle.putStringArrayList(EXTRA_SECTION_ID, sectionId)
            bundle.putString(EXTRA_SEARCH_BAR_PLACE_HOLDER, searchBarPlaceHolder)
            bundle.putString(EXTRA_SEARCH_BAR_SCREEN_NAME, searchBarScreenName)
            fragment.arguments = bundle
            return fragment
        }
    }
}