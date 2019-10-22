package com.tokopedia.product.detail.view.widget

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.financing.FinancingDataResponse
import com.tokopedia.product.detail.view.adapter.InstallmentDataPagerAdapter
import com.tokopedia.product.detail.view.fragment.FtPdpInstallmentCalculationFragment
import com.tokopedia.product.detail.view.util.FtInstallmentListItem
import com.tokopedia.referral.view.HeightWrappingViewPager
import java.util.*

class FtPDPInstallmentBottomSheet : BottomSheets() {

    companion object {
        const val KEY_PDP_FINANCING_DATA = "keyPDPFinancingData"
    }

    private var tabLayout: TabLayout? = null
    private var heightWrappingViewPager: HeightWrappingViewPager? = null
    internal var ftInstallmentItemList: MutableList<FtInstallmentListItem> = ArrayList()

    private var installmentData: FinancingDataResponse = FinancingDataResponse()


    override fun getLayoutResourceId(): Int {
        return R.layout.pdp_installment_calculation_bottom_sheet
    }

    override fun getBaseLayoutResourceId(): Int {
        return R.layout.widget_bottomsheet_installment_calculation
    }

    private fun getPageTitle(position: Int): CharSequence {
        return resources.getStringArray(R.array.ft_installment_type_title)[position]
    }

    override fun initView(view: View?) {
        view?.let {
            getArgumentsData()
            tabLayout = view.findViewById(R.id.tabs)
            heightWrappingViewPager = view.findViewById(R.id.view_pager)
        }
        loadData()
//        configBottomSheetHeight()
    }

    override fun title(): String {
        return "Simulasi Cicilan"
    }

    private fun loadData() {
        populateTwoTabItem()
        val instantLoanPagerAdapter = InstallmentDataPagerAdapter(fragmentManager!!)
        instantLoanPagerAdapter.setData(ftInstallmentItemList)
        heightWrappingViewPager!!.adapter = instantLoanPagerAdapter
        tabLayout!!.setupWithViewPager(heightWrappingViewPager)
        heightWrappingViewPager!!.currentItem = 0
        tabLayout!!.getTabAt(0)?.select()
    }

    private fun configBottomSheetHeight() {
        dialog?.run {
            val parent = findViewById<FrameLayout>(android.support.design.R.id.design_bottom_sheet)
            val displaymetrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(displaymetrics)
            val screenHeight = displaymetrics.heightPixels
            val maxHeight = (screenHeight * 0.9f).toInt()
            parent.measure(
                    View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(maxHeight, View.MeasureSpec.AT_MOST)
            )
            val params = parent.layoutParams
            params.height = parent.measuredHeight
            parent.layoutParams = params
        }
    }

    private fun populateTwoTabItem() {
        ftInstallmentItemList.add(FtInstallmentListItem(getPageTitle(0),
                getCreditInstallmentFragment()))
        ftInstallmentItemList.add(FtInstallmentListItem(getPageTitle(1),
                getNonCreditInstallmentFragment()))
    }

    private fun getNonCreditInstallmentFragment(): Fragment? {
        return FtPdpInstallmentCalculationFragment.createInstance(
                installmentData.ftInstallmentCalculation.data.nonCreditCardInstallmentData)
    }

    private fun getCreditInstallmentFragment(): Fragment? {
        return FtPdpInstallmentCalculationFragment.createInstance(
                installmentData.ftInstallmentCalculation.data.creditCardInstallmentData)
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        super.setupDialog(dialog, style)
        dialog?.run {
            findViewById<FrameLayout>(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(android.support.design.R.id.design_bottom_sheet)
            if (bottomSheet != null) {
                val behavior = BottomSheetBehavior.from(bottomSheet)
                behavior.skipCollapsed = true
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return bottomSheetDialog
    }

    private fun getArgumentsData() {
        arguments?.let {
            installmentData = it.getParcelable(KEY_PDP_FINANCING_DATA) ?: FinancingDataResponse()
        }
    }
}
