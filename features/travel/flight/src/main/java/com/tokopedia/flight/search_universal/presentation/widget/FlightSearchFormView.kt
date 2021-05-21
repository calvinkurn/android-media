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
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.view.model.FlightAirportModel
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.homepage.data.cache.FlightDashboardCache
import com.tokopedia.flight.homepage.presentation.model.FlightClassModel
import com.tokopedia.flight.homepage.presentation.model.FlightPassengerModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
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

    private val flightSearchData: FlightSearchPassDataModel = FlightSearchPassDataModel()

    private lateinit var departureDate: Date
    private lateinit var returnDate: Date

    init {
        View.inflate(context, R.layout.layout_flight_search_view, this)
        init()
    }

    fun init() {
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
                        .before(generateDefaultDepartureDate())) {
            setDepartureDate(FlightDateUtil.stringToDate(
                    FlightDateUtil.DEFAULT_FORMAT, flightDashboardCache.departureDate))
        } else {
            setDepartureDate(generateDefaultDepartureDate())
        }

        if (flightDashboardCache.returnDate.isNotEmpty() &&
                !FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_FORMAT, flightDashboardCache.returnDate)
                        .before(generateDefaultReturnDate(departureDate))) {
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

    fun setOriginAirport(originAirport: FlightAirportModel) {
        flightSearchData.departureAirport = originAirport
        tvFlightOriginAirport.text = buildAirportTextFormatted(true)
    }

    fun setDestinationAirport(destinationAirport: FlightAirportModel) {
        flightSearchData.arrivalAirport = destinationAirport
        tvFlightDestinationAirport.text = buildAirportTextFormatted(false)
    }

    fun setDepartureDate(departureDate: Date) {
        this.departureDate = departureDate
        flightSearchData.departureDate = FlightDateUtil.dateToString(departureDate, FlightDateUtil.DEFAULT_FORMAT)
        tvFlightDepartureDate.text = FlightDateUtil.dateToString(departureDate, FlightDateUtil.DEFAULT_VIEW_FORMAT)

        // check return date
        val oneYear = FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, 1),
                Calendar.DATE,
                -1)
        if (::returnDate.isInitialized &&
                returnDate.after(departureDate) &&
                returnDate.before(oneYear)) {
            setReturnDate(returnDate)
        } else {
            setReturnDate(generateDefaultReturnDate(departureDate))
        }
    }

    fun setReturnDate(returnDate: Date) {
        this.returnDate = returnDate
        flightSearchData.returnDate = FlightDateUtil.dateToString(returnDate, FlightDateUtil.DEFAULT_FORMAT)
        tvFlightReturnDate.text = FlightDateUtil.dateToString(returnDate, FlightDateUtil.DEFAULT_VIEW_FORMAT)
    }

    fun setPassengerView(passengerModel: FlightPassengerModel) {
        flightSearchData.flightPassengerModel = passengerModel
        tvFlightPassenger.text = buildPassengerTextFormatted(
                passengerModel.adult,
                passengerModel.children,
                passengerModel.infant)
    }

    fun setClassView(classModel: FlightClassModel) {
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

    fun autoSearch() {
        onSaveSearch()
    }

    private fun setViewClickListener() {

        switchFlightRoundTrip.setOnCheckedChangeListener { compoundButton, isChecked ->
            listener?.onRoundTripSwitchChanged(isChecked)
            toggleOneWay(isChecked)
        }
        imgFlightReverseAirport.setOnClickListener { onReverseAirportClicked() }
        tvFlightOriginLabel.setOnClickListener { listener?.onDepartureAirportClicked() }
        tvFlightOriginAirport.setOnClickListener { listener?.onDepartureAirportClicked() }
        tvFlightDestinationLabel.setOnClickListener { listener?.onDestinationAirportClicked() }
        tvFlightDestinationAirport.setOnClickListener { listener?.onDestinationAirportClicked() }
        tvFlightDepartureDateLabel.setOnClickListener {
            val departureAndArrivalAirports = getDepartureAndArrivalAirports()
            listener?.onDepartureDateClicked(departureAndArrivalAirports.first, departureAndArrivalAirports.second,
                    flightSearchData.flightClass.id, departureDate, returnDate, isRoundTrip())
        }
        tvFlightDepartureDate.setOnClickListener {
            val departureAndArrivalAirports = getDepartureAndArrivalAirports()
            listener?.onDepartureDateClicked(departureAndArrivalAirports.first,
                    departureAndArrivalAirports.second,
                    flightSearchData.flightClass.id, departureDate, returnDate, isRoundTrip())
        }
        tvFlightReturnDateLabel.setOnClickListener {
            val departureAndArrivalAirports = getDepartureAndArrivalAirports()
            listener?.onReturnDateClicked(departureDate, returnDate, departureAndArrivalAirports.first,
                    departureAndArrivalAirports.second, flightSearchData.flightClass.id)
        }
        tvFlightReturnDate.setOnClickListener {
            val departureAndArrivalAirports = getDepartureAndArrivalAirports()
            listener?.onReturnDateClicked(departureDate, returnDate,
                    departureAndArrivalAirports.first,
                    departureAndArrivalAirports.second, flightSearchData.flightClass.id)
        }
        tvFlightPassengerLabel.setOnClickListener { listener?.onPassengerClicked(flightSearchData.flightPassengerModel) }
        tvFlightPassenger.setOnClickListener { listener?.onPassengerClicked(flightSearchData.flightPassengerModel) }
        tvFlightClassLabel.setOnClickListener { listener?.onClassClicked(flightSearchData.flightClass.id) }
        tvFlightClass.setOnClickListener { listener?.onClassClicked(flightSearchData.flightClass.id) }
        btnFlightSearch.setOnClickListener { onSaveSearch() }
    }

    private fun getDepartureAndArrivalAirports(): Pair<String, String> {
        val departureAirport = if (flightSearchData.departureAirport.cityAirports != null &&
                flightSearchData.departureAirport.cityAirports.size > 0)
            flightSearchData.departureAirport.cityCode else flightSearchData.departureAirport.airportCode
        val arrivalAirport = if (flightSearchData.arrivalAirport.cityAirports != null &&
                flightSearchData.arrivalAirport.cityAirports.size > 0)
            flightSearchData.arrivalAirport.cityCode else flightSearchData.arrivalAirport.airportCode
        return Pair(departureAirport, arrivalAirport)
    }

    private fun setOriginAirport(departureAirportId: String,
                                 departureCityCode: String,
                                 departureCityName: String) {
        val departureAirport = FlightAirportModel()

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
        val arrivalAirport = FlightAirportModel()

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
        flightSearchData.flightPassengerModel = FlightPassengerModel(adult, children, infant)
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
                text.append("\n")
                text.append(cityNameText)
            }
            return text
        }
    }

    private fun buildPassengerTextFormatted(adult: Int, children: Int, infant: Int): String {
        var passengerFmt = ""
        if (adult > 0) {
            SplitCompat.installActivity(context)
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

    private fun getClassById(classId: Int): FlightClassModel {
        return when (classId) {
            1 -> FlightClassModel(1, "Ekonomi")
            2 -> FlightClassModel(2, "Bisnis")
            3 -> FlightClassModel(3, "Utama")
            else -> FlightClassModel(0, "")
        }
    }

    private fun toggleOneWay(isChecked: Boolean) {
        flightSearchData.isOneWay = !isChecked
        if (isChecked) setReturnDate(generateDefaultReturnDate(departureDate))
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
            FlightDateUtil.removeTime(FlightDateUtil.addTimeToCurrentDate(Calendar.DATE, DEFAULT_MIN_DEPARTURE_DATE_FROM_TODAY))

    private fun generateDefaultReturnDate(departureDate: Date): Date =
            FlightDateUtil.removeTime(FlightDateUtil.addDate(departureDate, 1))

    private fun makeBold(text: SpannableStringBuilder): SpannableStringBuilder {
        if (text.isEmpty()) return text

        text.setSpan(StyleSpan(Typeface.BOLD),
                0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text.setSpan(RelativeSizeSpan(1.25f),
                0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700)),
                0, text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
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
                flightSearchData.flightPassengerModel.adult,
                flightSearchData.flightPassengerModel.children,
                flightSearchData.flightPassengerModel.infant
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
        fun onRoundTripSwitchChanged(isRoundTrip: Boolean)
        fun onDepartureAirportClicked()
        fun onDestinationAirportClicked()
        fun onDepartureDateClicked(departureAirport: String, arrivalAirport: String, flightClassId: Int,
                                   departureDate: Date, returnDate: Date, isRoundTrip: Boolean)

        fun onReturnDateClicked(departureDate: Date, returnDate: Date, departureAirport: String,
                                arrivalAirport: String, flightClassId: Int)

        fun onPassengerClicked(passengerModel: FlightPassengerModel?)
        fun onClassClicked(flightClassId: Int = -1)
        fun onSaveSearch(flightSearchData: FlightSearchPassDataModel)
    }

    companion object {
        const val DEFAULT_MIN_DEPARTURE_DATE_FROM_TODAY = 2
    }

}