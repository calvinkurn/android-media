package com.tokopedia.sellerhome.settings.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.FreeShippingWidgetUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify

class FreeShippingViewHolder(itemView: View?,
                             private val onFreeShippingClicked: () -> Unit,
                             private val onErrorClicked: () -> Unit,
                             private val onFreeShippingImpression: () -> Unit) :
    AbstractViewHolder<FreeShippingWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_sah_new_other_shop_free_shipping
    }

    private val freeShippingImageView: ImageUnify? =
        itemView?.findViewById(R.id.iv_sah_new_other_shop_free_shipping)
    private val errorLayout: ConstraintLayout? =
        itemView?.findViewById(R.id.error_state_sah_new_other_shop_free_shipping)
    private val shimmerLoading: LoaderUnify? =
        itemView?.findViewById(R.id.shimmer_sah_new_other_free_shipping)

    override fun bind(element: FreeShippingWidgetUiModel) {
        when(val state = element.state) {
            is SettingResponseState.SettingSuccess -> {
                setFreeShippingImage(state.data)
                itemView.addOnImpressionListener(element.impressHolder) {
                    onFreeShippingImpression()
                }
            }
            is SettingResponseState.SettingError -> setWidgetError()
            else -> setWidgetLoading()
        }
    }

    private fun setFreeShippingImage(freeShippingUrl: String) {
        freeShippingImageView?.run {
            show()
            setImageUrl(freeShippingUrl)
        }
        errorLayout?.gone()
        shimmerLoading?.gone()
        itemView.setOnClickListener {
            onFreeShippingClicked()
        }
    }

    private fun setWidgetError() {
        freeShippingImageView?.gone()
        errorLayout?.run {
            show()
            setOnClickListener {
                onErrorClicked()
            }
        }
        shimmerLoading?.gone()
        itemView.setOnClickListener(null)
    }

    private fun setWidgetLoading() {
        freeShippingImageView?.gone()
        errorLayout?.gone()
        shimmerLoading?.show()
        itemView.setOnClickListener(null)
    }

}