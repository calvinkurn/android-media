package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.*
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.review.common.presentation.InboxUnifiedRemoteConfig.isInboxUnified
import com.tokopedia.review.common.util.TimeConverter
import com.tokopedia.review.feature.inbox.buyerreview.view.customview.ShopReputationView
import com.tokopedia.review.feature.inbox.buyerreview.view.customview.UserReputationView
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputation
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel
import com.tokopedia.review.inbox.R
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifycomponents.NotificationUnify.COLOR_PRIMARY
import com.tokopedia.unifycomponents.NotificationUnify.COLOR_SECONDARY
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by nisie on 8/19/17.
 */
class InboxReputationViewHolder constructor(
    context: Context?,
    itemView: View,
    viewListener: InboxReputation.View
) : AbstractViewHolder<InboxReputationItemUiModel?>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT: Int = R.layout.inbox_reputation_item
    }

    private val viewListener: InboxReputation.View
    private val mainView: View
    private val textDeadline: Typography
    private val deadline: Typography
    private val invoice: Typography
    private val avatar: ImageView
    private val name: Typography
    private val userReputationView: UserReputationView
    private val shopReputationView: ShopReputationView
    private val date: Typography
    private val action: Typography
    private val unreadNotification: NotificationUnify
    private val context: Context?

    init {
        mainView = itemView.findViewById(R.id.main_view)
        textDeadline = itemView.findViewById(R.id.deadline_text)
        deadline = itemView.findViewById(R.id.label_deadline)
        invoice = itemView.findViewById(R.id.invoice)
        avatar = itemView.findViewById(R.id.avatar)
        name = itemView.findViewById(R.id.name)
        userReputationView = itemView.findViewById(R.id.user_reputation)
        shopReputationView = itemView.findViewById(R.id.shop_reputation)
        date = itemView.findViewById(R.id.date)
        action = itemView.findViewById(R.id.action)
        unreadNotification = itemView.findViewById(R.id.unread_notif)
        this.viewListener = viewListener
        this.context = context
    }

    override fun bind(element: InboxReputationItemUiModel) {
        name.text = MethodChecker.fromHtml(element.revieweeName)
        date.text = getDate(element.createTime)
        invoice.text = element.invoice
        ImageHandler.loadImageCircle2(avatar.context, avatar, element.revieweePicture)
        setDeadline(element)
        setReputation(element)
        setAction(element)
        setUnreadNotification(element)
        mainView.setOnClickListener {
            viewListener.onGoToDetail(
                element.reputationId,
                element.invoice,
                element.createTime,
                element.revieweeName,
                element.revieweePicture,
                element.reputationDataUiModel,
                getTextDeadline(element),
                adapterPosition,
                element.role
            )
        }
        action.setOnClickListener {
            viewListener.onGoToDetail(
                element.reputationId,
                element.invoice,
                element.createTime,
                element.revieweeName,
                element.revieweePicture,
                element.reputationDataUiModel,
                getTextDeadline(element),
                adapterPosition,
                element.role
            )
        }
    }

    private fun setUnreadNotification(element: InboxReputationItemUiModel) {
        if (isInboxUnified()) {
            unreadNotification.setNotification("", NotificationUnify.NONE_TYPE, COLOR_SECONDARY)
        } else {
            unreadNotification.setNotification("", NotificationUnify.NONE_TYPE, COLOR_PRIMARY)
        }
        if (element.reputationDataUiModel.isShowBookmark) {
            unreadNotification.visibility = View.VISIBLE
        } else {
            unreadNotification.visibility = View.GONE
        }
    }

    private fun getDate(createTime: String?): String {
        return TimeConverter.generateTimeYearly(createTime)
    }

    private fun setAction(inboxReputationItemUiModel: InboxReputationItemUiModel) {
        action.text = inboxReputationItemUiModel.reputationDataUiModel.actionMessage
    }

    private fun setReputation(element: InboxReputationItemUiModel) {
        if (element.role == InboxReputationItemUiModel.ROLE_BUYER) {
            userReputationView.visibility = View.VISIBLE
            shopReputationView.visibility = View.GONE
            userReputationView.setValue(
                element.revieweeBadgeCustomerUiModel.positivePercentage,
                element.revieweeBadgeCustomerUiModel.noReputation == 1,
                element.revieweeBadgeCustomerUiModel.positive,
                element.revieweeBadgeCustomerUiModel.neutral,
                element.revieweeBadgeCustomerUiModel.negative
            )
        } else {
            userReputationView.visibility = View.GONE
            shopReputationView.visibility = View.VISIBLE
            shopReputationView.setValue(
                element.revieweeBadgeSellerUiModel.reputationBadge?.set,
                element.revieweeBadgeSellerUiModel.reputationBadge?.level,
                element.revieweeBadgeSellerUiModel.score.toString()
            )
        }
    }

    private fun setDeadline(element: InboxReputationItemUiModel) {
        if (element.reputationDataUiModel.isShowLockingDeadline) {
            deadline.visibility = View.VISIBLE
            textDeadline.visibility = View.VISIBLE
            setIconDeadline(deadline, element.reputationDaysLeft)
        } else {
            deadline.visibility = View.INVISIBLE
            textDeadline.visibility = View.INVISIBLE
        }
    }

    private fun getTextDeadline(element: InboxReputationItemUiModel): String {
        return (context!!.getString(R.string.deadline_prefix)
                + " " + element.reputationDaysLeft + " " +
                context.getString(R.string.deadline_suffix))
    }

    private fun setIconDeadline(deadline: Typography, reputationDaysLeft: String?) {
        deadline.text = reputationDaysLeft + " " + context!!.getString(R.string.deadline_suffix)
        val background: Drawable = MethodChecker.getDrawable(context, R.drawable.custom_label)
        when (reputationDaysLeft) {
            "1" -> background.colorFilter = PorterDuffColorFilter(
                MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_R600
                ),
                PorterDuff.Mode.MULTIPLY
            )
            "2" -> background.colorFilter = PorterDuffColorFilter(
                MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_Y300
                ),
                PorterDuff.Mode.MULTIPLY
            )
            else -> background.colorFilter = PorterDuffColorFilter(
                MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_B400
                ),
                PorterDuff.Mode.MULTIPLY
            )
        }
        MethodChecker.setBackground(deadline, background)
    }
}