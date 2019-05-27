package com.tokopedia.topupbills.telco.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.TelcoCustomComponentData
import com.tokopedia.topupbills.telco.data.TelcoCustomData
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentName
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.data.constant.TelcoProductType
import com.tokopedia.topupbills.telco.view.adapter.DigitalTelcoProductTabAdapter
import com.tokopedia.topupbills.telco.view.di.DigitalTopupInstance
import com.tokopedia.topupbills.telco.view.model.DigitalTabTelcoItem
import com.tokopedia.topupbills.telco.view.viewmodel.DigitalTelcoCustomViewModel
import com.tokopedia.topupbills.telco.view.viewmodel.SharedProductTelcoViewModel
import com.tokopedia.topupbills.telco.view.widget.DigitalClientNumberWidget
import com.tokopedia.topupbills.telco.view.widget.DigitalTelcoBuyWidget
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 11/04/19.
 */
class DigitalTelcoPrepaidFragment : DigitalBaseTelcoFragment() {

    private lateinit var telcoClientNumberWidget: DigitalClientNumberWidget
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var buyWidget: DigitalTelcoBuyWidget
    private lateinit var customViewModel: DigitalTelcoCustomViewModel
    private lateinit var sharedModel: SharedProductTelcoViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var telcoCustomData: TelcoCustomComponentData =
            TelcoCustomComponentData(TelcoCustomData(mutableListOf()))

    override fun onStart() {
        context?.let {
            GraphqlClient.init(it)
        }
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            customViewModel = viewModelProvider.get(DigitalTelcoCustomViewModel::class.java)
            sharedModel = viewModelProvider.get(SharedProductTelcoViewModel::class.java)
            sharedModel.setShowTotalPrice(false)
        }
    }

    override fun getScreenName(): String {
        return DigitalTelcoPrepaidFragment::class.java.simpleName
    }

    override fun initInjector() {
        activity?.let {
            val digitalTopupComponent = DigitalTopupInstance.getComponent(it.application)
            digitalTopupComponent.inject(this)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedModel.productItem.observe(this, Observer {
            it?.run {
                buyWidget.setTotalPrice(it.product.attributes.price)
            }
        })
        sharedModel.showTotalPrice.observe(this, Observer {
            it?.run {
                buyWidget.setVisibilityLayout(it)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_digital_telco_prepaid, container, false)
        recentNumbersView = view.findViewById(R.id.recent_numbers)
        telcoClientNumberWidget = view.findViewById(R.id.telco_input_number)
        promoListView = view.findViewById(R.id.promo_widget)
        viewPager = view.findViewById(R.id.product_view_pager)
        tabLayout = view.findViewById(R.id.tab_layout)
        buyWidget = view.findViewById(R.id.buy_widget)
        tickerView = view.findViewById(R.id.ticker_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        renderTicker()
        renderInputNumber()
        renderRecentTransactions()
        renderPromoList()
        getInputFilterDataCollections()
    }

    fun getInputFilterDataCollections() {
        var mapParam = HashMap<String, kotlin.Any>()
        mapParam.put("componentID", TelcoComponentType.CLIENT_NUMBER)
        customViewModel.getCustomData(GraphqlHelper.loadRawString(resources, R.raw.query_custom_digital_telco), mapParam,
                this::onSuccessCustomData, this::onErrorCustomData)
    }

    fun onSuccessCustomData(telcoData: TelcoCustomComponentData) {
        this.telcoCustomData = telcoData
        renderProductListFromInputfilter()
    }

    fun renderProductListFromInputfilter() {
        val prefixClientNumber = telcoClientNumberWidget.getInputNumber().substring(0, 4)
        try {
            val dataFound = this.telcoCustomData.rechargeCustomData.customDataCollections.filter {
                it.value.equals(prefixClientNumber)
            }.single()

            renderViewPager(dataFound.operator.id)
            recentNumbersView.visibility = View.GONE
            promoListView.visibility = View.GONE
            tabLayout.visibility = View.VISIBLE
            viewPager.visibility = View.VISIBLE
        } catch (exception: Exception) {
            Toast.makeText(activity, "error exception", Toast.LENGTH_SHORT).show()
        }
    }

    fun onErrorCustomData(throwable: Throwable) {
        Toast.makeText(activity, "input filter " + throwable.message, Toast.LENGTH_SHORT).show()
    }

    fun renderInputNumber() {
        telcoClientNumberWidget.setListener(object : DigitalClientNumberWidget.ActionListener {
            override fun navigateToContact() {
                navigateContact()
            }

            override fun renderOperator() {
                // TODO showing image on client number
                telcoCustomData.rechargeCustomData.customDataCollections.isEmpty()?.let {
                    if (it) {
                        getInputFilterDataCollections()
                    } else {
                        renderProductListFromInputfilter()
                    }
                }
            }

            override fun clearAutoComplete() {
                recentNumbersView.visibility = View.VISIBLE
                promoListView.visibility = View.VISIBLE
                tabLayout.visibility = View.GONE
                viewPager.visibility = View.GONE
                sharedModel.setShowTotalPrice(false)
            }
        })
    }

    override fun setInputNumberFromContact(contactNumber: String) {
        telcoClientNumberWidget.setInputNumber(contactNumber)
    }

    fun renderViewPager(operatorId: String) {
        val listProductTab = mutableListOf<DigitalTabTelcoItem>()
        listProductTab.add(DigitalTabTelcoItem(DigitalTelcoProductFragment.newInstance(
                TelcoComponentType.PRODUCT_PULSA, TelcoComponentName.PRODUCT_PULSA, operatorId, TelcoProductType.PRODUCT_GRID),
                TelcoComponentName.PRODUCT_PULSA))
        listProductTab.add(DigitalTabTelcoItem(DigitalTelcoProductFragment.newInstance(
                TelcoComponentType.PRODUCT_PAKET_DATA, TelcoComponentName.PRODUCT_PAKET_DATA, operatorId, TelcoProductType.PRODUCT_LIST),
                TelcoComponentName.PRODUCT_PAKET_DATA))
        listProductTab.add(DigitalTabTelcoItem(DigitalTelcoProductFragment.newInstance(
                TelcoComponentType.PRODUCT_ROAMING, TelcoComponentName.PRODUCT_ROAMING, operatorId, TelcoProductType.PRODUCT_LIST),
                TelcoComponentName.PRODUCT_ROAMING))
        val pagerAdapter = DigitalTelcoProductTabAdapter(listProductTab, childFragmentManager)
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onDestroy() {
        customViewModel.clear()
        super.onDestroy()
    }

    companion object {

        val REQUEST_CODE_CONTACT_PICKER = 78;

        fun newInstance(): Fragment {
            val fragment = DigitalTelcoPrepaidFragment()
            return fragment
        }
    }
}