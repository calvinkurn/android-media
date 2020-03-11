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
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightDashboardPassDataViewModel
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel
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

    private val flightSearchData: FlightDashboardPassDataViewModel = FlightDashboardPassDataViewModel()

    private var departureDate: Date? = null
    private var returnDate: Date? = null
    private var passengerModel: FlightPassengerViewModel? = null

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

        if (flightDashboardCache.departureDate.isNotEmpty() &&
                FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_FORMAT, flightDashboardCache.departureDate)
                        .after(FlightDateUtil.getCurrentDate())) {
            setDepartureDate(FlightDateUtil.stringToDate(
                    FlightDateUtil.DEFAULT_FORMAT, flightDashboardCache.departureDate))
        } else {
            setDepartureDate(generateDefaultDepartureDate())
        }

        if (flightDashboardCache.returnDate.isNotEmpty() &&
                FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_FORMAT, flightDashboardCache.returnDate)
                        .after(FlightDateUtil.addTimeToCurrentDate(Calendar.DATE, 1))) {
            setReturnDate(FlightDateUtil.stringToDate(
                    FlightDateUtil.DEFAULT_FORMAT, flightDashboardCache.returnDate))
        } else {
            setReturnDate(generateDefaultReturnDate(departureDate
                    ?: generateDefaultDepartureDate()))
        }

        setPassengerView(flightDashboardCache.passengerAdult, flightDashboardCache.passengerChild, flightDashboardCache.passengerInfant)
        setClassView(getClassById(flightDashboardCache.classCache))

        renderTripView()
    }

    fun setRoundTrip(isRoundTrip: Boolean) {
        flightSearchData.isRoundTrip = isRoundTrip
    }

    fun setOriginAirport(originAirport: FlightAirportViewModel) {
        flightSearchData.departureAirportId = if (originAirport.cityAirports != null && originAirport.cityAirports.size > 0) {
            buildAirportListString(originAirport.cityAirports)
        } else {
            originAirport.airportCode
        }
        flightSearchData.departureCityCode = originAirport.cityCode
        flightSearchData.departureCityName = originAirport.cityName
        tvFlightOriginAirport.text = buildAirportTextFormatted(true)
    }

    fun setDestinationAirport(destinationAirport: FlightAirportViewModel) {
        flightSearchData.arrivalAirportId = if (destinationAirport.cityAirports != null && destinationAirport.cityAirports.size > 0) {
            buildAirportListString(destinationAirport.cityAirports)
        } else {
            destinationAirport.airportCode
        }
        flightSearchData.arrivalCityCode = destinationAirport.cityCode
        flightSearchData.arrivalCityName = destinationAirport.cityName
        tvFlightDestinationAirport.text = buildAirportTextFormatted(false)
    }

    fun setDepartureDate(departureDate: Date) {
        this.departureDate = departureDate
        flightSearchData.departureDate = FlightDateUtil.dateToString(departureDate, FlightDateUtil.DEFAULT_FORMAT)
        tvFlightDepartureDate.text = FlightDateUtil.dateToString(departureDate, FlightDateUtil.DEFAULT_VIEW_FORMAT)
    }

    fun setReturnDate(returnDate: Date) {
        this.returnDate = returnDate
        flightSearchData.returnDate = FlightDateUtil.dateToString(returnDate, FlightDateUtil.DEFAULT_FORMAT)
        tvFlightReturnDate.text = FlightDateUtil.dateToString(returnDate, FlightDateUtil.DEFAULT_VIEW_FORMAT)
    }

    fun setPassengerView(passengerModel: FlightPassengerViewModel) {
        this.passengerModel = passengerModel
        flightSearchData.adultPassengerCount = passengerModel.adult
        flightSearchData.childPassengerCount = passengerModel.children
        flightSearchData.infantPassengerCount = passengerModel.infant

        tvFlightPassenger.text = buildPassengerTextFormatted(
                passengerModel.adult,
                passengerModel.children,
                passengerModel.infant)
    }

    fun setClassView(classModel: FlightClassViewModel) {
        flightSearchData.flightClass = classModel.id
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
        switchFlightRoundTrip.setOnClickListener { toggleOneWay() }
        imgFlightReverseAirport.setOnClickListener { onReverseAirportClicked() }
        tvFlightOriginLabel.setOnClickListener { listener?.onDepartureAirportClicked() }
        tvFlightOriginAirport.setOnClickListener { listener?.onDepartureAirportClicked() }
        tvFlightDestinationLabel.setOnClickListener { listener?.onDestinationAirportClicked() }
        tvFlightDestinationAirport.setOnClickListener { listener?.onDestinationAirportClicked() }
        tvFlightDepartureDateLabel.setOnClickListener { listener?.onDepartureDateClicked(returnDate) }
        tvFlightDepartureDate.setOnClickListener { listener?.onDepartureDateClicked(returnDate) }
        tvFlightReturnDateLabel.setOnClickListener { listener?.onReturnDateClicked(returnDate) }
        tvFlightReturnDate.setOnClickListener { listener?.onReturnDateClicked(returnDate) }
        tvFlightPassengerLabel.setOnClickListener { listener?.onPassengerClicked(passengerModel) }
        tvFlightPassenger.setOnClickListener { listener?.onPassengerClicked(passengerModel) }
        tvFlightClassLabel.setOnClickListener { listener?.onClassClicked(flightSearchData.flightClass) }
        tvFlightClass.setOnClickListener { listener?.onClassClicked(flightSearchData.flightClass) }
    }

    private fun setOriginAirport(departureAirportId: String,
                                 departureCityCode: String,
                                 departureCityName: String) {
        flightSearchData.departureAirportId = departureAirportId
        flightSearchData.departureCityCode = departureCityCode
        flightSearchData.departureCityName = departureCityName
        tvFlightOriginAirport.text = buildAirportTextFormatted(true)
    }

    private fun setDestinationAirport(
            arrivalAirportId: String,
            arrivalCityCode: String,
            arrivalCityName: String) {
        flightSearchData.arrivalAirportId = arrivalAirportId
        flightSearchData.arrivalCityCode = arrivalCityCode
        flightSearchData.arrivalCityName = arrivalCityName
        tvFlightDestinationAirport.text = buildAirportTextFormatted(false)
    }

    private fun setPassengerView(adult: Int = 1, children: Int = 0, infant: Int = 0) {
        this.passengerModel = FlightPassengerViewModel(adult, children, infant)
        tvFlightPassenger.text = buildPassengerTextFormatted(adult, children, infant)
    }

    private fun buildAirportTextFormatted(isOrigin: Boolean): CharSequence {
        val text = SpannableStringBuilder()

        if (isOrigin) {
            if (flightSearchData.departureAirportId.isEmpty() || flightSearchData.departureAirportId.contains(",")) {
                if (flightSearchData.departureCityCode.isEmpty()) {
                    text.append(flightSearchData.departureCityName)
                    return makeBold(text)
                } else {
                    text.append(flightSearchData.departureCityCode)
                }
            } else {
                text.append(flightSearchData.departureAirportId)
            }
            makeBold(text)
            if (flightSearchData.departureCityName.isNotEmpty()) {
                val cityNameText = SpannableStringBuilder(flightSearchData.departureCityName)
                makeSmall(cityNameText)
                text.append("\n")
                text.append(cityNameText)
            }
            return text
        } else {
            if (flightSearchData.arrivalAirportId.isEmpty() || flightSearchData.arrivalAirportId.contains(",")) {
                if (flightSearchData.arrivalCityCode.isEmpty()) {
                    text.append(flightSearchData.arrivalCityName)
                    return makeBold(text)
                } else {
                    text.append(flightSearchData.arrivalCityCode)
                }
            } else {
                text.append(flightSearchData.arrivalAirportId)
            }
            makeBold(text)
            if (flightSearchData.arrivalCityName.isNotEmpty()) {
                val cityNameText = SpannableStringBuilder(flightSearchData.arrivalCityName)
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

    private fun toggleOneWay() {
        flightSearchData.isRoundTrip = !flightSearchData.isRoundTrip
        renderTripView()
    }

    private fun renderTripView() {
        if (flightSearchData.isRoundTrip) {
            switchFlightRoundTrip.isSelected = true
            showReturnDateView()
        } else {
            switchFlightRoundTrip.isSelected = false
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
            FlightDateUtil.addTimeToCurrentDate(Calendar.DATE, 1)

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
        val tempAirportId = flightSearchData.departureAirportId
        val tempCityCode = flightSearchData.departureCityCode
        val tempCityName = flightSearchData.departureCityName

        setOriginAirport(
                flightSearchData.arrivalAirportId,
                flightSearchData.arrivalCityCode,
                flightSearchData.arrivalCityName
        )
        setDestinationAirport(
                tempAirportId,
                tempCityCode,
                tempCityName
        )

        val shake = AnimationUtils.loadAnimation(context, R.anim.flight_rotate)
        imgFlightReverseAirport.startAnimation(shake)
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
        fun onDepartureDateClicked(departureDate: Date?)
        fun onReturnDateClicked(returnDate: Date?)
        fun onPassengerClicked(passengerModel: FlightPassengerViewModel?)
        fun onClassClicked(flightClassId: Int = -1)
    }

}