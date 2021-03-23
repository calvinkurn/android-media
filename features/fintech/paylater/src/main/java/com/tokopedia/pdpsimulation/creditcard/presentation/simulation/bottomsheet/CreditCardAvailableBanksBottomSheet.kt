package com.tokopedia.pdpsimulation.creditcard.presentation.simulation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.creditcard.domain.model.SimulationBank
import com.tokopedia.pdpsimulation.creditcard.presentation.simulation.adapter.CreditCardAvailableBanksAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.base_list_bottomsheet_widget.*

class CreditCardAvailableBanksBottomSheet : BottomSheetUnify() {

    init {
        setShowListener {
            bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(view: View, slideOffset: Float) {
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                    }
                }
            })
        }
    }

    private val childLayoutRes = R.layout.base_list_bottomsheet_widget
    private var bankList: ArrayList<SimulationBank> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgumentData()
        setDefaultParams()
        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter()
    }

    private fun getArgumentData() {
        arguments?.let {
            bankList = it.getParcelableArrayList(CREDIT_CARD_BANK_DATA) ?: arrayListOf()
        }
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(childLayoutRes,
                null, false)
        setChild(childView)
    }

    private fun initAdapter() {
        baseList.adapter = CreditCardAvailableBanksAdapter(bankList)
        baseList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }


    private fun setDefaultParams() {
        setTitle("Tersedia cicilan 6 bulan")
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        isFullpage = true
    }

    companion object {
        const val CREDIT_CARD_BANK_DATA = "BANK_DATA"
        private const val TAG = "CreditCardAvailableBanksBottomSheet"

        fun show(bundle: Bundle, childFragmentManager: FragmentManager) {
            val creditCardAvailableBanksBottomSheet = CreditCardAvailableBanksBottomSheet().apply {
                arguments = bundle
            }
            creditCardAvailableBanksBottomSheet.show(childFragmentManager, TAG)
        }
    }
}