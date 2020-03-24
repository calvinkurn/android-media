package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductNotifyMeDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.item_rates_estimation_blackbox.view.*
import kotlinx.android.synthetic.main.partial_product_notify_me.view.*
import java.sql.Timestamp
import java.util.*
import java.util.concurrent.TimeUnit

class ProductNotifyMeViewHolder(view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductNotifyMeDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.partial_product_notify_me
        const val SOURCE = "campaign"
        const val SERVER_TIME_OFFSET: Long = 0
    }

    override fun bind(element: ProductNotifyMeDataModel) {
        if (element != null) {
            bindSubTitle(element)
            bindButton(element)
            bindListener(element)
        }
    }

    private fun bindSubTitle(data: ProductNotifyMeDataModel) {
        try {
            val now = System.currentTimeMillis()
            val stamp = Timestamp(System.currentTimeMillis())
            val startDate = Date(stamp.time)
            val dayLeft = TimeUnit.MICROSECONDS.toDays(now - data.startDate.toLong())
            itemView.layout_notify_me.visible()
            when {
                dayLeft < 1 -> itemView.notify_count_down.setup(SERVER_TIME_OFFSET, startDate) {
                    itemView.notify_count_down.visible()
                    itemView.product_notify_subtitle.text = getString(R.string.notify_me_subtitle)
                }
                dayLeft < 2 -> {
                    itemView.notify_count_down.gone()
                    itemView.product_notify_subtitle.text = MethodChecker.fromHtml(
                            getString(R.string.notify_me_subtitle, "<b>2 hari lagi</b>")
                    )
                }
                dayLeft < 3 -> {
                    itemView.notify_count_down.gone()
                    itemView.product_notify_subtitle.text = MethodChecker.fromHtml(
                            getString(R.string.notify_me_subtitle, "<b>3 hari lagi</b>")
                    )
                }
                dayLeft < 4 -> {
                    itemView.notify_count_down.gone()
                    itemView.product_notify_subtitle.text = MethodChecker.fromHtml(
                            getString(R.string.notify_me_subtitle, "<b>4 hari lagi</b>")
                    )
                }
                else -> {
                    itemView.layout_notify_me.gone()
                }
            }
        } catch (ex: Exception) {
            itemView.layout_notify_me.gone()
        }
    }

    private fun bindButton(data: ProductNotifyMeDataModel) {
        when (data.notifyMe) {
            true -> {
                itemView.btn_notify_me.buttonType = UnifyButton.Type.ALTERNATE
                itemView.btn_notify_me.text = getString(R.string.notify_me_active)
            }
            false -> {
                itemView.btn_notify_me.buttonType = UnifyButton.Type.MAIN
                itemView.btn_notify_me.text = getString(R.string.notify_me_inactive)
            }

        }
    }

    private fun bindListener(data: ProductNotifyMeDataModel) {
//        listener.onNotifyMeClicked(data.campaignID.toInt(), SOURCE)
    }
}
