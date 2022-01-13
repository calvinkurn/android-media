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

    fun renderMCCMGrid(denomGridListener: RechargeDenomGridListener, textColor:String, titleDenomGrid: String, listDenomGrid: List<DenomWidgetModel>){
        with(widgetRechargeMCCMFlashSaleGridWidget){
            headerMccmGrid.setChannel(DenomMCCMFlashSaleMapper.getChannelMCCM(titleDenomGrid, textColor), object : HeaderListener {
                override fun onChannelExpired(channelModel: ChannelModel) {

                }

                override fun onSeeAllClick(link: String) {

                }
            })
            renderAdapter(denomGridListener, listDenomGrid)
        }
    }

    fun renderFlashSaleGrid(denomGridListener: RechargeDenomGridListener, textColor: String, titleFlashSale: String, subtitleFlashSale: String, listDenomGrid: List<DenomWidgetModel>){
        with(widgetRechargeMCCMFlashSaleGridWidget){
            headerMccmGrid.setChannel(DenomMCCMFlashSaleMapper.getChannelFlashSale(titleFlashSale, subtitleFlashSale,  textColor), object : HeaderListener {
                override fun onChannelExpired(channelModel: ChannelModel) {

                }

                override fun onSeeAllClick(link: String) {

                }
            })
            renderAdapter(denomGridListener, listDenomGrid)
        }
    }

    private fun renderAdapter(denomGridListener: RechargeDenomGridListener, listDenomGrid: List<DenomWidgetModel>){
        val adapterDenomGrid = DenomGridAdapter()
        with(widgetRechargeMCCMFlashSaleGridWidget){
            rvMccmGrid.apply {
                show()
                adapterDenomGrid.clearDenomGridData()
                adapterDenomGrid.setDenomGridList(listDenomGrid)
                adapterDenomGrid.listener = denomGridListener
                adapterDenomGrid.selectedProductIndex = null
                adapterDenomGrid.denomWidgetType = DenomWidgetEnum.MCCM_TYPE
                adapter = adapterDenomGrid
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }
        }
    }
}