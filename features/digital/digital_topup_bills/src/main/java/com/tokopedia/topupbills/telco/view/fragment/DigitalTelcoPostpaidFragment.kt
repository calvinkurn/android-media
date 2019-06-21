package com.tokopedia.topupbills.telco.view.fragment

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.*
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.view.activity.DigitalSearchNumberActivity
import com.tokopedia.topupbills.telco.view.di.DigitalTopupInstance
import com.tokopedia.topupbills.telco.view.listener.ClientNumberPostpaidListener
import com.tokopedia.topupbills.telco.view.model.DigitalFavNumber
import com.tokopedia.topupbills.telco.view.viewmodel.DigitalTelcoEnquiryViewModel
import com.tokopedia.topupbills.telco.view.widget.DigitalClientNumberWidget
import com.tokopedia.topupbills.telco.view.widget.DigitalPostpaidClientNumberWidget
import com.tokopedia.topupbills.telco.view.widget.DigitalTelcoBuyWidget

/**
 * Created by nabillasabbaha on 06/05/19.
 */
class DigitalTelcoPostpaidFragment : DigitalBaseTelcoFragment() {

    private lateinit var postpaidClientNumberWidget: DigitalPostpaidClientNumberWidget
    private lateinit var buyWidget: DigitalTelcoBuyWidget
    private lateinit var enquiryViewModel: DigitalTelcoEnquiryViewModel
    private lateinit var layoutProgressBar: RelativeLayout
    private lateinit var operatorSelected: TelcoCustomDataCollection

    private val favNumberList = mutableListOf<TelcoFavNumber>()
    private var operatorData: TelcoCustomComponentData =
            TelcoCustomComponentData(TelcoCustomData(mutableListOf()))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            enquiryViewModel = viewModelProvider.get(DigitalTelcoEnquiryViewModel::class.java)
        }
    }

    override fun onStart() {
        context?.let {
            GraphqlClient.init(it)
        }
        super.onStart()
    }

    override fun getScreenName(): String {
        return DigitalTelcoPostpaidFragment::class.java.simpleName
    }

    override fun initInjector() {
        activity?.let {
            val digitalTopupComponent = DigitalTopupInstance.getComponent(it.application)
            digitalTopupComponent.inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_digital_telco_postpaid, container, false)
        mainContainer = view.findViewById(R.id.main_container)
        recentNumbersView = view.findViewById(R.id.recent_numbers)
        postpaidClientNumberWidget = view.findViewById(R.id.telco_input_number)
        promoListView = view.findViewById(R.id.promo_widget)
        buyWidget = view.findViewById(R.id.buy_widget)
        tickerView = view.findViewById(R.id.ticker_view)
        layoutProgressBar = view.findViewById(R.id.layout_progress_bar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleFocusClientNumber()
        getInputFilterDataCollections()
        renderClientNumber()
        getCatalogMenuDetail()
    }

    fun renderClientNumber() {
        postpaidClientNumberWidget.resetClientNumberPostpaid()
        postpaidClientNumberWidget.setListener(object : DigitalClientNumberWidget.ActionListener {
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
                postpaidClientNumberWidget.setButtonEnquiryEnable()
            }

            override fun onClearAutoComplete() {
                postpaidClientNumberWidget.resetClientNumberPostpaid()
                recentNumbersView.visibility = View.VISIBLE
                promoListView.visibility = View.VISIBLE
                buyWidget.setVisibilityLayout(false)
            }

            override fun onClientNumberHasFocus(clientNumber: String) {
                startActivityForResult(activity?.let { DigitalSearchNumberActivity.newInstance(it, DigitalFavNumber(), "", favNumberList) },
                        REQUEST_CODE_DIGITAL_SEARCH_NUMBER)
            }
        })
        postpaidClientNumberWidget.setPostpaidListener(object : ClientNumberPostpaidListener {
            override fun enquiryNumber() {
                getEnquiryNumber()
            }
        })
    }

    fun getEnquiryNumber() {
        if (::operatorSelected.isInitialized) {
            var mapParam = HashMap<String, kotlin.Any>()
            mapParam.put("clientNumber", postpaidClientNumberWidget.getInputNumber())
            mapParam.put("productId", operatorSelected.operator.attributes.defaultProductId.toString())
            enquiryViewModel.getEnquiry(GraphqlHelper.loadRawString(resources, R.raw.query_enquiry_digital_telco),
                    mapParam, this::onSuccessEnquiry, this::onErrorEnquiry)
        }
    }

    override fun getMapCustomData(): Map<String, Any> {
        var mapParam = HashMap<String, kotlin.Any>()
        mapParam.put("componentID", TelcoComponentType.CLIENT_NUMBER_PROSTPAID)
        return mapParam
    }

    override fun onSuccessCustomData(telcoData: TelcoCustomComponentData) {
        this.operatorData = telcoData
        renderProductFromCustomData()
    }

    fun renderProductFromCustomData() {
        val prefixClientNumber = postpaidClientNumberWidget.getInputNumber().substring(0, 4)
        try {
            operatorSelected = this.operatorData.rechargeCustomData.customDataCollections.filter {
                it.value.equals(prefixClientNumber)
            }.single()

            postpaidClientNumberWidget.setIconOperator(operatorSelected.operator.attributes.imageUrl)

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

    fun onSuccessEnquiry(telcoEnquiryData: TelcoEnquiryData) {
        postpaidClientNumberWidget.showEnquiryResultPostpaid(telcoEnquiryData)
        recentNumbersView.visibility = View.GONE
        promoListView.visibility = View.GONE
        buyWidget.setTotalPrice(telcoEnquiryData.enquiry.attributes.price)
        buyWidget.setVisibilityLayout(true)
    }

    fun onErrorEnquiry(throwable: Throwable) {
        Toast.makeText(activity, "enquiry " + throwable.message, Toast.LENGTH_SHORT).show()
    }

    override fun setInputNumberFromContact(contactNumber: String) {
        postpaidClientNumberWidget.setInputNumber(contactNumber)
    }

    override fun getMapCatalogMenuDetail(): Map<String, Any> {
        var mapParam = HashMap<String, kotlin.Any>()
        mapParam.put("menuID", TelcoComponentType.TELCO_POSTPAID)
        return mapParam
    }

    override fun getMapFavNumbers(): Map<String, Any> {
        var mapParam = HashMap<String, kotlin.Any>()
        mapParam.put("categoryID", TelcoComponentType.FAV_NUMBER_POSTPAID)
        return mapParam
    }

    override fun handleCallbackSearchNumber(orderClientNumber: TelcoFavNumber) {
        postpaidClientNumberWidget.setInputNumber(orderClientNumber.clientNumber)
        postpaidClientNumberWidget.clearFocus()
    }

    override fun handleCallbackSearchNumberCancel() {
        postpaidClientNumberWidget.clearFocus()
    }

    override fun onClickItemRecentNumber(telcoRecommendation: TelcoRecommendation) {
        postpaidClientNumberWidget.setInputNumber(telcoRecommendation.clientNumber)
    }

    override fun setFavNumbers(data: TelcoRechargeFavNumberData) {
        favNumberList.addAll(data.favNumber.favNumberList)
    }

    override fun onDestroy() {
        enquiryViewModel.clear()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        postpaidClientNumberWidget.clearFocus()
    }

    companion object {

        fun newInstance(): Fragment {
            val fragment = DigitalTelcoPostpaidFragment()
            return fragment
        }
    }
}
