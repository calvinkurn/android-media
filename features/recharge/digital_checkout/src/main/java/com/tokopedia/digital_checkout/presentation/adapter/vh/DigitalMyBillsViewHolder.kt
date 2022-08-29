package com.tokopedia.digital_checkout.presentation.adapter.vh

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common_digital.atc.data.response.FintechProduct
import com.tokopedia.digital_checkout.presentation.widget.DigitalCartMyBillsWidget
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_digital_checkout_my_bills_section.view.*

/**
 * @author by jessica on 10/03/21
 */

class DigitalMyBillsViewHolder(view: View, val listener: MyBillsActionListener) :
    RecyclerView.ViewHolder(view) {
    fun bindSubscription(subscription: FintechProduct) {
        with(itemView) {
            if (subscription.info.title.isNotEmpty()) {
                itemView.show()
                listener.onSubscriptionImpression(subscription)

                widgetMyBills.hasMoreInfo(true)
                widgetMyBills.setTitle(subscription.info.title)
                if (subscription.optIn) {
                    widgetMyBills.setDescription(subscription.info.checkedSubtitle)
                } else {
                    widgetMyBills.setDescription(subscription.info.subtitle)
                }

                widgetMyBills.actionListener = object : DigitalCartMyBillsWidget.ActionListener {
                    override fun onMoreInfoClicked() {
                        listener.onSubscriptionMoreInfoClicked(subscription)
                    }

                    override fun onCheckChanged(isChecked: Boolean) {
                        if (isChecked) widgetMyBills.setDescription(subscription.info.checkedSubtitle)
                        else widgetMyBills.setDescription(subscription.info.subtitle)
                        listener.onSubscriptionChecked(subscription, isChecked)
                    }
                }

                if (subscription.checkBoxDisabled) {
                    widgetMyBills.disableCheckBox()
                } else {
                    if (!widgetMyBills.isChecked() && subscription.optIn) {
                        widgetMyBills.setChecked(subscription.optIn)
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
                widgetMyBills.hasMoreInfo(
                    fintechProduct.info.urlLink.isNotEmpty() ||
                            fintechProduct.info.tooltipText.isNotEmpty()
                )

                widgetMyBills.setAdditionalImage(fintechProduct.info.iconUrl)
                if (fintechProduct.info.iconUrl.isNotEmpty()) {
                    listener.onTebusMurahImpression(fintechProduct, position)
                } else listener.onCrossellImpression(fintechProduct, position)

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

                widgetMyBills.visibility = View.VISIBLE

                if (fintechProduct.checkBoxDisabled) {
                    widgetMyBills.disableCheckBox()
                } else {
                    if (!widgetMyBills.isChecked() && fintechProduct.optIn) {
                        widgetMyBills.setChecked(fintechProduct.optIn)
                    }
                }
            }
        }
    }
}

interface MyBillsActionListener {
    fun onSubscriptionChecked(fintechProduct: FintechProduct, isChecked: Boolean)
    fun onSubscriptionImpression(fintechProduct: FintechProduct)
    fun onSubscriptionMoreInfoClicked(fintechProduct: FintechProduct)
    fun onTebusMurahImpression(fintechProduct: FintechProduct, position: Int)
    fun onCrossellImpression(fintechProduct: FintechProduct, position: Int)
    fun onTebusMurahChecked(fintechProduct: FintechProduct, position: Int, isChecked: Boolean)
    fun onFintechProductChecked(fintechProduct: FintechProduct, isChecked: Boolean, position: Int)
    fun onFintechMoreInfoChecked(info: FintechProduct.FintechProductInfo)
}