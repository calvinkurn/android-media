package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.recharge_component.databinding.WidgetRechargeDenomGridBinding
import com.tokopedia.recharge_component.listener.RechargeDenomGridListener
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
            tgDenomGridWidgetTitle.text = titleDenomGrid
            rvDenomGridCardTitle.apply {
                adapterDenomGrid.setDenomGridList(listDenomGrid)
                adapterDenomGrid.listener = denomGridListener
                adapterDenomGrid.selectedProductIndex = null
                adapter = adapterDenomGrid
                layoutManager = StaggeredGridLayoutManager(STAG_GRID_SIZE, RecyclerView.VERTICAL)
            }
        }
    }

    companion object{
        const val STAG_GRID_SIZE = 2
    }

}