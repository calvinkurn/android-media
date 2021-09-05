package com.tokopedia.sellerhome.settings.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.ShopOperationalData
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.ShopOperationalWidgetUiModel
import com.tokopedia.unifyprinciples.Typography

class ShopOperationalViewHolder(itemView: View?) :
    AbstractViewHolder<ShopOperationalWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_sah_new_other_shop_operational_hour
    }

    private val successGroup: Group? =
        itemView?.findViewById(R.id.group_sah_new_other_shop_hour_success)
    private val loadingLayout: ConstraintLayout? =
        itemView?.findViewById(R.id.shimmer_sah_new_other_shop_hour)
    private val errorLayout: ConstraintLayout? =
        itemView?.findViewById(R.id.error_state_sah_new_other_shop_hour)
    private val titleTextView: Typography? =
        itemView?.findViewById(R.id.tv_sah_new_other_shop_hour_title)
    private val shopOperationalIcon: IconUnify? =
        itemView?.findViewById(R.id.ic_sah_new_other_shop_hour)
    private val descTextView: Typography? =
        itemView?.findViewById(R.id.tv_sah_new_other_shop_hour_desc)

    override fun bind(element: ShopOperationalWidgetUiModel) {
        when(val state = element.state) {
            is SettingResponseState.SettingSuccess -> setSuccessOperationalHour(state.data)
            is SettingResponseState.SettingError -> setErrorOperationalHour()
            else -> setLoadingOperationalHour()
        }
    }

    private fun setSuccessOperationalHour(shopOperational: ShopOperationalData) {
        setOperationalHourTitle(shopOperational)
        setOperationalHourIcon(shopOperational)

        shopOperational.timeDescriptionRes?.let { timeRes ->
            descTextView?.text = getString(timeRes)
        }
        shopOperational.timeDescription?.let { timeDesc ->
            descTextView?.text = timeDesc
        }

        successGroup?.show()
        loadingLayout?.gone()
        errorLayout?.gone()
    }

    private fun setOperationalHourTitle(shopOperational: ShopOperationalData) {
        val titleText =
            when {
                shopOperational.isShopOpen -> getString(R.string.sah_new_other_operational_shop_open)
                shopOperational.isShopClosed -> getString(R.string.sah_new_other_operational_shop_off_hour)
                else -> getString(R.string.sah_new_other_operational_shop_closed)
            }
        titleTextView?.text = titleText
    }

    private fun setOperationalHourIcon(shopOperational: ShopOperationalData) {
        shopOperationalIcon?.run {
            setImage(
                newIconId = shopOperational.operationalIconType,
                newLightEnable = MethodChecker.getColor(context, shopOperational.operationalIconColorRes)
            )
        }
    }

    private fun setLoadingOperationalHour() {
        successGroup?.gone()
        loadingLayout?.show()
        errorLayout?.gone()
    }

    private fun setErrorOperationalHour() {
        successGroup?.gone()
        loadingLayout?.gone()
        errorLayout?.show()
    }
}