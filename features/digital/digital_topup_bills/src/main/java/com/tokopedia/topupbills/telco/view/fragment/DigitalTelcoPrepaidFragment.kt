package com.tokopedia.topupbills.telco.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.*
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentName
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.data.constant.TelcoProductType
import com.tokopedia.topupbills.telco.view.activity.DigitalSearchNumberActivity
import com.tokopedia.topupbills.telco.view.adapter.DigitalTelcoProductTabAdapter
import com.tokopedia.topupbills.telco.view.di.DigitalTopupInstance
import com.tokopedia.topupbills.telco.view.model.DigitalFavNumber
import com.tokopedia.topupbills.telco.view.model.DigitalTabTelcoItem
import com.tokopedia.topupbills.telco.view.viewmodel.SharedProductTelcoViewModel
import com.tokopedia.topupbills.telco.view.widget.DigitalClientNumberWidget
import com.tokopedia.topupbills.telco.view.widget.DigitalTelcoBuyWidget

/**
 * Created by nabillasabbaha on 11/04/19.
 */
class DigitalTelcoPrepaidFragment : DigitalBaseTelcoFragment() {

    private lateinit var telcoClientNumberWidget: DigitalClientNumberWidget
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var buyWidget: DigitalTelcoBuyWidget
    private lateinit var sharedModel: SharedProductTelcoViewModel
    private lateinit var layoutProgressBar: RelativeLayout
    private lateinit var favNumberList: List<TelcoFavNumber>
    private var operatorData: TelcoCustomComponentData =
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
        mainContainer = view.findViewById(R.id.main_container)
        recentNumbersView = view.findViewById(R.id.recent_numbers)
        telcoClientNumberWidget = view.findViewById(R.id.telco_input_number)
        promoListView = view.findViewById(R.id.promo_widget)
        viewPager = view.findViewById(R.id.product_view_pager)
        tabLayout = view.findViewById(R.id.tab_layout)
        buyWidget = view.findViewById(R.id.buy_widget)
        tickerView = view.findViewById(R.id.ticker_view)
        layoutProgressBar = view.findViewById(R.id.layout_progress_bar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleFocusClientNumber()
        getInputFilterDataCollections()
        renderInputNumber()
        getCatalogMenuDetail()
    }

    override fun getMapCustomData(): Map<String, Any> {
        var mapParam = HashMap<String, kotlin.Any>()
        mapParam.put("componentID", TelcoComponentType.CLIENT_NUMBER_PREPAID)
        return mapParam
    }

    override fun onSuccessCustomData(telcoData: TelcoCustomComponentData) {
        this.operatorData = telcoData
        renderProductFromCustomData()
    }

    fun renderProductFromCustomData() {
        val prefixClientNumber = telcoClientNumberWidget.getInputNumber().substring(0, 4)
        try {
            val operatorSelected = this.operatorData.rechargeCustomData.customDataCollections.filter {
                it.value.equals(prefixClientNumber)
            }.single()

            renderViewPager(operatorSelected.operator.id)
            telcoClientNumberWidget.setIconOperator(operatorSelected.operator.attributes.imageUrl)

            recentNumbersView.visibility = View.GONE
            promoListView.visibility = View.GONE
            tabLayout.visibility = View.VISIBLE
            viewPager.visibility = View.VISIBLE
        } catch (exception: Exception) {
            Toast.makeText(activity, "error exception", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onErrorCustomData(throwable: Throwable) {
        Toast.makeText(activity, "input filter " + throwable.message, Toast.LENGTH_SHORT).show()
    }

    override fun onLoadingMenuDetail(showLoading: Boolean) {
        if (showLoading) {
            layoutProgressBar.visibility = View.VISIBLE
            recentNumbersView.visibility = View.GONE
            promoListView.visibility = View.GONE
        } else {
            layoutProgressBar.visibility = View.GONE
            recentNumbersView.visibility = View.VISIBLE
            promoListView.visibility = View.VISIBLE
        }
    }

    fun renderInputNumber() {
        telcoClientNumberWidget.setListener(object : DigitalClientNumberWidget.ActionListener {
            override fun onNavigateToContact() {
                navigateContact()
            }

            override fun onRenderOperator() {
                operatorData.rechargeCustomData.customDataCollections.isEmpty()?.let {
                    if (it) {
                        getInputFilterDataCollections()
                    } else {
                        renderProductFromCustomData()
                    }
                }
            }

            override fun onClearAutoComplete() {
                recentNumbersView.visibility = View.VISIBLE
                promoListView.visibility = View.VISIBLE
                tabLayout.visibility = View.GONE
                viewPager.visibility = View.GONE
                sharedModel.setShowTotalPrice(false)
            }

            override fun onClientNumberHasFocus(clientNumber: String) {
                if (::favNumberList.isInitialized) {
                    startActivityForResult(activity?.let { DigitalSearchNumberActivity.newInstance(it, DigitalFavNumber(), "", favNumberList) },
                            REQUEST_CODE_DIGITAL_SEARCH_NUMBER)
                }
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

    override fun getMapCatalogMenuDetail(): Map<String, Any> {
        var mapParam = HashMap<String, kotlin.Any>()
        mapParam.put("menuID", TelcoComponentType.TELCO_PREPAID)
        return mapParam
    }

    override fun handleCallbackSearchNumber(orderClientNumber: TelcoFavNumber) {
        telcoClientNumberWidget.setInputNumber(orderClientNumber.clientNumber)
        telcoClientNumberWidget.clearFocus()
    }

    override fun handleCallbackSearchNumberCancel() {
        telcoClientNumberWidget.clearFocus()
    }

    override fun onClickRecentNumber(telcoRecommendation: TelcoRecommendation) {
        Toast.makeText(activity, telcoRecommendation.clientNumber, Toast.LENGTH_SHORT).show()
    }

    override fun getMapFavNumbers(): Map<String, Any> {
        var mapParam = HashMap<String, kotlin.Any>()
        mapParam.put("categoryID", TelcoComponentType.FAV_NUMBER_PREPAID)
        return mapParam
    }

    override fun setFavNumbers(data: TelcoRechargeFavNumberData) {
        this.favNumberList = data.favNumber.favNumberList
    }

    override fun onDestroy() {
        sharedModel.clear()
        super.onDestroy()
    }

    companion object {

        fun newInstance(): Fragment {
            val fragment = DigitalTelcoPrepaidFragment()
            return fragment
        }
    }
}