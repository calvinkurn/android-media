package com.tokopedia.filter.newdynamicfilter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.view.View

import com.tokopedia.filter.R
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.adapter.DynamicFilterBrandAdapter
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData
import com.tokopedia.filter.widget.AlphabeticalSideBar

import java.util.ArrayList

class DynamicFilterDetailBrandActivity : AbstractDynamicFilterDetailActivity<DynamicFilterBrandAdapter>() {

    private lateinit var sidebar: AlphabeticalSideBar

    override fun bindView() {
        super.bindView()
        sidebar = findViewById<View>(R.id.filter_detail_sidebar) as AlphabeticalSideBar
    }

    override fun initRecyclerView() {
        super.initRecyclerView()
        sidebar.visibility = View.VISIBLE
        sidebar.setRecyclerView(recyclerView)
    }

    override fun getAdapter(): DynamicFilterBrandAdapter {
        return DynamicFilterBrandAdapter(this)
    }

    override fun loadFilterItems(options: List<Option>) {
        abstractDynamicFilterAdapter.setOptionList(options)
    }

    override fun resetFilter() {
        super.resetFilter()
        abstractDynamicFilterAdapter.resetAllOptionsInputState()
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

            if (activity != null && optionList != null) {
                val intent = Intent(activity, DynamicFilterDetailBrandActivity::class.java)
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
