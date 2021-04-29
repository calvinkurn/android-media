package com.tokopedia.digital_checkout.presentation.adapter.vh

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common_digital.atc.data.response.FintechProduct
import com.tokopedia.digital_checkout.data.DigitalCartCrossSellingType
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData
import com.tokopedia.digital_checkout.presentation.widget.DigitalCartMyBillsWidget
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_digital_checkout_my_bills_section.view.*

/**
 * @author by jessica on 10/03/21
 */

class DigitalMyBillsViewHolder(view: View, val listener: MyBillsActionListener) : RecyclerView.ViewHolder(view) {
    fun bindSubscription(subscription: CartDigitalInfoData.CrossSellingConfig, crossSellingType: Int) {
        with(itemView) {
            if (subscription.bodyTitle.isNotEmpty()) {
                itemView.show()
                if (crossSellingType == DigitalCartCrossSellingType.SUBSCRIBED.id) {
                    widgetMyBills.disableCheckBox()
                }
                widgetMyBills.hasMoreInfo(false)

                widgetMyBills.setTitle(subscription.bodyTitle)
                if (subscription.isChecked) widgetMyBills.setDescription(subscription.bodyContentAfter)
                else widgetMyBills.setDescription(subscription.bodyContentBefore)

                if (!widgetMyBills.isChecked()) widgetMyBills.setChecked(subscription.isChecked)
                listener.onSubscriptionChecked(subscription, widgetMyBills.isChecked())

                widgetMyBills.actionListener = object : DigitalCartMyBillsWidget.ActionListener {
                    override fun onMoreInfoClicked() {}

                    override fun onCheckChanged(isChecked: Boolean) {
                        if (isChecked) widgetMyBills.setDescription(subscription.bodyContentAfter)
                        else widgetMyBills.setDescription(subscription.bodyContentBefore)
                        listener.onSubscriptionChecked(subscription, isChecked)
                    }
                }
            }
        }
    }

    fun bindFintechProduct(fintechProduct: FintechProduct, position: Int) {
        with(itemView) {
            if (fintechProduct.info.title.isNotEmpty()) {
                widgetMyBills.setTitle(fintechProduct.info.title)
                widgetMyBills.setDescription(fintechProduct.info.subtitle)
                widgetMyBills.hasMoreInfo(true)

                widgetMyBills.setAdditionalImage(fintechProduct.info.iconUrl)
                if (fintechProduct.info.iconUrl.isNotEmpty()) {
                    listener.onTebusMurahImpression(fintechProduct, position)
                } else listener.onCrossellImpression(fintechProduct, position)

                widgetMyBills.visibility = View.VISIBLE

                if (fintechProduct.checkBoxDisabled) {
                    widgetMyBills.disableCheckBox()
                } else {
                    if (!widgetMyBills.isChecked()) {
                        widgetMyBills.setChecked(fintechProduct.optIn)
                    }
                    listener.onFintechProductChecked(fintechProduct, widgetMyBills.isChecked(), position)
                }

                widgetMyBills.actionListener = object : DigitalCartMyBillsWidget.ActionListener {
                    override fun onMoreInfoClicked() {
                        listener.onFintechMoreInfoChecked(fintechProduct.info)
                    }

                    override fun onCheckChanged(isChecked: Boolean) {
                        if (fintechProduct.info.iconUrl.isNotEmpty()) {
                            listener.onTebusMurahChecked(fintechProduct, position, isChecked)
                        } else {
                            listener.onFintechProductChecked(fintechProduct, isChecked, position)
                        }
                    }
                }
            }
        }
    }
}

interface MyBillsActionListener {
    fun onSubscriptionChecked(subscription: CartDigitalInfoData.CrossSellingConfig, isChecked: Boolean)
    fun onTebusMurahImpression(fintechProduct: FintechProduct, position: Int)
    fun onCrossellImpression(fintechProduct: FintechProduct, position: Int)
    fun onTebusMurahChecked(fintechProduct: FintechProduct, position: Int, isChecked: Boolean)
    fun onFintechProductChecked(fintechProduct: FintechProduct, isChecked: Boolean, position: Int)
    fun onFintechMoreInfoChecked(info: FintechProduct.FintechProductInfo)
}