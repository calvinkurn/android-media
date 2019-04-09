package com.tokopedia.hotel.search.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.hotel.R
import com.tokopedia.hotel.search.data.util.CommonParam
import com.tokopedia.hotel.search.presentation.fragment.HotelSearchFilterFragment

class HotelSearchFilterActivity: BaseSimpleActivity() {
    lateinit var currentTag: String

    override fun getLayoutRes(): Int = R.layout.activity_hotel_search_filter

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
        if (savedInstanceState == null){
            currentTag = tagFragment
        } else {
            currentTag = savedInstanceState.getString(ARG_SAVED_TAG, HotelSearchFilterFragment.TAG)
        }
        updateCloseButton()
    }

    private fun updateCloseButton(){
        if (supportFragmentManager.backStackEntryCount > 1){
            supportActionBar?.setHomeAsUpIndicator(null)
        } else {
            supportActionBar?.setHomeAsUpIndicator(ContextCompat.getDrawable(this,
                    com.tokopedia.abstraction.R.drawable.ic_close_default))
        }
    }

    override fun getTagFragment(): String = HotelSearchFilterFragment.TAG

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(ARG_SAVED_TAG, currentTag)
    }
}