package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.databinding.WidgetRechargeMccmBinding
import com.tokopedia.recharge_component.listener.RechargeDenomGridListener
import com.tokopedia.recharge_component.model.denom.DenomWidgetGridEnum
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.presentation.adapter.DenomGridAdapter
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

class MCCMFlashSaleGridWidget@JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                       defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val widgetRechargeMCCMFlashSaleGridWidget = WidgetRechargeMccmBinding.inflate(
        LayoutInflater.from(context), this, true)

    fun renderMCCMGrid(denomGridListener: RechargeDenomGridListener, titleDenomGrid: String, listDenomGrid: List<DenomWidgetModel>){
        with(widgetRechargeMCCMFlashSaleGridWidget){
            headerMccm.setChannel(getChannelMCCM(titleDenomGrid), object : HeaderListener {
                override fun onChannelExpired(channelModel: ChannelModel) {

                }

                override fun onSeeAllClick(link: String) {

                }
            })
            renderAdapter(denomGridListener, listDenomGrid)
        }
    }

    fun renderFlashSaleGrid(denomGridListener: RechargeDenomGridListener, titleDenomGrid: String, subtitle: String, listDenomGrid: List<DenomWidgetModel>){
        with(widgetRechargeMCCMFlashSaleGridWidget){
            renderAdapter(denomGridListener, listDenomGrid)
        }
    }

    private fun renderAdapter(denomGridListener: RechargeDenomGridListener, listDenomGrid: List<DenomWidgetModel>){
        val adapterDenomGrid = DenomGridAdapter()
        with(widgetRechargeMCCMFlashSaleGridWidget){
            rvMccm.apply {
                show()
                adapterDenomGrid.clearDenomGridData()
                adapterDenomGrid.setDenomGridList(listDenomGrid)
                adapterDenomGrid.listener = denomGridListener
                adapterDenomGrid.selectedProductIndex = null
                adapterDenomGrid.denomWidgetGridType = DenomWidgetGridEnum.MCCM_TYPE
                adapter = adapterDenomGrid
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }
        }
    }

    private fun getChannelMCCM(titleDenomGrid: String): ChannelModel {
        return ChannelModel(
            id = CHANNEL_ID,
            groupId = CHANNEL_ID,
            channelHeader = ChannelHeader(
                name = titleDenomGrid,
                textColor = resources.getString(com.tokopedia.unifyprinciples.R.color.Unify_N0)
            )
        )
    }


    companion object {
        private const val CHANNEL_ID = "1"
    }
}