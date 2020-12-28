package com.tokopedia.paylater.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.paylater.helper.PayLaterHelper
import com.tokopedia.paylater.R
import com.tokopedia.paylater.di.component.PayLaterComponent
import com.tokopedia.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.paylater.domain.model.PayLaterItemProductData
import com.tokopedia.paylater.domain.model.UserCreditApplicationStatus
import com.tokopedia.paylater.presentation.adapter.PayLaterPagerAdapter
import com.tokopedia.paylater.presentation.viewModel.PayLaterViewModel
import com.tokopedia.paylater.presentation.widget.bottomsheet.PayLaterSignupBottomSheet
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_paylater.*
import javax.inject.Inject


class PayLaterFragment : BaseDaggerFragment(),
        SimulationFragment.RegisterPayLaterCallback,
        PayLaterOffersFragment.PayLaterOfferCallback,
        TabLayout.OnTabSelectedListener,
        ViewPager.OnPageChangeListener {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val payLaterViewModel: PayLaterViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(PayLaterViewModel::class.java)
    }

    private var payLaterDataList = arrayListOf<PayLaterItemProductData>()
    private var applicationStatusList = arrayListOf<PayLaterApplicationDetail>()

    override fun getScreenName(): String {
        return "PayLater & Cicilan"
    }

    override fun initInjector() {
        getComponent(PayLaterComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_paylater, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderTabAndViewPager()
        initListeners()
        fetchPayLaterProductData()
    }

    private fun fetchPayLaterProductData() {
        payLaterViewModel.getPayLaterProductData()
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
        paylaterTabLayout.tabLayout.addOnTabSelectedListener(this)
        payLaterViewPager.addOnPageChangeListener(this)
    }

    private fun renderTabAndViewPager() {
        payLaterViewPager.adapter = getViewPagerAdapter()
        paylaterTabLayout?.run {
            setupWithViewPager(payLaterViewPager)
            addNewTab(context.getString(R.string.payLater_simulation_tab_title))
            addNewTab(context.getString(R.string.payLater_offer_details_tab_title))
        }
    }

    private fun getViewPagerAdapter(): PagerAdapter {
        val list = mutableListOf<Fragment>()
        val simulationFragment = SimulationFragment.newInstance(arguments)
        val payLaterOffersFragment = PayLaterOffersFragment.newInstance()
        simulationFragment.setPayLaterClickedListener(this)
        payLaterOffersFragment.setPayLaterProductCallback(this)
        list.add(simulationFragment)
        list.add(payLaterOffersFragment)
        val pagerAdapter = PayLaterPagerAdapter(context!!, childFragmentManager, 0)
        pagerAdapter.setList(list)
        return pagerAdapter
    }

    override fun onRefreshPayLater() {
        fetchPayLaterProductData()
    }

    override fun onRegisterPayLaterClicked() {
        if (payLaterDataList.isNotEmpty()) {
            PayLaterSignupBottomSheet.getInstance(
                    populatePayLaterBundle()).also {
                it.setActionListener(object : PayLaterSignupBottomSheet.Listener {
                    override fun onPayLaterSignupClicked(productItemData: PayLaterItemProductData, partnerApplicationDetail: PayLaterApplicationDetail?) {
                        PayLaterHelper.openBottomSheet(context, childFragmentManager, productItemData, partnerApplicationDetail)
                    }
                })
                it.show(childFragmentManager, PayLaterSignupBottomSheet.TAG)
            }
        }
    }

    private fun populatePayLaterBundle(): Bundle {
        val payLaterBundle = Bundle()
        if (applicationStatusList.isNotEmpty())
            payLaterBundle.putParcelableArrayList(PayLaterSignupBottomSheet.PAY_LATER_APPLICATION_DATA, applicationStatusList)
        payLaterBundle.putParcelableArrayList(PayLaterSignupBottomSheet.PAY_LATER_PARTNER_DATA, payLaterDataList)
        return payLaterBundle
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        if (tab.position == SIMULATION_TAB_INDEX && PayLaterHelper.isKredivoApplicationStatusEmpty(applicationStatusList))
            daftarGroup.visible()
        else daftarGroup.gone()
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

    companion object {
        const val SIMULATION_TAB_INDEX = 0
        const val PAY_LATER_PRODUCT_DETAIL_TAB_INDEX = 1

        @JvmStatic
        fun newInstance(bundle: Bundle) : PayLaterFragment {
            val fragment = PayLaterFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}