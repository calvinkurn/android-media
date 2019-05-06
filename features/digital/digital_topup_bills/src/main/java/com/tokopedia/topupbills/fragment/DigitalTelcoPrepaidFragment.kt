package com.tokopedia.topupbills.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.digital.topupbillsproduct.compoundview.DigitalRecentNumbersView
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.model.DigitalRecentNumber

/**
 * Created by nabillasabbaha on 11/04/19.
 */
class DigitalTelcoPrepaidFragment : BaseDaggerFragment() {

    private lateinit var recentNumbersView: DigitalRecentNumbersView
    private val recentNumbers = mutableListOf<DigitalRecentNumber>()

    override fun getScreenName(): String {
        return DigitalTelcoPrepaidFragment::class.java.simpleName
    }

    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_digital_telco_prepaid, container, false)
        recentNumbersView = view.findViewById(R.id.recent_numbers)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recentNumbersView.setListener(object : DigitalRecentNumbersView.ActionListener {
            override fun onClickRecentNumber(digitalRecentNumber : DigitalRecentNumber) {
                Toast.makeText(activity, digitalRecentNumber.clientNumber, Toast.LENGTH_LONG).show()
            }
        })
        recentNumbers.add(DigitalRecentNumber("https://ecs7.tokopedia.net/img/recharge/category/pulsa.png", "coba title 1", "081723823112", "", "", 316, "Simpati"))
        recentNumbers.add(DigitalRecentNumber("https://ecs7.tokopedia.net/img/recharge/category/pulsa.png", "coba title 2", "082313134234", "", "", 316, "Simpati"))
        recentNumbers.add(DigitalRecentNumber("https://ecs7.tokopedia.net/img/recharge/category/pulsa.png", "coba title 3", "081342424234", "", "", 316, "Simpati"))
        recentNumbers.add(DigitalRecentNumber("https://ecs7.tokopedia.net/img/recharge/category/pulsa.png", "coba title 4", "087823173712", "", "", 316, "Simpati"))
        recentNumbers.add(DigitalRecentNumber("https://ecs7.tokopedia.net/img/recharge/category/pulsa.png", "coba title 5", "082343432423", "", "", 316, "Simpati"))
        recentNumbers.add(DigitalRecentNumber("https://ecs7.tokopedia.net/img/recharge/category/pulsa.png", "coba title 6", "081231313233", "", "", 316, "Simpati"))
        recentNumbersView.setRecentNumbers(recentNumbers)
    }

    companion object {

        fun newInstance() : Fragment {
            val fragment = DigitalTelcoPrepaidFragment()
            return fragment
        }
    }
}