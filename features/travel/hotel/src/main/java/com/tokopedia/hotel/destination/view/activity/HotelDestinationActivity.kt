package com.tokopedia.hotel.destination.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.destination.di.DaggerHotelDestinationComponent
import com.tokopedia.hotel.destination.di.HotelDestinationComponent
import com.tokopedia.hotel.destination.view.fragment.HotelRecommendationFragment
import com.tokopedia.hotel.destination.view.fragment.HotelSearchDestinationFragment
import com.tokopedia.hotel.destination.view.widget.HotelSearchInputView
import kotlinx.android.synthetic.main.activity_hotel_destination.*
import kotlinx.coroutines.*

/**
 * @author by jessica on 25/03/19
 */

class HotelDestinationActivity : HotelBaseActivity(), HasComponent<HotelDestinationComponent>, HotelSearchInputView.ActionListener {

    var isSearching: Boolean = false

    private var searchTemp = ""
    private var onTextChangedJob: Job? = null

    override fun shouldShowOptionMenu(): Boolean = false

    override fun getNewFragment(): Fragment = HotelRecommendationFragment.getInstance()

    override fun getLayoutRes(): Int = R.layout.activity_hotel_destination

    override fun getParentViewResourceID(): Int = R.id.parent_view

    override fun getToolbarResourceID(): Int = R.id.toolbar_hotel_destination

    override fun isShowCloseButton(): Boolean = true

    override fun getComponent(): HotelDestinationComponent = DaggerHotelDestinationComponent.builder()
        .hotelComponent(getHotelComponent())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle(getString(R.string.hotel_destination_toolbar_title))

        initInjector()
        initView()
    }

    fun initView() {
        initEditText()
    }

    private fun initEditText() {
        search_input_view.actionListener = this
        search_input_view.buildView()
    }

    fun initInjector() {
        component.inject(this)
    }

    private fun showSearchDestinationResult() {
        supportFragmentManager.beginTransaction().replace(
            R.id.parent_view,
            HotelSearchDestinationFragment(),
            SEARCH_DESTINATION_FRAGMENT_TAG
        ).addToBackStack(null).commit()
    }

    private fun backToHotelRecommendation() {
        if (supportFragmentManager.backStackEntryCount > 0) supportFragmentManager.popBackStack()
    }

    override fun onSearchTextChanged(text: String) {
        if (text == searchTemp) return
        searchTemp = text
        onTextChangedJob?.cancel()
        onTextChangedJob = CoroutineScope(Dispatchers.Main).launch {
            delay(DEFAULT_DELAY_MS.toLong())
            if (text != searchTemp) return@launch
            if (text.length <= DEFAULT_MIN_CHARACTER && isSearching) {
                isSearching = false
                backToHotelRecommendation()
            } else if (text.isNotEmpty() && text.length >= DEFAULT_MIN_CHARACTER && !isSearching) {
                isSearching = true
                showSearchDestinationResult()
            } else if (isSearching) {
                // search
                doSearch(text)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onTextChangedJob?.cancel()
    }

    private fun doSearch(text: String) {
        if (supportFragmentManager.findFragmentById(R.id.parent_view) is HotelSearchDestinationFragment) {
            (supportFragmentManager.findFragmentById(R.id.parent_view) as HotelSearchDestinationFragment).onSearchQueryChange(text)
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(
            com.tokopedia.common.travel.R.anim.travel_anim_stay,
            com.tokopedia.common.travel.R.anim.travel_slide_out_up
        )
    }

    companion object {
        const val SEARCH_DESTINATION_FRAGMENT_TAG = "SEARCH_DESTINATION"
        const val DEFAULT_DELAY_MS = 500
        const val DEFAULT_MIN_CHARACTER = 2

        const val HOTEL_DESTINATION_NAME = "name"
        const val HOTEL_DESTINATION_SEARCH_TYPE = "search_type"
        const val HOTEL_DESTINATION_SEARCH_ID = "search_id"
        const val HOTEL_CURRENT_LOCATION_LANG = "lang"
        const val HOTEL_CURRENT_LOCATION_LAT = "lat"
        const val HOTEL_DESTINATION_RESULT_SOURCE = "source"

        fun createInstance(context: Context): Intent =
            Intent(context, HotelDestinationActivity::class.java)
    }
}
