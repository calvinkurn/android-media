package com.tokopedia.tokopedianow.categorylist.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.categorylist.presentation.bottomsheet.TokoNowCategoryListBottomSheet

class TokoNowCategoryListFragment: Fragment() {

    companion object {

        fun newInstance(): TokoNowCategoryListFragment {
            return TokoNowCategoryListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tokopedianow_category_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showCategoryListBottomSheet()
    }

    private fun showCategoryListBottomSheet() {
        TokoNowCategoryListBottomSheet.newInstance()
            .show(childFragmentManager)
    }
}
