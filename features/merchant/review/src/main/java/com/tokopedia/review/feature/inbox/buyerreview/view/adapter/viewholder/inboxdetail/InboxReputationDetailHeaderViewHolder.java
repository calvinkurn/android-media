package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.inboxdetail;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.review.R;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.ReputationAdapter;
import com.tokopedia.review.feature.inbox.buyerreview.view.customview.ShopReputationView;
import com.tokopedia.review.feature.inbox.buyerreview.view.customview.UserReputationView;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailHeaderUiModel;
import com.tokopedia.unifyprinciples.Typography;


/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailHeaderViewHolder extends
        AbstractViewHolder<InboxReputationDetailHeaderUiModel> {

    private static final int NO_REPUTATION = 0;
    public static final int SMILEY_BAD = -1;
    public static final int SMILEY_NEUTRAL = 1;
    public static final int SMILEY_GOOD = 2;
    private final ReputationAdapter.ReputationListener reputationListener;

    ImageView userAvatar;
    Typography name;
    UserReputationView userReputationView;
    ShopReputationView shopReputationView;
    View deadlineLayout;
    Typography deadline;
    View lockedLayout;
    Typography lockedText;
    Typography promptMessage;
    View favoriteButton;
    Typography favoriteText;
    Typography changeButton;
    RecyclerView smiley;
    Typography opponentSmileyText;
    ImageView opponentSmiley;
    ReputationAdapter adapter;
    GridLayoutManager gridLayout;
    LinearLayoutManager linearLayoutManager;
    Context context;

    @LayoutRes
    public static final int LAYOUT = R.layout.inbox_reputation_detail_header;

    public InboxReputationDetailHeaderViewHolder(View itemView,
                                                 final ReputationAdapter.ReputationListener reputationListener) {
        super(itemView);
        this.reputationListener = reputationListener;
        this.context = itemView.getContext();
        userAvatar = itemView.findViewById(R.id.user_avatar);
        name = (Typography) itemView.findViewById(R.id.name);
        userReputationView =  itemView.findViewById(R.id.user_reputation);
        shopReputationView = itemView.findViewById(R.id.shop_reputation);
        deadline = (Typography) itemView.findViewById(R.id.deadline_text);
        deadlineLayout = itemView.findViewById(R.id.deadline);
        lockedLayout = itemView.findViewById(R.id.locked);
        lockedText = (Typography) itemView.findViewById(R.id.locked_text);
        promptMessage = (Typography) itemView.findViewById(R.id.prompt_text);
        favoriteButton = itemView.findViewById(R.id.favorite_button);
        favoriteText = (Typography) itemView.findViewById(R.id.favorite_text);
        changeButton = (Typography) itemView.findViewById(R.id.change_button);
        smiley = (RecyclerView) itemView.findViewById(R.id.smiley);
        opponentSmileyText = (Typography) itemView.findViewById(R.id.opponent_smiley_text);
        opponentSmiley = (ImageView) itemView.findViewById(R.id.opponent_smiley);
        adapter = ReputationAdapter.createInstance(itemView.getContext(), reputationListener);
        gridLayout = new GridLayoutManager(itemView.getContext(), 3,
                LinearLayoutManager.VERTICAL, false);
        linearLayoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager
                .HORIZONTAL, false);
        smiley.setLayoutManager(gridLayout);
        smiley.setAdapter(adapter);
    }

    @Override
    public void bind(final InboxReputationDetailHeaderUiModel element) {
        ImageHandler.loadImageCircle2(userAvatar.getContext(), userAvatar, element.getAvatarImage());
        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToInfoPage(element);
            }
        });
        name.setText(MethodChecker.fromHtml(element.getName()));
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToInfoPage(element);
            }
        });

        setReputation(element);

        if (!TextUtils.isEmpty(element.getDeadline()) && element.getReputationDataUiModel()
                .isShowLockingDeadline()) {
            deadline.setText(element.getDeadline());
            deadlineLayout.setVisibility(View.VISIBLE);
        } else {
            deadlineLayout.setVisibility(View.GONE);
        }

        if (element.getReputationDataUiModel().isAutoScored()) {
            lockedLayout.setVisibility(View.VISIBLE);
            lockedText.setText(R.string.review_auto_scored);
            promptMessage.setText(context.getString(R.string.your_scoring));
            smiley.setLayoutManager(linearLayoutManager);
            setSmiley(element, adapter);
        } else if (element.getReputationDataUiModel().isLocked()
                && element.getReputationDataUiModel().isInserted()) {
            lockedLayout.setVisibility(View.VISIBLE);
            lockedText.setText(R.string.review_is_saved);
            promptMessage.setText(context.getString(R.string.your_scoring));
            smiley.setLayoutManager(linearLayoutManager);
            setSmiley(element, adapter);
        } else if (element.getReputationDataUiModel().isLocked()) {
            lockedLayout.setVisibility(View.VISIBLE);
            lockedText.setText(R.string.locked_reputation);
            promptMessage.setText(context.getString(R.string.your_scoring));
            smiley.setLayoutManager(linearLayoutManager);
            adapter.showLockedSmiley();
        } else if (element.getReputationDataUiModel().isInserted()) {
            lockedLayout.setVisibility(View.GONE);
            promptMessage.setText(context.getString(R.string.your_scoring));
            smiley.setLayoutManager(linearLayoutManager);
            setSmiley(element, adapter);
        } else {
            smiley.setLayoutManager(gridLayout);
            adapter.showAllSmiley();
            promptMessage.setText(MethodChecker.fromHtml(getPromptText(element)));
        }

        if (element.getReputationDataUiModel().isEditable()) {
            changeButton.setVisibility(View.VISIBLE);
            changeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (changeButton.getText().equals(context.getString(R.string
                            .change))) {
                        adapter.showChangeSmiley(element.getReputationDataUiModel().getReviewerScore());
                        changeButton.setText(context.getString(R.string
                                .title_cancel));
                    } else {
                        setSmiley(element, adapter);
                        changeButton.setText(context.getString(R.string
                                .change));
                    }
                }
            });
        } else
            changeButton.setVisibility(View.GONE);

        setSmileyOpponent(element);

    }

    private void goToInfoPage(InboxReputationDetailHeaderUiModel element) {
        if (element.getRole() == InboxReputationItemUiModel.ROLE_SELLER) {
            reputationListener.onGoToShopDetail(element.getShopId());
        } else {
            reputationListener.onGoToPeopleProfile(element.getUserId());
        }
    }

    private void setSmileyOpponent(InboxReputationDetailHeaderUiModel element) {

        opponentSmileyText.setText(getOpponentSmileyPromptText(element));

        if (!element.getReputationDataUiModel().isShowRevieweeScore()
                && element.getReputationDataUiModel().
                getRevieweeScore() != NO_REPUTATION) {
            ImageHandler.loadImageWithIdWithoutPlaceholder(opponentSmiley, R.drawable.reputation_ic_done_24dp);
        } else {
            switch (element.getReputationDataUiModel().getRevieweeScore()) {
                case NO_REPUTATION:
                    ImageHandler.loadImageWithIdWithoutPlaceholder(opponentSmiley, R.drawable.review_ic_smiley_empty);
                    break;
                case SMILEY_BAD:
                    ImageHandler.loadImageWithIdWithoutPlaceholder(opponentSmiley, R.drawable.review_ic_smiley_bad);
                    break;
                case SMILEY_NEUTRAL:
                    ImageHandler.loadImageWithIdWithoutPlaceholder(opponentSmiley, R.drawable.review_ic_smiley_neutral);
                    break;
                case SMILEY_GOOD:
                    ImageHandler.loadImageWithIdWithoutPlaceholder(opponentSmiley, R.drawable.review_ic_smiley_good);
                    break;
            }

        }
    }

    private String getOpponentSmileyPromptText(InboxReputationDetailHeaderUiModel element) {
        if (element.getReputationDataUiModel().getRevieweeScore() == NO_REPUTATION)
            return element.getRole() == InboxReputationItemUiModel
                    .ROLE_SELLER ? context.getString(R.string
                    .seller_has_not_review) : context.getString(R.string
                    .buyer_has_not_review);
        else
            return element.getRole() == InboxReputationItemUiModel
                    .ROLE_SELLER ? context.getString(R.string
                    .score_from_seller) : context.getString(R.string
                    .score_from_buyer);
    }

    private void setSmiley(InboxReputationDetailHeaderUiModel element, ReputationAdapter adapter) {
        changeButton.setText(context.getString(R.string
                .change));
        switch (element.getReputationDataUiModel().getReviewerScore()) {
            case SMILEY_BAD:
                adapter.showSmileyBad();
                break;
            case SMILEY_NEUTRAL:
                adapter.showSmileyNeutral();
                break;

            case SMILEY_GOOD:
                adapter.showSmileyGood();
                break;
        }
    }

    private String getPromptText(InboxReputationDetailHeaderUiModel element) {
        return context.getString(R.string
                .reputation_prompt) + " " + element.getName() + "?";
    }

    public void setReputation(InboxReputationDetailHeaderUiModel element) {
        if (element.getRole() == InboxReputationItemUiModel.ROLE_BUYER) {
            userReputationView.setVisibility(View.VISIBLE);
            shopReputationView.setVisibility(View.GONE);
            userReputationView.setValue(
                    element.getRevieweeBadgeCustomerUiModel().getPositivePercentage(),
                    element.getRevieweeBadgeCustomerUiModel().getNoReputation() == 1,
                    element.getRevieweeBadgeCustomerUiModel().getPositive(),
                    element.getRevieweeBadgeCustomerUiModel().getNeutral(),
                    element.getRevieweeBadgeCustomerUiModel().getNegative()
            );
        } else {
            userReputationView.setVisibility(View.GONE);
            shopReputationView.setVisibility(View.VISIBLE);
            shopReputationView.setValue(
                    element.getRevieweeBadgeSellerUiModel().getReputationBadge().getSet(),
                    element.getRevieweeBadgeSellerUiModel().getReputationBadge().getLevel(),
                    String.valueOf(element.getRevieweeBadgeSellerUiModel().getScore()));

        }
    }
}
