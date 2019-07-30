package com.tokopedia.hotel.destination.view.activity

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.destination.di.DaggerHotelDestinationComponent
import com.tokopedia.hotel.destination.di.HotelDestinationComponent
import com.tokopedia.hotel.destination.view.fragment.HotelRecommendationFragment
import com.tokopedia.hotel.destination.view.fragment.HotelSearchDestinationFragment
import com.tokopedia.hotel.destination.view.viewmodel.HotelDestinationViewModel
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import kotlinx.android.synthetic.main.activity_hotel_destination.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by jessica on 25/03/19
 */

class HotelDestinationActivity : HotelBaseActivity(), HasComponent<HotelDestinationComponent>, SearchInputView.Listener,
        SearchInputView.ResetListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var destinationViewModel: HotelDestinationViewModel

    var isSearching: Boolean = false

    private var searchTemp = ""

    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    override fun shouldShowOptionMenu(): Boolean = false

    override fun getNewFragment(): Fragment = HotelRecommendationFragment.getInstance()

    override fun getLayoutRes(): Int = R.layout.activity_hotel_destination

    override fun isShowCloseButton(): Boolean = true

    override fun getComponent(): HotelDestinationComponent = DaggerHotelDestinationComponent.builder()
            .hotelComponent(getHotelComponent())
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle(getString(R.string.hotel_destination_toolbar_title))

        initInjector()
        initView()

        run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            destinationViewModel = viewModelProvider.get(HotelDestinationViewModel::class.java)
        }

        permissionCheckerHelper = PermissionCheckerHelper()
        destinationViewModel.setPermissionChecker(permissionCheckerHelper)
    }

    fun initView() {
        initEditText()
    }

    fun initEditText() {
        search_input_view.searchImageView.setImageDrawable(resources.getDrawable(R.drawable.ic_system_action_search_grayscale_24))
        search_input_view.closeImageButton.setImageDrawable(resources.getDrawable(R.drawable.ic_system_action_close_grayscale_16))
        search_input_view.setListener(this)
        search_input_view.setResetListener(this)
    }

    fun initInjector() {
        component.inject(this)
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

        if (text == searchTemp) return
        searchTemp = text

        GlobalScope.launch(Dispatchers.Main) {
            delay(300)
            if (text != searchTemp) return@launch
            if (text.isEmpty() && isSearching) {
                isSearching = false
                backToHotelRecommendation()
            } else if (text.isNotEmpty() && !isSearching) {
                isSearching = true
                showSearchDestinationResult()
            } else if (isSearching) {
                //search
                doSearch(text)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheckerHelper.onRequestPermissionsResult(this,
                    requestCode, permissions,
                    grantResults)
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
        overridePendingTransition(R.anim.travel_anim_stay, R.anim.travel_slide_out_up)
    }

    companion object {
        val SEARCH_DESTINATION_FRAGMENT_TAG = "SEARCH_DESTINATION"

        const val HOTEL_DESTINATION_ID = "destinationID"
        const val HOTEL_DESTINATION_NAME = "name"
        const val HOTEL_DESTINATION_TYPE = "type"
        const val HOTEL_CURRENT_LOCATION_LANG = "lang"
        const val HOTEL_CURRENT_LOCATION_LAT = "lat"

        fun createInstance(context: Context): Intent =
                Intent(context, HotelDestinationActivity::class.java)
    }
}