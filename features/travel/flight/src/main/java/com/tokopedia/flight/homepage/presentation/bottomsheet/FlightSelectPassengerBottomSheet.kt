package com.tokopedia.flight.homepage.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import com.tokopedia.flight.R
import com.tokopedia.flight.homepage.presentation.model.FlightPassengerModel
import com.tokopedia.flight.homepage.presentation.validator.FlightSelectPassengerValidator
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.bottom_sheet_flight_select_passenger.*

/**
 * @author by furqan on 28/04/2020
 */
class FlightSelectPassengerBottomSheet : BottomSheetUnify() {

    lateinit var listener: Listener
    var passengerModel: FlightPassengerModel? = null

    private var passengerValidator: FlightSelectPassengerValidator = FlightSelectPassengerValidator()
    private lateinit var mChildView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        bottomSheetHeader.setPadding(
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2),
                bottomSheetHeader.top,
                bottomSheetWrapper.right,
                bottomSheetHeader.bottom)
        bottomSheetWrapper.setPadding(0,
                bottomSheetWrapper.paddingTop,
                0,
                bottomSheetWrapper.paddingBottom)
    }

    private fun initBottomSheet() {
        showCloseIcon = true
        showKnob = false
        isDragable = true
        setTitle(getString(R.string.select_passenger_toolbar_title))

        mChildView = View.inflate(requireContext(), R.layout.bottom_sheet_flight_select_passenger, null)
        setChild(mChildView)
    }

    private fun initView() {

        passengerModel?.let {
            qtyFlightPassengerAdult.setValue(it.adult)
            qtyFlightPassengerChild.setValue(it.children)
            qtyFlightPassengerInfant.setValue(it.infant)
            if (it.adult <= FlightSelectPassengerValidator.MAX_INFANT_VALUE) {
                qtyFlightPassengerInfant.maxValue = it.adult
            }
            setupQuantity(it)
        }

        tickerPassengerInfo.setHtmlDescription(getString(R.string.select_passenger_infant_greater_than_adult_error_message))
        qtyFlightPassengerAdult.editText.isFocusable = false
        qtyFlightPassengerChild.editText.isFocusable = false
        qtyFlightPassengerInfant.editText.isFocusable = false

        qtyFlightPassengerAdult.setAddClickListener {
            val tempPassengerModel = clonePassengerModel()
            tempPassengerModel.adult = qtyFlightPassengerAdult.getValue()
            if (validatePassenger(tempPassengerModel)) {
                if (tempPassengerModel.adult <= FlightSelectPassengerValidator.MAX_INFANT_VALUE) {
                    qtyFlightPassengerInfant.maxValue = tempPassengerModel.adult
                }
                passengerModel = tempPassengerModel
                setupQuantity(tempPassengerModel)
            } else {
                qtyFlightPassengerAdult.setValue(qtyFlightPassengerAdult.getValue() - 1)
            }
        }

        qtyFlightPassengerAdult.setSubstractListener {
            val tempPassengerModel = clonePassengerModel()
            tempPassengerModel.adult = qtyFlightPassengerAdult.getValue()
            if (validatePassenger(tempPassengerModel)) {
                if (tempPassengerModel.adult <= FlightSelectPassengerValidator.MAX_INFANT_VALUE) {
                    qtyFlightPassengerInfant.maxValue = tempPassengerModel.adult
                }
                passengerModel = tempPassengerModel
                setupQuantity(tempPassengerModel)
            } else {
                qtyFlightPassengerAdult.setValue(qtyFlightPassengerAdult.getValue() + 1)
            }
        }

        qtyFlightPassengerChild.setAddClickListener {
            val tempPassengerModel = clonePassengerModel()
            tempPassengerModel.children = qtyFlightPassengerChild.getValue()
            if (validatePassenger(tempPassengerModel)) {
                passengerModel = tempPassengerModel
                setupQuantity(tempPassengerModel)
            } else {
                qtyFlightPassengerChild.setValue(qtyFlightPassengerChild.getValue() - 1)
            }
        }

        qtyFlightPassengerChild.setSubstractListener {
            val tempPassengerModel = clonePassengerModel()
            tempPassengerModel.children = qtyFlightPassengerChild.getValue()
            if (validatePassenger(tempPassengerModel)) {
                passengerModel = tempPassengerModel
                setupQuantity(tempPassengerModel)
            } else {
                qtyFlightPassengerChild.setValue(qtyFlightPassengerChild.getValue() + 1)
            }
        }

        qtyFlightPassengerInfant.setAddClickListener {
            val tempPassengerModel = clonePassengerModel()
            tempPassengerModel.infant = qtyFlightPassengerInfant.getValue()
            if (validatePassenger(tempPassengerModel)) {
                passengerModel = tempPassengerModel
                setupQuantity(tempPassengerModel)
            } else {
                qtyFlightPassengerInfant.setValue(qtyFlightPassengerInfant.getValue() - 1)
            }
        }

        qtyFlightPassengerInfant.setSubstractListener {
            val tempPassengerModel = clonePassengerModel()
            tempPassengerModel.infant = qtyFlightPassengerInfant.getValue()
            if (validatePassenger(tempPassengerModel)) {
                passengerModel = tempPassengerModel
                setupQuantity(tempPassengerModel)
            } else {
                qtyFlightPassengerInfant.setValue(qtyFlightPassengerInfant.getValue() + 1)
            }
        }

        btnFlightPassenger.setOnClickListener {
            passengerModel?.let {
                if (validatePassenger(it) && ::listener.isInitialized) {
                    listener.onSavedPassenger(it)
                    dismiss()
                }
            }
        }
    }

    private fun clonePassengerModel(): FlightPassengerModel {
        return try {
            passengerModel?.clone() as FlightPassengerModel
        } catch (t: Throwable) {
            t.printStackTrace()
            FlightPassengerModel()
        }
    }

    private fun showErrorMessage(@StringRes resId: Int) {
        Toaster.build(mChildView, getString(resId), Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show()
    }

    private fun validatePassenger(passengers: FlightPassengerModel): Boolean {
        var isValid = true

        if (!passengerValidator.validateTotalPassenger(passengers)) {
            isValid = false
            showErrorMessage(R.string.select_passenger_total_passenger_error_message)
        } else if (!passengerValidator.validateInfantNotGreaterThanAdult(passengers)) {
            isValid = false
        } else if (!passengerValidator.validateInfantMoreThanFour(passengers.infant)) {
            isValid = false
        } else if (!passengerValidator.validateAdultCountAtleastOne(passengers)) {
            isValid = false
        }

        return isValid
    }

    private fun setupQuantity(passengers: FlightPassengerModel) {
        val total = passengers.adult + passengers.children

        if (total >= FlightSelectPassengerValidator.MAX_PASSENGER_VALUE) {
            qtyFlightPassengerAdult.addButton.isEnabled = false
            qtyFlightPassengerChild.addButton.isEnabled = false
        } else {
            qtyFlightPassengerAdult.addButton.isEnabled = true
            qtyFlightPassengerChild.addButton.isEnabled = true
        }

        qtyFlightPassengerAdult.subtractButton.isEnabled = passengers.adult > passengers.infant && passengers.adult > MIN_ADULT_PASSENGER
        qtyFlightPassengerInfant.addButton.isEnabled = passengers.infant < qtyFlightPassengerInfant.maxValue
    }

    interface Listener {
        fun onSavedPassenger(passengerModel: FlightPassengerModel)
    }

    companion object {
        const val TAG_SELECT_PASSENGER = "TagFlightSelectPassengerBottomSheet"

        private const val MIN_ADULT_PASSENGER = 1
    }
}