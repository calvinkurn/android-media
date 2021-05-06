package com.tokopedia.product.detail.view.widget

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.design.bottomsheet.BottomSheetViewPager
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.financing.FtInstallmentCalculationDataResponse
import com.tokopedia.product.detail.view.adapter.InstallmentDataPagerAdapter
import com.tokopedia.product.detail.view.fragment.FtPdpInstallmentCalculationFragment
import com.tokopedia.product.detail.view.util.FtInstallmentListItem

class FtPDPInstallmentBottomSheet : BottomSheetDialogFragment() {

    companion object {
        const val KEY_PDP_FINANCING_DATA = "keyPDPFinancingData"
        const val KEY_PDP_PRODUCT_PRICE = "keyPDPProductPrice"
        const val KEY_PDP_IS_OFFICIAL = "kryPDPIsOfficial"
        const val PDP_INSTALLMENT_SCREEN_NAME = "Simulasi Cicilan"
    }

    private var tabLayout: TabLayout? = null
    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null
    private var heightWrappingViewPager: BottomSheetViewPager? = null
    internal var ftInstallmentItemList: MutableList<FtInstallmentListItem> = ArrayList()

    private var installmentDataId: String = ""
    private var installmentData: FtInstallmentCalculationDataResponse = FtInstallmentCalculationDataResponse()
    private var productPrice: Float = 0f
    private var isOfficialStore: Boolean = false

    private fun getBaseLayoutResourceId(): Int {
        return R.layout.widget_bottomsheet_installment_calculation
    }

    private fun getPageTitle(position: Int): CharSequence {
        return resources.getStringArray(R.array.ft_installment_type_title)[position]
    }

    fun initView(view: View?) {
        view?.let {
            getArgumentsData()
            tabLayout = view.findViewById(R.id.tabs)
            heightWrappingViewPager = view.findViewById(R.id.view_pager)
        }
        loadData()
    }

    fun title(): String {
        return PDP_INSTALLMENT_SCREEN_NAME
    }

    private fun loadData() {
        populateTwoTabItem()
        val installmentDataPagerAdapter = InstallmentDataPagerAdapter(childFragmentManager)
        installmentDataPagerAdapter.setData(ftInstallmentItemList)
        heightWrappingViewPager?.adapter = installmentDataPagerAdapter
        tabLayout?.setupWithViewPager(heightWrappingViewPager)
        heightWrappingViewPager?.currentItem = 0
        tabLayout?.getTabAt(0)?.select()
    }

    protected fun configView(parentView: View) {
        val textViewTitle = parentView.findViewById<TextView>(R.id.pdp_installment_tv_title)
        textViewTitle.text = title()

        val layoutTitle = parentView.findViewById<View>(R.id.pdp_installment_layout_title)
        layoutTitle.setOnClickListener { v -> onCloseButtonClick() }

        val closeButton = parentView.findViewById<View>(R.id.pdp_installment_btn_close)
        closeButton.setOnClickListener {
            dismiss()
        }

        initView(parentView)
    }

    protected fun onCloseButtonClick() {
        if (bottomSheetBehavior == null) {
            return
        }

        if (bottomSheetBehavior?.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior?.setState(BottomSheetBehavior.STATE_COLLAPSED)
        } else if (bottomSheetBehavior?.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val inflatedView = inflater.inflate(getBaseLayoutResourceId(), container, false)
        configView(inflatedView)
        inflatedView.measure(0, 0)
        val height = inflatedView.measuredHeight

        try {
            val params = (inflatedView.getParent() as View).layoutParams
            inflatedView.measure(0, 0)
            val displayMetrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            val screenHeight = displayMetrics.heightPixels

            if (bottomSheetBehavior != null)
                bottomSheetBehavior?.peekHeight = height
            params.height = screenHeight
        } catch (illegalEx: IllegalArgumentException) {
        } catch (ignored: Exception) {
        }

        return inflatedView

    }

    private fun populateTwoTabItem() {
        ftInstallmentItemList.add(FtInstallmentListItem(getPageTitle(0),
                getNonCreditInstallmentFragment()))
        ftInstallmentItemList.add(FtInstallmentListItem(getPageTitle(1),
                getCreditInstallmentFragment()))
    }

    private fun getNonCreditInstallmentFragment(): Fragment? {
        return FtPdpInstallmentCalculationFragment.createInstance(context, productPrice,
                installmentData.data.tncDataList, isOfficialStore,
                installmentData.data.nonCreditCardInstallmentData)
    }

    private fun getCreditInstallmentFragment(): Fragment? {
        return FtPdpInstallmentCalculationFragment.createInstance(context, productPrice,
                installmentData.data.tncDataList, isOfficialStore,
                installmentData.data.creditCardInstallmentData)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
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
            installmentDataId = it.getString(KEY_PDP_FINANCING_DATA) ?: ""

            val cacheManager = SaveInstanceCacheManager(context!!, installmentDataId)
            installmentData = cacheManager.get(
                    FtInstallmentCalculationDataResponse::class.java.simpleName,
                    FtInstallmentCalculationDataResponse::class.java
            ) ?: FtInstallmentCalculationDataResponse()
            productPrice = it.getFloat(KEY_PDP_PRODUCT_PRICE)
            isOfficialStore = it.getBoolean(KEY_PDP_IS_OFFICIAL)
        }
    }
}
