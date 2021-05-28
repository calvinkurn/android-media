package com.tokopedia.flight.booking.presentation.adapter

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.R
import com.tokopedia.flight.booking.data.FlightCart
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
                else renderHighlightBenefit(insurance.benefits, insurance.tncUrl, insurance.name, listener ?: null)

                listener?.onInsuranceChecked(insurance, insurance.defaultChecked)
                cb_insurance.setOnCheckedChangeListener { _, checked -> listener?.onInsuranceChecked(insurance, checked) }
            }
        }

        private fun renderHighlightBenefit(list: List<FlightCart.Benefit>, tncUrl: String, name: String, listener: ActionListener?) {
            with(view) {
                insurance_highlight_benefit_container.show()
                val highlightedBenefit = list[0]
                tv_insurance_highlight.text = highlightedBenefit.title
                tv_insurance_highlight_detail.text = setUpTncText(highlightedBenefit.description, tncUrl, name, listener, context)
                tv_insurance_highlight_detail.movementMethod = LinkMovementMethod.getInstance()
                iv_insurance.loadImage(highlightedBenefit.icon)
                if (list.size > 1) renderMoreBenefit(list.subList(1, list.size))
                else {
                    seperator_2.hide()
                    layout_insurance_highlight.hide()
                    seperator_3.hide()
                    rv_more_benefits.hide()
                }
            }
        }

        private fun setUpTncText(description: String, tncUrl: String, title: String, listener: ActionListener?, context: Context): SpannableStringBuilder{
            val color = itemView.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G500)
            var fullText = String.format("%s. ", description)
            if (tncUrl != null && tncUrl.isNotEmpty()) {
                fullText += context.getString(R.string.flight_booking_insurance_see_detail)
            }
            val stopIndex = fullText.length
            val descriptionStr = SpannableStringBuilder(fullText)
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    if (listener != null && tncUrl != null && tncUrl.isNotEmpty()) {
                        listener.onClickInsuranceTnc(tncUrl, title)
                    }
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    ds.color = color
                    ds.typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
                }
            }
            descriptionStr.setSpan(clickableSpan, description.length + 2, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            return descriptionStr
        }

        private fun renderMoreBenefit(list: List<FlightCart.Benefit>) {
            with(view) {
                tv_insurance_highlight_see_more.text = String.format(context.getString(R.string.flight_booking_insurance_other_benefit), list.size)
                rv_more_benefits.layoutManager = LinearLayoutManager(context)
                rv_more_benefits.adapter = FlightInsuranceBenefitAdapter(list)

                layout_insurance_highlight.setOnClickListener {
                    if (rv_more_benefits.isVisible) {
                        seperator_3.hide()
                        rv_more_benefits.hide()
                        iv_insurance_highlight_see_more_arrow.setImageResource(com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_down_normal_24)
                    } else {
                        seperator_3.show()
                        rv_more_benefits.show()
                        iv_insurance_highlight_see_more_arrow.setImageResource(com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_up_normal_24)
                    }
                }
            }
        }

        interface ActionListener {
            fun onInsuranceChecked(insurance: FlightCart.Insurance, checked: Boolean)
            fun onClickInsuranceTnc(tncUrl: String, tncTitle: String)
        }

        companion object {
            val LAYOUT = R.layout.item_flight_booking_insurance
        }
    }


}