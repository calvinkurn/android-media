package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.R
import com.tokopedia.recharge_component.databinding.ViewRechargeDenomGridBinding
import com.tokopedia.recharge_component.databinding.WidgetRechargeMccmGridBinding
import com.tokopedia.recharge_component.listener.RechargeDenomGridListener
import com.tokopedia.recharge_component.mapper.DenomMCCMFlashSaleMapper
import com.tokopedia.recharge_component.model.denom.DenomConst
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.presentation.adapter.DenomGridAdapter
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

    fun clearSelectedProduct(position: Int){
        adapterDenomGrid.run {
            selectedProductIndex = null
            notifyItemChanged(position)
        }
    }

    private fun renderAdapter(denomGridListener: RechargeDenomGridListener, denomListTitle: String, listDenomGrid: List<DenomData>, denomType: DenomWidgetEnum, selectedProduct: Int? = null){
        with(widgetRechargeMCCMFlashSaleGridWidget){
            val possiblehighestDenomCard = getHighestCard(listDenomGrid)
            populateHighestItemToContainer(possiblehighestDenomCard, denomType)
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
            }
        }
    }

    /** Give the recyclerview the information of the possible highest item card.
     * This logic may need to be updated if there are changes in view positioning */
    private fun getHighestCard(listDenomFull: List<DenomData>): DenomData {
        var highestDenom = listDenomFull[0]
        var highestCount = 0
        var attrCount = 0
        for (denom in listDenomFull) {
            if (denom.title.isNotEmpty()) attrCount++
            if (denom.description.isNotEmpty()) attrCount++
            if (denom.quotaInfo.isNotEmpty() || denom.expiredDate.isNotEmpty()) attrCount++
            if (denom.specialLabel.isNotEmpty() || denom.status == DenomConst.DENOM_STATUS_OUT_OF_STOCK) attrCount++
            if (denom.price.isNotEmpty()) attrCount++
            if (denom.flashSaleLabel.isNotEmpty()) attrCount++
            if (denom.flashSalePercentage.isMoreThanZero()) attrCount++

            if (attrCount > highestCount) {
                highestCount = attrCount
                highestDenom = denom
            } else if (attrCount == highestCount && highestDenom.title.length < denom.title.length) {
                highestCount = attrCount
                highestDenom = denom
            }

            attrCount = 0
        }
        return highestDenom
    }

    /** Create invisible siblings next to recyclerview, so that recyclerview will have the same
     * height as the highest/tallest card. */
    private fun populateHighestItemToContainer(denomData: DenomData, denomWidgetType: DenomWidgetEnum) {
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
            view.root.invisible()

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

            // start to end recyclerview
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
}