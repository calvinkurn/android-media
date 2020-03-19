package com.tokopedia.flight.search_universal.presentation.widget

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.dashboard.view.fragment.cache.FlightDashboardCache
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightClassViewModel
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.layout_flight_search_view.view.*
import java.util.*

/**
 * @author by furqan on 09/03/2020
 */
class FlightSearchFormView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    var listener: FlightSearchFormListener? = null

    private var flightDashboardCache: FlightDashboardCache = FlightDashboardCache(context)

    private val flightSearchData: FlightSearchPassDataViewModel = FlightSearchPassDataViewModel()

    private lateinit var departureDate: Date
    private lateinit var returnDate: Date

    init {
        View.inflate(context, R.layout.layout_flight_search_view, this)

        renderFromCache()
        setViewClickListener()
    }

    private fun renderFromCache() {
        setRoundTrip(flightDashboardCache.isRoundTrip)
        setOriginAirport(
                flightDashboardCache.departureAirport,
                flightDashboardCache.departureCityCode,
                flightDashboardCache.departureCityName
        )
        setDestinationAirport(
                flightDashboardCache.arrivalAirport,
                flightDashboardCache.arrivalCityCode,
                flightDashboardCache.arrivalCityName
        )
        setPassengerView(flightDashboardCache.passengerAdult, flightDashboardCache.passengerChild, flightDashboardCache.passengerInfant)
        setClassView(getClassById(flightDashboardCache.classCache))

        renderTripView()

        if (flightDashboardCache.departureDate.isNotEmpty() &&
                !FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_FORMAT, flightDashboardCache.departureDate)
                        .before(FlightDateUtil.getCurrentDate())) {
            setDepartureDate(FlightDateUtil.stringToDate(
                    FlightDateUtil.DEFAULT_FORMAT, flightDashboardCache.departureDate))
        } else {
            setDepartureDate(generateDefaultDepartureDate())
        }

        if (flightDashboardCache.returnDate.isNotEmpty() &&
                !FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_FORMAT, flightDashboardCache.returnDate)
                        .before(FlightDateUtil.addTimeToCurrentDate(Calendar.DATE, 1))) {
            setReturnDate(FlightDateUtil.stringToDate(
                    FlightDateUtil.DEFAULT_FORMAT, flightDashboardCache.returnDate))
        } else {
            setReturnDate(generateDefaultReturnDate(departureDate))
        }
    }

    fun setRoundTrip(isRoundTrip: Boolean) {
        flightSearchData.isOneWay = !isRoundTrip
    }

    fun isRoundTrip(): Boolean = !flightSearchData.isOneWay

    fun setOriginAirport(originAirport: FlightAirportViewModel) {
        flightSearchData.departureAirport = originAirport
        tvFlightOriginAirport.text = buildAirportTextFormatted(true)
    }

    fun setDestinationAirport(destinationAirport: FlightAirportViewModel) {
        flightSearchData.arrivalAirport = destinationAirport
        tvFlightDestinationAirport.text = buildAirportTextFormatted(false)
    }

    fun setDepartureDate(departureDate: Date) {
        this.departureDate = departureDate
        flightSearchData.departureDate = FlightDateUtil.dateToString(departureDate, FlightDateUtil.DEFAULT_FORMAT)
        tvFlightDepartureDate.text = FlightDateUtil.dateToString(departureDate, FlightDateUtil.DEFAULT_VIEW_FORMAT)

        // check return date
        if (isRoundTrip()) {
            if (::returnDate.isInitialized && returnDate < departureDate) {
                val oneYear = FlightDateUtil.addTimeToSpesificDate(
                        FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, 1),
                        Calendar.DATE,
                        -1)
                if (returnDate.after(oneYear)) {
                    setReturnDate(departureDate)
                } else {
                    setReturnDate(generateDefaultReturnDate(departureDate))
                }
            } else {
                setReturnDate(generateDefaultReturnDate(departureDate))
            }
        }
    }

    fun setReturnDate(returnDate: Date) {
        this.returnDate = returnDate
        flightSearchData.returnDate = FlightDateUtil.dateToString(returnDate, FlightDateUtil.DEFAULT_FORMAT)
        tvFlightReturnDate.text = FlightDateUtil.dateToString(returnDate, FlightDateUtil.DEFAULT_VIEW_FORMAT)
    }

    fun setPassengerView(passengerModel: FlightPassengerViewModel) {
        flightSearchData.flightPassengerViewModel = passengerModel
        tvFlightPassenger.text = buildPassengerTextFormatted(
                passengerModel.adult,
                passengerModel.children,
                passengerModel.infant)
    }

    fun setClassView(classModel: FlightClassViewModel) {
        flightSearchData.flightClass = classModel
        tvFlightClass.text = classModel.title
    }

    fun removeFocus() {
        tvFlightOriginLabel.clearFocus()
        tvFlightOriginAirport.clearFocus()
        tvFlightDestinationLabel.clearFocus()
        tvFlightDestinationAirport.clearFocus()
        tvFlightDepartureDateLabel.clearFocus()
        tvFlightDepartureDate.clearFocus()
        tvFlightReturnDateLabel.clearFocus()
        tvFlightReturnDate.clearFocus()
        tvFlightPassengerLabel.clearFocus()
        tvFlightPassenger.clearFocus()
        tvFlightClassLabel.clearFocus()
        tvFlightClass.clearFocus()
    }

    private fun setViewClickListener() {
        switchFlightRoundTrip.setOnCheckedChangeListener { compoundButton, isChecked -> toggleOneWay(isChecked) }
        imgFlightReverseAirport.setOnClickListener { onReverseAirportClicked() }
        tvFlightOriginLabel.setOnClickListener { listener?.onDepartureAirportClicked() }
        tvFlightOriginAirport.setOnClickListener { listener?.onDepartureAirportClicked() }
        tvFlightDestinationLabel.setOnClickListener { listener?.onDestinationAirportClicked() }
        tvFlightDestinationAirport.setOnClickListener { listener?.onDestinationAirportClicked() }
        tvFlightDepartureDateLabel.setOnClickListener {
            val departureAirport = if (flightSearchData.departureAirport.cityAirports != null &&
                    flightSearchData.departureAirport.cityAirports.size > 0)
                flightSearchData.departureAirport.cityCode else flightSearchData.departureAirport.airportCode
            val arrivalAirport = if (flightSearchData.arrivalAirport.cityAirports != null &&
                    flightSearchData.arrivalAirport.cityAirports.size > 0)
                flightSearchData.arrivalAirport.cityCode else flightSearchData.arrivalAirport.airportCode

            listener?.onDepartureDateClicked(departureAirport, arrivalAirport,
                    flightSearchData.flightClass.id, departureDate, returnDate, isRoundTrip())
        }
        tvFlightDepartureDate.setOnClickListener {
            val departureAirport = if (flightSearchData.departureAirport.cityAirports != null &&
                    flightSearchData.departureAirport.cityAirports.size > 0)
                flightSearchData.departureAirport.cityCode else flightSearchData.departureAirport.airportCode
            val arrivalAirport = if (flightSearchData.arrivalAirport.cityAirports != null &&
                    flightSearchData.arrivalAirport.cityAirports.size > 0)
                flightSearchData.arrivalAirport.cityCode else flightSearchData.arrivalAirport.airportCode

            listener?.onDepartureDateClicked(departureAirport, arrivalAirport,
                    flightSearchData.flightClass.id, departureDate, returnDate, isRoundTrip())
        }
        tvFlightReturnDateLabel.setOnClickListener {
            listener?.onReturnDateClicked(departureDate, returnDate)
        }
        tvFlightReturnDate.setOnClickListener {
            listener?.onReturnDateClicked(departureDate, returnDate)
        }
        tvFlightPassengerLabel.setOnClickListener { listener?.onPassengerClicked(flightSearchData.flightPassengerViewModel) }
        tvFlightPassenger.setOnClickListener { listener?.onPassengerClicked(flightSearchData.flightPassengerViewModel) }
        tvFlightClassLabel.setOnClickListener { listener?.onClassClicked(flightSearchData.flightClass.id) }
        tvFlightClass.setOnClickListener { listener?.onClassClicked(flightSearchData.flightClass.id) }
        btnFlightSearch.setOnClickListener { onSaveSearch() }
    }

    private fun setOriginAirport(departureAirportId: String,
                                 departureCityCode: String,
                                 departureCityName: String) {
        val departureAirport = FlightAirportViewModel()

        departureAirport.cityCode = departureCityCode
        departureAirport.cityName = departureCityName
        if (departureAirportId.contains(",")) {
            val airportIds = departureAirportId.split(",")
            departureAirport.cityAirports = arrayListOf()
            for (item in airportIds) {
                departureAirport.cityAirports.add(item)
            }
        } else {
            departureAirport.airportCode = departureAirportId
        }
        flightSearchData.departureAirport = departureAirport

        tvFlightOriginAirport.text = buildAirportTextFormatted(true)
    }

    private fun setDestinationAirport(
            arrivalAirportId: String,
            arrivalCityCode: String,
            arrivalCityName: String) {
        val arrivalAirport = FlightAirportViewModel()

        arrivalAirport.cityCode = arrivalCityCode
        arrivalAirport.cityName = arrivalCityName
        if (arrivalAirportId.contains(",")) {
            val airportIds = arrivalAirportId.split(",")
            arrivalAirport.cityAirports = arrayListOf()
            for (item in airportIds) {
                arrivalAirport.cityAirports.add(item)
            }
        } else {
            arrivalAirport.airportCode = arrivalAirportId
        }
        flightSearchData.arrivalAirport = arrivalAirport
        tvFlightDestinationAirport.text = buildAirportTextFormatted(false)
    }

    private fun setPassengerView(adult: Int = 1, children: Int = 0, infant: Int = 0) {
        flightSearchData.flightPassengerViewModel = FlightPassengerViewModel(adult, children, infant)
        tvFlightPassenger.text = buildPassengerTextFormatted(adult, children, infant)
    }

    private fun buildAirportTextFormatted(isOrigin: Boolean): CharSequence {
        val text = SpannableStringBuilder()

        if (isOrigin) {
            if (flightSearchData.departureAirport.cityAirports != null && flightSearchData.departureAirport.cityAirports.size > 0) {
                if (flightSearchData.departureAirport.cityCode.isEmpty()) {
                    text.append(flightSearchData.departureAirport.cityName)
                    return makeBold(text)
                } else {
                    text.append(flightSearchData.departureAirport.cityCode)
                }
            } else {
                text.append(flightSearchData.departureAirport.airportCode)
            }
            makeBold(text)
            if (flightSearchData.departureAirport.cityName.isNotEmpty()) {
                val cityNameText = SpannableStringBuilder(flightSearchData.departureAirport.cityName)
                makeSmall(cityNameText)
                text.append("\n")
                text.append(cityNameText)
            }
            return text
        } else {
            if (flightSearchData.arrivalAirport.cityAirports != null && flightSearchData.arrivalAirport.cityAirports.size > 0) {
                if (flightSearchData.arrivalAirport.cityCode.isEmpty()) {
                    text.append(flightSearchData.arrivalAirport.cityName)
                    return makeBold(text)
                } else {
                    text.append(flightSearchData.arrivalAirport.cityCode)
                }
            } else {
                text.append(flightSearchData.arrivalAirport.airportCode)
            }
            makeBold(text)
            if (flightSearchData.arrivalAirport.cityName.isNotEmpty()) {
                val cityNameText = SpannableStringBuilder(flightSearchData.arrivalAirport.cityName)
                makeSmall(cityNameText)
                text.append("\n")
                text.append(cityNameText)
            }
            return text
        }
    }

    private fun buildPassengerTextFormatted(adult: Int, children: Int, infant: Int): String {
        var passengerFmt = ""
        if (adult > 0) {
            passengerFmt = adult.toString() + " " + context.getString(R.string.flight_dashboard_adult_passenger)
            if (children > 0) {
                passengerFmt += ", " + children + " " + context.getString(R.string.flight_dashboard_adult_children)
            }
            if (infant > 0) {
                passengerFmt += ", " + infant + " " + context.getString(R.string.flight_dashboard_adult_infant)
            }
        }

        return passengerFmt
    }

    private fun getClassById(classId: Int): FlightClassViewModel {
        return when (classId) {
            1 -> FlightClassViewModel().apply {
                id = 1
                title = "Ekonomi"
            }
            2 -> FlightClassViewModel().apply {
                id = 2
                title = "Bisnis"
            }
            3 -> FlightClassViewModel().apply {
                id = 3
                title = "Utama"
            }
            else -> FlightClassViewModel()
        }
    }

    private fun toggleOneWay(isChecked: Boolean) {
        flightSearchData.isOneWay = !isChecked
        renderTripView()
    }

    private fun renderTripView() {
        if (isRoundTrip()) {
            switchFlightRoundTrip.isChecked = true
            showReturnDateView()
        } else {
            switchFlightRoundTrip.isChecked = false
            hideReturnDateView()
        }
    }

    private fun showReturnDateView() {
        icFlightReturnDate.show()
        tvFlightReturnDateLabel.show()
        tvFlightReturnDate.show()
        separatorReturnDate.show()
    }

    private fun hideReturnDateView() {
        icFlightReturnDate.hide()
        tvFlightReturnDateLabel.hide()
        tvFlightReturnDate.hide()
        separatorReturnDate.hide()
    }

    private fun generateDefaultDepartureDate(): Date =
            FlightDateUtil.getCurrentDate()

    private fun generateDefaultReturnDate(departureDate: Date): Date =
            FlightDateUtil.addDate(departureDate, 1)

    private fun makeBold(text: SpannableStringBuilder): SpannableStringBuilder {
        if (text.isEmpty()) return text

        text.setSpan(StyleSpan(Typeface.BOLD),
                0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text.setSpan(RelativeSizeSpan(1.25f),
                0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, android.R.color.black)),
                0, text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return text
    }

    private fun makeSmall(text: SpannableStringBuilder): SpannableStringBuilder {
        if (text.isEmpty()) return text
        text.setSpan(RelativeSizeSpan(0.75f),
                0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return text
    }

    private fun onReverseAirportClicked() {
        val tempAirport = flightSearchData.departureAirport

        setOriginAirport(flightSearchData.arrivalAirport)
        setDestinationAirport(tempAirport)

        val shake = AnimationUtils.loadAnimation(context, R.anim.flight_rotate)
        imgFlightReverseAirport.startAnimation(shake)
    }

    private fun onSaveSearch() {
        updateSearchParamCache()
        listener?.onSaveSearch(flightSearchData)
    }

    private fun updateSearchParamCache() {
        val departureAirport = if (flightSearchData.departureAirport.cityAirports != null && flightSearchData.departureAirport.cityAirports.size > 0)
            buildAirportListString(flightSearchData.departureAirport.cityAirports)
        else flightSearchData.departureAirport.airportCode

        val arrivalAirport = if (flightSearchData.arrivalAirport.cityAirports != null && flightSearchData.arrivalAirport.cityAirports.size > 0)
            buildAirportListString(flightSearchData.arrivalAirport.cityAirports)
        else flightSearchData.arrivalAirport.airportCode

        flightDashboardCache.putDepartureAirport(departureAirport)
        flightDashboardCache.putDepartureCityCode(flightSearchData.departureAirport.cityCode)
        flightDashboardCache.putDepartureCityName(flightSearchData.departureAirport.cityName)
        flightDashboardCache.putArrivalAirport(arrivalAirport)
        flightDashboardCache.putArrivalCityCode(flightSearchData.arrivalAirport.cityCode)
        flightDashboardCache.putArrivalCityName(flightSearchData.arrivalAirport.cityName)
        flightDashboardCache.putRoundTrip(!flightSearchData.isOneWay)
        flightDashboardCache.putDepartureDate(flightSearchData.departureDate)
        if (isRoundTrip()) flightDashboardCache.putReturnDate(flightSearchData.returnDate)
        flightDashboardCache.putPassengerCount(
                flightSearchData.flightPassengerViewModel.adult,
                flightSearchData.flightPassengerViewModel.children,
                flightSearchData.flightPassengerViewModel.infant
        )
        flightDashboardCache.putClassCache(flightSearchData.flightClass.id)
    }

    private fun buildAirportListString(airportIdList: List<String>): String {
        var airportId = ""

        for ((index, item) in airportIdList.withIndex()) {
            airportId += if (index < airportIdList.size - 1) {
                "$item,"
            } else {
                item
            }
        }

        return airportId
    }

    interface FlightSearchFormListener {
        fun onDepartureAirportClicked()
        fun onDestinationAirportClicked()
        fun onDepartureDateClicked(departureAirport: String, arrivalAirport: String, flightClassId: Int,
                                   departureDate: Date, returnDate: Date, isRoundTrip: Boolean)

        fun onReturnDateClicked(departureDate: Date, returnDate: Date)
        fun onPassengerClicked(passengerModel: FlightPassengerViewModel?)
        fun onClassClicked(flightClassId: Int = -1)
        fun onSaveSearch(flightSearchData: FlightSearchPassDataViewModel)
    }

}