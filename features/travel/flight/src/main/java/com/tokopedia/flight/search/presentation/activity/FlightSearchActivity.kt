package com.tokopedia.flight.search.presentation.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.presentation.model.FlightAirportModel
import com.tokopedia.flight.booking.presentation.activity.FlightBookingActivity
import com.tokopedia.flight.common.constant.FlightFlowConstant
import com.tokopedia.flight.common.constant.FlightFlowExtraConstant
import com.tokopedia.flight.common.util.FlightAnalyticsCategory
import com.tokopedia.flight.common.util.FlightAnalyticsDefaults
import com.tokopedia.flight.common.util.FlightAnalyticsScreenName
import com.tokopedia.flight.common.util.FlightAnalyticsTrackerId
import com.tokopedia.flight.common.util.FlightFlowUtil
import com.tokopedia.flight.common.view.BaseFlightActivity
import com.tokopedia.flight.homepage.presentation.model.FlightPassengerModel
import com.tokopedia.flight.search.presentation.fragment.FlightSearchFragment
import com.tokopedia.flight.search.presentation.model.FlightPriceModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
import com.tokopedia.flight.search.presentation.util.FlightSearchCache
import com.tokopedia.flight.search_universal.presentation.bottomsheet.FlightSearchUniversalBottomSheet
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.universal_sharing.tracker.UniversalSharebottomSheetTracker
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.date.DateUtil
import kotlinx.android.synthetic.main.activity_flight_search.*
import java.util.*

/**
 * @author by furqan on 06/04/2020
 */
open class FlightSearchActivity :
    BaseFlightActivity(),
    FlightSearchFragment.OnFlightSearchFragmentListener,
    FlightSearchUniversalBottomSheet.Listener {

    protected lateinit var flightSearchPassDataModel: FlightSearchPassDataModel
    protected var dateString = ""
    protected var passengerString = ""
    protected var classString = ""
    private var shareTitle = ""
    var isSearchFromWidget = false

    private lateinit var wrapper: LinearLayout

    private lateinit var coachMarkCache: FlightSearchCache

    private val shareTracker: UniversalSharebottomSheetTracker by lazy {
        UniversalSharebottomSheetTracker(userSession as UserSession)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initializeDataFromExtras()
        super.onCreate(savedInstanceState)

        coachMarkCache = FlightSearchCache(this)

        setupSearchToolbarAction()
        setupSearchToolbarText()
    }

    override fun getScreenName(): String = FlightAnalyticsScreenName.SEARCH

    override fun getLayoutRes(): Int = R.layout.activity_flight_search

    override fun getToolbarResourceID(): Int = R.id.flight_search_header

    override fun getParentViewResourceID(): Int = R.id.flight_search_parent_view

    override fun getNewFragment(): Fragment? =
        FlightSearchFragment.newInstance(flightSearchPassDataModel)

    override fun onCreateOptionsMenu(menu: Menu): Boolean = false

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_RETURN, REQUEST_CODE_BOOKING -> {
                if (data != null) {
                    when (data.getIntExtra(FlightFlowExtraConstant.EXTRA_FLOW_DATA.value, 0)) {
                        FlightFlowConstant.PRICE_CHANGE.value -> {
                            if (fragment is FlightSearchFragment) {
                                (fragment as FlightSearchFragment).resetDateAndReload(true)
                            }
                        }
                        FlightFlowConstant.EXPIRED_JOURNEY.value -> {
                            FlightFlowUtil.actionSetResultAndClose(
                                this,
                                intent,
                                FlightFlowConstant.EXPIRED_JOURNEY.value
                            )
                        }
                        FlightFlowConstant.CHANGE_SEARCH_PARAM.value -> {
                            if (fragment is FlightSearchFragment) {
                                (fragment as FlightSearchFragment).setSearchPassData((data.getParcelableExtra(EXTRA_PASS_DATA) as? FlightSearchPassDataModel ?: FlightSearchPassDataModel()))
                                (fragment as FlightSearchFragment).resetDateAndReload(true)
                            }
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
        flightSearchPassDataModel.flightPassengerModel = flightSearchParams.flightPassengerModel
        flightSearchPassDataModel.flightClass = flightSearchParams.flightClass
        flightSearchPassDataModel.searchRequestId = ""

        if (validateSearchPassData(flightSearchParams)) {
            if (isReturnPage()) {
                val intent = Intent()
                intent.putExtra(EXTRA_PASS_DATA, flightSearchPassDataModel)
                FlightFlowUtil.actionSetResultAndClose(this, intent, FlightFlowConstant.CHANGE_SEARCH_PARAM.value)
            } else {
                if (fragment is FlightSearchFragment) {
                    (fragment as FlightSearchFragment).setSearchPassData(flightSearchPassDataModel)
                    (fragment as FlightSearchFragment).resetDateAndReload(true)
                }
            }
        }
    }

    override fun selectFlight(selectedFlightID: String, selectedTerm: String, flightPriceModel: FlightPriceModel, isBestPairing: Boolean, isCombineDone: Boolean, requestId: String) {
        flightSearchPassDataModel.searchRequestId = requestId
        if (flightSearchPassDataModel.isOneWay) {
            startActivityForResult(
                FlightBookingActivity.getCallingIntent(
                    this,
                    flightSearchPassDataModel,
                    selectedFlightID,
                    selectedTerm,
                    flightPriceModel
                ),
                REQUEST_CODE_BOOKING
            )
        } else {
            startActivityForResult(
                FlightSearchReturnActivity.getCallingIntent(
                    this,
                    flightSearchPassDataModel,
                    selectedFlightID,
                    selectedTerm,
                    isBestPairing,
                    flightPriceModel,
                    isCombineDone
                ),
                REQUEST_CODE_RETURN
            )
        }
    }

    override fun changeDate(flightSearchPassDataModel: FlightSearchPassDataModel) {
        this.flightSearchPassDataModel = flightSearchPassDataModel
        initializeToolbarData()
        setupSearchToolbarText()
    }

    open fun initializeToolbarData() {
        dateString = DateUtil.formatDate(
            DateUtil.YYYY_MM_DD,
            DateUtil.EEE_DD_MMM_YY,
            flightSearchPassDataModel.departureDate
        )
        passengerString = buildPassengerTextFormatted(flightSearchPassDataModel.flightPassengerModel)
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
            ?: FlightSearchPassDataModel()
        isSearchFromWidget = intent.getBooleanExtra(EXTRA_SEARCH_FROM_WIDGET, false)
        initializeToolbarData()
    }

    private fun setupSearchToolbarAction() {
        wrapper = LinearLayout(this)
        wrapper.apply {
            val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            background = resources.getDrawable(R.drawable.bg_white_rounded_no_stroke)
            layoutParams = param
        }
        wrapper.setPadding(DIMEN_4_IN_PX, DIMEN_4_IN_PX, DIMEN_4_IN_PX, DIMEN_4_IN_PX)

        val imageView = ImageView(this)
        val param = LinearLayout.LayoutParams(DIMEN_24_IN_PX, DIMEN_24_IN_PX)
        imageView.layoutParams = param
        imageView.setImageResource(R.drawable.ic_flight_edit)

        val imageViewShare = IconUnify(this)
        val paramShare = LinearLayout.LayoutParams(DIMEN_24_IN_PX, DIMEN_24_IN_PX)
        paramShare.leftMargin = DIMEN_14_IN_PX
        imageViewShare.layoutParams = paramShare
        imageViewShare.setImage(IconUnify.SHARE_MOBILE)

        wrapper.addView(imageView)
        wrapper.addView(imageViewShare)
        imageView.tag = TAG_CHANGE_BUTTON
        imageView.setOnClickListener {
            showChangeSearchBottomSheet()
        }

        imageViewShare.setOnClickListener {
            shareTracker.trackClickShare(
                flightSearchPassDataModel.searchRequestId,
                FlightAnalyticsCategory.CLICK_DG_FLIGHT_PAGE,
                FlightAnalyticsTrackerId.CLICK_SHARE,
                FlightAnalyticsDefaults.DIGITAL_CURRENT_SITE
            )

            showUniversalBottomSheet()
        }

        flight_search_header.addCustomRightContent(wrapper)
        flight_search_header.isShowBackButton = true
        flight_search_header.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupSearchToolbarText() {
        val departureCode = if (getDepartureAirport().airportCode != null && getDepartureAirport().airportCode.isNotEmpty()) {
            getDepartureAirport().airportCode
        } else {
            getDepartureAirport().cityCode
        }
        val arrivalCode = if (getArrivalAirport().airportCode != null && getArrivalAirport().airportCode.isNotEmpty()) {
            getArrivalAirport().airportCode
        } else {
            getArrivalAirport().cityCode
        }
        val title = "${getDepartureAirport().cityName} ($departureCode) ➝ ${getArrivalAirport().cityName} ($arrivalCode)"
        val subtitle = "$dateString | $passengerString"
        flight_search_header.title = title
        flight_search_header.subtitle = subtitle
        flight_search_header.subheaderView?.setTextColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_NN950_44))

        supportActionBar?.elevation = 0f
    }

    private fun showUniversalBottomSheet() {
        val departureCode = if (getDepartureAirport().airportCode != null && getDepartureAirport().airportCode.isNotEmpty()) {
            getDepartureAirport().airportCode
        } else {
            getDepartureAirport().cityCode
        }
        val arrivalCode = if (getArrivalAirport().airportCode != null && getArrivalAirport().airportCode.isNotEmpty()) {
            getArrivalAirport().airportCode
        } else {
            getArrivalAirport().cityCode
        }

        val date = DateUtil.formatDate(
            DateUtil.YYYY_MM_DD,
            EEE_DD_MMM_YYYY,
            flightSearchPassDataModel.departureDate
        )
        shareTitle = "$departureCode ➝ $arrivalCode | $date"

        SharingUtil.saveImageFromURLToStorage(this, IMG_FLIGHT_SHARE) { pathImg ->
            UniversalShareBottomSheet.createInstance().apply {
                imageSaved(pathImg)
                val bottomSheet = this
                init(object : ShareBottomsheetListener {
                    override fun onShareOptionClicked(shareModel: ShareModel) {
                        onClickChannelShare(shareModel, bottomSheet)
                        shareTracker.trackClickShareChannel(
                            flightSearchPassDataModel.searchRequestId,
                            shareModel.channel ?: "",
                            UniversalShareBottomSheet.KEY_IMAGE_DEFAULT,
                            FlightAnalyticsCategory.DG_FLIGHT_PAGE,
                            FlightAnalyticsTrackerId.CLICK_CHANNEL,
                            FlightAnalyticsDefaults.DIGITAL_CURRENT_SITE
                        )
                    }

                    override fun onCloseOptionClicked() {
                        shareTracker.trackCloseShare(
                            flightSearchPassDataModel.searchRequestId,
                            FlightAnalyticsCategory.DG_FLIGHT_PAGE,
                            FlightAnalyticsTrackerId.CLOSE_SHARE,
                            FlightAnalyticsDefaults.DIGITAL_CURRENT_SITE
                        )
                    }
                })
                setUtmCampaignData("Flight", userSession.userId, flightSearchPassDataModel.searchRequestId, "share")
                setMetaData(shareTitle, IMG_FLIGHT_SHARE)
                setOgImageUrl(IMG_FLIGHT_SHARE)

                this.setShowListener {
                    shareTracker.trackViewShare(
                        flightSearchPassDataModel.searchRequestId,
                        FlightAnalyticsCategory.DG_FLIGHT_PAGE,
                        FlightAnalyticsTrackerId.VIEW_SHARE,
                        FlightAnalyticsDefaults.DIGITAL_CURRENT_SITE
                    )
                }
            }.show(supportFragmentManager, "")
        }
    }

    private fun onClickChannelShare(shareModel: ShareModel, bottomSheet: UniversalShareBottomSheet) {
        val departureTime = DateUtil.formatDate(
            DateUtil.YYYY_MM_DD,
            EEEE_DD_MMM_YY,
            flightSearchPassDataModel.departureDate
        )
        val linkerData = getLinkerData(shareModel)
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(
                0,
                linkerData,
                object : ShareCallback {
                    override fun urlCreated(linkerShareData: LinkerShareResult) {
                        val shareString = getString(
                            R.string.share_string_container,
                            flight_search_header.title.toString(),
                            departureTime,
                            linkerShareData.url
                        )
                        SharingUtil.executeShareIntent(
                            shareModel,
                            linkerShareData,
                            this@FlightSearchActivity,
                            fragment?.view,
                            shareString
                        )
                        bottomSheet.dismiss()
                    }

                    override fun onError(linkerError: LinkerError) {
                    }
                }
            )
        )
    }

    private fun getLinkerData(shareModel: ShareModel): LinkerShareData {
        val uri = Uri.parse(flightSearchPassDataModel.linkUrl)
        val query = uri.query
        val linkerData = LinkerData().apply {
            this.id = flightSearchPassDataModel.searchRequestId
            this.feature = shareModel.feature
            this.campaign = shareModel.campaign
            this.channel = shareModel.channel
            this.uri = getDesktopUri()
            this.ogTitle = shareTitle
            this.ogImageUrl = shareModel.ogImgUrl
            this.description = ""
            this.type = LinkerData.FLIGHT_TYPE
            this.deepLink = getDeeplinkFlight()
        }
        return LinkerShareData().apply {
            this.linkerData = linkerData
        }
    }

    private fun getDeeplinkFlight(): String {
        return if (flightSearchPassDataModel.isOneWay) {
            String.format(
                DEEPLINK_FLIGHT_SEARCH_ONEWAY,
                flightSearchPassDataModel.getDepartureAirport(false),
                flightSearchPassDataModel.departureAirport.cityName,
                flightSearchPassDataModel.getArrivalAirport(false),
                flightSearchPassDataModel.arrivalAirport.cityName,
                flightSearchPassDataModel.departureDate,
                flightSearchPassDataModel.flightPassengerModel.adult,
                flightSearchPassDataModel.flightPassengerModel.children,
                flightSearchPassDataModel.flightPassengerModel.infant,
                flightSearchPassDataModel.flightClass.id
            )
        } else {
            String.format(
                DEEPLINK_FLIGHT_TWOWAY,
                flightSearchPassDataModel.getDepartureAirport(false),
                flightSearchPassDataModel.departureAirport.cityName,
                flightSearchPassDataModel.getArrivalAirport(false),
                flightSearchPassDataModel.arrivalAirport.cityName,
                flightSearchPassDataModel.departureDate,
                flightSearchPassDataModel.getDepartureAirport(true),
                flightSearchPassDataModel.arrivalAirport.cityName,
                flightSearchPassDataModel.getArrivalAirport(true),
                flightSearchPassDataModel.departureAirport.cityName,
                flightSearchPassDataModel.returnDate,
                flightSearchPassDataModel.flightPassengerModel.adult,
                flightSearchPassDataModel.flightPassengerModel.children,
                flightSearchPassDataModel.flightPassengerModel.infant,
                flightSearchPassDataModel.flightClass.id
            )
        }
    }

    private fun getDesktopUri(): String {
        val departureDate = DateUtil.formatDate(
            DateUtil.YYYY_MM_DD,
            DateUtil.YYYYMMDD,
            flightSearchPassDataModel.departureDate
        )
        val returnDate = DateUtil.formatDate(
            DateUtil.YYYY_MM_DD,
            DateUtil.YYYYMMDD,
            flightSearchPassDataModel.returnDate
        )
        val departureCode = if (getDepartureAirport().airportCode != null && getDepartureAirport().airportCode.isNotEmpty()) {
            getDepartureAirport().airportCode
        } else {
            getDepartureAirport().cityCode
        }
        val arrivalCode = if (getArrivalAirport().airportCode != null && getArrivalAirport().airportCode.isNotEmpty()) {
            getArrivalAirport().airportCode
        } else {
            getArrivalAirport().cityCode
        }
        val paramDate = if (flightSearchPassDataModel.isOneWay) {
            departureDate
        } else {
            "$departureDate,$returnDate"
        }
        val paramTrip = "$departureCode.$arrivalCode"
        return String.format(
            DESKTOP_URL_FLIGHT_SEARCH,
            paramTrip,
            paramDate,
            flightSearchPassDataModel.flightPassengerModel.adult.toString(),
            flightSearchPassDataModel.flightPassengerModel.children.toString(),
            flightSearchPassDataModel.flightPassengerModel.infant.toString(),
            flightSearchPassDataModel.flightClass.id.toString()
        )
    }

    fun showChangeSearchBottomSheet() {
        flightAnalytics.eventChangeSearchClick(if (userSession.isLoggedIn) userSession.userId else "")

        val flightChangeSearchBottomSheet = FlightSearchUniversalBottomSheet.getInstance()
        flightChangeSearchBottomSheet.listener = this
        flightChangeSearchBottomSheet.setShowListener { flightChangeSearchBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        flightChangeSearchBottomSheet.show(supportFragmentManager, FlightSearchUniversalBottomSheet.TAG_SEARCH_FORM)
    }

    fun setupAndShowCoachMarkSearchFromWidget() {
        if (::wrapper.isInitialized && isSearchFromWidget) {
            val coachMarkItems = arrayListOf<CoachMarkItem>()
            coachMarkItems.add(
                CoachMarkItem(
                    wrapper,
                    getString(R.string.flight_search_coach_mark_change_title),
                    getString(R.string.flight_search_coach_mark_from_widget)
                )
            )

            val coachMark = CoachMarkBuilder().build()
            coachMark.show(this, TAG_CHANGE_COACH_MARK, coachMarkItems)
            Handler().postDelayed({
                if (coachMark.isAdded && coachMark.isVisible) {
                    coachMark.dismissAllowingStateLoss()
                }
                isSearchFromWidget = false
            }, DELAY_THREE_SECONDS)
            coachMark.onFinishListener = {
                isSearchFromWidget = false
            }
            coachMark.overlayOnClickListener = (
                {
                    isSearchFromWidget = false
                }
                )
        }
    }

    fun setupAndShowCoachMark() {
        if (::wrapper.isInitialized) {
            val coachMarkItems = arrayListOf<CoachMarkItem>()
            coachMarkItems.add(
                CoachMarkItem(
                    wrapper,
                    getString(R.string.flight_search_coach_mark_change_title),
                    getString(R.string.flight_search_coach_mark_change_description)
                )
            )

            val coachMark = CoachMarkBuilder().build()
            coachMark.show(this, TAG_CHANGE_COACH_MARK, coachMarkItems)
            Handler().postDelayed({
                if (coachMark.isAdded && coachMark.isVisible) {
                    coachMark.dismissAllowingStateLoss()
                }
                coachMarkCache.setSearchCoachMarkIsShowed()
            }, DELAY_THREE_SECONDS)
            coachMark.onFinishListener = {
                coachMarkCache.setSearchCoachMarkIsShowed()
            }
            coachMark.overlayOnClickListener = (
                {
                    coachMarkCache.setSearchCoachMarkIsShowed()
                }
                )
        }
    }

    private fun validateSearchPassData(flightSearchData: FlightSearchPassDataModel): Boolean {
        var isValid = true

        if (flightSearchData.departureAirport.cityCode != null &&
            flightSearchData.departureAirport.cityCode.isNotEmpty() &&
            flightSearchData.arrivalAirport.cityCode != null &&
            flightSearchData.arrivalAirport.cityCode.isNotEmpty() &&
            flightSearchData.departureAirport.cityCode == flightSearchData.arrivalAirport.cityCode
        ) {
            isValid = false
            showMessageErrorInSnackbar(R.string.flight_dashboard_arrival_departure_same_error)
        } else if (flightSearchData.departureAirport.cityAirports != null &&
            flightSearchData.departureAirport.cityAirports.isNotEmpty() &&
            flightSearchData.departureAirport.cityAirports.contains(flightSearchData.arrivalAirport.airportCode)
        ) {
            isValid = false
            showMessageErrorInSnackbar(R.string.flight_dashboard_arrival_departure_same_error)
        } else if (flightSearchData.arrivalAirport.cityAirports != null &&
            flightSearchData.arrivalAirport.cityAirports.isNotEmpty() &&
            flightSearchData.arrivalAirport.cityAirports.contains(flightSearchData.departureAirport.airportCode)
        ) {
            isValid = false
            showMessageErrorInSnackbar(R.string.flight_dashboard_arrival_departure_same_error)
        } else if (flightSearchData.departureAirport.airportCode != null &&
            flightSearchData.departureAirport.airportCode.isNotEmpty() &&
            flightSearchData.arrivalAirport.airportCode != null &&
            flightSearchData.arrivalAirport.airportCode.isNotEmpty() &&
            flightSearchData.departureAirport.airportCode == flightSearchData.arrivalAirport.airportCode
        ) {
            isValid = false
            showMessageErrorInSnackbar(R.string.flight_dashboard_arrival_departure_same_error)
        } else if (flightSearchData.departureAirport.cityName != null &&
            flightSearchData.departureAirport.cityName.isNotEmpty() &&
            flightSearchData.arrivalAirport.cityName != null &&
            flightSearchData.arrivalAirport.cityName.isNotEmpty() &&
            flightSearchData.departureAirport.cityName == flightSearchData.arrivalAirport.cityName
        ) {
            isValid = false
            showMessageErrorInSnackbar(R.string.flight_dashboard_arrival_departure_same_error)
        }

        return isValid
    }

    private fun showMessageErrorInSnackbar(@StringRes stringResourceId: Int) {
        Toaster.build(
            findViewById(parentViewResourceID),
            getString(stringResourceId),
            Toaster.LENGTH_SHORT,
            Toaster.TYPE_ERROR,
            getString(R.string.flight_booking_action_okay)
        ).show()
    }

    companion object {
        const val TAG_CHANGE_COACH_MARK = "TagChangeSearchCoachMark"
        const val EXTRA_PASS_DATA = "EXTRA_PASS_DATA"
        const val EXTRA_SEARCH_FROM_WIDGET = "EXTRA_SEARCH_FROM_WIDGET"

        private const val TAG_CHANGE_BUTTON = "TagChangeSearchButton"

        private const val REQUEST_CODE_BOOKING = 10
        private const val REQUEST_CODE_RETURN = 11
        private const val DELAY_THREE_SECONDS: Long = 3000
        private val DIMEN_24_IN_PX = 24.toPx()
        private val DIMEN_4_IN_PX = 4.toPx()
        private val DIMEN_14_IN_PX = 14.toPx()
        private const val IMG_FLIGHT_SHARE = "https://images.tokopedia.net/img/flight/img/img_flight_thumbnail_share.jpg"
        private const val EEE_DD_MMM_YYYY = "EEE, dd MMM yyyy"
        private const val EEEE_DD_MMM_YY = "EEEE, dd MMM yyyy"
        private const val DESKTOP_URL_FLIGHT_SEARCH = "https://www.tokopedia.com/flight/search?r=%s&d=%s&a=%s&c=%s&i=%s&k=%s"
        private const val DEEPLINK_FLIGHT_SEARCH_ONEWAY = "tokopedia://pesawat/search?dest=%s_%s_%s_%s_%s&a=%s&c=%s&i=%s&s=%s&auto_search=1"
        private const val DEEPLINK_FLIGHT_TWOWAY = "tokopedia://pesawat/search?dest=%s_%s_%s_%s_%s,%s_%s_%s_%s_%s&a=%s&c=%s&i=%s&s=%s&auto_search=1"

        fun getCallingIntent(
            context: Context,
            passDataModel: FlightSearchPassDataModel,
            isSearchFromWidget: Boolean
        ): Intent =
            Intent(context, FlightSearchActivity::class.java)
                .putExtra(EXTRA_PASS_DATA, passDataModel)
                .putExtra(EXTRA_SEARCH_FROM_WIDGET, isSearchFromWidget)
    }
}
