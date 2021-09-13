package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.inboxdetail

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.ReputationAdapter
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.ReputationAdapter.ReputationListener
import com.tokopedia.review.feature.inbox.buyerreview.view.customview.ShopReputationView
import com.tokopedia.review.feature.inbox.buyerreview.view.customview.UserReputationView
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailHeaderUiModel
import com.tokopedia.review.inbox.R
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by nisie on 8/19/17.
 */
class InboxReputationDetailHeaderViewHolder(
    itemView: View,
    private val reputationListener: ReputationListener
) : AbstractViewHolder<InboxReputationDetailHeaderUiModel?>(itemView) {
    var userAvatar: ImageView
    var name: Typography
    var userReputationView: UserReputationView
    var shopReputationView: ShopReputationView
    var deadlineLayout: View
    var deadline: Typography
    var lockedLayout: View
    var lockedText: Typography
    var promptMessage: Typography
    var favoriteButton: View
    var favoriteText: Typography
    var changeButton: Typography
    var smiley: RecyclerView
    var opponentSmileyText: Typography
    var opponentSmiley: ImageView
    var adapter: ReputationAdapter
    var gridLayout: GridLayoutManager
    var linearLayoutManager: LinearLayoutManager
    var context: Context
    override fun bind(element: InboxReputationDetailHeaderUiModel) {
        ImageHandler.loadImageCircle2(userAvatar.context, userAvatar, element.avatarImage)
        userAvatar.setOnClickListener(View.OnClickListener { goToInfoPage(element) })
        name.text = MethodChecker.fromHtml(element.name)
        name.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                goToInfoPage(element)
            }
        })
        setReputation(element)
        if (!TextUtils.isEmpty(element.deadline) && element.reputationDataUiModel
                .isShowLockingDeadline
        ) {
            deadline.text = element.deadline
            deadlineLayout.visibility = View.VISIBLE
        } else {
            deadlineLayout.visibility = View.GONE
        }
        if (element.reputationDataUiModel.isAutoScored) {
            lockedLayout.visibility = View.VISIBLE
            lockedText.setText(R.string.review_auto_scored)
            promptMessage.text = context.getString(R.string.your_scoring)
            smiley.layoutManager = linearLayoutManager
            setSmiley(element, adapter)
        } else if ((element.reputationDataUiModel.isLocked
                    && element.reputationDataUiModel.isInserted)
        ) {
            lockedLayout.visibility = View.VISIBLE
            lockedText.setText(R.string.review_is_saved)
            promptMessage.text = context.getString(R.string.your_scoring)
            smiley.layoutManager = linearLayoutManager
            setSmiley(element, adapter)
        } else if (element.reputationDataUiModel.isLocked) {
            lockedLayout.visibility = View.VISIBLE
            lockedText.setText(R.string.locked_reputation)
            promptMessage.text = context.getString(R.string.your_scoring)
            smiley.layoutManager = linearLayoutManager
            adapter.showLockedSmiley()
        } else if (element.reputationDataUiModel.isInserted) {
            lockedLayout.visibility = View.GONE
            promptMessage.text = context.getString(R.string.your_scoring)
            smiley.layoutManager = linearLayoutManager
            setSmiley(element, adapter)
        } else {
            smiley.layoutManager = gridLayout
            adapter.showAllSmiley()
            promptMessage.text = MethodChecker.fromHtml(getPromptText(element))
        }
        if (element.reputationDataUiModel.isEditable) {
            changeButton.visibility = View.VISIBLE
            changeButton.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    if ((changeButton.text == context.getString(R.string.change))) {
                        adapter.showChangeSmiley(element.reputationDataUiModel.reviewerScore)
                        changeButton.text = context.getString(R.string.title_cancel)
                    } else {
                        setSmiley(element, adapter)
                        changeButton.text = context.getString(R.string.change)
                    }
                }
            })
        } else changeButton.visibility = View.GONE
        setSmileyOpponent(element)
    }

    private fun goToInfoPage(element: InboxReputationDetailHeaderUiModel) {
        if (element.role == InboxReputationItemUiModel.Companion.ROLE_SELLER) {
            reputationListener.onGoToShopDetail(element.shopId)
        } else {
            reputationListener.onGoToPeopleProfile(element.userId)
        }
    }

    private fun setSmileyOpponent(element: InboxReputationDetailHeaderUiModel) {
        opponentSmileyText.text = getOpponentSmileyPromptText(element)
        if ((!element.reputationDataUiModel.isShowRevieweeScore
                    && element.reputationDataUiModel.revieweeScore != NO_REPUTATION)
        ) {
            ImageHandler.loadImageWithIdWithoutPlaceholder(
                opponentSmiley,
                R.drawable.reputation_ic_done_24dp
            )
        } else {
            when (element.reputationDataUiModel.revieweeScore) {
                NO_REPUTATION -> ImageHandler.loadImageWithIdWithoutPlaceholder(
                    opponentSmiley,
                    R.drawable.review_ic_smiley_empty
                )
                SMILEY_BAD -> ImageHandler.loadImageWithIdWithoutPlaceholder(
                    opponentSmiley,
                    R.drawable.review_ic_smiley_bad
                )
                SMILEY_NEUTRAL -> ImageHandler.loadImageWithIdWithoutPlaceholder(
                    opponentSmiley,
                    R.drawable.review_ic_smiley_neutral
                )
                SMILEY_GOOD -> ImageHandler.loadImageWithIdWithoutPlaceholder(
                    opponentSmiley,
                    R.drawable.review_ic_smiley_good
                )
            }
        }
    }

    private fun getOpponentSmileyPromptText(element: InboxReputationDetailHeaderUiModel): String {
        return if (element.reputationDataUiModel.revieweeScore == NO_REPUTATION) if (element.role == InboxReputationItemUiModel.Companion.ROLE_SELLER) context.getString(
            R.string.seller_has_not_review
        ) else context.getString(R.string.buyer_has_not_review) else if (element.role == InboxReputationItemUiModel.Companion.ROLE_SELLER) context.getString(
            R.string.score_from_seller
        ) else context.getString(R.string.score_from_buyer)
    }

    private fun setSmiley(element: InboxReputationDetailHeaderUiModel, adapter: ReputationAdapter) {
        changeButton.text = context.getString(R.string.change)
        when (element.reputationDataUiModel.reviewerScore) {
            SMILEY_BAD -> adapter.showSmileyBad()
            SMILEY_NEUTRAL -> adapter.showSmileyNeutral()
            SMILEY_GOOD -> adapter.showSmileyGood()
        }
    }

    private fun getPromptText(element: InboxReputationDetailHeaderUiModel): String {
        return context.getString(R.string.reputation_prompt) + " " + element.name + "?"
    }

    fun setReputation(element: InboxReputationDetailHeaderUiModel) {
        if (element.role == InboxReputationItemUiModel.Companion.ROLE_BUYER) {
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
                element.revieweeBadgeSellerUiModel.reputationBadge.set,
                element.revieweeBadgeSellerUiModel.reputationBadge.level,
                element.revieweeBadgeSellerUiModel.score.toString()
            )
        }
    }

    companion object {
        private val NO_REPUTATION = 0
        val SMILEY_BAD = -1
        val SMILEY_NEUTRAL = 1
        val SMILEY_GOOD = 2

        @LayoutRes
        val LAYOUT = R.layout.inbox_reputation_detail_header
    }

    init {
        context = itemView.context
        userAvatar = itemView.findViewById(R.id.user_avatar)
        name = itemView.findViewById<View>(R.id.name) as Typography
        userReputationView = itemView.findViewById(R.id.user_reputation)
        shopReputationView = itemView.findViewById(R.id.shop_reputation)
        deadline = itemView.findViewById<View>(R.id.deadline_text) as Typography
        deadlineLayout = itemView.findViewById(R.id.deadline)
        lockedLayout = itemView.findViewById(R.id.locked)
        lockedText = itemView.findViewById<View>(R.id.locked_text) as Typography
        promptMessage = itemView.findViewById<View>(R.id.prompt_text) as Typography
        favoriteButton = itemView.findViewById(R.id.favorite_button)
        favoriteText = itemView.findViewById<View>(R.id.favorite_text) as Typography
        changeButton = itemView.findViewById<View>(R.id.change_button) as Typography
        smiley = itemView.findViewById<View>(R.id.smiley) as RecyclerView
        opponentSmileyText = itemView.findViewById<View>(R.id.opponent_smiley_text) as Typography
        opponentSmiley = itemView.findViewById<View>(R.id.opponent_smiley) as ImageView
        adapter = ReputationAdapter.Companion.createInstance(itemView.context, reputationListener)
        gridLayout = GridLayoutManager(
            itemView.context, 3,
            LinearLayoutManager.VERTICAL, false
        )
        linearLayoutManager =
            LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        smiley.layoutManager = gridLayout
        smiley.adapter = adapter
    }
}