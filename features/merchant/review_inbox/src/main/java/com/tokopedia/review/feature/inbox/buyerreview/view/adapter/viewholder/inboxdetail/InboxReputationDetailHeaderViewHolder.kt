package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.inboxdetail

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
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
) : AbstractViewHolder<InboxReputationDetailHeaderUiModel>(itemView) {

    companion object {
        private const val NO_REPUTATION = 0
        const val SMILEY_BAD = -1
        const val SMILEY_NEUTRAL = 1
        const val SMILEY_GOOD = 2
        const val SPAN_COUNT = 3

        @LayoutRes
        val LAYOUT = R.layout.inbox_reputation_detail_header
    }

    private var userAvatar: ImageView? = itemView.findViewById(R.id.user_avatar)
    private var name: Typography? = itemView.findViewById(R.id.name)
    private var userReputationView: UserReputationView? =
        itemView.findViewById(R.id.user_reputation)
    private var shopReputationView: ShopReputationView? =
        itemView.findViewById(R.id.shop_reputation)
    private var deadlineLayout: View? = itemView.findViewById(R.id.deadline)
    private var deadline: Typography? = itemView.findViewById(R.id.deadline_text)
    private var lockedLayout: View? = itemView.findViewById(R.id.locked)
    private var lockedText: Typography? = itemView.findViewById(R.id.locked_text)
    private var promptMessage: Typography? = itemView.findViewById(R.id.prompt_text)
    private var changeButton: Typography? = itemView.findViewById(R.id.change_button)
    private var smiley: RecyclerView? = itemView.findViewById(R.id.smiley)
    private var opponentSmileyText: Typography? = itemView.findViewById(R.id.opponent_smiley_text)
    private var opponentSmiley: ImageView? = itemView.findViewById(R.id.opponent_smiley)
    private var adapter: ReputationAdapter =
        ReputationAdapter.createInstance(itemView.context, reputationListener)
    private var gridLayout: GridLayoutManager =
        GridLayoutManager(itemView.context, SPAN_COUNT, LinearLayoutManager.VERTICAL, false)
    private var linearLayoutManager: LinearLayoutManager =
        LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

    init {
        smiley?.apply {
            layoutManager = gridLayout
            adapter = this@InboxReputationDetailHeaderViewHolder.adapter
        }
    }

    override fun bind(element: InboxReputationDetailHeaderUiModel) {
        ImageHandler.loadImageCircle2(userAvatar?.context, userAvatar, element.avatarImage)
        userAvatar?.setOnClickListener { goToInfoPage(element) }
        name?.text = MethodChecker.fromHtml(element.name)
        name?.setOnClickListener { goToInfoPage(element) }
        setReputation(element)
        if (!TextUtils.isEmpty(element.deadline) && element.reputationDataUiModel
                .isShowLockingDeadline
        ) {
            deadline?.text = element.deadline
            deadlineLayout?.visibility = View.VISIBLE
        } else {
            deadlineLayout?.visibility = View.GONE
        }
        when {
            element.reputationDataUiModel.isAutoScored -> {
                lockedLayout?.visibility = View.VISIBLE
                lockedText?.setText(R.string.review_auto_scored)
                promptMessage?.text = itemView.context.getString(R.string.your_scoring)
                smiley?.layoutManager = linearLayoutManager
                setSmiley(element, adapter)
            }
            element.reputationDataUiModel.isLocked
                    && element.reputationDataUiModel.isInserted -> {
                lockedLayout?.visibility = View.VISIBLE
                lockedText?.setText(R.string.review_is_saved)
                promptMessage?.text = itemView.context.getString(R.string.your_scoring)
                smiley?.layoutManager = linearLayoutManager
                setSmiley(element, adapter)
            }
            element.reputationDataUiModel.isLocked -> {
                lockedLayout?.visibility = View.VISIBLE
                lockedText?.setText(R.string.locked_reputation)
                promptMessage?.text = itemView.context.getString(R.string.your_scoring)
                smiley?.layoutManager = linearLayoutManager
                adapter.showLockedSmiley()
            }
            element.reputationDataUiModel.isInserted -> {
                lockedLayout?.visibility = View.GONE
                promptMessage?.text = itemView.context.getString(R.string.your_scoring)
                smiley?.layoutManager = linearLayoutManager
                setSmiley(element, adapter)
            }
            else -> {
                smiley?.layoutManager = gridLayout
                adapter.showAllSmiley()
                promptMessage?.text = MethodChecker.fromHtml(getPromptText(element))
            }
        }
        if (element.reputationDataUiModel.isEditable) {
            changeButton?.visibility = View.VISIBLE
            changeButton?.setOnClickListener {
                if ((changeButton?.text == itemView.context.getString(R.string.change))) {
                    adapter.showChangeSmiley(element.reputationDataUiModel.reviewerScore)
                    changeButton?.text = itemView.context.getString(R.string.title_cancel)
                } else {
                    setSmiley(element, adapter)
                    changeButton?.text = itemView.context.getString(R.string.change)
                }
            }
        } else changeButton?.visibility = View.GONE
        setSmileyOpponent(element)
    }

    private fun goToInfoPage(element: InboxReputationDetailHeaderUiModel) {
        if (element.role == InboxReputationItemUiModel.ROLE_SELLER) {
            reputationListener.onGoToShopDetail(element.shopId)
        } else {
            reputationListener.onGoToPeopleProfile(element.userId)
        }
    }

    private fun setSmileyOpponent(element: InboxReputationDetailHeaderUiModel) {
        opponentSmileyText?.text = getOpponentSmileyPromptText(element)
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
        return if (element.reputationDataUiModel.revieweeScore == NO_REPUTATION) if (element.role == InboxReputationItemUiModel.ROLE_SELLER) itemView.context.getString(
            R.string.seller_has_not_review
        ) else itemView.context.getString(R.string.buyer_has_not_review) else if (element.role == InboxReputationItemUiModel.ROLE_SELLER) itemView.context.getString(
            R.string.score_from_seller
        ) else itemView.context.getString(R.string.score_from_buyer)
    }

    private fun setSmiley(element: InboxReputationDetailHeaderUiModel, adapter: ReputationAdapter) {
        changeButton?.text = itemView.context.getString(R.string.change)
        when (element.reputationDataUiModel.reviewerScore) {
            SMILEY_BAD -> adapter.showSmileyBad()
            SMILEY_NEUTRAL -> adapter.showSmileyNeutral()
            SMILEY_GOOD -> adapter.showSmileyGood()
        }
    }

    private fun getPromptText(element: InboxReputationDetailHeaderUiModel): String {
        return itemView.context.getString(R.string.reputation_prompt) + " " + element.name + "?"
    }

    fun setReputation(element: InboxReputationDetailHeaderUiModel) {
        if (element.role == InboxReputationItemUiModel.ROLE_BUYER) {
            userReputationView?.visibility = View.VISIBLE
            shopReputationView?.visibility = View.GONE
            userReputationView?.setValue(
                element.revieweeBadgeCustomerUiModel.positivePercentage,
                element.revieweeBadgeCustomerUiModel.noReputation == 1,
                element.revieweeBadgeCustomerUiModel.positive,
                element.revieweeBadgeCustomerUiModel.neutral,
                element.revieweeBadgeCustomerUiModel.negative
            )
        } else {
            userReputationView?.visibility = View.GONE
            shopReputationView?.visibility = View.VISIBLE
            shopReputationView?.setValue(
                element.revieweeBadgeSellerUiModel.reputationBadgeUrl,
                element.revieweeBadgeSellerUiModel.score
            )
        }
    }
}