package com.tokopedia.flight.searchV4.presentation.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.view.model.FlightAirportModel
import com.tokopedia.flight.common.constant.FlightFlowConstant
import com.tokopedia.flight.common.constant.FlightFlowExtraConstant
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.common.util.FlightFlowUtil
import com.tokopedia.flight.common.view.BaseFlightActivity
import com.tokopedia.flight.dashboard.view.fragment.model.FlightPassengerModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
import com.tokopedia.flight.search.util.FlightSearchCache
import com.tokopedia.flight.searchV4.presentation.fragment.FlightSearchFragment
import com.tokopedia.flight.search_universal.presentation.bottomsheet.FlightSearchUniversalBottomSheet
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.activity_flight_search.*

/**
 * @author by furqan on 06/04/2020
 */
open class FlightSearchActivity : BaseFlightActivity(),
        FlightSearchUniversalBottomSheet.Listener {

    protected lateinit var flightSearchPassDataModel: FlightSearchPassDataModel
    protected var dateString = ""
    protected var passengerString = ""
    protected var classString = ""

    private lateinit var wrapper: LinearLayout

    private lateinit var coachMarkCache: FlightSearchCache

    override fun onCreate(savedInstanceState: Bundle?) {
        initializeDataFromExtras()
        super.onCreate(savedInstanceState)

        coachMarkCache = FlightSearchCache(this)

        setupSearchToolbarAction()
        setupSearchToolbarText()
    }

    override fun getScreenName(): String = FlightAnalytics.Screen.SEARCH

    override fun getLayoutRes(): Int = R.layout.activity_flight_search

    override fun getToolbarResourceID(): Int = R.id.flight_search_header

    override fun getParentViewResourceID(): Int = R.id.flight_search_parent_view

    override fun getNewFragment(): Fragment? =
            FlightSearchFragment.newInstance(flightSearchPassDataModel)

    override fun onCreateOptionsMenu(menu: Menu?): Boolean = false

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_RETURN, REQUEST_CODE_BOOKING -> {
                if (data != null) {
                    when (data.getIntExtra(FlightFlowExtraConstant.EXTRA_FLOW_DATA, 0)) {
                        FlightFlowConstant.PRICE_CHANGE -> {
                            /*if (fragment is FlightSearchFragment) {
                                (fragment as FlightSearchFragment).flightSearchPresenter
                                        .attachView(fragment as FlightSearchFragment)
                                (fragment as FlightSearchFragment).refreshData()
                            }*/
                        }
                        FlightFlowConstant.EXPIRED_JOURNEY -> {
                            FlightFlowUtil.actionSetResultAndClose(this, intent,
                                    FlightFlowConstant.EXPIRED_JOURNEY)
                        }
                        FlightFlowConstant.CHANGE_SEARCH_PARAM -> {
                            /*if (fragment is FlightSearchFragment) {
                                (fragment as FlightSearchFragment).flightSearchPresenter
                                        .attachView(fragment as FlightSearchFragment)
                                (fragment as FlightSearchFragment)
                                        .setSearchPassData((data.getParcelableExtra(EXTRA_PASS_DATA) as FlightSearchPassDataModel))
                                (fragment as FlightSearchFragment).refreshData()
                            }*/
                        }
                    }
                }
            }
        }
    }

    override fun onSaveSearchParams(flightSearchParams: FlightSearchPassDataModel) {
        flightSearchPassDataModel.departureAirport = flightSearchParams.departureAirport
        flightSearchPassDataModel.arrivalAirport = flightSearchParams.arrivalAirport
        flightSearchPassDataModel.isOneWay = flightSearchParams.isOneWay
        flightSearchPassDataModel.departureDate = flightSearchParams.departureDate
        flightSearchPassDataModel.returnDate = flightSearchParams.returnDate
        flightSearchPassDataModel.flightPassengerViewModel = flightSearchParams.flightPassengerViewModel
        flightSearchPassDataModel.flightClass = flightSearchParams.flightClass

        if (isReturnPage()) {
            val intent = Intent()
            intent.putExtra(EXTRA_PASS_DATA, flightSearchPassDataModel)
            FlightFlowUtil.actionSetResultAndClose(this, intent, FlightFlowConstant.CHANGE_SEARCH_PARAM)
        } else {
            /*if (fragment is FlightSearchFragment) {
                (fragment as FlightSearchFragment).flightSearchPresenter
                        .attachView(fragment as FlightSearchFragment)
                (fragment as FlightSearchFragment).setSearchPassData(passDataModel)
                (fragment as FlightSearchFragment).refreshData()
            }*/
        }
    }

    open fun initializeToolbarData() {
        dateString = FlightDateUtil.formatDate(
                FlightDateUtil.DEFAULT_FORMAT,
                FlightDateUtil.DEFAULT_VIEW_FORMAT,
                flightSearchPassDataModel.departureDate
        )
        passengerString = buildPassengerTextFormatted(flightSearchPassDataModel.flightPassengerViewModel)
        classString = flightSearchPassDataModel.flightClass.title
    }

    open fun getDepartureAirport(): FlightAirportModel = flightSearchPassDataModel.departureAirport

    open fun getArrivalAirport(): FlightAirportModel = flightSearchPassDataModel.arrivalAirport

    open fun isReturnPage(): Boolean = false

    protected fun buildPassengerTextFormatted(passData: FlightPassengerModel): String {
        var passengerFmt = ""

        if (passData.adult > 0) {
            passengerFmt += "${passData.adult}  ${getString(R.string.flight_dashboard_adult_passenger)}"
            if (passData.children > 0) {
                passengerFmt += ", ${passData.children} ${getString(R.string.flight_dashboard_adult_children)}"
            }
            if (passData.infant > 0) {
                passengerFmt += ", ${passData.infant} ${getString(R.string.flight_dashboard_adult_infant)}"
            }
        }

        return passengerFmt
    }

    private fun initializeDataFromExtras() {
        flightSearchPassDataModel = intent.getParcelableExtra(EXTRA_PASS_DATA)
        initializeToolbarData()
    }

    private fun setupSearchToolbarAction() {
        wrapper = LinearLayout(this)
        wrapper.apply {
            val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            setBackgroundColor(Color.WHITE)
            layoutParams = param
        }

        val imageView = ImageView(this)
        val param = LinearLayout.LayoutParams(DIMEN_24_IN_PX, DIMEN_24_IN_PX)
        imageView.layoutParams = param
        imageView.setImage(R.drawable.ic_flight_edit, CORNER_RADIUS)

        wrapper.addView(imageView)
        wrapper.setOnClickListener {
            showChangeSearchBottomSheet()
        }

        flight_search_header.addCustomRightContent(wrapper)
        flight_search_header.isShowBackButton = true
        flight_search_header.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupSearchToolbarText() {
        val title = "${getDepartureAirport().cityName} ➝ ${getArrivalAirport().cityName}"
        val subtitle = "$dateString | $passengerString | $classString"

        flight_search_header.title = title
        flight_search_header.subtitle = subtitle

        supportActionBar?.elevation = 0f
    }

    private fun showChangeSearchBottomSheet() {
        flightAnalytics.eventChangeSearchClick()

        val flightChangeSearchBottomSheet = FlightSearchUniversalBottomSheet.getInstance()
        flightChangeSearchBottomSheet.listener = this
        flightChangeSearchBottomSheet.setShowListener { flightChangeSearchBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        flightChangeSearchBottomSheet.show(supportFragmentManager, FlightSearchUniversalBottomSheet.TAG_SEARCH_FORM)
    }

    fun setupAndShowCoachMark() {
        if (::wrapper.isInitialized) {
            val coachMarkItems = arrayListOf<CoachMarkItem>()
            coachMarkItems.add(CoachMarkItem(
                    wrapper,
                    getString(R.string.flight_search_coach_mark_change_title),
                    getString(R.string.flight_search_coach_mark_change_description)
            ))

            val coachMark = CoachMarkBuilder().build()
            coachMark.show(this, TAG_CHANGE_COACH_MARK, coachMarkItems)
            Handler().postDelayed({
                if (coachMark.isAdded && coachMark.isVisible) {
                    coachMark.dismiss()
                }
                coachMarkCache.setSearchCoachMarkIsShowed()
            }, DELAY_THREE_SECONDS)
            coachMark.onFinishListener = {
                coachMarkCache.setSearchCoachMarkIsShowed()
            }
            coachMark.overlayOnClickListener = ({
                coachMarkCache.setSearchCoachMarkIsShowed()
            })
        }
    }

    companion object {
        const val TAG_CHANGE_COACH_MARK = "TagChangeSearchCoachMark"
        const val EXTRA_PASS_DATA = "EXTRA_PASS_DATA"

        private const val REQUEST_CODE_BOOKING = 10
        private const val REQUEST_CODE_RETURN = 11
        private const val DELAY_THREE_SECONDS: Long = 3000
        private val DIMEN_24_IN_PX = 24.toPx()
        private const val CORNER_RADIUS = 8f

        fun getCallingIntent(context: Context, passDataModel: FlightSearchPassDataModel): Intent =
                Intent(context, FlightSearchActivity::class.java)
                        .putExtra(EXTRA_PASS_DATA, passDataModel)

       /* fun getCallingIntent(context: Context): Intent {
            val passDataModel = FlightSearchPassDataModel(
                    "2020-10-01",
                    "",
                    true,
                    FlightPassengerModel(1, 0, 0),
                    FlightAirportModel().apply {
                        cityName = "Tokyo"
                        cityCode = "TKYA"
                    },
                    FlightAirportModel().apply {
                        cityName = "Jakarta"
                        cityCode = "JKTA"
                    },
                    FlightClassModel().apply {
                        id = 1
                        title = "Ekonomi"
                    },
                    "", ""
            )

            return getCallingIntent(context, passDataModel)
        }*/
    }
}