package com.tokopedia.power_merchant.subscribe.view.adapter

import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantSpannableUtil
import com.tokopedia.power_merchant.subscribe.view.model.RegistrationTermUiModel
import kotlinx.android.synthetic.main.item_pm_registration_term.view.*

/**
 * Created By @ilhamsuaib on 03/03/21
 */

class RegistrationTermAdapter(
        private val onTermCtaClicked: ((RegistrationTermUiModel) -> Unit)? = null
) : RecyclerView.Adapter<RegistrationTermAdapter.RegistrationTermViewHolder>() {

    private var terms: List<RegistrationTermUiModel> = emptyList()

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

    fun setItems(terms: List<RegistrationTermUiModel>) {
        this.terms = terms
    }

    inner class RegistrationTermViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(term: RegistrationTermUiModel) {
            with(itemView) {
                tvPmTermItemTitle.text = term.title.parseAsHtml()
                icPmHeaderTermItem.loadImageDrawable(term.resDrawableIcon)
                setupTermDescription(term)
            }
        }

        private fun setupTermDescription(term: RegistrationTermUiModel) {
            if (!term.clickableText.isNullOrBlank() && !term.appLinkOrUrl.isNullOrBlank()) {
                val ctaTextColor = com.tokopedia.unifyprinciples.R.color.Unify_G500
                val termDescription = PowerMerchantSpannableUtil.createSpannableString(
                        text = term.descriptionHtml.parseAsHtml(),
                        highlightText = term.clickableText.orEmpty(),
                        colorId = itemView.context.getResColor(ctaTextColor),
                        isBold = true
                ) {
                    RouteManager.route(itemView.context, term.appLinkOrUrl)
                    onTermCtaClicked?.invoke(term)
                }
                itemView.tvPmTermItemDesc.movementMethod = LinkMovementMethod.getInstance()
                itemView.tvPmTermItemDesc.text = termDescription
            } else {
                itemView.tvPmTermItemDesc.text = term.descriptionHtml.parseAsHtml()
            }
        }
    }
}