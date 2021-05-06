package com.tokopedia.tokomart.categorylist.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.categorylist.presentation.bottomsheet.TokoMartCategoryListBottomSheet

class TokoMartCategoryListFragment: Fragment() {

    companion object {

        fun newInstance(): TokoMartCategoryListFragment {
            return TokoMartCategoryListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tokomart_category_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoryListBottomSheet = TokoMartCategoryListBottomSheet.newInstance()
        categoryListBottomSheet.show(childFragmentManager)
    }
}