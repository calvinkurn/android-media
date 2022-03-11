package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.databinding.WidgetRechargeDenomGridBinding
import com.tokopedia.recharge_component.listener.RechargeDenomGridListener
import com.tokopedia.recharge_component.mapper.DigitalPDPAnalyticsUtils
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.presentation.adapter.DenomGridAdapter
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

class DenomGridWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private var rechargeDenomGridViewBinding = WidgetRechargeDenomGridBinding.inflate(LayoutInflater.from(context), this, true)
    private val adapterDenomGrid = DenomGridAdapter()

    fun renderDenomGridLayout(denomGridListener: RechargeDenomGridListener,
                              denomData: DenomWidgetModel,
                              selectedProductPosition: Int? = null
    ){
        if (!denomData.listDenomData.isNullOrEmpty()) {
            with(rechargeDenomGridViewBinding) {
                root.show()
                denomGridShimmering.root.hide()
                tgDenomGridWidgetTitle.run {
                    show()
                    text = denomData.mainTitle
                }
                rvDenomGridCard.run {
                    show()
                    adapterDenomGrid.clearDenomGridData()
                    adapterDenomGrid.setDenomGridList(denomData.listDenomData)
                    adapterDenomGrid.listener = denomGridListener
                    adapterDenomGrid.productTitleList = denomData.mainTitle
                    adapterDenomGrid.selectedProductIndex = selectedProductPosition
                    adapterDenomGrid.denomWidgetType = DenomWidgetEnum.GRID_TYPE
                    adapter = adapterDenomGrid
                    layoutManager = GridLayoutManager(context, GRID_SIZE)
                    trackFirstVisibleItemToUser(this, denomGridListener, denomData.listDenomData)
                    clearOnScrollListeners()
                    addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                            super.onScrollStateChanged(recyclerView, newState)
                            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                calculateProductItemVisibleItemTracking(this@run, denomGridListener,
                                    denomData.listDenomData)
                            }
                        }
                    })
                }
            }
        } else renderFailDenomGrid()
    }

    fun renderFailDenomGrid(){
        rechargeDenomGridViewBinding.root.hide()
    }

    fun clearSelectedProduct(){
        adapterDenomGrid.run {
            selectedProductIndex = null
            notifyDataSetChanged()
        }
    }

    fun renderDenomGridShimmering(){
        with(rechargeDenomGridViewBinding){
            root.show()
            tgDenomGridWidgetTitle.hide()
            denomGridShimmering.root.show()
            rvDenomGridCard.hide()
        }
    }

    private fun trackFirstVisibleItemToUser(recyclerView: RecyclerView, denomGridListener: RechargeDenomGridListener, listDenomData: List<DenomData>) {
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

    private fun calculateProductItemVisibleItemTracking(recyclerView: RecyclerView, denomGridListener: RechargeDenomGridListener, listDenomData: List<DenomData>){
        val indexes = DigitalPDPAnalyticsUtils.getVisibleItemIndexes(recyclerView)
        if (DigitalPDPAnalyticsUtils.hasVisibleItems(indexes)) {
            denomGridListener.onDenomGridImpression(listDenomData.subList(
                indexes.first, indexes.second + 1), DenomWidgetEnum.GRID_TYPE)
        }
    }

    companion object{
        const val GRID_SIZE = 2
    }

}