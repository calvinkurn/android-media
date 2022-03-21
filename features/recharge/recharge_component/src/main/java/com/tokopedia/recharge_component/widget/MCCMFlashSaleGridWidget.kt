package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.databinding.WidgetRechargeMccmGridBinding
import com.tokopedia.recharge_component.listener.RechargeDenomGridListener
import com.tokopedia.recharge_component.mapper.DenomMCCMFlashSaleMapper
import com.tokopedia.recharge_component.mapper.DigitalPDPAnalyticsUtils
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.presentation.adapter.DenomGridAdapter
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

class MCCMFlashSaleGridWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                       defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val widgetRechargeMCCMFlashSaleGridWidget = WidgetRechargeMccmGridBinding.inflate(
        LayoutInflater.from(context), this, true)
    private val adapterDenomGrid = DenomGridAdapter()

    fun renderMCCMGrid(denomGridListener: RechargeDenomGridListener, denomData: DenomWidgetModel, textColor: String, selectedProductIndex: Int? = null){
        with(widgetRechargeMCCMFlashSaleGridWidget){
            if (!denomData.listDenomData.isNullOrEmpty()) {
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
                renderAdapter(denomGridListener, denomData.mainTitle, denomData.listDenomData, DenomWidgetEnum.MCCM_GRID_TYPE, selectedProductIndex)
            }
        }
    }

    fun renderFlashSaleGrid(denomGridListener: RechargeDenomGridListener, denomData: DenomWidgetModel, textColor: String, selectedProductIndex: Int? = null){
        with(widgetRechargeMCCMFlashSaleGridWidget){
            if (!denomData.listDenomData.isNullOrEmpty()) {
                headerMccmGrid.setChannel(
                    DenomMCCMFlashSaleMapper.getChannelFlashSale(
                        denomData.mainTitle,
                        denomData.subTitle,
                        textColor,
                        endTime = ""
                    ), object : HeaderListener {
                        override fun onChannelExpired(channelModel: ChannelModel) {

                        }

                        override fun onSeeAllClick(link: String) {

                        }
                    })
                renderAdapter(denomGridListener, denomData.mainTitle, denomData.listDenomData, DenomWidgetEnum.FLASH_GRID_TYPE, selectedProductIndex)
            }
        }
    }

    fun clearSelectedProduct(){
        adapterDenomGrid.run {
            selectedProductIndex = null
            notifyDataSetChanged()
        }
    }

    private fun renderAdapter(denomGridListener: RechargeDenomGridListener, denomListTitle: String, listDenomGrid: List<DenomData>, denomType: DenomWidgetEnum, selectedProduct: Int? = null){
        with(widgetRechargeMCCMFlashSaleGridWidget){
            rvMccmGrid.apply {
                show()
                adapterDenomGrid.clearDenomGridData()
                adapterDenomGrid.setDenomGridList(listDenomGrid)
                adapterDenomGrid.listener = denomGridListener
                adapterDenomGrid.selectedProductIndex = selectedProduct
                adapterDenomGrid.denomWidgetType = denomType
                adapterDenomGrid.productTitleList = denomListTitle
                adapter = adapterDenomGrid
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                trackFirstVisibleItemToUser(this, denomGridListener, listDenomGrid)
                clearOnScrollListeners()
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            calculateProductItemVisibleItemTracking(this@apply, denomGridListener,
                                listDenomGrid)
                        }
                    }
                })
            }
        }
    }

    private fun trackFirstVisibleItemToUser(recyclerView: RecyclerView, denomGridListener: RechargeDenomGridListener,
                                            listDenomData: List<DenomData>) {
        recyclerView.viewTreeObserver
            .addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        // At this point the layout is complete and the
                        // dimensions of recyclerView and any child views
                        // are known.
                        calculateProductItemVisibleItemTracking(recyclerView, denomGridListener, listDenomData)
                        recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
    }

    private fun calculateProductItemVisibleItemTracking(recyclerView: RecyclerView, denomGridListener: RechargeDenomGridListener,
                                                        listDenomData: List<DenomData>){
        val indexes = DigitalPDPAnalyticsUtils.getVisibleItemIndexes(recyclerView)
        if (DigitalPDPAnalyticsUtils.hasVisibleItems(indexes)) {
            denomGridListener.onDenomGridImpression(listDenomData.subList(
                indexes.first, indexes.second + 1), DenomWidgetEnum.MCCM_GRID_TYPE)
        }
    }
}