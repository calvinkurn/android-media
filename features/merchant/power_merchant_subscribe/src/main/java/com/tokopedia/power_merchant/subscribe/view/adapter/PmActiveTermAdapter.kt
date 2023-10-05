package com.tokopedia.power_merchant.subscribe.view.adapter

import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.gm.common.utils.SpannableUtil
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.databinding.ItemPmRegistrationTermBinding
import com.tokopedia.power_merchant.subscribe.view.model.PmActiveTermUiModel

/**
 * Created By @ilhamsuaib on 03/03/21
 */

class PmActiveTermAdapter(
    private val onTermCtaClicked: ((PmActiveTermUiModel) -> Unit)? = null
) : RecyclerView.Adapter<PmActiveTermAdapter.PmActiveTermViewHolder>() {

    private var terms: List<PmActiveTermUiModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PmActiveTermViewHolder {
        val binding = ItemPmRegistrationTermBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PmActiveTermViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PmActiveTermViewHolder, position: Int) {
        val term = terms[position]
        holder.bind(term)
    }

    override fun getItemCount(): Int = terms.size

    fun setItems(terms: List<PmActiveTermUiModel>) {
        this.terms = terms
    }

    inner class PmActiveTermViewHolder(private val binding: ItemPmRegistrationTermBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(term: PmActiveTermUiModel) {
            with(binding) {
                tvPmTermItemTitle.text = term.title.parseAsHtml()
                icPmHeaderTermItem.loadImage(term.resDrawableIcon)
                setupTermDescription(term)
            }
        }

        private fun setupTermDescription(term: PmActiveTermUiModel) {
            with(binding) {
                if (!term.clickableText.isNullOrBlank() && !term.appLinkOrUrl.isNullOrBlank()) {
                    val ctaTextColor = com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    val termDescription = SpannableUtil.createSpannableString(
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
            }
        }
    }
}