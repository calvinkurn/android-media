package com.tokopedia.flight.homepage.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import com.tokopedia.flight.R
import com.tokopedia.flight.homepage.presentation.model.FlightClassModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_flight_select_class.*

/**
 * @author by furqan on 28/04/2020
 */
class FlightSelectClassBottomSheet : BottomSheetUnify() {

    var listener: Listener? = null
    var selectedClass: FlightClassModel? = null

    private var flightClassList = generateFlightClass()
    private lateinit var mChildView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    fun setSelectedClass(classId: Int) {
        selectedClass = flightClassList.firstOrNull {
            it.id == classId
        }
    }

    private fun initBottomSheet() {
        showCloseIcon = true
        showKnob = false
        isDragable = true
        setTitle(getString(R.string.flight_classes_toolbar_title))

        mChildView = View.inflate(requireContext(), R.layout.bottom_sheet_flight_select_class, null)
        setChild(mChildView)
    }

    private fun initView() {
        selectedClass?.let {
            when (it.id) {
                ID_EKONOMI -> {
                    radioEkonomiClass.isChecked = true
                }
                ID_BISNIS -> {
                    radioBisnisClass.isChecked = true
                }
                ID_UTAMA -> {
                    radioUtamaClass.isChecked = true
                }
            }
        }

        radioEkonomiClass.text = flightClassList[0].title
        radioBisnisClass.text = flightClassList[1].title
        radioUtamaClass.text = flightClassList[2].title

        radioGroupFlightClass.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId) {
                R.id.radioEkonomiClass -> {
                    selectedClass = flightClassList[0]
                }
                R.id.radioBisnisClass -> {
                    selectedClass = flightClassList[1]
                }
                R.id.radioUtamaClass -> {
                    selectedClass = flightClassList[2]
                }
            }

            selectedClass?.let {
                listener?.onClassSelected(it)
                dismiss()
            }
        }
    }

    private fun generateFlightClass(): List<FlightClassModel> =
            arrayListOf(
                    FlightClassModel(ID_EKONOMI, CLASS_EKONOMI),
                    FlightClassModel(ID_BISNIS, CLASS_BISNIS),
                    FlightClassModel(ID_UTAMA, CLASS_UTAMA)
            )

    interface Listener {
        fun onClassSelected(classEntity: FlightClassModel)
    }

    companion object {
        const val TAG_SELECT_CLASS = "TagFlightSelectClassBottomSheet"

        private const val ID_EKONOMI = 1
        private const val ID_BISNIS = 2
        private const val ID_UTAMA = 3
        private const val CLASS_EKONOMI = "Ekonomi"
        private const val CLASS_BISNIS = "Bisnis"
        private const val CLASS_UTAMA = "Utama"
    }
}