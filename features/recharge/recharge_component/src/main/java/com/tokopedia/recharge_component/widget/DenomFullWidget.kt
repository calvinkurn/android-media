package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.databinding.WidgetRechargeDenomFullBinding
import com.tokopedia.recharge_component.listener.RechargeDenomFullListener
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
                }
            }
        } else renderFailDenomFull()
    }

    fun renderDenomFullShimmering() {
        with(rechargeDenomFullWidgetBinding) {
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

}