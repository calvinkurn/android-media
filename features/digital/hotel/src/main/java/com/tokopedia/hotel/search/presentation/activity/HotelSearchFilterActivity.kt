package com.tokopedia.hotel.search.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.hotel.R
import com.tokopedia.hotel.search.data.util.CommonParam
import com.tokopedia.hotel.search.presentation.fragment.HotelSearchFilterFragment

class HotelSearchFilterActivity : BaseSimpleActivity() {
    lateinit var currentTag: String

    override fun getLayoutRes(): Int = R.layout.activity_hotel_search_filter

    override fun getParentViewResourceID() = R.id.parent_view

    override fun getToolbarResourceID(): Int = R.id.toolbar

    override fun isShowCloseButton(): Boolean = true

    override fun getTagFragment(): String = HotelSearchFilterFragment.TAG

    override fun getNewFragment(): Fragment {
        return HotelSearchFilterFragment.createInstance(intent.getStringExtra(CommonParam.ARG_CACHE_FILTER_ID))
    }

    companion object {
        private const val ARG_SAVED_TAG = "saved_fragment_tag"
        fun createIntent(context: Context, cacheFilterId: String?): Intent =
                Intent(context, HotelSearchFilterActivity::class.java)
                        .putExtra(CommonParam.ARG_CACHE_FILTER_ID, cacheFilterId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            currentTag = tagFragment
        } else {
            currentTag = savedInstanceState.getString(ARG_SAVED_TAG, HotelSearchFilterFragment.TAG)
        }
        updateTitle(getString(com.tokopedia.design.R.string.label_filter))
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(ARG_SAVED_TAG, currentTag)
    }
}