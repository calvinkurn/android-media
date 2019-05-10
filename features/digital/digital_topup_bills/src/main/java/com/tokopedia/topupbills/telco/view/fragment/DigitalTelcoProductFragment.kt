package com.tokopedia.topupbills.telco.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.view.model.DigitalProductTelco
import com.tokopedia.topupbills.telco.view.widget.DigitalProductGridWidget

/**
 * Created by nabillasabbaha on 09/05/19.
 */
class DigitalTelcoProductFragment: BaseDaggerFragment() {

    private lateinit var telcoProductView: DigitalProductGridWidget

    override fun getScreenName(): String {
        return DigitalTelcoProductFragment::class.java.simpleName
    }

    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_digital_telco_product, container, false)
        telcoProductView = view.findViewById(R.id.telco_product_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productList = mutableListOf<DigitalProductTelco>()
        productList.add(DigitalProductTelco("1", "24.400", "25.000", "Masa aktif 30 hari", "Pulsa 40.000", false))
        productList.add(DigitalProductTelco("2", "24.400", "25.000", "Masa aktif 30 hari", "Pulsa 40.000", false))
        productList.add(DigitalProductTelco("3", "24.400", "25.000", "Masa aktif 30 hari", "Pulsa 40.000", false))
        productList.add(DigitalProductTelco("4", "24.400", "25.000", "Masa aktif 30 hari", "Pulsa 40.000", false))
        productList.add(DigitalProductTelco("5", "24.400", "25.000", "Masa aktif 30 hari", "Pulsa 40.000", false))
        productList.add(DigitalProductTelco("6", "24.400", "25.000", "Masa aktif 30 hari", "Pulsa 40.000", false))
        telcoProductView.renderGridProductList(productList)
    }

    companion object {

        fun newInstance(): Fragment {
            val fragment = DigitalTelcoProductFragment()
            return fragment
        }
    }
}