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
import com.tokopedia.recharge_component.databinding.WidgetRechargeMccmBinding
import com.tokopedia.recharge_component.listener.RechargeDenomGridListener
import com.tokopedia.recharge_component.model.denom.DenomWidgetGridEnum
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.presentation.adapter.DenomGridAdapter
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull
import java.text.SimpleDateFormat
import java.util.*

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

    fun renderFlashSaleGrid(denomGridListener: RechargeDenomGridListener, titleFlashSale: String, subtitleFlashSale: String, listDenomGrid: List<DenomWidgetModel>){
        with(widgetRechargeMCCMFlashSaleGridWidget){
            headerMccm.setChannel(getChannelFlashSale(titleFlashSale, subtitleFlashSale), object : HeaderListener {
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

    private fun getChannelMCCM(titleMCCM: String): ChannelModel {
        return ChannelModel(
            id = CHANNEL_ID,
            groupId = CHANNEL_ID,
            channelHeader = ChannelHeader(
                name = titleMCCM,
                textColor = resources.getString(com.tokopedia.unifyprinciples.R.color.Unify_N0)
            )
        )
    }

    private fun getChannelFlashSale(titleFlashSale: String, subtitleFlashSale:String): ChannelModel {

        val currentDate = Calendar.getInstance().time
        val currentTimeInSeconds = currentDate.time / SECOND_IN_MILIS

        val parser = SimpleDateFormat(EXPIRED_DATE_PATTERN)
        val expiredTime = if (DUMMY_END_TIME.isNotEmpty())
            parser.format(Date(DUMMY_END_TIME.toLong() * SECOND_IN_MILIS))
        else ""

        return ChannelModel(
            id = CHANNEL_ID,
            groupId = CHANNEL_ID,
            channelHeader = ChannelHeader(
                name = titleFlashSale,
                subtitle = subtitleFlashSale,
                textColor = resources.getString(com.tokopedia.unifyprinciples.R.color.Unify_N0),
                expiredTime = expiredTime,
                serverTimeUnix = currentTimeInSeconds
            ),
            channelConfig = ChannelConfig(
                serverTimeOffset = ServerTimeOffsetUtil.getServerTimeOffsetFromUnix(
                    currentTimeInSeconds
                )
            )
        )
    }

    companion object {
        private const val CHANNEL_ID = "1"
        private const val EXPIRED_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZZZZ"
        private const val SECOND_IN_MILIS = 1000

        private const val DUMMY_END_TIME = "1641998905"
    }
}