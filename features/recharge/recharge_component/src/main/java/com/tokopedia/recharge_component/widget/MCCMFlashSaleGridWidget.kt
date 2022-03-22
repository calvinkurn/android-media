package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.R
import com.tokopedia.recharge_component.databinding.ViewRechargeDenomFullBinding
import com.tokopedia.recharge_component.databinding.ViewRechargeDenomGridBinding
import com.tokopedia.recharge_component.databinding.WidgetRechargeMccmGridBinding
import com.tokopedia.recharge_component.listener.RechargeDenomGridListener
import com.tokopedia.recharge_component.mapper.DenomMCCMFlashSaleMapper
import com.tokopedia.recharge_component.mapper.DigitalPDPAnalyticsUtils
import com.tokopedia.recharge_component.model.denom.DenomConst
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.presentation.adapter.DenomGridAdapter
import com.tokopedia.recharge_component.presentation.adapter.viewholder.denom.DenomFullViewHolder
import com.tokopedia.recharge_component.presentation.adapter.viewholder.denom.DenomGridViewHolder
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
                renderAdapter(denomGridListener, denomData.mainTitle, denomData.listDenomData, DenomWidgetEnum.MCCM_GRID_TYPE, selectedProductIndex)
            }
        }
    }

    fun renderFlashSaleGrid(denomGridListener: RechargeDenomGridListener, denomData: DenomWidgetModel, textColor: String, selectedProductIndex: Int? = null){
        with(widgetRechargeMCCMFlashSaleGridWidget){
            if (!denomData.listDenomData.isNullOrEmpty()) {
                root.show()
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

    fun renderFailMCCMGrid(){
        widgetRechargeMCCMFlashSaleGridWidget.root.hide()
    }

    fun clearSelectedProduct(){
        adapterDenomGrid.run {
            selectedProductIndex = null
            notifyDataSetChanged()
        }
    }

    private fun renderAdapter(denomGridListener: RechargeDenomGridListener, denomListTitle: String, listDenomGrid: List<DenomData>, denomType: DenomWidgetEnum, selectedProduct: Int? = null){
        with(widgetRechargeMCCMFlashSaleGridWidget){
            val possibleHeighestDenomCard = getHeighestItem(listDenomGrid)
            populateHeighestItemToContainer(possibleHeighestDenomCard, denomType)
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

    private fun getHeighestItem(listDenomFull: List<DenomData>): DenomData {
        var heighestDenom = listDenomFull[0]
        var heighestCount = 0
        var attrCount = 0
        for (denom in listDenomFull) {
            if (denom.title.isNotEmpty()) attrCount += 1
            if (denom.description.isNotEmpty()) attrCount += 1
            if (denom.quotaInfo.isNotEmpty() || denom.expiredDate.isNotEmpty()) attrCount += 1
            if (denom.specialLabel.isNotEmpty() || denom.status == DenomConst.DENOM_STATUS_OUT_OF_STOCK)
                if (denom.price.isNotEmpty()) attrCount += 1
            if (denom.flashSaleLabel.isNotEmpty()) attrCount += 1
            if (denom.flashSalePercentage.isMoreThanZero()) attrCount += 1

            if (attrCount > heighestCount) {
                heighestCount = attrCount
                heighestDenom = denom
            } else if (attrCount == heighestCount && heighestDenom.title.length < denom.title.length) {
                heighestCount = attrCount
                heighestDenom = denom
            }

            attrCount = 0
        }
        return heighestDenom
    }

    private fun populateHeighestItemToContainer(denomData: DenomData, denomWidgetType: DenomWidgetEnum) {
        widgetRechargeMCCMFlashSaleGridWidget.containerMccmGrid.apply {
            val layoutInflater = LayoutInflater.from(context)
            val view = ViewRechargeDenomGridBinding.inflate(layoutInflater)

            val viewHolder = DenomGridViewHolder(adapterDenomGrid, view)
            viewHolder.bind(
                denomData,
                denomWidgetType,
                isSelectedItem = false,
                position = 0,
                isPlacebo = true
            )

            // setup layout params
            val layoutParams = ViewGroup.LayoutParams(getDimens(R.dimen.widget_denom_full_width), LayoutParams.WRAP_CONTENT)
            view.root.layoutParams = layoutParams

            addView(view.root)

            // mimic recycler view's margin
            view.root.setMargin(
                getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                getDimens(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2),
                getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                getDimens(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
            )

            val constraintSet = ConstraintSet()
            constraintSet.clone(this)

            // top to top parent
            constraintSet.connect(
                view.root.id,
                ConstraintSet.TOP,
                widgetRechargeMCCMFlashSaleGridWidget.headerMccmGrid.id,
                ConstraintSet.BOTTOM
            )

            // start to start parent
            constraintSet.connect(
                view.root.id,
                ConstraintSet.START,
                widgetRechargeMCCMFlashSaleGridWidget.rvMccmGrid.id,
                ConstraintSet.END
            )

            // bottom to bottom parent
            constraintSet.connect(
                view.root.id,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
            )

            constraintSet.applyTo(this)
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