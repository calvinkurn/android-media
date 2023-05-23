package com.tokopedia.flight.cancellation.presentation.adapter

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationRefundModel
import com.tokopedia.flight.databinding.ItemFlightCancellationRefundBinding

class FlightCancellationRefundAdapter : RecyclerView.Adapter<FlightCancellationRefundAdapter.ViewHolder>() {
    private var descriptions: List<FlightCancellationRefundModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFlightCancellationRefundBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(descriptions[position])
    }

    override fun getItemCount(): Int = descriptions.size

    fun setDescriptions(descriptions: List<FlightCancellationRefundModel>) {
        this.descriptions = descriptions
    }

    inner class ViewHolder(val binding: ItemFlightCancellationRefundBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(description: FlightCancellationRefundModel) {
            val color = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_68)
            val fullText = description.title + ", " + description.subtitle
            val startIndex = fullText.indexOf(description.subtitle)
            val stopIndex = fullText.length
            val descriptionStr = SpannableString(fullText)
            val clickableSpan: ClickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {}
                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    ds.color = color
                    ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                }
            }
            descriptionStr.setSpan(clickableSpan, startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.tvDescription.text = descriptionStr
        }

    }
}