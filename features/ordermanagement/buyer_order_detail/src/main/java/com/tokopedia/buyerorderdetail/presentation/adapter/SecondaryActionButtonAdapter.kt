package com.tokopedia.buyerorderdetail.presentation.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.kotlin.extensions.view.orZero
import kotlinx.android.synthetic.main.item_buyer_order_detail_secondary_action_button.view.*

class SecondaryActionButtonAdapter(private val listener: ActionButtonClickListener) : RecyclerView.Adapter<SecondaryActionButtonAdapter.ViewHolder>() {

    private var secondaryActionButtons: List<ActionButtonsUiModel.ActionButton> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_buyer_order_detail_secondary_action_button, parent, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return secondaryActionButtons.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(secondaryActionButtons.getOrNull(position))
    }

    fun setSecondaryActionButtons(secondaryActionButtons: List<ActionButtonsUiModel.ActionButton>) {
        this.secondaryActionButtons = secondaryActionButtons
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View, private val listener: ActionButtonClickListener) : RecyclerView.ViewHolder(itemView) {
        fun bind(button: ActionButtonsUiModel.ActionButton?) {
            itemView.tvBuyerOrderDetailSecondaryActionButton.text = button?.label.orEmpty()
            itemView.tvBuyerOrderDetailSecondaryActionButton.setOnClickListener {
                button?.let {
                    listener.onActionButtonClicked(false, button)
                }
            }
        }

        class ItemDivider(context: Context) : RecyclerView.ItemDecoration() {
            private val divider = MethodChecker.getDrawable(context, R.drawable.secondary_action_button_divider)
            private val margin = context.resources.getDimension(com.tokopedia.unifycomponents.R.dimen.layout_lvl2).toInt()

            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                val isLastItem: Boolean = parent.getChildAdapterPosition(view) == parent.adapter?.itemCount.orZero() - 1
                val layoutParams = view.layoutParams as RecyclerView.LayoutParams
                layoutParams.topMargin = margin
                if (!isLastItem) {
                    layoutParams.bottomMargin = margin
                }
            }

            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                val left = parent.paddingLeft
                val right = parent.width - parent.paddingRight
                val childCount = parent.childCount
                for (i in 0 until childCount - 1) {
                    val child = parent.getChildAt(i)
                    val layoutParams = child.layoutParams as RecyclerView.LayoutParams
                    val top = child.bottom + layoutParams.bottomMargin
                    val bottom = top + divider.intrinsicHeight
                    divider.setBounds(left, top, right, bottom)
                    divider.draw(c)
                }
            }
        }
    }
}