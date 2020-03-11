package com.tokopedia.centralizedpromo.view.viewholder

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.centralizedpromo.view.model.OnGoingPromoUiModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerhome.R.layout.centralized_promo_item_on_going_promo
import kotlinx.android.synthetic.main.centralized_promo_item_on_going_promo.view.*

class OnGoingPromoViewHolder(view: View?) : AbstractViewHolder<OnGoingPromoUiModel>(view) {

    companion object {
        val RES_LAYOUT = centralized_promo_item_on_going_promo
    }

    override fun bind(element: OnGoingPromoUiModel) {
        with(itemView) {
            tvOnGoingPromoTitle.text = element.title
            tvOnGoingPromoStatus.text = element.status.text
            tvOnGoingPromoCount.text = element.status.count.toString()
            tvSeePastPromotion.text = element.footer.text
        }

        initListeners(element)
    }

    private fun initListeners(element: OnGoingPromoUiModel) {
        with(itemView) {
            tvOnGoingPromoTitle.setOnClickListener {
                openApplink(element.status.url)
            }
            tvSeePastPromotion.setOnClickListener {
                openApplink(element.footer.url)
            }
            ivCaret.setOnClickListener {
                openApplink(element.footer.url)
            }
        }
    }

    private fun openApplink(url: String) {
        with(itemView) {
            RouteManager.route(context, url)
        }
    }

    class ItemDecoration(private val margin: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val isFirstItem: Boolean = parent.getChildAdapterPosition(view) == 0
            val isLastItem: Boolean = parent.getChildAdapterPosition(view) == parent.adapter?.itemCount.orZero() - 1
            if (isFirstItem) {
                outRect.left = margin
            } else if (isLastItem) {
                outRect.right = margin
            }
        }
    }
}