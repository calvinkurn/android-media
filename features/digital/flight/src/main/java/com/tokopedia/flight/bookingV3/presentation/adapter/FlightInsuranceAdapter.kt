package com.tokopedia.flight.bookingV3.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.R
import com.tokopedia.flight.bookingV3.data.FlightCart
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_flight_booking_insurance.view.*

/**
 * @author by jessica on 2019-11-01
 */

class FlightInsuranceAdapter: RecyclerView.Adapter<FlightInsuranceAdapter.ViewHolder>() {

    var insuranceList: List<FlightCart.Insurance> = listOf()
    lateinit var listener: ViewHolder.ActionListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(ViewHolder.LAYOUT, parent, false))

    override fun getItemCount(): Int = insuranceList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(insuranceList[position], listener)
    }

    fun updateList(list: List<FlightCart.Insurance>) {
        insuranceList = list
        notifyDataSetChanged()
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        fun bind(insurance: FlightCart.Insurance, listener: ActionListener?) {
            with(view) {
                tv_insurance_title.text = insurance.name
                tv_insurance_subtitle.text = insurance.description
                cb_insurance.isChecked = insurance.defaultChecked

                if (insurance.benefits.isEmpty()) insurance_highlight_benefit_container.hide()
                else renderHighlightBenefit(insurance.benefits)

                listener?.onInsuranceChecked(insurance, insurance.defaultChecked)
                cb_insurance.setOnCheckedChangeListener { _, checked -> listener?.onInsuranceChecked(insurance, checked) }
            }
        }

        private fun renderHighlightBenefit(list: List<FlightCart.Benefit>) {
            with(view) {
                insurance_highlight_benefit_container.show()
                val highlightedBenefit = list[0]
                tv_insurance_highlight.text = highlightedBenefit.title
                tv_insurance_highlight_detail.text = highlightedBenefit.description
                iv_insurance.loadImage(highlightedBenefit.icon)
                if (list.size > 1) renderMoreBenefit(list.subList(1, list.size))
                else {
                    seperator_2.hide()
                    tv_insurance_highlight_see_more.hide()
                    seperator_3.hide()
                    rv_more_benefits.hide()
                }
            }
        }

        private fun renderMoreBenefit(list: List<FlightCart.Benefit>) {
            with(view) {
                tv_insurance_highlight_see_more.text = String.format("Termasuk %d Proteksi Lain", list.size)
                rv_more_benefits.layoutManager = LinearLayoutManager(context)
                rv_more_benefits.adapter = FlightInsuranceBenefitAdapter(list)

                tv_insurance_highlight_see_more.setOnClickListener {
                    if (rv_more_benefits.isVisible) {
                        seperator_3.hide()
                        rv_more_benefits.hide()
                    } else {
                        seperator_3.show()
                        rv_more_benefits.show()
                    }
                }
            }
        }

        interface ActionListener {
            fun onInsuranceChecked(insurance: FlightCart.Insurance, checked: Boolean)
        }

        companion object {
            val LAYOUT = R.layout.item_flight_booking_insurance
        }
    }


}