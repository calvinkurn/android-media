package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.databinding.WidgetRechargeDenomGridBinding
import com.tokopedia.recharge_component.listener.RechargeDenomGridListener
import com.tokopedia.recharge_component.model.denom.DenomWidgetGridEnum
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.presentation.adapter.DenomGridAdapter
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

class DenomGridWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private var rechargeDenomGridViewBinding = WidgetRechargeDenomGridBinding.inflate(LayoutInflater.from(context), this, true)

    fun renderDenomGridLayout(denomGridListener: RechargeDenomGridListener, titleDenomGrid: String, listDenomGrid: List<DenomWidgetModel>){
        val adapterDenomGrid = DenomGridAdapter()
        with(rechargeDenomGridViewBinding){
            denomGridShimmering.root.hide()
            tgDenomGridWidgetTitle.text = titleDenomGrid
            rvDenomGridCardTitle.run {
                show()
                adapterDenomGrid.clearDenomGridData()
                adapterDenomGrid.setDenomGridList(listDenomGrid)
                adapterDenomGrid.listener = denomGridListener
                adapterDenomGrid.selectedProductIndex = null
                adapterDenomGrid.denomWidgetGridType = DenomWidgetGridEnum.GRID_TYPE
                adapter = adapterDenomGrid
                layoutManager = GridLayoutManager(context, GRID_SIZE)
            }
        }
    }

    fun renderDenomGridPulsaShimmering(titleDenomGrid: String){
        with(rechargeDenomGridViewBinding){
            tgDenomGridWidgetTitle.text = titleDenomGrid
            denomGridShimmering.root.show()
            rvDenomGridCardTitle.hide()
        }
    }

    companion object{
        const val GRID_SIZE = 2
    }

}