package com.tokopedia.topads.view.adapter.adgrouplist.viewholder

import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.create.R
import com.tokopedia.topads.create.databinding.AdGroupItemViewholderLayoutBinding
import com.tokopedia.topads.data.AdGroupSettingData
import com.tokopedia.topads.data.AdGroupStatsData
import com.tokopedia.topads.view.adapter.adgrouplist.model.AdGroupUiModel
import com.tokopedia.topads.view.customviews.AdStatsView
import com.tokopedia.topads.view.datamodel.AdStatModel
import com.tokopedia.topads.view.sheet.AdGroupStatisticsBottomSheet
import com.tokopedia.topads.view.sheet.AdvertisingCostBottomSheet
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.view.binding.viewBinding

class AdGroupViewHolder(itemView: View,private val listener:AdGroupListener) : AbstractViewHolder<AdGroupUiModel>(itemView) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.ad_group_item_viewholder_layout
    }

    private val binding:AdGroupItemViewholderLayoutBinding? by viewBinding()
    private var selected = false

    init {
       binding?.adGroupItemHeader?.setOnClickListener {
           selected = if(!selected){
               selectAdGroup()
               true
           }
           else{
               unselectAdGroup()
               false
           }
       }
    }

    override fun bind(data: AdGroupUiModel?) {
       data?.let {
           binding?.apply {
               adGroupTitleTv.text = data.groupName
               bindTopRow(data.adGroupStats)
               bindBottomRow(data.adGroupSetting)
           }
       }
    }

    private fun bindTopRow(data:AdGroupStatsData){
        if(data.loading){
            binding?.adStatsTopRow?.setAllLoading(3)
            removeStatClickListeners(binding?.adStatsTopRow)
            return
        }
        val impressionStatText = itemView.context.getString(R.string.ad_group_impression)
        val clickStatText = itemView.context.getString(R.string.ad_group_click)
        val conversionStatText = itemView.context.getString(R.string.ad_group_conversion)
        val impressionStatValue = CurrencyFormatHelper.convertToRupiah(data.totalImpressionStats)
        val clickStatValue = CurrencyFormatHelper.convertToRupiah(data.totalClickStats)
        val conversionStatValue = CurrencyFormatHelper.convertToRupiah(data.totalConversionStats)
        binding?.apply {
            adStatsBottomRow.submitStatList(
                listOf(
                    AdStatModel(description = impressionStatText, value = impressionStatValue),
                    AdStatModel(description = clickStatText, value = clickStatValue),
                    AdStatModel(description = conversionStatText, value = conversionStatValue)
                )
            )
        }
        attachStatClickListeners(binding?.adStatsTopRow,0)
    }

    private fun bindBottomRow(data:AdGroupSettingData){
        if(data.loading){
            binding?.adStatsBottomRow?.setAllLoading(3)
            removeStatClickListeners(binding?.adStatsBottomRow)
            return
        }
        val searchStatText = itemView.context.getString(R.string.ad_group_search_stat)
        val recomStatText = itemView.context.getString(R.string.ad_group_recom_stat)
        val searchStatValue = CurrencyFormatHelper.convertToRupiah(data.productSearch.toInt().toString())
        val recomStatValue = CurrencyFormatHelper.convertToRupiah(data.productBrowse.toInt().toString())
        binding?.apply {
            adStatsBottomRow.submitStatList(
                listOf(
                    AdStatModel(description = searchStatText, value = searchStatValue),
                    AdStatModel(description = recomStatText, value = recomStatValue)
                )
            )
        }
        attachStatClickListeners(binding?.adStatsBottomRow,1)
    }

    private fun attachStatClickListeners(view:AdStatsView?,index:Int){
        view?.setOnClickListener {
            listener.onAdStatClicked(getBottomSheetInstance(index))
        }
    }

    private fun getBottomSheetInstance(index:Int) : BottomSheetUnify{
        return if(index==0) AdGroupStatisticsBottomSheet()
               else AdvertisingCostBottomSheet()
    }

    private fun removeStatClickListeners(view:AdStatsView?){
        view?.setOnClickListener(null)
    }

    private fun selectAdGroup(){
        binding?.adGroupContainer?.background = itemView.context.getDrawable(R.drawable.low_emphasis_border_focused)
        binding?.adGroupFooter?.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_GN100
            )
        )
    }

    private fun unselectAdGroup(){
        binding?.adGroupContainer?.background = itemView.context.getDrawable(R.drawable.low_emphasis_border)
        binding?.adGroupFooter?.backgroundTintList = null
    }
    
    interface AdGroupListener{
        fun onAdStatClicked(bottomSheet:BottomSheetUnify)
    }
}
