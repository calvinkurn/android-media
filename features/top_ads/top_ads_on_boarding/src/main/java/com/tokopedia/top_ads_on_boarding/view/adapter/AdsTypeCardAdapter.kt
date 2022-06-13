package com.tokopedia.top_ads_on_boarding.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tokopedia.applink.RouteManager
import com.tokopedia.top_ads_on_boarding.R
import com.tokopedia.top_ads_on_boarding.model.AdsTypeModel
import com.tokopedia.top_ads_on_boarding.view.activity.TopAdsOnBoardingActivity
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class AdsTypeCardAdapter : RecyclerView.Adapter<AdsTypeCardAdapter.AdsTypeCardViewHolder>() {

    private val adsTypeList: ArrayList<AdsTypeModel> by lazy { ArrayList() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsTypeCardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.topads_ads_type_card_item_layout, parent, false)
        return AdsTypeCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdsTypeCardViewHolder, position: Int) {
        holder.bind(adsTypeList, position)
    }

    override fun getItemCount(): Int {
        return adsTypeList.size
    }

    fun submitList(adsTypeList: List<AdsTypeModel>) {
        this.adsTypeList.clear()
        this.adsTypeList.addAll(adsTypeList)
        notifyDataSetChanged()
    }

    inner class AdsTypeCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val adsTypeTitle: Typography = itemView.findViewById(R.id.adsTypeTitle)
        private val adsTypeSubtitle: Typography = itemView.findViewById(R.id.adsTypeSubtitle)
        private val adsTypeDescription: Typography = itemView.findViewById(R.id.adsTypeDescription)
        private val iconAutomaticAds: ImageView = itemView.findViewById(R.id.iconAutomaticAds)
        private val adsTypeImage: DeferredImageView = itemView.findViewById(R.id.adsTypeImage)
        private val adsTypePositiveButton: UnifyButton =
            itemView.findViewById(R.id.adsTypePositiveButton)
        private val adsTypeNegativeButton: UnifyButton =
            itemView.findViewById(R.id.adsTypeNegativeButton)

        fun bind(adsTypeList: ArrayList<AdsTypeModel>, position: Int) {
            val adsType = adsTypeList[position]
            if (adsType.isAdEnable) {
                bindEnabledAd(adsType)
            } else {
                bindDisabledAd(adsType)
            }
        }

        private fun bindDisabledAd(adsType: AdsTypeModel) {
            bindEnabledAd(adsType)
            adsTypeTitle.setWeight(Typography.REGULAR)
            adsTypeTitle.setType(Typography.BODY_1)
            adsTypePositiveButton.isEnabled = false
        }

        private fun bindEnabledAd(adsType: AdsTypeModel) {
            adsTypeTitle.text = adsType.adsTypeTitle
            adsTypeSubtitle.text = adsType.adsTypeSubTitle
            adsTypeDescription.text = adsType.adsTypeDescription
            iconAutomaticAds.setImageDrawable(adsType.adsTypeIcon?.let {
                ContextCompat.getDrawable(itemView.context, it)
            })
            adsType.adsTypeImage?.let {
                adsTypeImage.loadRemoteImageDrawable(it.first, it.second)
            }
            adsTypePositiveButton.text = adsType.adsTypePositiveButton
            adsTypeNegativeButton.text = adsType.adsTypeNegativeButton
            adsTypePositiveButton.setOnClickListener {
                RouteManager.route(itemView.context, adsType.positiveButtonLink)
            }
            adsTypeNegativeButton.setOnClickListener {
                (itemView.context as? TopAdsOnBoardingActivity)?.goToWebView(adsType.negativeButtonLink)
            }
        }

    }
}
