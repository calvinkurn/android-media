package com.tokopedia.search.result.mps.bottomsheet

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant.SearchTabPosition
import com.tokopedia.search.R
import com.tokopedia.search.result.SearchViewModel
import com.tokopedia.search.result.mps.MPSFragment
import com.tokopedia.search.result.presentation.view.activity.SearchComponent
import javax.inject.Inject

class MPSShimmeringFragment @Inject constructor(
    private val viewModelProvider: ViewModelProvider.Factory
): Fragment(R.layout.search_mps_product_tab),
    MPSQueryBottomSheet.Listener {


    private val searchViewModel: SearchViewModel? by viewModels { viewModelProvider }
    private var mpsQueryBottomSheet: MPSQueryBottomSheet? = null
    private val searchParameter
        get() = searchViewModel?.stateFlow?.value?.searchParameter ?: mapOf()

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (isVisibleToUser && isAdded)
            mpsQueryBottomSheet = showMPSQueryBottomSheet(childFragmentManager)
    }

    private fun showMPSQueryBottomSheet(fragmentManager: FragmentManager): MPSQueryBottomSheet {
        return mpsQueryBottomSheet ?: (MPSQueryBottomSheet.create(
            queryList = listOf(
                searchParameter[SearchApiConst.Q1] ?: "",
                searchParameter[SearchApiConst.Q2] ?: "",
                searchParameter[SearchApiConst.Q3] ?: "",
            ).filter { it.isNotBlank() },
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
        @JvmStatic
        internal fun newInstance(
            classLoader: ClassLoader,
            fragmentFactory: FragmentFactory,
        ): MPSShimmeringFragment {
            return fragmentFactory.instantiate(
                classLoader,
                MPSShimmeringFragment::class.java.name,
            ) as MPSShimmeringFragment
        }
    }
}
