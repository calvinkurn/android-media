package com.tokopedia.tokopedianow.categorylist.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.categorylist.presentation.activity.CategoryListActivity.Companion.PARAM_WAREHOUSE_ID
import com.tokopedia.tokopedianow.categorylist.presentation.bottomsheet.CategoryListBottomSheet

class CategoryListFragment: Fragment() {

    companion object {

        fun newInstance(warehouseId: String): CategoryListFragment {
            return CategoryListFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_WAREHOUSE_ID, warehouseId)
                }
            }
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
        val warehouseId = arguments?.getString(PARAM_WAREHOUSE_ID).orEmpty()
        CategoryListBottomSheet.newInstance(warehouseId)
            .show(childFragmentManager)
    }
}