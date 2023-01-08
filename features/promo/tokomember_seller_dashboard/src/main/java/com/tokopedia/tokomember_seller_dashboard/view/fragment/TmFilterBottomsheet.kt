package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmFilterCallback
import com.tokopedia.tokomember_seller_dashboard.model.TripleDotsItem
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_VOUCHER_FILTER_STATUS
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_VOUCHER_FILTER_TYPE
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.tm_filter_bottomsheet.*

class TmFilterBottomsheet: BottomSheetUnify() {

    private var selectedStatus = "1,2,3,4"
    private var selectedType = "0"
    private val childLayoutRes = R.layout.tm_filter_bottomsheet
    private var tripleDotsList: List<TripleDotsItem?>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        arguments?.getString(BUNDLE_VOUCHER_FILTER_STATUS)?.let {
            selectedStatus = it
        }
        arguments?.getString(BUNDLE_VOUCHER_FILTER_TYPE)?.let {
            selectedType = it
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultParams()
        initBottomSheet()
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        showHeader=false
        showCloseIcon = false
        showKnob = true
        setChild(childView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        radioSemuaStatus.setOnClickListener{
            if(selectedStatus != "1,2,3,4")
            {
                radioSemuaStatus.isChecked = true
                radioStatusAktif.isChecked = false
                radioBelumAktif.isChecked = false
                radioSudahBeharkhir.isChecked = false
                selectedStatus = "1,2,3,4"
            }
            else{
                radioSemuaStatus.isChecked = true
                radioStatusAktif.isChecked = false
                radioBelumAktif.isChecked = false
                radioSudahBeharkhir.isChecked = false
                selectedStatus = "1,2,3,4"
            }
        }
        radioStatusAktif.setOnClickListener{
            if(selectedStatus != "2")
            {
                radioStatusAktif.isChecked = true
                radioSemuaStatus.isChecked = false
                radioBelumAktif.isChecked = false
                radioSudahBeharkhir.isChecked = false
                selectedStatus = "2"
            }
            else{
                radioSemuaStatus.isChecked = true
                radioStatusAktif.isChecked = false
                radioBelumAktif.isChecked = false
                radioSudahBeharkhir.isChecked = false
                selectedStatus = "1,2,3,4"
            }
        }
        radioBelumAktif.setOnClickListener{
            if(selectedStatus != "1")
            {
                radioBelumAktif.isChecked = true
                radioStatusAktif.isChecked = false
                radioSemuaStatus.isChecked = false
                radioSudahBeharkhir.isChecked = false
                selectedStatus = "1"
            }
            else{
                radioSemuaStatus.isChecked = true
                radioStatusAktif.isChecked = false
                radioBelumAktif.isChecked = false
                radioSudahBeharkhir.isChecked = false
                selectedStatus = "1,2,3,4"
            }
        }
        radioSudahBeharkhir.setOnClickListener{
            if(selectedStatus != "4")
            {
                radioSudahBeharkhir.isChecked = true
                radioStatusAktif.isChecked = false
                radioSemuaStatus.isChecked = false
                radioBelumAktif.isChecked = false
                selectedStatus = "4"
            }
            else{
                radioSemuaStatus.isChecked = true
                radioStatusAktif.isChecked = false
                radioBelumAktif.isChecked = false
                radioSudahBeharkhir.isChecked = false
                selectedStatus = "1,2,3,4"
            }
        }

        radioTypeSemua.setOnClickListener{
            if(selectedType != "0")
            {
                radioTypeSemua.isChecked = true
                radioTypeCashback.isChecked = false
                radioTypeGratisOngkir.isChecked = false
                selectedType = "0"
            }
            else{
                radioTypeSemua.isChecked = true
                radioTypeCashback.isChecked = false
                radioTypeGratisOngkir.isChecked = false
                selectedType = "0"
            }
        }
        radioTypeCashback.setOnClickListener{
            if(selectedType != "3")
            {
                selectedType = "3"
                radioTypeCashback.isChecked = true
                radioTypeSemua.isChecked = false
                radioTypeGratisOngkir.isChecked = false
            }
            else{
                radioTypeSemua.isChecked = true
                radioTypeCashback.isChecked = false
                radioTypeGratisOngkir.isChecked = false
                selectedType = "0"
            }
        }
        radioTypeGratisOngkir.setOnClickListener{
            if(selectedType != "1")
            {
                selectedType = "1"
                radioTypeGratisOngkir.isChecked = true
                radioTypeSemua.isChecked = false
                radioTypeCashback.isChecked = false
            }
            else{
                radioTypeSemua.isChecked = true
                radioTypeCashback.isChecked = false
                radioTypeGratisOngkir.isChecked = false
                selectedType = "0"
            }
        }

        when(selectedStatus){
            "1,2,3,4" -> {
                radioSemuaStatus.isChecked = true
            }
            "2" -> {
                radioStatusAktif.isChecked = true
            }
            "1" -> {
                radioBelumAktif.isChecked = true
            }
            "4" -> {
                radioSudahBeharkhir.isChecked = true
            }
        }

        when(selectedType){
            "0" -> {
                radioTypeSemua.isChecked = true
            }
            "3" -> {
                radioTypeCashback.isChecked = true
            }
            "1" -> {
                radioTypeGratisOngkir.isChecked = true
            }
        }

        filterStatusSemua.setOnClickListener {
            if(selectedStatus != "1,2,3,4")
            {
                radioSemuaStatus.isChecked = true
                radioStatusAktif.isChecked = false
                radioBelumAktif.isChecked = false
                radioSudahBeharkhir.isChecked = false
                selectedStatus = "1,2,3,4"
            }
            else{
                radioSemuaStatus.isChecked = true
                radioStatusAktif.isChecked = false
                radioBelumAktif.isChecked = false
                radioSudahBeharkhir.isChecked = false
                selectedStatus = "1,2,3,4"
            }
        }
        filterStatusAktif.setOnClickListener {
            if(selectedStatus != "2")
            {
                radioStatusAktif.isChecked = true
                radioSemuaStatus.isChecked = false
                radioBelumAktif.isChecked = false
                radioSudahBeharkhir.isChecked = false
                selectedStatus = "2"
            }
            else{
                radioSemuaStatus.isChecked = true
                radioStatusAktif.isChecked = false
                radioBelumAktif.isChecked = false
                radioSudahBeharkhir.isChecked = false
                selectedStatus = "1,2,3,4"
            }
        }
        filterStatusBelumAktif.setOnClickListener {
            if(selectedStatus != "1")
            {
                radioBelumAktif.isChecked = true
                radioStatusAktif.isChecked = false
                radioSemuaStatus.isChecked = false
                radioSudahBeharkhir.isChecked = false
                selectedStatus = "1"
            }
            else{
                radioSemuaStatus.isChecked = true
                radioStatusAktif.isChecked = false
                radioBelumAktif.isChecked = false
                radioSudahBeharkhir.isChecked = false
                selectedStatus = "1,2,3,4"
            }
        }
        filterStatusSudahBerakhir.setOnClickListener {
            if(selectedStatus != "4")
            {
                radioSudahBeharkhir.isChecked = true
                radioStatusAktif.isChecked = false
                radioSemuaStatus.isChecked = false
                radioBelumAktif.isChecked = false
                selectedStatus = "4"
            }
            else{
                radioSemuaStatus.isChecked = true
                radioStatusAktif.isChecked = false
                radioBelumAktif.isChecked = false
                radioSudahBeharkhir.isChecked = false
                selectedStatus = "1,2,3,4"
            }
        }

        filterTipeSemua.setOnClickListener {
            if(selectedType != "0")
            {
                radioTypeSemua.isChecked = true
                radioTypeCashback.isChecked = false
                radioTypeGratisOngkir.isChecked = false
                selectedType = "0"
            }
            else{
                radioTypeSemua.isChecked = true
                radioTypeCashback.isChecked = false
                radioTypeGratisOngkir.isChecked = false
                selectedType = "0"
            }
        }
        filterTipeCashback.setOnClickListener {
            if(selectedType != "3")
            {
                selectedType = "3"
                radioTypeCashback.isChecked = true
                radioTypeSemua.isChecked = false
                radioTypeGratisOngkir.isChecked = false
            }
            else{
                radioTypeSemua.isChecked = true
                radioTypeCashback.isChecked = false
                radioTypeGratisOngkir.isChecked = false
                selectedType = "0"
            }
        }
        filterTipeGratisOngkir.setOnClickListener {
            if(selectedType != "1")
            {
                selectedType = "1"
                radioTypeGratisOngkir.isChecked = true
                radioTypeSemua.isChecked = false
                radioTypeCashback.isChecked = false
            }
            else{
                radioTypeSemua.isChecked = true
                radioTypeCashback.isChecked = false
                radioTypeGratisOngkir.isChecked = false
                selectedType = "0"
            }
        }

        btnFilter.setOnClickListener {
            tmFilterCallback.selectedFilter(selectedStatus, selectedType)
            dismiss()
        }

    }

    private fun setDefaultParams() {
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true

        customPeekHeight = (getScreenHeight()).toDp()
    }

    companion object {

        const val TAG = "TM_DASH_OPTIONS_MENU_BOTTOM_SHEET"
        private lateinit var tmFilterCallback: TmFilterCallback

        fun show(
            childFragmentManager: FragmentManager,
            tmFilterCallback: TmFilterCallback,
            voucherStatus: String,
            voucherType: String
        ) {
            val bundle = Bundle()
            bundle.putString(BUNDLE_VOUCHER_FILTER_STATUS, voucherStatus)
            bundle.putString(BUNDLE_VOUCHER_FILTER_TYPE, voucherType)
            this.tmFilterCallback = tmFilterCallback
            val tokomemberIntroBottomsheet = TmFilterBottomsheet().apply {
                arguments = bundle
            }
            tokomemberIntroBottomsheet.show(childFragmentManager, TAG)
        }

    }

}