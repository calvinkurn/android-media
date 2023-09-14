package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.databinding.WidgetRechargeMccmVerticalFullBinding
import com.tokopedia.recharge_component.listener.RechargeDenomFullListener
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.presentation.adapter.DenomFullAdapter
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

class MCCMVerticalFullWidget @JvmOverloads constructor(
    @NotNull context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {
    private val widgetRechargeMCCMVerticalFullWidget =
        WidgetRechargeMccmVerticalFullBinding.inflate(
            LayoutInflater.from(context), this, true
        )
    private val adapterDenomFull = DenomFullAdapter()

    fun renderMCCMVertical(
        denomFullListener: RechargeDenomFullListener,
        denomData: DenomWidgetModel,
        selectedProductIndex: Int? = null
    ) {
        with(widgetRechargeMCCMVerticalFullWidget) {
            if (!denomData.listDenomData.isNullOrEmpty()) {
                tgMccmVerticalFullTitle.text = denomData.mainTitle
                val denomInitialList = if (denomData.listDenomData.size > MIN_LIST) {
                    denomData.listDenomData.subList(Int.ZERO, MIN_LIST)
                } else {
                    denomData.listDenomData
                }
                renderAdapter(
                    denomFullListener,
                    denomData.mainTitle,
                    denomInitialList,
                    DenomWidgetEnum.MCCM_FULL_VERTICAL_TYPE,
                    selectedProductIndex
                )
            }
            renderUpdateSeeMore(denomData.listDenomData, denomFullListener)
        }
    }

    fun clearSelectedProductMCCMVertical(position: Int) {
        adapterDenomFull.run {
            selectedProductIndex = null
            notifyItemChanged(position)
        }
    }

    private fun renderAdapter(
        denomFullListener: RechargeDenomFullListener,
        denomListTitle: String,
        listDenomFull: List<DenomData>,
        denomType: DenomWidgetEnum,
        selectedProduct: Int? = null
    ) {
        with(widgetRechargeMCCMVerticalFullWidget) {
            rvMccmVerticalFull.run {
                show()
                with(adapterDenomFull) {
                    clearDenomFullData()
                    setDenomFullList(listDenomFull)
                    listener = denomFullListener
                    productTitleList = denomListTitle
                    selectedProductIndex = selectedProduct
                    denomWidgetType = denomType
                    adapter = adapterDenomFull
                    layoutManager = LinearLayoutManager(
                        context,
                        androidx.recyclerview.widget.RecyclerView.VERTICAL,
                        false
                    )
                }
            }
        }
    }

    private fun renderUpdateSeeMore(listDenomFull: List<DenomData>, denomFullListener: RechargeDenomFullListener) {
        with(widgetRechargeMCCMVerticalFullWidget) {
            if (listDenomFull.size > MIN_LIST) {
                tgMccmSeeMore.show()
                ivMccmSeeMoreArrow.show()
                tgMccmSeeMore.setOnClickListener {
                    denomFullListener.onShowMoreClicked()
                    tgMccmSeeMore.hide()
                    ivMccmSeeMoreArrow.hide()
                    adapterDenomFull.setDenomFullList(listDenomFull)
                }
            } else {
                tgMccmSeeMore.hide()
                ivMccmSeeMoreArrow.hide()
            }
        }
    }

    companion object {
        private const val MIN_LIST = 2
    }
}
