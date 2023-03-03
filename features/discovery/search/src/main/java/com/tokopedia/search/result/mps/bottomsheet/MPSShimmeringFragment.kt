package com.tokopedia.search.result.mps.bottomsheet

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.discovery.common.constants.SearchConstant.SearchTabPosition
import com.tokopedia.search.R
import com.tokopedia.search.result.SearchViewModel
import com.tokopedia.search.result.presentation.view.activity.SearchComponent
import javax.inject.Inject

class MPSShimmeringFragment:
    Fragment(R.layout.search_mps_product_tab),
    MPSQueryBottomSheet.Listener {

    @Inject
    @Suppress("LateinitUsage")
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private val searchViewModel: SearchViewModel? by viewModels { viewModelProvider }
    private var mpsQueryBottomSheet: MPSQueryBottomSheet? = null

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (isVisibleToUser && isAdded)
            mpsQueryBottomSheet = showMPSQueryBottomSheet(childFragmentManager)
    }

    private fun showMPSQueryBottomSheet(fragmentManager: FragmentManager): MPSQueryBottomSheet {
        return mpsQueryBottomSheet ?: (MPSQueryBottomSheet.create(
            queryList = listOf("samsung", "xiaomi", "iphone"),
            listener = this,
        ).apply {
            show(fragmentManager, MPSQueryBottomSheet.TAG)
        })
    }

    override fun onDismiss() {
        mpsQueryBottomSheet = null
        searchViewModel?.setActiveTab(SearchTabPosition.TAB_SECOND_POSITION)
    }

    companion object {
        internal fun newInstance(searchComponent: SearchComponent?): MPSShimmeringFragment {
            return MPSShimmeringFragment().apply {
                searchComponent?.inject(this)
            }
        }
    }
}
