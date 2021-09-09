package com.tokopedia.tokopedianow.datefilter.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.datefilter.presentation.activity.TokoNowDateFilterActivity.Companion.DATE_LABEL_POSITION
import com.tokopedia.tokopedianow.datefilter.presentation.activity.TokoNowDateFilterActivity.Companion.END_DATE
import com.tokopedia.tokopedianow.datefilter.presentation.activity.TokoNowDateFilterActivity.Companion.START_DATE

class TokoNowDateFilterFragment: Fragment() {

    companion object {
        fun newInstance(dateLabel: String, startDate: String, endDate: String): TokoNowDateFilterFragment {
            return TokoNowDateFilterFragment().apply {
                arguments = Bundle().apply {
                    putString(DATE_LABEL_POSITION, dateLabel)
                    putString(START_DATE, startDate)
                    putString(END_DATE, endDate)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tokopedianow_date_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showCategoryListBottomSheet()
    }

    private fun showCategoryListBottomSheet() {
        val dateLabel = arguments?.getString(DATE_LABEL_POSITION).orEmpty()
        val startDate = arguments?.getString(START_DATE).orEmpty()
        val endDate = arguments?.getString(END_DATE).orEmpty()
        TokoNowDateFilterBottomSheet.newInstance().show(childFragmentManager, dateLabel.toIntOrZero(), startDate, endDate)
    }
}