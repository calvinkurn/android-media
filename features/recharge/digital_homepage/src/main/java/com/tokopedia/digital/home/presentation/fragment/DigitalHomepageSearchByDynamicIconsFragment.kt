package com.tokopedia.digital.home.presentation.fragment

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.view_recharge_home_search.*

/**
 * @author by jessica on 21/01/21
 */
class DigitalHomepageSearchByDynamicIconsFragment: DigitalHomePageSearchFragment() {

    private var platformId: Int = 0
    private var enablePersonalized: Boolean = true
    private var sectionId: ArrayList<Int> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            platformId = it.getInt(EXTRA_PLATFORM_ID, 0)
            enablePersonalized = it.getBoolean(EXTRA_ENABLE_PERSONALIZE, true)
            sectionId = it.getIntegerArrayList(EXTRA_SECTION_ID) ?: arrayListOf()
        }
    }

    override fun searchCategory(searchQuery: String) {
        viewModel.searchByDynamicIconsCategory(searchQuery, platformId, sectionId, enablePersonalized)
    }

    companion object {
        private const val EXTRA_PLATFORM_ID = "platform_id"
        private const val EXTRA_ENABLE_PERSONALIZE = "personalize"
        private const val EXTRA_SECTION_ID = "section_id"

        fun newInstance(platformId: Int, enablePersonalize: Boolean = false, sectionId: ArrayList<Int>)
                : DigitalHomepageSearchByDynamicIconsFragment {
            val fragment = DigitalHomepageSearchByDynamicIconsFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_PLATFORM_ID, platformId)
            bundle.putBoolean(EXTRA_ENABLE_PERSONALIZE, enablePersonalize)
            bundle.putIntegerArrayList(EXTRA_SECTION_ID, sectionId)
            fragment.arguments = bundle
            return fragment
        }
    }
}