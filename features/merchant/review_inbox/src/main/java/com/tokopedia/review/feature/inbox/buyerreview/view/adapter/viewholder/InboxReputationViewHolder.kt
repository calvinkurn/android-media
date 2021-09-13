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
import com.tokopedia.unifycomponents.NotificationUnify.Companion.COLOR_PRIMARY
import com.tokopedia.unifycomponents.NotificationUnify.Companion.COLOR_SECONDARY
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by nisie on 8/19/17.
 */
class InboxReputationViewHolder constructor(
    context: Context?,
    itemView: View,
    viewListener: InboxReputation.View
) : AbstractViewHolder<InboxReputationItemUiModel?>(itemView) {
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
    public override fun bind(element: InboxReputationItemUiModel) {
        name.setText(MethodChecker.fromHtml(element.getRevieweeName()))
        date.setText(getDate(element.getCreateTime()))
        invoice.setText(element.getInvoice())
        ImageHandler.loadImageCircle2(avatar.getContext(), avatar, element.getRevieweePicture())
        setDeadline(element)
        setReputation(element)
        setAction(element)
        setUnreadNotification(element)
        mainView.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                viewListener.onGoToDetail(
                    element.getReputationId(),
                    element.getInvoice(),
                    element.getCreateTime(),
                    element.getRevieweeName(),
                    element.getRevieweePicture(),
                    element.getReputationDataUiModel(),
                    getTextDeadline(element),
                    getAdapterPosition(),
                    element.getRole()
                )
            }
        })
        action.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(view: View) {
                viewListener.onGoToDetail(
                    element.getReputationId(),
                    element.getInvoice(),
                    element.getCreateTime(),
                    element.getRevieweeName(),
                    element.getRevieweePicture(),
                    element.getReputationDataUiModel(),
                    getTextDeadline(element),
                    getAdapterPosition(),
                    element.getRole()
                )
            }
        })
    }

    private fun setUnreadNotification(element: InboxReputationItemUiModel) {
        if (isInboxUnified()) {
            unreadNotification.setNotification("", NotificationUnify.NONE_TYPE, COLOR_SECONDARY)
        } else {
            unreadNotification.setNotification("", NotificationUnify.NONE_TYPE, COLOR_PRIMARY)
        }
        if (element.getReputationDataUiModel().isShowBookmark()) {
            unreadNotification.setVisibility(View.VISIBLE)
        } else {
            unreadNotification.setVisibility(View.GONE)
        }
    }

    private fun getDate(createTime: String?): String {
        return TimeConverter.generateTimeYearly(createTime)
    }

    private fun setAction(inboxReputationItemUiModel: InboxReputationItemUiModel) {
        action.setText(inboxReputationItemUiModel.getReputationDataUiModel().getActionMessage())
    }

    private fun setReputation(element: InboxReputationItemUiModel) {
        if (element.getRole() == InboxReputationItemUiModel.Companion.ROLE_BUYER) {
            userReputationView.setVisibility(View.VISIBLE)
            shopReputationView.setVisibility(View.GONE)
            userReputationView.setValue(
                element.getRevieweeBadgeCustomerUiModel().getPositivePercentage(),
                element.getRevieweeBadgeCustomerUiModel().getNoReputation() == 1,
                element.getRevieweeBadgeCustomerUiModel().getPositive(),
                element.getRevieweeBadgeCustomerUiModel().getNeutral(),
                element.getRevieweeBadgeCustomerUiModel().getNegative()
            )
        } else {
            userReputationView.setVisibility(View.GONE)
            shopReputationView.setVisibility(View.VISIBLE)
            shopReputationView.setValue(
                element.getRevieweeBadgeSellerUiModel().getReputationBadge().getSet(),
                element.getRevieweeBadgeSellerUiModel().getReputationBadge().getLevel(),
                element.getRevieweeBadgeSellerUiModel().getScore().toString()
            )
        }
    }

    private fun setDeadline(element: InboxReputationItemUiModel) {
        if (element.getReputationDataUiModel().isShowLockingDeadline()) {
            deadline.setVisibility(View.VISIBLE)
            textDeadline.setVisibility(View.VISIBLE)
            setIconDeadline(deadline, element.getReputationDaysLeft())
        } else {
            deadline.setVisibility(View.INVISIBLE)
            textDeadline.setVisibility(View.INVISIBLE)
        }
    }

    private fun getTextDeadline(element: InboxReputationItemUiModel): String {
        return (context!!.getString(R.string.deadline_prefix)
                + " " + element.getReputationDaysLeft() + " " +
                context.getString(R.string.deadline_suffix))
    }

    private fun setIconDeadline(deadline: Typography, reputationDaysLeft: String?) {
        deadline.setText(reputationDaysLeft + " " + context!!.getString(R.string.deadline_suffix))
        val background: Drawable = MethodChecker.getDrawable(context, R.drawable.custom_label)
        when (reputationDaysLeft) {
            "1" -> background.setColorFilter(
                PorterDuffColorFilter(
                    MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_R600
                    ),
                    PorterDuff.Mode.MULTIPLY
                )
            )
            "2" -> background.setColorFilter(
                PorterDuffColorFilter(
                    MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_Y300
                    ),
                    PorterDuff.Mode.MULTIPLY
                )
            )
            else -> background.setColorFilter(
                PorterDuffColorFilter(
                    MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_B400
                    ),
                    PorterDuff.Mode.MULTIPLY
                )
            )
        }
        MethodChecker.setBackground(deadline, background)
    }

    companion object {
        @LayoutRes
        val LAYOUT: Int = R.layout.inbox_reputation_item
    }

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
}