package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.district_recommendation

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_recommendation.DistrictRecommendationItemUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_recommendation.DistrictRecommendationResponseUiModel
import kotlinx.android.synthetic.main.bottomsheet_district_recommendation_item.view.*

/**
 * Created by fwidjaja on 2019-05-31.
 */
class DistrictRecommendationBottomSheetAdapter(private var actionListener: ActionListener) : RecyclerView.Adapter<DistrictRecommendationBottomSheetAdapter.ViewHolder>() {
    var listDistrictRecommendation = mutableListOf<DistrictRecommendationItemUiModel>()

    interface ActionListener {
        fun onDistrictItemClicked(districtRecommendationItemUiModel: DistrictRecommendationItemUiModel)
        fun onSuccessGetDistrictRecommendation(getDistrictRecommendationResponseUiModel: DistrictRecommendationResponseUiModel, numPage: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_district_recommendation_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listDistrictRecommendation.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val provinceName = listDistrictRecommendation[position].provinceName
        val cityName = listDistrictRecommendation[position].cityName
        val districtName = listDistrictRecommendation[position].districtName
        val districtSelected = "$provinceName, $cityName, $districtName"
        holder.itemView.tv_district_name.text = districtSelected
        holder.itemView.tv_district_name.setOnClickListener {
            actionListener.onDistrictItemClicked(listDistrictRecommendation[position])
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun loadDistrictRecommendation(listDistrict: MutableList<DistrictRecommendationItemUiModel>) {
        listDistrictRecommendation.clear()
        listDistrictRecommendation.addAll(listDistrict)
    }

    fun loadDistrictRecommendationNextPage(listDistrict: MutableList<DistrictRecommendationItemUiModel>) {
        listDistrictRecommendation.addAll(listDistrict)
    }
}