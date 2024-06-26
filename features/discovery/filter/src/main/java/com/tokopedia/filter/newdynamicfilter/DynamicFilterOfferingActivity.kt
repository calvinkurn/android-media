package com.tokopedia.filter.newdynamicfilter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.adapter.DynamicFilterDetailAdapter
import com.tokopedia.filter.newdynamicfilter.adapter.DynamicFilterDetailOfferingAdapter
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData

import java.util.ArrayList

class DynamicFilterOfferingActivity : DynamicFilterDetailGeneralActivity() {

    override fun getAdapter(): DynamicFilterDetailAdapter {
        return DynamicFilterDetailOfferingAdapter(this)
    }

    companion object {

        fun moveTo(activity: AppCompatActivity?,
                            pageTitle: String,
                            optionList: List<Option>,
                            isSearchable: Boolean,
                            searchHint: String,
                            isUsingTracking: Boolean,
                            trackingData: FilterTrackingData?) {

            if (activity != null) {
                val intent = Intent(activity, DynamicFilterOfferingActivity::class.java)
                intent.putExtra(EXTRA_PAGE_TITLE, pageTitle)
                intent.putParcelableArrayListExtra(EXTRA_OPTION_LIST, ArrayList(optionList))
                intent.putExtra(EXTRA_IS_SEARCHABLE, isSearchable)
                intent.putExtra(EXTRA_SEARCH_HINT, searchHint)
                intent.putExtra(EXTRA_IS_USING_TRACKING, isUsingTracking)
                intent.putExtra(EXTRA_TRACKING_DATA, trackingData)
                activity.startActivityForResult(intent, REQUEST_CODE)
            }
        }
    }
}
