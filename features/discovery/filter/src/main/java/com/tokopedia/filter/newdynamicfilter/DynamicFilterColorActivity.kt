package com.tokopedia.filter.newdynamicfilter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.adapter.DynamicFilterDetailAdapter
import com.tokopedia.filter.newdynamicfilter.adapter.DynamicFilterDetailColorAdapter
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData

import java.util.ArrayList

class DynamicFilterColorActivity : DynamicFilterDetailGeneralActivity() {

    override fun getAdapter(): DynamicFilterDetailAdapter {
        return DynamicFilterDetailColorAdapter(this)
    }

    companion object {
        @JvmStatic
        fun moveTo(activity: AppCompatActivity?,
                   pageTitle: String?,
                   optionList: List<Option>?,
                   isSearchable: Boolean,
                   searchHint: String?,
                   isUsingTracking: Boolean,
                   trackingData: FilterTrackingData?) {

            if (activity != null && optionList!= null) {
                val intent = Intent(activity, DynamicFilterColorActivity::class.java)
                intent.putExtra(EXTRA_PAGE_TITLE, pageTitle?: "")
                intent.putParcelableArrayListExtra(EXTRA_OPTION_LIST, ArrayList(optionList))
                intent.putExtra(EXTRA_IS_SEARCHABLE, isSearchable)
                intent.putExtra(EXTRA_SEARCH_HINT, searchHint?: "")
                intent.putExtra(EXTRA_IS_USING_TRACKING, isUsingTracking)
                intent.putExtra(EXTRA_TRACKING_DATA, trackingData)
                activity.startActivityForResult(intent, REQUEST_CODE)
            }
        }
    }
}
