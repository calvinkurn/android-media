package com.tokopedia.tokopedianow.sortfilter.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.sortfilter.presentation.activity.TokoNowSortFilterActivity
import com.tokopedia.tokopedianow.sortfilter.presentation.activity.TokoNowSortFilterActivity.Companion.SORT_VALUE
import com.tokopedia.tokopedianow.sortfilter.presentation.bottomsheet.TokoNowSortFilterBottomSheet
import com.tokopedia.tokopedianow.sortfilter.presentation.bottomsheet.TokoNowSortFilterBottomSheet.*

class TokoNowSortFilterFragment:
    Fragment(),
    TokoNowRepurchaseSortFilterBottomSheetListener {

    companion object {
        fun newInstance(sortValue: String): TokoNowSortFilterFragment {
            return TokoNowSortFilterFragment().apply {
                arguments = Bundle().apply {
                    putString(SORT_VALUE, sortValue)
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

    override fun onApplySortFilter(value: Int) {
        val intent = Intent()
        intent.putExtra(SORT_VALUE, value)
        activity?.apply {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun showCategoryListBottomSheet() {
        val sortValue = arguments?.getString(SORT_VALUE).orEmpty()
        TokoNowSortFilterBottomSheet.newInstance().show(childFragmentManager, sortValue.toIntOrZero(), this)
    }
}