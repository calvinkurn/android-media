package com.tokopedia.paylater.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
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
import com.tokopedia.paylater.PRODUCT_PRICE
import com.tokopedia.paylater.R
import com.tokopedia.paylater.data.mapper.CreditCard
import com.tokopedia.paylater.data.mapper.PayLater
import com.tokopedia.paylater.data.mapper.PaymentMode
import com.tokopedia.paylater.di.component.PayLaterComponent
import com.tokopedia.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.paylater.domain.model.PayLaterItemProductData
import com.tokopedia.paylater.domain.model.UserCreditApplicationStatus
import com.tokopedia.paylater.helper.PayLaterHelper
import com.tokopedia.paylater.presentation.adapter.PayLaterPagerAdapter
import com.tokopedia.paylater.presentation.viewModel.PayLaterViewModel
import com.tokopedia.paylater.presentation.widget.bottomsheet.PayLaterSignupBottomSheet
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_paylater.*
import javax.inject.Inject

class PayLaterFragment : BaseDaggerFragment(),
        PayLaterSimulationFragment.PayLaterSimulationCallback,
        PayLaterOffersFragment.PayLaterOfferCallback,
        TabLayout.OnTabSelectedListener,
        ViewPager.OnPageChangeListener,
        CompoundButton.OnCheckedChangeListener, View.OnTouchListener {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val payLaterViewModel: PayLaterViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(PayLaterViewModel::class.java)
    }

    private val productPrice: Int by lazy {
        arguments?.getInt(PRODUCT_PRICE) ?: 0
    }

    private var paymentMode: PaymentMode = PayLater
    private var payLaterDataList = arrayListOf<PayLaterItemProductData>()
    private var applicationStatusList = arrayListOf<PayLaterApplicationDetail>()

    override fun getScreenName(): String {
        return "PayLater & Cicilan"
    }

    override fun initInjector() {
        getComponent(PayLaterComponent::class.java).inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_paylater, container, false)
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
        payLaterViewModel.getPayLaterSimulationData(productPrice)
    }

    override fun getPayLaterProductInfo() {
        payLaterViewModel.getPayLaterProductData()
    }

    override fun getApplicationStatusInfo() {
        payLaterViewModel.getPayLaterApplicationStatus()
    }

    private fun observeViewModel() {
        payLaterViewModel.payLaterApplicationStatusResultLiveData.observe(
                viewLifecycleOwner,
                {
                    when (it) {
                        is Success -> onApplicationStatusLoaded(it.data)
                        is Fail -> onApplicationStatusLoadingFail(it.throwable)
                    }
                },
        )
    }

    private fun onApplicationStatusLoadingFail(throwable: Throwable) {
        payLaterDataList = payLaterViewModel.getPayLaterOptions()
        setPayLaterUI()
    }

    private fun onApplicationStatusLoaded(data: UserCreditApplicationStatus) {
        payLaterDataList = payLaterViewModel.getPayLaterOptions()
        applicationStatusList = data.applicationDetailList
        setPayLaterUI()
    }

    private fun setPayLaterUI() {
        // if Kredivo status is empty then show
        if (applicationStatusList.getOrNull(0)?.payLaterApplicationStatus?.isEmpty() == true
                && payLaterViewPager.currentItem == SIMULATION_TAB_INDEX) {
            daftarGroup.visible()
        }
    }

    private fun initListeners() {
        paylaterDaftarWidget.setOnClickListener {
            onRegisterPayLaterClicked()
        }
        modeSwitcher.setOnTouchListener(this)
        modeSwitcher.setOnCheckedChangeListener(this)
        paylaterTabLayout.tabLayout.addOnTabSelectedListener(this)
        payLaterViewPager.addOnPageChangeListener(this)
    }

    private fun renderTabAndViewPager() {
        setUpTabLayout()
        val pagerAdapter = PayLaterPagerAdapter(context!!, childFragmentManager, 0)
        pagerAdapter.setList(getFragments())
        payLaterViewPager.adapter = pagerAdapter
    }

    private fun setUpTabLayout() {
        paylaterTabLayout?.run {
            setupWithViewPager(payLaterViewPager)
            getUnifyTabLayout().removeAllTabs()
            addNewTab(context.getString(R.string.payLater_simulation_tab_title))
            when (paymentMode) {
                is CreditCard -> addNewTab(context.getString(R.string.payLater_credit_card_tnc_title))
                else -> addNewTab(context.getString(R.string.payLater_offer_details_tab_title))
            }
        }
    }

    private fun getFragments(): List<Fragment> {
        val fragmentList = mutableListOf<Fragment>()
        when (paymentMode) {
            is CreditCard -> {
                val simulationFragment = CreditCardSimulationFragment.newInstance()
                val tncFragment = CreditCardTncFragment.newInstance()
                fragmentList.add(simulationFragment)
                fragmentList.add(tncFragment)
            }
            else -> {
                val simulationFragment = PayLaterSimulationFragment.newInstance()
                val payLaterOffersFragment = PayLaterOffersFragment.newInstance()
                simulationFragment.setSimulationListener(this)
                payLaterOffersFragment.setPayLaterProductCallback(this)
                fragmentList.add(simulationFragment)
                fragmentList.add(payLaterOffersFragment)
            }
        }
        return fragmentList
    }

    override fun onRegisterPayLaterClicked() {
        if (payLaterDataList.isNotEmpty()) {
            val bottomSheet = PayLaterSignupBottomSheet.getInstance(
                    populatePayLaterBundle())
            bottomSheet.also {
                it.setActionListener(object : PayLaterSignupBottomSheet.Listener {
                    override fun onPayLaterSignupClicked(productItemData: PayLaterItemProductData, partnerApplicationDetail: PayLaterApplicationDetail?) {
                        PayLaterHelper.openBottomSheet(context, childFragmentManager, productItemData, partnerApplicationDetail)
                    }
                })
                it.show(childFragmentManager, PayLaterSignupBottomSheet.TAG)
            }
        }
    }

    override fun noInternetCallback() {
        parentDataGroup.gone()
        daftarGroup.gone()
        payLaterParentGlobalError.setType(GlobalError.NO_CONNECTION)
        payLaterParentGlobalError.show()
        payLaterParentGlobalError.setActionClickListener {
            payLaterParentGlobalError.gone()
            getSimulationProductInfo()
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
        if (position == 0 && PayLaterHelper.isKredivoApplicationStatusEmpty(applicationStatusList))
            daftarGroup.visible()
        else daftarGroup.gone()
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onCheckedChanged(modeButton: CompoundButton, isChecked: Boolean) {
        val text = if (isChecked) "This is Kartu Kredit" else "This is PayLater"
        // @Todo comment it out afterwards
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        if (isChecked) paymentMode = CreditCard else paymentMode = PayLater
        renderTabAndViewPager()
    }

    companion object {
        const val SIMULATION_TAB_INDEX = 0
        const val PAY_LATER_PRODUCT_DETAIL_TAB_INDEX = 1

        @JvmStatic
        fun newInstance(bundle: Bundle): PayLaterFragment {
            val fragment = PayLaterFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        return when(event.action) {
            MotionEvent.ACTION_UP -> {
                val xPosition = event.x.toInt()
                val isModeChanged = xPosition >= modeSwitcher.width / 2
                modeSwitcher.isChecked = isModeChanged
                true
            } else -> false
        }
    }

}