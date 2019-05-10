package com.tokopedia.topupbills.telco.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.design.component.ticker.TickerView
import com.tokopedia.topupbills.telco.view.widget.DigitalSubMenuWidget
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.view.model.DigitalProductSubMenu

/**
 * Created by nabillasabbaha on 06/05/19.
 */
class DigitalTelcoFragment : BaseDaggerFragment() {

    private lateinit var headerView: DigitalSubMenuWidget
    private lateinit var tickerView: TickerView

    override fun getScreenName(): String {
        return DigitalTelcoFragment::class.java.simpleName
    }

    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_digital_telco, container, false)
        headerView = view.findViewById(R.id.header_view)
        tickerView = view.findViewById(R.id.ticker_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        renderSubMenu()
        renderTicker()
    }

    fun renderSubMenu() {
        val list = mutableListOf<DigitalProductSubMenu>()
        list.add(DigitalProductSubMenu("2", "telco-prepaid", "Prabayar", ""))
        list.add(DigitalProductSubMenu("3", "telco-postpaid", "Pascabayar", ""))
        headerView.setHeaderActive(DigitalSubMenuWidget.HEADER_LEFT)
        headerView.setListener(object : DigitalSubMenuWidget.ActionListener {
            override fun onClickSubMenu(subMenu: DigitalProductSubMenu) {
                if (subMenu.name.equals("telco-prepaid")) {
                    replaceFragment(DigitalTelcoPrepaidFragment.newInstance())
                } else {
                    replaceFragment(DigitalTelcoPostpaidFragment.newInstance())
                }
            }
        })
        headerView.setHeader(list)
    }

    fun renderTicker() {
        val messages = ArrayList<String>()
        messages.add("Untuk paket data Indosat, telkomsel dan tri akan tersedia kembali pukul 18.00 WIB")
        tickerView.setListMessage(messages)
        tickerView.buildView()
        tickerView.visibility = View.VISIBLE
    }

    fun replaceFragment(fragment: Fragment) {
        fragmentManager?.let {
            val transaction = it.beginTransaction().replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    companion object {

        fun newInstance(): Fragment {
            val fragment = DigitalTelcoFragment()
            return fragment
        }
    }

}
