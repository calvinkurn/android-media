package com.tokopedia.power_merchant.subscribe.view.adapter

import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantSpannableUtil
import com.tokopedia.power_merchant.subscribe.databinding.ItemPmRegistrationTermBinding
import com.tokopedia.power_merchant.subscribe.view.model.RegistrationTermUiModel

/**
 * Created By @ilhamsuaib on 03/03/21
 */

class RegistrationTermAdapter(
    private val onTermCtaClicked: ((RegistrationTermUiModel) -> Unit)? = null
) : RecyclerView.Adapter<RegistrationTermAdapter.RegistrationTermViewHolder>() {

    private var terms: List<RegistrationTermUiModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegistrationTermViewHolder {
        val binding = ItemPmRegistrationTermBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RegistrationTermViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RegistrationTermViewHolder, position: Int) {
        val term = terms[position]
        holder.bind(term)
    }

    override fun getItemCount(): Int = terms.size

    fun setItems(terms: List<RegistrationTermUiModel>) {
        this.terms = terms
    }

    inner class RegistrationTermViewHolder(private val binding: ItemPmRegistrationTermBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(term: RegistrationTermUiModel) {
            with(binding) {
                tvPmTermItemTitle.text = term.title.parseAsHtml()
                icPmHeaderTermItem.loadImage(term.resDrawableIcon)
                setupTermDescription(term)
            }
        }

        private fun setupTermDescription(term: RegistrationTermUiModel) {
            with(binding) {
                if (!term.clickableText.isNullOrBlank() && !term.appLinkOrUrl.isNullOrBlank()) {
                    val ctaTextColor = com.tokopedia.unifyprinciples.R.color.Unify_G500
                    val termDescription = PowerMerchantSpannableUtil.createSpannableString(
                        text = term.descriptionHtml.parseAsHtml(),
                        highlightText = term.clickableText.orEmpty(),
                        colorId = root.context.getResColor(ctaTextColor),
                        isBold = true
                    ) {
                        RouteManager.route(root.context, term.appLinkOrUrl)
                        onTermCtaClicked?.invoke(term)
                    }
                    tvPmTermItemDesc.apply {
                        movementMethod = LinkMovementMethod.getInstance()
                        text = termDescription
                    }
                } else {
                    tvPmTermItemDesc.text = term.descriptionHtml.parseAsHtml()
                }
                if (term.isNewSeller && !term.isFirstMondayNewSeller) {
                    tvPmTermItemDesc.setTextColor(
                        root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
                    )
                }
            }
        }
    }
}