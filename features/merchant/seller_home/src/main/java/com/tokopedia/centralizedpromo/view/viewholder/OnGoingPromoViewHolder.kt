package com.tokopedia.centralizedpromo.view.viewholder

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoTracking
import com.tokopedia.centralizedpromo.view.model.OnGoingPromoUiModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerhome.R.layout.centralized_promo_item_on_going_promo
import com.tokopedia.sellerhome.databinding.CentralizedPromoItemOnGoingPromoBinding

class OnGoingPromoViewHolder(
    view: View?
) : AbstractViewHolder<OnGoingPromoUiModel>(view) {

    companion object {
        val RES_LAYOUT = centralized_promo_item_on_going_promo
    }

    private val binding by lazy {
        CentralizedPromoItemOnGoingPromoBinding.bind(itemView)
    }

    override fun bind(element: OnGoingPromoUiModel) {
        binding.run {
            tvOnGoingPromoTitle.text = element.title
            tvOnGoingPromoStatus.text = element.status.text
            tvOnGoingPromoCount.text = element.status.count.toString()
        }

        initListeners(element)
    }

    private fun initListeners(element: OnGoingPromoUiModel) {
        binding.run {
            tvOnGoingPromoTitle.setOnClickListener {
                onStatusClicked(
                    element.status.url,
                    element.title
                )
            }
            ivCaret.setOnClickListener {
                onStatusClicked(
                    element.status.url,
                    element.title
                )
            }
            tvOnGoingPromoStatus.setOnClickListener {
                onFooterClicked(element.footer.url, element.title)
            }
            tvOnGoingPromoCount.setOnClickListener {
                onFooterClicked(element.footer.url, element.title)
            }

            root.addOnImpressionListener(element.impressHolder) {
                CentralizedPromoTracking.sendImpressionOnGoingPromoStatus(
                    widgetName = element.title
                )
            }
        }
    }

    private fun onStatusClicked(appLink: String, title: String) {
        if (openApplink(appLink)) {
            CentralizedPromoTracking.sendClickOnGoingPromoStatus(
                widgetName = title
            )
        }
    }

    private fun onFooterClicked(applink: String, title: String) {
        if (openApplink(applink)) {
            CentralizedPromoTracking.sendClickOnGoingPromoFooter(
                widgetName = title
            )
        }
    }

    private fun openApplink(url: String): Boolean = RouteManager.route(itemView.context, url)

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