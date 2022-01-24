package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ServerTimeOffsetUtil
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.databinding.WidgetRechargeMccmGridBinding
import com.tokopedia.recharge_component.listener.RechargeDenomGridListener
import com.tokopedia.recharge_component.mapper.DenomMCCMFlashSaleMapper
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.presentation.adapter.DenomGridAdapter
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull
import java.text.SimpleDateFormat
import java.util.*

class MCCMFlashSaleGridWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                       defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val widgetRechargeMCCMFlashSaleGridWidget = WidgetRechargeMccmGridBinding.inflate(
        LayoutInflater.from(context), this, true)
    private val adapterDenomGrid = DenomGridAdapter()

    fun renderMCCMGrid(denomGridListener: RechargeDenomGridListener, denomData: DenomWidgetModel, textColor: String){
        with(widgetRechargeMCCMFlashSaleGridWidget){
            if (!denomData.listDenomData.isNullOrEmpty()) {
                root.show()
                headerMccmGrid.setChannel(
                    DenomMCCMFlashSaleMapper.getChannelMCCM(
                        denomData.mainTitle,
                        textColor
                    ), object : HeaderListener {
                        override fun onChannelExpired(channelModel: ChannelModel) {

                        }

                        override fun onSeeAllClick(link: String) {

                        }
                    })
                renderAdapter(denomGridListener, denomData.listDenomData, DenomWidgetEnum.MCCM_GRID_TYPE)
            }
        }
    }

    fun renderFlashSaleGrid(denomGridListener: RechargeDenomGridListener, denomData: DenomWidgetModel, textColor: String){
        with(widgetRechargeMCCMFlashSaleGridWidget){
            if (!denomData.listDenomData.isNullOrEmpty()) {
                root.show()
                headerMccmGrid.setChannel(
                    DenomMCCMFlashSaleMapper.getChannelFlashSale(
                        denomData.mainTitle,
                        denomData.subTitle,
                        textColor
                    ), object : HeaderListener {
                        override fun onChannelExpired(channelModel: ChannelModel) {

                        }

                        override fun onSeeAllClick(link: String) {

                        }
                    })
                renderAdapter(denomGridListener, denomData.listDenomData, DenomWidgetEnum.FLASH_GRID_TYPE)
            }
        }
    }

    fun clearSelectedProduct(){
        adapterDenomGrid.run {
            selectedProductIndex = null
            notifyDataSetChanged()
        }
    }

    private fun renderAdapter(denomGridListener: RechargeDenomGridListener, listDenomGrid: List<DenomData>, denomType: DenomWidgetEnum){
        with(widgetRechargeMCCMFlashSaleGridWidget){
            rvMccmGrid.apply {
                show()
                adapterDenomGrid.clearDenomGridData()
                adapterDenomGrid.setDenomGridList(listDenomGrid)
                adapterDenomGrid.listener = denomGridListener
                adapterDenomGrid.selectedProductIndex = null
                adapterDenomGrid.denomWidgetType = denomType
                adapter = adapterDenomGrid
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }
        }
    }
}