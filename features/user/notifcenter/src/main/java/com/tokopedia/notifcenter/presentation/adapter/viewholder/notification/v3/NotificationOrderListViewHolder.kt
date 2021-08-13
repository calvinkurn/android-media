package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.graphics.drawable.Drawable
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.orderlist.NotifOrderListUiModel
import com.tokopedia.notifcenter.listener.v3.NotificationItemListener
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.payload.PayloadOrderList
import com.tokopedia.notifcenter.util.view.ShadowGenerator
import com.tokopedia.notifcenter.widget.ItemOrderListLinearLayout

class NotificationOrderListViewHolder constructor(
        itemView: View?,
        private val notificationItemListener: NotificationItemListener?
) : AbstractViewHolder<NotifOrderListUiModel>(itemView) {

    private val firstCard: ItemOrderListLinearLayout? = itemView?.findViewById(R.id.ll_first_card)
    private val secondCard: ItemOrderListLinearLayout? = itemView?.findViewById(R.id.ll_second_card)
    private val titleWithIconMargin = itemView?.context?.resources?.getDimension(
            com.tokopedia.unifyprinciples.R.dimen.unify_space_4
    )
    private val titleWithoutIconMargin = itemView?.context?.resources?.getDimension(
            com.tokopedia.unifyprinciples.R.dimen.unify_space_8
    )
    private val bgFirst = ShadowGenerator.generateBackgroundWithShadow(
            firstCard,
            com.tokopedia.unifyprinciples.R.color.Unify_N0,
            com.tokopedia.unifyprinciples.R.dimen.unify_space_8,
            com.tokopedia.unifyprinciples.R.dimen.unify_space_8,
            com.tokopedia.unifyprinciples.R.dimen.unify_space_8,
            com.tokopedia.unifyprinciples.R.dimen.unify_space_8,
            R.color.notifcenter_dms_order_list_card_shadow,
            R.dimen.notif_dp_6,
            R.dimen.notif_dp_6
    )
    private val bgSecond = ShadowGenerator.generateBackgroundWithShadow(
            secondCard,
            com.tokopedia.unifyprinciples.R.color.Unify_N0,
            com.tokopedia.unifyprinciples.R.dimen.unify_space_8,
            com.tokopedia.unifyprinciples.R.dimen.unify_space_8,
            com.tokopedia.unifyprinciples.R.dimen.unify_space_8,
            com.tokopedia.unifyprinciples.R.dimen.unify_space_8,
            R.color.notifcenter_dms_order_list_card_shadow,
            R.dimen.notif_dp_6,
            R.dimen.notif_dp_6
    )

    override fun bind(element: NotifOrderListUiModel, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        when (payloads.first()) {
            is PayloadOrderList -> {
                bind(element)
            }
        }
    }

    override fun bind(element: NotifOrderListUiModel) {
        bindCard(element, 0, firstCard)
        bindCardBackground(firstCard, bgFirst)
        bindCardTitleMargin(firstCard)
        bindCardWidthRatio(firstCard, 6.75f)
        bindCard(element, 1, secondCard)
        bindCardBackground(secondCard, bgSecond)
        bindCardTitleMargin(secondCard)
        bindCardWidthRatio(secondCard, 3.25f)
    }

    private fun bindCardWidthRatio(
            cardLayout: ItemOrderListLinearLayout?,
            buyerWidthRatio: Float
    ) {
        val horizontalWeight = if (notificationItemListener?.amISeller() == true) {
            5.0f
        } else {
            buyerWidthRatio
        }
        val cardLp = cardLayout?.layoutParams as? ConstraintLayout.LayoutParams ?: return
        cardLp.horizontalWeight = horizontalWeight
        cardLayout.layoutParams = cardLp
    }

    private fun bindCard(
            element: NotifOrderListUiModel,
            cardIndex: Int,
            cardLayout: ItemOrderListLinearLayout?
    ) {
        val order = element.list.getOrNull(cardIndex)
        if (order == null) {
            cardLayout?.hide()
        } else {
            cardLayout?.show()
            cardLayout?.bindItem(order) {
                notificationItemListener?.trackClickOrderListItem(order)
            }
        }
    }

    private fun bindCardBackground(cardLayout: ItemOrderListLinearLayout?, drawable: Drawable?) {
        if (cardLayout?.isVisible == true) {
            cardLayout.background = drawable
        }
    }

    private fun bindCardTitleMargin(cardLayout: ItemOrderListLinearLayout?) {
        if (cardLayout?.hasVisibleIcon() == true) {
            cardLayout.setTitleMarginStart(titleWithIconMargin)
        } else {
            cardLayout?.setTitleMarginStart(titleWithoutIconMargin)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_notification_order_list
    }
}