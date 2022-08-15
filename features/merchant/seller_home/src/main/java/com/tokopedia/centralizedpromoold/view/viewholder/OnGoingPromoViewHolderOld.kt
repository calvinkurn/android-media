package com.tokopedia.centralizedpromoold.view.viewholder

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.centralizedpromoold.view.model.OnGoingPromoUiModelOld
import com.tokopedia.centralizedpromoold.analytic.CentralizedPromoTrackingOld
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.R.layout.centralized_promo_item_on_going_promo_old
import com.tokopedia.sellerhome.databinding.CentralizedPromoItemOnGoingPromoOldBinding

class OnGoingPromoViewHolderOld(private val onProductCouponOngoingPromoClicked: (String) -> Unit,
                                view: View?) : AbstractViewHolder<OnGoingPromoUiModelOld>(view) {

    companion object {
        val RES_LAYOUT = centralized_promo_item_on_going_promo_old
    }

    private val binding by lazy {
        CentralizedPromoItemOnGoingPromoOldBinding.bind(itemView)
    }

    override fun bind(element: OnGoingPromoUiModelOld) {
        binding.run {
            tvOnGoingPromoTitle.text = element.title
            tvOnGoingPromoStatus.text = element.status.text
            tvOnGoingPromoCount.text = element.status.count.toString()
            tvSeePastPromotion.text = element.footer.text
        }

        initListeners(element)
    }

    private fun initListeners(element: OnGoingPromoUiModelOld) {
        binding.run {
            tvOnGoingPromoTitle.setOnClickListener {
                onStatusClicked(
                    element.status.url,
                    element.title,
                    element.status.count,
                    element.status.text
                )
            }
            tvOnGoingPromoStatus.setOnClickListener {
                onStatusClicked(
                    element.status.url,
                    element.title,
                    element.status.count,
                    element.status.text
                )
            }
            tvOnGoingPromoCount.setOnClickListener {
                onStatusClicked(
                    element.status.url,
                    element.title,
                    element.status.count,
                    element.status.text
                )
            }
            tvSeePastPromotion.setOnClickListener {
                onFooterClicked(element.footer.url, element.title, element.footer.text)
            }
            ivCaret.setOnClickListener {
                onFooterClicked(element.footer.url, element.title, element.footer.text)
            }
            root.addOnImpressionListener(element.impressHolder) {
                CentralizedPromoTrackingOld.sendImpressionOnGoingPromoStatus(
                    widgetName = element.title,
                    value = element.status.count,
                    state = element.status.text
                )
            }
        }
    }

    private fun onStatusClicked(appLink: String, title: String, value: Int, state: String) {
        if (openApplink(appLink)) {
            if (title.isProductCouponCard()) {
                onProductCouponOngoingPromoClicked(title)
            } else {
                CentralizedPromoTrackingOld.sendClickOnGoingPromoStatus(
                    widgetName = title,
                    value = value,
                    state = state
                )
            }
        }
    }

    private fun onFooterClicked(applink: String, title: String, footerText: String) {
        if (openApplink(applink)) {
            if (title.isProductCouponCard()) {
                onProductCouponOngoingPromoClicked(title)
            } else {
                CentralizedPromoTrackingOld.sendClickOnGoingPromoFooter(
                    widgetName = title,
                    footerText = footerText
                )
            }
        }
    }

    private fun openApplink(url: String): Boolean = RouteManager.route(itemView.context, url)

    private fun String.isProductCouponCard(): Boolean {
        return this.equals(
            getString(R.string.centralized_promo_promo_creation_voucher_product_title),
            ignoreCase = true
        )
    }

    class ItemDecoration(private val margin: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val isFirstItem: Boolean = parent.getChildAdapterPosition(view) == 0
            val isLastItem: Boolean =
                parent.getChildAdapterPosition(view) == parent.adapter?.itemCount.orZero() - 1
            if (isFirstItem) {
                outRect.left = margin
            } else if (isLastItem) {
                outRect.right = margin
            }
        }
    }
}