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

    private var isOneWay: Boolean = true
    private var originAirportId: String = ""
    private var originCityCode: String = ""
    private var originCityName: String = ""
    private var destinationAirportId: String = ""
    private var destinationCityCode: String = ""
    private var destinationCityName: String = ""
    private var departureDate: Date? = null
    private var departureDateString: String = ""
    private var returnDate: Date? = null
    private var returnDateString: String = ""
    private var passengerModel: FlightPassengerViewModel? = null
    private var passengerString: String = ""
    private var classModel: FlightClassViewModel? = null

    init {
        View.inflate(context, R.layout.layout_flight_search_view, this)

        renderFromCache()
        setViewClickListener()
    }

    private fun renderFromCache() {
        setTrip(!flightDashboardCache.isRoundTrip)
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

    fun setTrip(isOneWay: Boolean) {
        this.isOneWay = isOneWay
    }

    fun isOneWay(): Boolean = isOneWay

    fun setOriginAirport(originAirport: FlightAirportViewModel) {
        originAirportId = if (originAirport.cityAirports != null && originAirport.cityAirports.size > 0) {
            buildAirportListString(originAirport.cityAirports)
        } else {
            originAirport.airportCode
        }
        originCityCode = originAirport.cityCode
        originCityName = originAirport.cityName
        tvFlightOriginAirport.text = buildAirportTextFormatted(true)
    }

    fun setDestinationAirport(destinationAirport: FlightAirportViewModel) {
        destinationAirportId = if (destinationAirport.cityAirports != null && destinationAirport.cityAirports.size > 0) {
            buildAirportListString(destinationAirport.cityAirports)
        } else {
            destinationAirport.airportCode
        }
        destinationCityCode = destinationAirport.cityCode
        destinationCityName = destinationAirport.cityName
        tvFlightDestinationAirport.text = buildAirportTextFormatted(false)
    }

    fun setDepartureDate(departureDate: Date) {
        this.departureDate = departureDate
        this.departureDateString = FlightDateUtil.dateToString(departureDate, FlightDateUtil.DEFAULT_VIEW_FORMAT)
        tvFlightDepartureDate.text = departureDateString
    }

    fun setReturnDate(returnDate: Date) {
        this.returnDate = returnDate
        this.returnDateString = FlightDateUtil.dateToString(returnDate, FlightDateUtil.DEFAULT_VIEW_FORMAT)
        tvFlightReturnDate.text = returnDateString
    }

    fun setPassengerView(passengerModel: FlightPassengerViewModel) {
        this.passengerModel = passengerModel
        this.passengerString = buildPassengerTextFormatted(
                passengerModel.adult,
                passengerModel.children,
                passengerModel.infant)
        tvFlightPassenger.text = passengerString
    }

    fun setClassView(classModel: FlightClassViewModel) {
        this.classModel = classModel
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
        tvFlightClassLabel.setOnClickListener { listener?.onClassClicked(classModel) }
        tvFlightClass.setOnClickListener { listener?.onClassClicked(classModel) }
    }

    private fun setOriginAirport(departureAirportId: String,
                                 departureCityCode: String,
                                 departureCityName: String) {
        this.originAirportId = departureAirportId
        this.originCityCode = departureCityCode
        this.originCityName = departureCityName
        tvFlightOriginAirport.text = buildAirportTextFormatted(true)
    }

    private fun setDestinationAirport(
            arrivalAirportId: String,
            arrivalCityCode: String,
            arrivalCityName: String) {
        this.destinationAirportId = arrivalAirportId
        this.destinationCityCode = arrivalCityCode
        this.destinationCityName = arrivalCityName
        tvFlightDestinationAirport.text = buildAirportTextFormatted(false)
    }

    private fun setPassengerView(adult: Int = 1, children: Int = 0, infant: Int = 0) {
        this.passengerModel = FlightPassengerViewModel(adult, children, infant)
        this.passengerString = buildPassengerTextFormatted(adult, children, infant)
        tvFlightPassenger.text = passengerString
    }

    private fun buildAirportTextFormatted(isOrigin: Boolean): CharSequence {
        val text = SpannableStringBuilder()

        if (isOrigin) {
            if (originAirportId.isEmpty() || originAirportId.contains(",")) {
                if (originCityCode.isEmpty()) {
                    text.append(originCityName)
                    return makeBold(text)
                } else {
                    text.append(originCityCode)
                }
            } else {
                text.append(originAirportId)
            }
            makeBold(text)
            if (originCityName.isNotEmpty()) {
                val cityNameText = SpannableStringBuilder(originCityName)
                makeSmall(cityNameText)
                text.append("\n")
                text.append(cityNameText)
            }
            return text
        } else {
            if (destinationAirportId.isEmpty() || destinationAirportId.contains(",")) {
                if (destinationCityCode.isEmpty()) {
                    text.append(destinationCityName)
                    return makeBold(text)
                } else {
                    text.append(destinationCityCode)
                }
            } else {
                text.append(destinationAirportId)
            }
            makeBold(text)
            if (destinationCityName.isNotEmpty()) {
                val cityNameText = SpannableStringBuilder(destinationCityName)
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
        isOneWay = !isOneWay
        renderTripView()
    }

    private fun renderTripView() {
        if (isOneWay) {
            switchFlightRoundTrip.isSelected = false
            hideReturnDateView()
        } else {
            switchFlightRoundTrip.isSelected = true
            showReturnDateView()
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
        val tempAirportId = originAirportId
        val tempCityCode = originCityCode
        val tempCityName = originCityName

        setOriginAirport(
                destinationAirportId,
                destinationCityCode,
                destinationCityName
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
        fun onClassClicked(classModel: FlightClassViewModel?)
    }

}