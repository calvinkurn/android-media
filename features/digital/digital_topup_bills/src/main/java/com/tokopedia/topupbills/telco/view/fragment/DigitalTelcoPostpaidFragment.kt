package com.tokopedia.topupbills.telco.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.view.di.DigitalTopupInstance
import com.tokopedia.topupbills.telco.view.listener.ClientNumberPostpaidListener
import com.tokopedia.topupbills.telco.view.widget.DigitalAddToMyBillsWidget
import com.tokopedia.topupbills.telco.view.widget.DigitalClientNumberWidget
import com.tokopedia.topupbills.telco.view.widget.DigitalPostpaidClientNumberWidget
import com.tokopedia.topupbills.telco.view.widget.DigitalTelcoBuyWidget

/**
 * Created by nabillasabbaha on 06/05/19.
 */
class DigitalTelcoPostpaidFragment : DigitalBaseTelcoFragment() {

    private lateinit var postpaidClientNumberWidget: DigitalPostpaidClientNumberWidget
    private lateinit var buyWidget: DigitalTelcoBuyWidget
    private lateinit var addToMyBillsWidget: DigitalAddToMyBillsWidget

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
        recentNumbersView = view.findViewById(R.id.recent_numbers)
        postpaidClientNumberWidget = view.findViewById(R.id.telco_input_number)
        promoListView = view.findViewById(R.id.promo_widget)
        buyWidget = view.findViewById(R.id.buy_widget)
        tickerView = view.findViewById(R.id.ticker_view)
        addToMyBillsWidget = view.findViewById(R.id.addtomybills_widget)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        renderClientNumber()
        renderTicker()
        renderPromoList()
        renderRecentTransactions()
    }

    fun renderClientNumber() {
        addToMyBillsWidget.visibility = View.GONE
        addToMyBillsWidget.renderContent("25")
        postpaidClientNumberWidget.resetClientNumberPostpaid()
        postpaidClientNumberWidget.setListener(object : DigitalClientNumberWidget.ActionListener {
            override fun navigateToContact() {
                navigateContact()
            }

            override fun renderOperator() {
                // TODO showing image on client number
                postpaidClientNumberWidget.setButtonEnquiryEnable()
            }

            override fun clearAutoComplete() {
                postpaidClientNumberWidget.resetClientNumberPostpaid()
                recentNumbersView.visibility = View.VISIBLE
                promoListView.visibility = View.VISIBLE
                addToMyBillsWidget.visibility = View.GONE
                buyWidget.setVisibilityLayout(false)
            }
        })
        postpaidClientNumberWidget.setPostpaidListener(object : ClientNumberPostpaidListener {
            override fun enquiryNumber() {
                postpaidClientNumberWidget.showEnquiryResultPostpaid()
                recentNumbersView.visibility = View.GONE
                promoListView.visibility = View.GONE
                addToMyBillsWidget.visibility = View.VISIBLE
                buyWidget.setVisibilityLayout(true)
            }
        })
    }

    override fun setInputNumberFromContact(contactNumber: String) {
        postpaidClientNumberWidget.setInputNumber(contactNumber)
    }

    companion object {

        fun newInstance(): Fragment {
            val fragment = DigitalTelcoPostpaidFragment()
            return fragment
        }
    }
}
