package com.tokopedia.flight.homepage.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import com.tokopedia.flight.R
import com.tokopedia.flight.dashboard.view.fragment.model.FlightPassengerModel
import com.tokopedia.flight.dashboard.view.validator.FlightSelectPassengerValidator
import com.tokopedia.unifycomponents.BottomSheetUnify
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
            } else {
                qtyFlightPassengerAdult.setValue(qtyFlightPassengerAdult.getValue() - 1)
            }
        }

        qtyFlightPassengerAdult.setSubstractListener {
            val tempPassengerModel = clonePassengerModel()
            tempPassengerModel.adult = qtyFlightPassengerAdult.getValue()
            if (validatePassenger(tempPassengerModel)) {
                qtyFlightPassengerInfant.maxValue = tempPassengerModel.adult
                if (qtyFlightPassengerInfant.getValue() == qtyFlightPassengerInfant.maxValue) {
                    qtyFlightPassengerInfant.addButton.isEnabled = false
                }
                passengerModel = tempPassengerModel
            } else {
                qtyFlightPassengerAdult.setValue(qtyFlightPassengerAdult.getValue() + 1)
            }
        }

        qtyFlightPassengerChild.setValueChangedListener { newValue, oldValue, isOver ->
            val tempPassengerModel = clonePassengerModel()
            tempPassengerModel.children = newValue
            if (validatePassenger(tempPassengerModel)) {
                passengerModel = tempPassengerModel
            } else {
                qtyFlightPassengerChild.setValue(oldValue)
            }
        }

        qtyFlightPassengerInfant.setValueChangedListener { newValue, oldValue, isOver ->
            val tempPassengerModel = clonePassengerModel()
            tempPassengerModel.infant = newValue
            if (validatePassenger(tempPassengerModel)) {
                passengerModel = tempPassengerModel
            } else {
                qtyFlightPassengerInfant.setValue(oldValue)
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

    private fun validatePassenger(passengers: FlightPassengerModel): Boolean {
        var isValid = true

        if (!passengerValidator.validateTotalPassenger(passengers)) {
            isValid = false
        } else if (!passengerValidator.validateInfantNotGreaterThanAdult(passengers)) {
            isValid = false
        } else if (!passengerValidator.validateInfantMoreThanFour(passengers.infant)) {
            isValid = false
        } else if (!passengerValidator.validateAdultCountAtleastOne(passengers)) {
            isValid = false
        }

        return isValid
    }

    interface Listener {
        fun onSavedPassenger(passengerModel: FlightPassengerModel)
    }

    companion object {
        const val TAG_SELECT_PASSENGER = "TagFlightSelectPassengerBottomSheet"
    }
}