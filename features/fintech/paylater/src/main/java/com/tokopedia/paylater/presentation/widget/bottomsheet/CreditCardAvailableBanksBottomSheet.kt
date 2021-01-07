package com.tokopedia.paylater.presentation.widget.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.CreditCardBank
import com.tokopedia.paylater.presentation.adapter.CreditCardAvailableBanksAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.credit_card_bank_list_bottomsheet_widget.*
import kotlinx.android.synthetic.main.credit_card_bank_list_bottomsheet_widget.view.*


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

    private val childLayoutRes = R.layout.credit_card_bank_list_bottomsheet_widget
    private var bankList: ArrayList<CreditCardBank> = arrayListOf()

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
        rvBankList.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            v.onTouchEvent(event)
            true
        }
        rvBankList.adapter = CreditCardAvailableBanksAdapter()
        rvBankList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        (rvBankList.adapter as CreditCardAvailableBanksAdapter).setBankList(bankList)
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
        private const val TAG = "FT_TAG"

        fun show(bundle: Bundle, childFragmentManager: FragmentManager) {
            val creditCardAvailableBanksBottomSheet = CreditCardAvailableBanksBottomSheet().apply {
                arguments = bundle
            }
            creditCardAvailableBanksBottomSheet.show(childFragmentManager, TAG)
        }
    }
}