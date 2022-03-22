package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
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
import com.tokopedia.recharge_component.databinding.WidgetRechargeMccmFullBinding
import com.tokopedia.recharge_component.listener.RechargeDenomFullListener
import com.tokopedia.recharge_component.mapper.DenomMCCMFlashSaleMapper
import com.tokopedia.recharge_component.mapper.DigitalPDPAnalyticsUtils
import com.tokopedia.recharge_component.model.denom.DenomConst
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.presentation.adapter.DenomFullAdapter
import com.tokopedia.recharge_component.presentation.adapter.viewholder.denom.DenomFullViewHolder
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.R.dimen as unifyDimens
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
                        override fun onChannelExpired(channelModel: ChannelModel) {}
                        override fun onSeeAllClick(link: String) {}
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
                    textColor,
                    endTime = ""
                ), object :
                    HeaderListener {
                    override fun onChannelExpired(channelModel: ChannelModel) {}
                    override fun onSeeAllClick(link: String) {}
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
        with(widgetRechargeMCCMFlashSaleFullWidget){
            val possibleHeighestDenomCard = getHeighestItem(listDenomFull)
            populateHeighestItemToContainer(possibleHeighestDenomCard, denomType)
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
                trackFirstVisibleItemToUser(this, denomFullListener, listDenomFull)
                clearOnScrollListeners()
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            calculateProductItemVisibleItemTracking(this@run, denomFullListener,
                                listDenomFull)
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
        widgetRechargeMCCMFlashSaleFullWidget.containerMccmFull.apply {
            val layoutInflater = LayoutInflater.from(context)
            val view = ViewRechargeDenomFullBinding.inflate(layoutInflater)

            val viewHolder = DenomFullViewHolder(adapterDenomFull, view)
            viewHolder.bind(
                denomData,
                denomWidgetType,
                isSelectedItem = false,
                isOnlyOneSize = false,
                position = 0,
                isPlacebo = true
            )

            // setup layout params
            val layoutParams = ViewGroup.LayoutParams(getDimens(R.dimen.widget_denom_full_width), LayoutParams.WRAP_CONTENT)
            view.root.layoutParams = layoutParams

            addView(view.root)

            // mimic recycler view's margin
            view.root.setMargin(
                getDimens(unifyDimens.unify_space_0),
                getDimens(unifyDimens.spacing_lvl2),
                getDimens(unifyDimens.unify_space_0),
                getDimens(unifyDimens.spacing_lvl4)
            )

            val constraintSet = ConstraintSet()
            constraintSet.clone(this)

            // top to top parent
            constraintSet.connect(
                view.root.id,
                ConstraintSet.TOP,
                widgetRechargeMCCMFlashSaleFullWidget.headerMccmFull.id,
                ConstraintSet.BOTTOM
            )

            // start to start parent
            constraintSet.connect(
                view.root.id,
                ConstraintSet.START,
                widgetRechargeMCCMFlashSaleFullWidget.rvMccmFull.id,
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
                indexes.first, indexes.second + 1), DenomWidgetEnum.MCCM_FULL_TYPE)
        }
    }

}