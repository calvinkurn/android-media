package com.tokopedia.hotel.destination.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.design.component.EditTextCompat
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.destination.di.DaggerHotelDestinationComponent
import com.tokopedia.hotel.destination.di.HotelDestinationComponent
import com.tokopedia.hotel.destination.view.fragment.HotelRecommendationFragment
import com.tokopedia.hotel.destination.view.fragment.HotelSearchDestinationFragment
import kotlinx.android.synthetic.main.activity_hotel_destination.*
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

/**
 * @author by jessica on 25/03/19
 */

class HotelDestinationActivity: HotelBaseActivity(), HasComponent<HotelDestinationComponent>, SearchInputView.Listener,
        SearchInputView.ResetListener {

    var isSearching: Boolean = false

    lateinit var searchInputView: EditTextCompat

    override fun shouldShowOptionMenu(): Boolean = false

    override fun getNewFragment(): Fragment = HotelRecommendationFragment.getInstance()

    override fun getLayoutRes(): Int = R.layout.activity_hotel_destination

    override fun isShowCloseButton(): Boolean = true

    override fun getComponent(): HotelDestinationComponent = DaggerHotelDestinationComponent.builder()
            .hotelComponent(getHotelComponent())
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle(intent.getStringExtra(EXTRA_TOOLBAR_TITLE))

        initInjector()
        initView()
    }

    fun initView() {
        initEditText()
        initHotelRecommendationFragment()
    }

    fun initEditText() {
        //set image close button
        search_input_view.setListener(this)
        search_input_view.setResetListener(this)
    }

    fun initInjector() {
        component.inject(this)
    }

    fun initHotelRecommendationFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.parent_view,
                HotelRecommendationFragment()).commit()
    }

    fun showSearchDestinationResult() {
        supportFragmentManager.beginTransaction().replace(R.id.parent_view,
                HotelSearchDestinationFragment(), SEARCH_DESTINATION_FRAGMENT_TAG).addToBackStack(null).commit()
    }

    fun backToHotelRecommendation() {
        if (supportFragmentManager.backStackEntryCount > 0) supportFragmentManager.popBackStack()
    }

    override fun onSearchSubmitted(text: String?) {
        //ViewModel search Hotel based on Query
    }

    override fun onSearchTextChanged(text: String) {
        launch(Dispatchers.Main) {
            delay(300)
            if (text.isEmpty() && isSearching) {
                isSearching = false
                backToHotelRecommendation()
            } else if (text.isNotEmpty() && !isSearching && text.length > 2){
                isSearching = true
                showSearchDestinationResult()
            } else if (isSearching) {
                //search
                doSearch(text)
            }
        }
    }

    fun doSearch(text: String) {
        if (supportFragmentManager.findFragmentById(R.id.parent_view) is HotelSearchDestinationFragment)
            (supportFragmentManager.findFragmentById(R.id.parent_view) as HotelSearchDestinationFragment).onSearchQueryChange(text)
    }

    override fun onSearchReset() {
        //delete search query
    }

    override fun onBackPressed() {
        finish()
    }
    companion object {
        val EXTRA_TOOLBAR_TITLE = "EXTRA_TOOLBAR_TITLE"
        val SEARCH_DESTINATION_FRAGMENT_TAG = "SEARCH_DESTINATION"

        const val HOTEL_DESTINATION_ID = "destinationID"
        const val HOTEL_DESTINATION_NAME = "name"
        const val HOTEL_DESTINATION_TYPE = "type"
        const val HOTEL_CURRENT_LOCATION_LANG = "lang"
        const val HOTEL_CURRENT_LOCATION_LAT = "lat"

        fun createInstance(activity: Activity, title: String): Intent {
            val intent = Intent(activity, HotelDestinationActivity::class.java)
            intent.putExtra(EXTRA_TOOLBAR_TITLE, title)
            return intent
        }
    }
}