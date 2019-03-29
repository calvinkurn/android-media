package com.tokopedia.hotel.destination.view.activity

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.HotelModuleRouter
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.di.component.HotelComponent
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.destination.di.DaggerHotelDestinationComponent
import com.tokopedia.hotel.destination.di.HotelDestinationComponent
import com.tokopedia.hotel.destination.view.fragment.HotelRecommendationFragment
import com.tokopedia.hotel.destination.view.fragment.HotelSearchDestinationFragment
import com.tokopedia.hotel.destination.view.viewmodel.HotelDestinationViewModel
import kotlinx.android.synthetic.main.activity_hotel_destination.*
import javax.inject.Inject

/**
 * @author by jessica on 25/03/19
 */

class HotelDestinationActivity: HotelBaseActivity(), HasComponent<HotelDestinationComponent>, SearchInputView.Listener,
        SearchInputView.ResetListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var destinationViewModel: HotelDestinationViewModel

    override fun shouldShowOptionMenu(): Boolean = false

    override fun getNewFragment(): Fragment = HotelRecommendationFragment.getInstance()

    override fun getLayoutRes(): Int = R.layout.activity_hotel_destination

    override fun isShowCloseButton(): Boolean = true

    override fun getComponent(): HotelDestinationComponent {

        if (application is HotelModuleRouter) {
            return DaggerHotelDestinationComponent.builder()
                    .hotelComponent(getHotelComponent())
                    .build()
        }
        throw RuntimeException("Application must implement ModuleRouter")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle(intent.getStringExtra(EXTRA_TOOLBAR_TITLE))

        run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            destinationViewModel = viewModelProvider.get(HotelDestinationViewModel::class.java)
        }

        initView()
        initInjector()
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
        supportFragmentManager.beginTransaction().replace(R.id.fragment_frame_layout,
                HotelRecommendationFragment()).commit()
    }

    fun showSearchDestinationResult() {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_frame_layout,
                HotelSearchDestinationFragment()).addToBackStack(null).commit()
    }

    fun backToHotelRecommendation() {
        if (supportFragmentManager.backStackEntryCount > 0) supportFragmentManager.popBackStack()
    }

    override fun onSearchSubmitted(text: String?) {
        //ViewModel search Hotel based on Query
    }

    override fun onSearchTextChanged(text: String?) {
        if (TextUtils.isEmpty(text)) {
            backToHotelRecommendation()
        } else {
            showSearchDestinationResult()
            //ViewModel search Hotel based on Query
        }
    }

    override fun onSearchReset() {
        //delete search query
    }

    companion object {
        val EXTRA_TOOLBAR_TITLE = "EXTRA_TOOLBAR_TITLE"

        fun createInstance(activity: Activity, title: String): Intent {
            val intent = Intent(activity, HotelDestinationActivity::class.java)
            intent.putExtra(EXTRA_TOOLBAR_TITLE, title)
            return intent
        }
    }
}