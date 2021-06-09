package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantSpannableUtil
import com.tokopedia.power_merchant.subscribe.view.model.PmFeatureUiModel
import kotlinx.android.synthetic.main.item_pm_power_merchant_feature.view.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 25/02/21
 */

class PMFeatureAdapter @Inject constructor() : RecyclerView.Adapter<PMFeatureAdapter.PMFeatureViewHolder>() {

    var features: List<PmFeatureUiModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PMFeatureViewHolder {
        val view = View.inflate(parent.context, R.layout.item_pm_power_merchant_feature, null)
        return PMFeatureViewHolder(view)
    }

    override fun onBindViewHolder(holder: PMFeatureViewHolder, position: Int) {
        val feature = features[position]
        holder.bind(feature)
    }

    override fun getItemCount(): Int = features.size

    fun setItems(features: List<PmFeatureUiModel>) {
        this.features = features
    }

    inner class PMFeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(feature: PmFeatureUiModel) = with(itemView) {
            imgPmFeatureItem.loadImageDrawable(feature.imageResId)
            tvPmFeatureItemTitle.text = feature.title
            tvPmFeatureItemDescription.text = if (feature.clickableText.isNotBlank()) {
                val clickableUrl = feature.clickableUrl
                val clickableTextColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
                PowerMerchantSpannableUtil.createSpannableString(feature.description, feature.clickableText, clickableTextColor, true, feature.boldTextList) {
                    openUrl(clickableUrl)
                }
            } else {
                feature.description
            }
        }

        private fun openUrl(clickableUrl: String) {
            if (clickableUrl.isNotBlank()) {
                RouteManager.route(itemView.context, clickableUrl)
            }
        }
    }
}