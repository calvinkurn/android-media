package com.tokopedia.filter.common.manager

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.fragment.app.Fragment

import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.filter.R
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.filter.newdynamicfilter.RevampedDynamicFilterActivity.Companion.EXTRA_CALLER_SCREEN_NAME
import com.tokopedia.filter.newdynamicfilter.RevampedDynamicFilterActivity.Companion.EXTRA_QUERY_PARAMETERS
import com.tokopedia.filter.newdynamicfilter.RevampedDynamicFilterActivity.Companion.EXTRA_SELECTED_FILTERS
import com.tokopedia.filter.newdynamicfilter.RevampedDynamicFilterActivity.Companion.EXTRA_SELECTED_OPTIONS
import com.tokopedia.filter.newdynamicfilter.SortProductActivity.Companion.EXTRA_AUTO_APPLY_FILTER
import com.tokopedia.filter.newdynamicfilter.SortProductActivity.Companion.EXTRA_SELECTED_SORT
import com.tokopedia.filter.newdynamicfilter.SortProductActivity.Companion.EXTRA_SELECTED_SORT_NAME
import com.tokopedia.filter.newdynamicfilter.SortProductActivity.Companion.EXTRA_SORT_DATA
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData

import java.util.ArrayList
import java.util.HashMap

class FilterSortManager {

    companion object{
        private const val SORT_REQUEST_CODE = 1233
        private const val FILTER_REQUEST_CODE = 4320

        @JvmStatic
        fun openFilterPage(trackingData: FilterTrackingData?, fragment: Fragment?, callerScreenName: String?, queryParams: HashMap<String, String>?) {
            if (fragment == null || fragment.context == null || trackingData == null) return

            FilterTracking.eventOpenFilterPage(trackingData)

            val intent = RouteManager.getIntent(fragment.context, ApplinkConstInternalDiscovery.FILTER)
            intent.putExtra(EXTRA_CALLER_SCREEN_NAME, callerScreenName?: "")
            intent.putExtra(EXTRA_QUERY_PARAMETERS, queryParams?: "")

            fragment.startActivityForResult(intent, FILTER_REQUEST_CODE)

            fragment.activity?.overridePendingTransition(R.anim.pull_up, android.R.anim.fade_out)
        }

        @JvmStatic
        fun openSortActivity(fragment: Fragment?, sort: ArrayList<Sort>?, selectedSort: HashMap<String, String>?): Boolean {
            if (!isSortDataAvailable(sort) || fragment == null || fragment.context == null) {
                return false
            }
            val intent = RouteManager.getIntent(fragment.context, ApplinkConstInternalDiscovery.SORT)
            intent.putParcelableArrayListExtra(EXTRA_SORT_DATA, sort)
            if (selectedSort != null) {
                intent.putExtra(EXTRA_SELECTED_SORT, selectedSort)
            }

            fragment.startActivityForResult(intent, SORT_REQUEST_CODE)

            fragment.activity?.overridePendingTransition(R.anim.pull_up, android.R.anim.fade_out)
            return true
        }

        @JvmStatic
        private fun isSortDataAvailable(sort: ArrayList<Sort>?): Boolean {
            return sort != null && sort.isNotEmpty()
        }

        @JvmStatic
        fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?, callback: Callback?) {
            if(data == null || callback == null) return

            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == SORT_REQUEST_CODE) {
                    val selectedSort = getMapFromIntent(data, EXTRA_SELECTED_SORT)
                    val selectedSortName = data.getStringExtra(EXTRA_SELECTED_SORT_NAME)
                    val autoApplyFilterParams = data.getStringExtra(EXTRA_AUTO_APPLY_FILTER)
                    if (selectedSortName != null && autoApplyFilterParams != null) {
                        callback.onSortResult(selectedSort, selectedSortName, autoApplyFilterParams)
                    }

                } else if (requestCode == FILTER_REQUEST_CODE) {
                    val queryParams = getMapFromIntent(data, EXTRA_QUERY_PARAMETERS)
                    val selectedFilters = getMapFromIntent(data, EXTRA_SELECTED_FILTERS)
                    val selectedOptions = data.getParcelableArrayListExtra<Option>(EXTRA_SELECTED_OPTIONS)
                    if (selectedOptions != null) {
                        callback.onFilterResult(queryParams, selectedFilters, selectedOptions)
                    }

                }
            }
        }

        private fun getMapFromIntent(data: Intent, extraName: String): Map<String, String> {
            data.getSerializableExtra(extraName) ?: return HashMap()

            val filterParameterMapIntent = data.getSerializableExtra(extraName) as Map<*, *>
            val filterParameter = HashMap<String, String>(filterParameterMapIntent.size)

            for (entry in filterParameterMapIntent.entries) {
                filterParameter.put(entry.key.toString(), entry.value.toString())
            }

            return filterParameter
        }
    }

    interface Callback {
        fun onFilterResult(queryParams: Map<String, String>?, selectedFilters: Map<String, String>?, selectedOptions: List<Option>?)
        fun onSortResult(selectedSort: Map<String, String>?, selectedSortName: String?, autoApplyFilter: String?)
    }
}
