package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.databinding.WidgetRechargeDenomFullBinding
import com.tokopedia.recharge_component.listener.RechargeDenomFullListener
import com.tokopedia.recharge_component.mapper.DigitalPDPAnalyticsUtils
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.presentation.adapter.DenomFullAdapter
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

class DenomFullWidget @JvmOverloads constructor(
    @NotNull context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private var rechargeDenomFullWidgetBinding =
        WidgetRechargeDenomFullBinding.inflate(LayoutInflater.from(context), this, true)
    private val adapterDenomFull = DenomFullAdapter()

    fun renderDenomFullLayout(
        denomFullListener: RechargeDenomFullListener,
        denomData: DenomWidgetModel,
        selectedProductPosition: Int? = null
    ) {
        if (!denomData.listDenomData.isNullOrEmpty()) {
            with(rechargeDenomFullWidgetBinding) {
                root.show()
                denomFullShimmering.root.hide()
                tgDenomFullWidgetTitle.run {
                    show()
                    text = denomData.mainTitle
                }
                rvDenomFullCard.run {
                    show()
                    with(adapterDenomFull) {
                        clearDenomFullData()
                        setDenomFullList(denomData.listDenomData)
                        listener = denomFullListener
                        productTitleList = denomData.mainTitle
                        selectedProductIndex = selectedProductPosition
                        denomWidgetType = DenomWidgetEnum.FULL_TYPE
                        adapter = adapterDenomFull
                        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    }
                    trackFirstVisibleItemToUser(this, denomFullListener, denomData.listDenomData)
                    clearOnScrollListeners()
                    addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                            super.onScrollStateChanged(recyclerView, newState)
                            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                calculateProductItemVisibleItemTracking(this@run, denomFullListener,
                                    denomData.listDenomData)
                            }
                        }
                    })
                }
            }
        } else renderFailDenomFull()
    }

    fun renderDenomFullShimmering() {
        with(rechargeDenomFullWidgetBinding) {
            root.show()
            tgDenomFullWidgetTitle.hide()
            denomFullShimmering.root.show()
            rvDenomFullCard.hide()
        }
    }

    fun renderFailDenomFull() {
        rechargeDenomFullWidgetBinding.root.hide()
    }

    fun clearSelectedProduct(){
        adapterDenomFull.run {
            selectedProductIndex = null
            notifyDataSetChanged()
        }
    }

    private fun trackFirstVisibleItemToUser(recyclerView: RecyclerView, denomFullListener: RechargeDenomFullListener, listDenomData: List<DenomData>) {
        recyclerView.viewTreeObserver
            .addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        // At this point the layout is complete and the
                        // dimensions of recyclerView and any child views
                        // are known.
                        calculateProductItemVisibleItemTracking(recyclerView, denomFullListener, listDenomData)
                        recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
    }

    private fun calculateProductItemVisibleItemTracking(recyclerView: RecyclerView, denomFullListener: RechargeDenomFullListener, listDenomData: List<DenomData>){
        val indexes = DigitalPDPAnalyticsUtils.getVisibleItemIndexes(recyclerView)
        if (DigitalPDPAnalyticsUtils.hasVisibleItems(indexes)) {
            denomFullListener.onDenomFullImpression(listDenomData.subList(
                indexes.first, indexes.second + 1), DenomWidgetEnum.FULL_TYPE)
        }
    }
}