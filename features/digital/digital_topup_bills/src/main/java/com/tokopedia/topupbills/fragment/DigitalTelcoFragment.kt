package com.tokopedia.topupbills.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.digital.topupbillsproduct.compoundview.DigitalProductHeaderView
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.model.DigitalProductSubMenu

/**
 * Created by nabillasabbaha on 06/05/19.
 */
class DigitalTelcoFragment : BaseDaggerFragment() {

    private lateinit var headerView: DigitalProductHeaderView

    override fun getScreenName(): String {
        return DigitalTelcoFragment::class.java.simpleName
    }

    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_digital_telco, container, false)
        headerView = view.findViewById(R.id.header_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = mutableListOf<DigitalProductSubMenu>()
        list.add(DigitalProductSubMenu("2", "telco-prepaid", "Prabayar", ""))
        list.add(DigitalProductSubMenu("3", "telco-postpaid", "Pascabayar", ""))
        headerView.setHeaderActive(DigitalProductHeaderView.HEADER_LEFT)
        headerView.setListener(object : DigitalProductHeaderView.ActionListener {
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

    fun replaceFragment(fragment: Fragment) {
        val transaction = fragmentManager!!.beginTransaction().replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {

        fun newInstance(): Fragment {
            val fragment = DigitalTelcoFragment()
            return fragment
        }
    }

}
