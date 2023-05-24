package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.databinding.WidgetRechargeMccmVerticalFullBinding
import com.tokopedia.recharge_component.listener.RechargeDenomFullListener
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.presentation.adapter.DenomFullAdapter
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

class MCCMVerticalFullWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                       defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {
        private val widgetRechargeMCCMVerticalFullWidget = WidgetRechargeMccmVerticalFullBinding.inflate(
            LayoutInflater.from(context), this, true)
    private val adapterDenomFull = DenomFullAdapter()

    fun renderMCCMVertical(
        denomFullListener: RechargeDenomFullListener,
        denomData: DenomWidgetModel,
        selectedProductIndex: Int? = null
    ){
        with(widgetRechargeMCCMVerticalFullWidget) {
            if (!denomData.listDenomData.isNullOrEmpty()) {
                tgMccmVerticalFullTitle.text = denomData.mainTitle
                renderAdapter(
                    denomFullListener,
                    denomData.mainTitle,
                    denomData.listDenomData.subList(Int.ZERO, MIN_LIST),
                    DenomWidgetEnum.MCCM_FULL_VERTICAL_TYPE,
                    selectedProductIndex
                )
            }
            renderUpdateSeeMore(denomData.listDenomData)
        }
    }

    fun clearSelectedProductMCCMVertical(position: Int){
        adapterDenomFull.run {
            selectedProductIndex = null
            notifyItemChanged(position)
        }
    }

    private fun renderAdapter(
        denomFullListener: RechargeDenomFullListener,
        denomListTitle: String,
        listDenomFull: List<DenomData>,
        denomType: DenomWidgetEnum,
        selectedProduct: Int? = null
    ) {
        with(widgetRechargeMCCMVerticalFullWidget){
            rvMccmVerticalFull.run {
                show()
                with(adapterDenomFull){
                    clearDenomFullData()
                    setDenomFullList(listDenomFull)
                    listener = denomFullListener
                    productTitleList = denomListTitle
                    selectedProductIndex = selectedProduct
                    denomWidgetType = denomType
                    adapter = adapterDenomFull
                    layoutManager = LinearLayoutManager(context, androidx.recyclerview.widget.RecyclerView.VERTICAL, false)
                }
            }
        }
    }

    private fun renderUpdateSeeMore(listDenomFull: List<DenomData>) {
        var showLess = true
        with(widgetRechargeMCCMVerticalFullWidget){
            if (listDenomFull.size > MIN_LIST) {
                tgMccmSeeMore.show()
                tgMccmSeeMore.setOnClickListener {
                    if (showLess) {
                        tgMccmSeeMore.text =
                            context.resources.getString(com.tokopedia.recharge_component.R.string.mccm_show_less)
                        ivMccmSeeMoreArrow.setImageResource(com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_up_normal_24)
                        showLess = false
                        adapterDenomFull.setDenomFullList(listDenomFull)
                    } else {
                        tgMccmSeeMore.text =
                            context.resources.getString(com.tokopedia.recharge_component.R.string.mccm_show_more)
                        ivMccmSeeMoreArrow.setImageResource(com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_down_normal_24)
                        showLess = true
                        adapterDenomFull.setDenomFullList(listDenomFull.subList(Int.ZERO, MIN_LIST))
                    }
                }
            } else {
                tgMccmSeeMore.hide()
            }
        }
    }

    companion object {
        private const val MIN_LIST = 2
    }
}
