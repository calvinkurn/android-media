package com.tokopedia.flight.detail.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.flight.R
import com.tokopedia.flight.common.util.FlightCurrencyFormatUtil
import com.tokopedia.flight.databinding.FragmentFlightDetailPriceNewBinding
import com.tokopedia.flight.detail.view.model.FlightDetailModel
import com.tokopedia.flight.detail.view.widget.FlightDetailListener
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * @author by furqan on 21/04/2020
 */
class FlightDetailPriceFragment : Fragment() {

    lateinit var listener: FlightDetailListener

    private var binding by autoClearedNullable<FragmentFlightDetailPriceNewBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFlightDetailPriceNewBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateView()
    }

    private fun updateView() {
        if (::listener.isInitialized) {
            val flightDetailModel = listener.getDetailModel()
            val adultPriceTotal = flightDetailModel.adultNumericPrice * flightDetailModel.countAdult
            binding?.flightDetailPriceAdult?.text = getString(R.string.flight_label_currency, FlightCurrencyFormatUtil
                    .getThousandSeparatorString(adultPriceTotal.toDouble(), false, 0)?.formattedString)
            binding?.flightDetailPriceAdultLabel?.text = getString(R.string.flight_label_adult, flightDetailModel.countAdult)
            if (flightDetailModel.countChild > 0) {
                val childPriceTotal = flightDetailModel.childNumericPrice * flightDetailModel.countChild
                binding?.flightDetailPriceChild?.text = getString(R.string.flight_label_currency, FlightCurrencyFormatUtil
                        .getThousandSeparatorString(childPriceTotal.toDouble(), false, 0)?.formattedString)
                binding?.flightDetailPriceChildLabel?.text = getString(R.string.flight_label_child, flightDetailModel.countChild)
                binding?.flightDetailPriceChild?.visibility = View.VISIBLE
                binding?.flightDetailPriceChildLabel?.visibility = View.VISIBLE
            }
            if (flightDetailModel.countInfant > 0) {
                val infantPriceTotal = flightDetailModel.infantNumericPrice * flightDetailModel.countInfant
                binding?.flightDetailPriceInfant?.text = getString(R.string.flight_label_currency, FlightCurrencyFormatUtil
                        .getThousandSeparatorString(infantPriceTotal.toDouble(), false, 0)?.formattedString)
                binding?.flightDetailPriceInfantLabel?.text = getString(R.string.flight_label_infant, flightDetailModel.countInfant)
                binding?.flightDetailPriceInfant?.visibility = View.VISIBLE
                binding?.flightDetailPriceInfantLabel?.visibility = View.VISIBLE
            }
            binding?.flightDetailPriceTotal?.text = FlightCurrencyFormatUtil.convertToIdrPrice(calculateTotal(flightDetailModel))
        }
    }

    private fun calculateTotal(flightDetailModel: FlightDetailModel): Int {
        var total: Int = 0
        if (::listener.isInitialized) {
            total = flightDetailModel.countAdult * flightDetailModel.adultNumericPrice
            total += flightDetailModel.countChild * flightDetailModel.childNumericPrice
            total += flightDetailModel.countInfant * flightDetailModel.infantNumericPrice
        }
        return total
    }

}