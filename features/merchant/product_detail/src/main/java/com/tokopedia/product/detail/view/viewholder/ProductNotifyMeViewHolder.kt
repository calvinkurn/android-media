package com.tokopedia.product.detail.view.viewholder

import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductNotifyMeDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.partial_product_notify_me.view.*
import java.util.*
import java.util.concurrent.TimeUnit

class ProductNotifyMeViewHolder(view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductNotifyMeDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.partial_product_notify_me
        const val SECOND = 1000L
    }

    override fun bind(element: ProductNotifyMeDataModel) {
        if (element.campaignID.isNotEmpty() && !element.isUpcomingNplType()) {
            itemView.layout_notify_me?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
            itemView.layout_notify_me?.requestLayout()
            bindTitle(element)
            bindSubTitle(element)
            bindButton(element, listener.isOwner())
            bindListener(element, ComponentTrackDataModel(element.type, element.name, adapterPosition + 1))
        } else {
            hideContainer()
        }
    }

    private fun bindTitle(data: ProductNotifyMeDataModel) {
        itemView.product_notify_title?.showWithAction(data.campaignTypeName.isNotEmpty()) {
            it.text = data.campaignTypeName
        }
    }

    private fun TextView.showWithAction(shouldShow: Boolean, action: (TextView) -> Unit) {
        if (shouldShow) {
            action(this)
        } else {
            this.text = getString(R.string.notify_me_title)
        }
    }

    private fun bindSubTitle(data: ProductNotifyMeDataModel) {
        try {
            val now = System.currentTimeMillis()
            val startTime = data.startDate.toLongOrZero() * SECOND
            val startDate = Date(startTime)
            val dayLeft = TimeUnit.MILLISECONDS.toDays(startTime - now)
            val delta = startDate.time - startTime

            itemView.layout_notify_me?.visible()
            when {
                dayLeft < 0 -> {
                    hideContainer()
                }
                dayLeft < 1 -> {
                    itemView.notify_count_down?.setup(delta, startDate) {
                        listener.refreshPage()
                    }
                    itemView.notify_count_down?.visible()
                    itemView.product_notify_subtitle?.text = getString(R.string.notify_me_subtitle_main)
                }
                else -> {
                    itemView.notify_count_down?.gone()
                    itemView.product_notify_subtitle?.text = MethodChecker.fromHtml(
                            getString(R.string.notify_me_subtitle, dayLeft.toInt().toString())
                    )
                }
            }
        } catch (ex: Exception) {
            hideContainer()
        }
    }

    private fun hideContainer() {
        itemView.layout_notify_me?.layoutParams?.height = 0
        itemView.layout_notify_me?.requestLayout()

    }

    private fun bindButton(data: ProductNotifyMeDataModel, isShopOwner: Boolean) = with(itemView) {
        btn_notify_me?.showWithCondition(!isShopOwner)
        when (data.notifyMe) {
            true -> {
                btn_notify_me?.buttonType = UnifyButton.Type.ALTERNATE
                btn_notify_me?.text = getString(R.string.notify_me_active)
            }
            false -> {
                btn_notify_me?.buttonType = UnifyButton.Type.MAIN
                btn_notify_me?.text = getString(R.string.notify_me_inactive)
            }
        }
        btn_notify_me?.maxLines = 1
        btn_notify_me?.ellipsize = TextUtils.TruncateAt.END
    }

    private fun bindListener(data: ProductNotifyMeDataModel, componentTrackDataModel: ComponentTrackDataModel) {
        itemView.btn_notify_me?.setOnClickListener {
            listener.onNotifyMeClicked(data, componentTrackDataModel)
        }
    }

    override fun bind(element: ProductNotifyMeDataModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) {
            return
        }
        when (payloads[0] as Int) {
            ProductDetailConstant.PAYLOAD_NOTIFY_ME -> bindButton(element, listener.isOwner())
        }
    }
}
