package com.tokopedia.tokomart.categorylist.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.categorylist.presentation.activity.TokoMartCategoryListActivity.Companion.PARAM_WAREHOUSE_ID
import com.tokopedia.tokomart.categorylist.presentation.bottomsheet.TokoMartCategoryListBottomSheet

class TokoMartCategoryListFragment: Fragment() {

    companion object {

        fun newInstance(warehouseId: String): TokoMartCategoryListFragment {
            return TokoMartCategoryListFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_WAREHOUSE_ID, warehouseId)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tokomart_category_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showCategoryListBottomSheet()
    }

    private fun showCategoryListBottomSheet() {
        val warehouseId = arguments?.getString(PARAM_WAREHOUSE_ID).orEmpty()
        TokoMartCategoryListBottomSheet.newInstance(warehouseId)
            .show(childFragmentManager)
    }
}