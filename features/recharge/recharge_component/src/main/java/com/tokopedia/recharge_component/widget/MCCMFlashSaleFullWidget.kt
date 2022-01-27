package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.databinding.WidgetRechargeMccmFullBinding
import com.tokopedia.recharge_component.listener.RechargeDenomFullListener
import com.tokopedia.recharge_component.mapper.DenomMCCMFlashSaleMapper
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.presentation.adapter.DenomFullAdapter
import com.tokopedia.recharge_component.presentation.adapter.DenomGridAdapter
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

class MCCMFlashSaleFullWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                        defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val widgetRechargeMCCMFlashSaleFullWidget = WidgetRechargeMccmFullBinding.inflate(
        LayoutInflater.from(context), this, true)
    private val adapterDenomFull = DenomFullAdapter()

    fun renderMCCMFull(
        denomFullListener: RechargeDenomFullListener,
        denomData: DenomWidgetModel,
        textColor: String,
        selectedProductIndex: Int? = null
    ){
        with(widgetRechargeMCCMFlashSaleFullWidget){
            if (!denomData.listDenomData.isNullOrEmpty()) {
                root.show()
                headerMccmFull.setChannel(
                    DenomMCCMFlashSaleMapper.getChannelMCCM(
                        denomData.mainTitle,
                        textColor
                    ), object :
                        HeaderListener {
                        override fun onChannelExpired(channelModel: ChannelModel) {

                        }

                        override fun onSeeAllClick(link: String) {

                        }
                    })
                renderAdapter(
                    denomFullListener,
                    denomData.mainTitle,
                    denomData.listDenomData,
                    DenomWidgetEnum.MCCM_FULL_TYPE,
                    selectedProductIndex
                )
            }
        }
    }

    fun renderFlashSaleFull(
        denomFullListener: RechargeDenomFullListener,
        denomData: DenomWidgetModel,
        textColor: String,
        selectedProductIndex: Int? = null
    ){
        with(widgetRechargeMCCMFlashSaleFullWidget) {
            if (!denomData.listDenomData.isNullOrEmpty()) {
                root.show()
                headerMccmFull.setChannel(DenomMCCMFlashSaleMapper.getChannelFlashSale(
                    denomData.mainTitle,
                    denomData.subTitle,
                    textColor
                ), object :
                    HeaderListener {
                    override fun onChannelExpired(channelModel: ChannelModel) {

                    }

                    override fun onSeeAllClick(link: String) {

                    }
                })
                renderAdapter(
                    denomFullListener,
                    denomData.mainTitle,
                    denomData.listDenomData,
                    DenomWidgetEnum.FLASH_FULL_TYPE,
                    selectedProductIndex
                )
            }
        }
    }
    fun renderFailMCCMFull(){
        widgetRechargeMCCMFlashSaleFullWidget.root.hide()
    }

    fun clearSelectedProduct(){
        adapterDenomFull.run {
            selectedProductIndex = null
            notifyDataSetChanged()
        }
    }

    private fun renderAdapter(
        denomFullListener: RechargeDenomFullListener,
        denomListTitle: String,
        listDenomFull: List<DenomData>,
        denomType: DenomWidgetEnum,
        selectedProduct: Int? = null
    ) {
        val adapterDenomFull = DenomFullAdapter()
        with(widgetRechargeMCCMFlashSaleFullWidget){
            rvMccmFull.run {
                show()
                with(adapterDenomFull){
                    clearDenomFullData()
                    setDenomFullList(listDenomFull)
                    listener = denomFullListener
                    productTitleList = denomListTitle
                    selectedProductIndex = selectedProduct
                    denomWidgetType = denomType
                    adapter = adapterDenomFull
                    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                }
            }
        }
    }

}