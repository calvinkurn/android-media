package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.databinding.WidgetRechargeDenomGridBinding
import com.tokopedia.recharge_component.listener.RechargeDenomGridListener
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
            tgDenomGridWidgetTitle.hide()
            denomGridShimmering.root.show()
            rvDenomGridCard.hide()
        }
    }

    companion object{
        const val GRID_SIZE = 2
    }

}