package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.RegistrationTermUiModel
import kotlinx.android.synthetic.main.item_pm_registration_term.view.*

/**
 * Created By @ilhamsuaib on 03/03/21
 */

class RegistrationTermAdapter(
        private val terms: List<RegistrationTermUiModel>
) : RecyclerView.Adapter<RegistrationTermAdapter.RegistrationTermViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegistrationTermViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pm_registration_term, parent, false)
        return RegistrationTermViewHolder(view)
    }

    override fun onBindViewHolder(holder: RegistrationTermViewHolder, position: Int) {
        val term = terms[position]
        holder.bind(term)
    }

    override fun getItemCount(): Int = terms.size

    inner class RegistrationTermViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(term: RegistrationTermUiModel) {
            with(itemView) {
                tvPmTermItemTitle.text = term.title.parseAsHtml()
                tvPmTermItemDesc.text = term.description.parseAsHtml()
                if (term.isChecked) {
                    icPmHeaderTermItem.loadImageDrawable(R.drawable.ic_pm_checked)
                } else {
                    icPmHeaderTermItem.loadImageDrawable(R.drawable.ic_pm_not_checked)
                }
            }
        }
    }
}