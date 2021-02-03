package com.tokopedia.flight.detail.view.fragmentnew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.flight.R
import com.tokopedia.flight.common.util.FlightCurrencyFormatUtil
import com.tokopedia.flight.detail.view.model.FlightDetailModel
import com.tokopedia.flight.detail.view.widget.FlightDetailListener
import kotlinx.android.synthetic.main.fragment_flight_detail_price_new.*

/**
 * @author by furqan on 21/04/2020
 */
class FlightDetailPriceFragment : Fragment() {

    lateinit var listener: FlightDetailListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_flight_detail_price_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateView()
    }

    private fun updateView() {
        if (::listener.isInitialized) {
            val flightDetailModel = listener.getDetailModel()
            val adultPriceTotal = flightDetailModel.adultNumericPrice * flightDetailModel.countAdult
            flightDetailPriceAdult.text = getString(R.string.flight_label_currency, FlightCurrencyFormatUtil
                    .getThousandSeparatorString(adultPriceTotal.toDouble(), false, 0)?.formattedString)
            flightDetailPriceAdultLabel.text = getString(R.string.flight_label_adult, flightDetailModel.countAdult)
            if (flightDetailModel.countChild > 0) {
                val childPriceTotal = flightDetailModel.childNumericPrice * flightDetailModel.countChild
                flightDetailPriceChild.text = getString(R.string.flight_label_currency, FlightCurrencyFormatUtil
                        .getThousandSeparatorString(childPriceTotal.toDouble(), false, 0)?.formattedString)
                flightDetailPriceChildLabel.text = getString(R.string.flight_label_child, flightDetailModel.countChild)
                flightDetailPriceChild.visibility = View.VISIBLE
                flightDetailPriceChildLabel.visibility = View.VISIBLE
            }
            if (flightDetailModel.countInfant > 0) {
                val infantPriceTotal = flightDetailModel.infantNumericPrice * flightDetailModel.countInfant
                flightDetailPriceInfant.text = getString(R.string.flight_label_currency, FlightCurrencyFormatUtil
                        .getThousandSeparatorString(infantPriceTotal.toDouble(), false, 0)?.formattedString)
                flightDetailPriceInfantLabel.text = getString(R.string.flight_label_infant, flightDetailModel.countInfant)
                flightDetailPriceInfant.visibility = View.VISIBLE
                flightDetailPriceInfantLabel.visibility = View.VISIBLE
            }
            flightDetailPriceTotal.text = FlightCurrencyFormatUtil.convertToIdrPrice(calculateTotal(flightDetailModel))
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