package com.tokopedia.digital.home.presentation.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import kotlinx.android.synthetic.main.view_recharge_home_search.*

/**
 * @author by jessica on 21/01/21
 */
class DigitalHomepageSearchByDynamicIconsFragment: DigitalHomePageSearchFragment() {

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
            initSearchBarView(it.getString(EXTRA_SEARCH_BAR_PLACE_HOLDER, ""))
        }
    }

    override fun searchCategory(searchQuery: String) {
        viewModel.searchByDynamicIconsCategory(searchQuery, platformId, sectionId, enablePersonalized)
    }

    private fun initSearchBarView(placeHolder: String) {
        digital_homepage_search_view_search_bar.searchBarPlaceholder = placeHolder
    }

    companion object {
        private const val EXTRA_PLATFORM_ID = "platform_id"
        private const val EXTRA_ENABLE_PERSONALIZE = "personalize"
        private const val EXTRA_SECTION_ID = "section_id"
        private const val EXTRA_SEARCH_BAR_PLACE_HOLDER = "search_bar_place_holder"

        fun newInstance(platformId: Int, enablePersonalize: Boolean = false,
                        sectionId: ArrayList<String>,
                        searchBarPlaceHolder: String = "")
                : DigitalHomepageSearchByDynamicIconsFragment {
            val fragment = DigitalHomepageSearchByDynamicIconsFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_PLATFORM_ID, platformId)
            bundle.putBoolean(EXTRA_ENABLE_PERSONALIZE, enablePersonalize)
            bundle.putStringArrayList(EXTRA_SECTION_ID, sectionId)
            bundle.putString(EXTRA_SEARCH_BAR_PLACE_HOLDER, searchBarPlaceHolder)
            fragment.arguments = bundle
            return fragment
        }
    }
}