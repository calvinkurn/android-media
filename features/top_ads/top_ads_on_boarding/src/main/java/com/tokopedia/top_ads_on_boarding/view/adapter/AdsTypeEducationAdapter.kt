package com.tokopedia.top_ads_on_boarding.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tokopedia.top_ads_on_boarding.R
import com.tokopedia.top_ads_on_boarding.model.AdsTypeEducation
import com.tokopedia.top_ads_on_boarding.model.AdsTypeEducationModel
import com.tokopedia.unifyprinciples.Typography

class AdsTypeEducationAdapter :
    RecyclerView.Adapter<AdsTypeEducationAdapter.AdsTypeEducationViewHolder>() {

    private val educationPointList: ArrayList<AdsTypeEducation> by lazy { ArrayList() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsTypeEducationViewHolder {
        return if (viewType == R.layout.topads_edcation_point_item_layout) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.topads_edcation_point_item_layout, parent, false)
            AdsTypeEducationPointsViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.topads_edcation_header_item_layout, parent, false)
            AdsTypeEducationHeaderViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: AdsTypeEducationViewHolder, position: Int) {
        holder.bind(educationPointList[position])
    }

    override fun getItemCount(): Int {
        return educationPointList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) R.layout.topads_edcation_header_item_layout else R.layout.topads_edcation_point_item_layout
    }

    fun submitList(pointList: List<AdsTypeEducation>) {
        this.educationPointList.clear()
        this.educationPointList.addAll(pointList)
        notifyDataSetChanged()
    }

    inner class AdsTypeEducationPointsViewHolder(itemView: View) :
        AdsTypeEducationViewHolder(itemView) {

        private val pointIcon: ImageView = itemView.findViewById(R.id.pointIcon)
        private val pointTitle: Typography = itemView.findViewById(R.id.pointTitle)
        private val pointDescription: Typography = itemView.findViewById(R.id.pointDescription)

        override fun bind(adsTypeEducation: AdsTypeEducation) {
            if (adsTypeEducation is AdsTypeEducationModel.Points) {
                pointTitle.text = adsTypeEducation.points_Title
                pointDescription.text = adsTypeEducation.points_Description

                pointIcon.setImageDrawable(adsTypeEducation.points_Icon?.let {
                    ContextCompat.getDrawable(itemView.context, it)
                })
            }
        }
    }

    inner class AdsTypeEducationHeaderViewHolder(itemView: View) :
        AdsTypeEducationViewHolder(itemView) {

        private val headerImage: DeferredImageView = itemView.findViewById(R.id.headerImage)
        private val headerTitle: Typography = itemView.findViewById(R.id.headerTitle)
        private val headerSubtitle: Typography = itemView.findViewById(R.id.headerSubTitle)
        private val headerDescription: Typography = itemView.findViewById(R.id.headerDescription)


        override fun bind(adsTypeEducation: AdsTypeEducation) {
            if (adsTypeEducation is AdsTypeEducationModel.Header) {
                headerTitle.text = adsTypeEducation.title
                headerSubtitle.text = adsTypeEducation.subTitle
                headerDescription.text = adsTypeEducation.description
                headerImage.loadRemoteImageDrawable(
                    adsTypeEducation.headerImage?.first ?: "",
                    adsTypeEducation.headerImage?.second ?: ""
                )
            }
        }
    }

    open inner class AdsTypeEducationViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        open fun bind(adsTypeEducation: AdsTypeEducation) {}
    }


}



