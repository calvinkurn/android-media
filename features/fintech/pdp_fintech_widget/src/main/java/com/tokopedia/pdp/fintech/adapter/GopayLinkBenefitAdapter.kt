package com.tokopedia.pdp.fintech.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdp.fintech.adapter.GopayLinkBenefitAdapter.BenefitTextViewHolder
import com.tokopedia.pdp.fintech.domain.datamodel.ActivationBottomSheetDescriptions
import com.tokopedia.pdp_fintech.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.resources.isDarkMode


class GopayLinkBenefitAdapter(
    private var arrayOfFeatures: ArrayList<ActivationBottomSheetDescriptions>,
    val context: Context
) :
    RecyclerView.Adapter<BenefitTextViewHolder>() {
    inner class BenefitTextViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val greenTickActivationPage =
            itemView.findViewById<ImageUnify>(R.id.greenTickActivationPage)
        val benifitsText = itemView.findViewById<Typography>(R.id.activationGopayBenefit)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BenefitTextViewHolder {
        return BenefitTextViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.pdp_widget_individual_activation_benifit, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: BenefitTextViewHolder,
        position: Int
    ) {
        if (context.isDarkMode())
            arrayOfFeatures[position].lineIconDark?.let { darkIconUrl ->
                holder.greenTickActivationPage.setImageUrl(
                    darkIconUrl
                )
            }
        else
            arrayOfFeatures[position].lineIconLight?.let { lightIconUrl ->
                holder.greenTickActivationPage.setImageUrl(
                    lightIconUrl
                )
            }

        holder.benifitsText.text = arrayOfFeatures[position].text
    }

    override fun getItemCount(): Int {
        return arrayOfFeatures.size
    }

    fun updateData(descriptions: ArrayList<ActivationBottomSheetDescriptions>) {
        this.arrayOfFeatures = descriptions
        notifyDataSetChanged()
    }


}