package com.tokopedia.pdpsimulation.common.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.common.CreditCard
import com.tokopedia.pdpsimulation.common.PayLater
import com.tokopedia.pdpsimulation.common.PaymentMode
import com.tokopedia.pdpsimulation.common.constants.PRODUCT_PRICE
import com.tokopedia.pdpsimulation.common.di.component.PdpSimulationComponent
import com.tokopedia.pdpsimulation.common.helper.BottomSheetManager
import com.tokopedia.pdpsimulation.common.listener.PdpSimulationCallback
import com.tokopedia.pdpsimulation.common.presentation.adapter.PayLaterPagerAdapter
import com.tokopedia.pdpsimulation.creditcard.presentation.simulation.CreditCardSimulationFragment
import com.tokopedia.pdpsimulation.creditcard.presentation.tnc.CreditCardTncFragment
import com.tokopedia.pdpsimulation.creditcard.viewmodel.CreditCardViewModel
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterItemProductData
import com.tokopedia.pdpsimulation.paylater.domain.model.UserCreditApplicationStatus
import com.tokopedia.pdpsimulation.paylater.presentation.detail.PayLaterOffersFragment
import com.tokopedia.pdpsimulation.paylater.presentation.registration.PayLaterSignupBottomSheet
import com.tokopedia.pdpsimulation.paylater.presentation.simulation.PayLaterSimulationFragment
import com.tokopedia.pdpsimulation.paylater.viewModel.PayLaterViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_pdp_simulation.*
import javax.inject.Inject

class PdpSimulationFragment : BaseDaggerFragment(),
        PdpSimulationCallback,
        TabLayout.OnTabSelectedListener,
        ViewPager.OnPageChangeListener,
        CompoundButton.OnCheckedChangeListener {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val payLaterViewModel: PayLaterViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(PayLaterViewModel::class.java)
    }

    private val creditCardViewModel: CreditCardViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(CreditCardViewModel::class.java)
    }

    private val bottomSheetManager: BottomSheetManager by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetManager(childFragmentManager)
    }

    private val productPrice: Int by lazy {
        arguments?.getInt(PRODUCT_PRICE) ?: 0
    }

    private var paymentMode: PaymentMode = PayLater
    private var payLaterDataList = arrayListOf<PayLaterItemProductData>()
    private var applicationStatusList = arrayListOf<PayLaterApplicationDetail>()
    private var pagerAdapter: PayLaterPagerAdapter? = null

    override fun getScreenName(): String {
        return "PayLater & Cicilan"
    }

    override fun initInjector() {
        getComponent(PdpSimulationComponent::class.java).inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_pdp_simulation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSimulationProductInfo()
        renderTabAndViewPager()
        initListeners()
    }

    override fun getSimulationProductInfo() {
        parentDataGroup.visible()
        when (paymentMode) {
            is PayLater -> payLaterViewModel.getPayLaterSimulationData(productPrice)
            is CreditCard -> creditCardViewModel.getCreditCardSimulationData(productPrice.toFloat())
        }
    }

    private fun observeViewModel() {
        payLaterViewModel.payLaterApplicationStatusResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onApplicationStatusLoaded(it.data)
                is Fail -> onApplicationStatusLoadingFail(it.throwable)
            }
        })
    }

    private fun onApplicationStatusLoadingFail(throwable: Throwable) {
        payLaterDataList = payLaterViewModel.getPayLaterOptions()
    }

    private fun onApplicationStatusLoaded(data: UserCreditApplicationStatus) {
        payLaterDataList = payLaterViewModel.getPayLaterOptions()
        applicationStatusList = data.applicationDetailList ?: arrayListOf()
    }

    private fun isPayLaterSimulationPage(): Boolean {
        return (payLaterViewPager.currentItem == SIMULATION_TAB_INDEX &&
                paymentMode == PayLater)
    }

    private fun initListeners() {
        paylaterDaftarWidget.setOnClickListener {
            onRegisterWidgetClicked()
        }
        modeSwitcher.setOnCheckedChangeListener(this)
        paylaterTabLayout.tabLayout.addOnTabSelectedListener(this)
        payLaterViewPager.addOnPageChangeListener(this)
    }

    private fun renderTabAndViewPager() {
        setUpTabLayout()
        if (pagerAdapter == null)
            pagerAdapter = PayLaterPagerAdapter(context!!, childFragmentManager, 0)
        pagerAdapter?.setList(getFragments())
        payLaterViewPager.adapter = pagerAdapter
    }

    private fun setUpTabLayout() {
        paylaterTabLayout?.run {
            setupWithViewPager(payLaterViewPager)
            getUnifyTabLayout().removeAllTabs()
            addNewTab(context.getString(R.string.pdp_simulation_tab_title))
            when (paymentMode) {
                is CreditCard -> addNewTab(context.getString(R.string.pdp_simulation_credit_card_tnc_title))
                else -> addNewTab(context.getString(R.string.pay_later_offer_details_tab_title))
            }
        }
    }

    private fun getFragments(): List<Fragment> {
        val fragmentList = mutableListOf<Fragment>()
        when (paymentMode) {
            is CreditCard -> {
                fragmentList.add(CreditCardSimulationFragment.newInstance(this))
                fragmentList.add(CreditCardTncFragment.newInstance(this))
            }
            else -> {
                fragmentList.add(PayLaterSimulationFragment.newInstance(this))
                fragmentList.add(PayLaterOffersFragment.newInstance(this))
            }
        }
        return fragmentList
    }


    override fun showRegisterWidget() {
        if (isPayLaterSimulationPage())
            daftarGroup.visible()
        else daftarGroup.gone()
    }

    override fun onRegisterWidgetClicked() {
        if (payLaterDataList.isNotEmpty()) {
            openBottomSheet(populatePayLaterBundle(), PayLaterSignupBottomSheet::class.java)
        }
    }

    private fun populatePayLaterBundle() = Bundle().apply {
        putParcelableArrayList(PayLaterSignupBottomSheet.PAY_LATER_APPLICATION_DATA, applicationStatusList)
        putParcelableArrayList(PayLaterSignupBottomSheet.PAY_LATER_PARTNER_DATA, payLaterDataList)
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        onPageSelected(tab.position)
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        if (position == SIMULATION_TAB_INDEX && !payLaterViewModel.isPayLaterProductActive) showRegisterWidget()
        else daftarGroup.gone()
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onCheckedChanged(modeButton: CompoundButton, isChecked: Boolean) {
        if (isChecked) {
            paymentMode = CreditCard
            creditCardViewModel.getCreditCardSimulationData(productPrice.toFloat())
        } else {
            paymentMode = PayLater
        }
        renderTabAndViewPager()
    }

    override fun showNoNetworkView() {
        parentDataGroup.gone()
        daftarGroup.gone()
        payLaterParentGlobalError.setType(GlobalError.NO_CONNECTION)
        payLaterParentGlobalError.show()
        payLaterParentGlobalError.setActionClickListener {
            payLaterParentGlobalError.gone()
            getSimulationProductInfo()
        }
    }

    override fun <T : Any> openBottomSheet(bundle: Bundle, modelClass: Class<T>) {
        bottomSheetManager.showBottomSheet(modelClass, bundle, this)
    }

    override fun switchPaymentMode() {
        modeSwitcher.isChecked = !modeSwitcher.isChecked
    }

    companion object {
        const val SIMULATION_TAB_INDEX = 0

        @JvmStatic
        fun newInstance(bundle: Bundle): PdpSimulationFragment {
            val fragment = PdpSimulationFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}