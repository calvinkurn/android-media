package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder.inboxdetail;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.reputation.ShopReputationView;
import com.tokopedia.design.reputation.UserReputationView;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.ReputationAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.InboxReputationItemViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailHeaderViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.RevieweeBadgeSellerViewModel;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailHeaderViewHolder extends
        AbstractViewHolder<InboxReputationDetailHeaderViewModel> {

    private static final int NO_REPUTATION = 0;
    public static final int SMILEY_BAD = -1;
    public static final int SMILEY_NEUTRAL = 1;
    public static final int SMILEY_GOOD = 2;
    private final ReputationAdapter.ReputationListener reputationListener;

    ImageView userAvatar;
    TextView name;
    UserReputationView userReputationView;
    ShopReputationView shopReputationView;
    View deadlineLayout;
    TextView deadline;
    View lockedLayout;
    TextView lockedText;
    TextView promptMessage;
    View favoriteButton;
    TextView favoriteText;
    ImageView favoriteIcon;
    TextView changeButton;
    RecyclerView smiley;
    TextView opponentSmileyText;
    ImageView opponentSmiley;
    ReputationAdapter adapter;
    GridLayoutManager gridLayout;
    LinearLayoutManager linearLayoutManager;

    @LayoutRes
    public static final int LAYOUT = R.layout.inbox_reputation_detail_header;

    public InboxReputationDetailHeaderViewHolder(View itemView,
                                                 final ReputationAdapter.ReputationListener reputationListener) {
        super(itemView);
        this.reputationListener = reputationListener;
        userAvatar = itemView.findViewById(R.id.user_avatar);
        name = (TextView) itemView.findViewById(R.id.name);
        userReputationView =  itemView.findViewById(R.id.user_reputation);
        shopReputationView = itemView.findViewById(R.id.shop_reputation);
        deadline = (TextView) itemView.findViewById(R.id.deadline_text);
        deadlineLayout = itemView.findViewById(R.id.deadline);
        lockedLayout = itemView.findViewById(R.id.locked);
        lockedText = (TextView) itemView.findViewById(R.id.locked_text);
        promptMessage = (TextView) itemView.findViewById(R.id.prompt_text);
        favoriteButton = itemView.findViewById(R.id.favorite_button);
        favoriteText = (TextView) itemView.findViewById(R.id.favorite_text);
        favoriteIcon = (ImageView) itemView.findViewById(R.id.favorite_icon);
        changeButton = (TextView) itemView.findViewById(R.id.change_button);
        smiley = (RecyclerView) itemView.findViewById(R.id.smiley);
        opponentSmileyText = (TextView) itemView.findViewById(R.id.opponent_smiley_text);
        opponentSmiley = (ImageView) itemView.findViewById(R.id.opponent_smiley);
        adapter = ReputationAdapter.createInstance(reputationListener);
        gridLayout = new GridLayoutManager(itemView.getContext(), 3,
                LinearLayoutManager.VERTICAL, false);
        linearLayoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager
                .HORIZONTAL, false);
        smiley.setLayoutManager(gridLayout);
        smiley.setAdapter(adapter);
    }

    @Override
    public void bind(final InboxReputationDetailHeaderViewModel element) {
        ImageHandler.LoadImage(userAvatar, element.getAvatarImage());
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

        if (!TextUtils.isEmpty(element.getDeadline()) && element.getReputationDataViewModel()
                .isShowLockingDeadline()) {
            deadline.setText(element.getDeadline());
            deadlineLayout.setVisibility(View.VISIBLE);
        } else {
            deadlineLayout.setVisibility(View.GONE);
        }

        if (element.getReputationDataViewModel().isAutoScored()) {
            lockedLayout.setVisibility(View.VISIBLE);
            lockedText.setText(R.string.review_auto_scored);
            promptMessage.setText(MainApplication.getAppContext().getString(R.string.your_scoring));
            smiley.setLayoutManager(linearLayoutManager);
            setSmiley(element, adapter);
        } else if (element.getReputationDataViewModel().isLocked()
                && element.getReputationDataViewModel().isInserted()) {
            lockedLayout.setVisibility(View.VISIBLE);
            lockedText.setText(R.string.review_is_saved);
            promptMessage.setText(MainApplication.getAppContext().getString(R.string.your_scoring));
            smiley.setLayoutManager(linearLayoutManager);
            setSmiley(element, adapter);
        } else if (element.getReputationDataViewModel().isLocked()) {
            lockedLayout.setVisibility(View.VISIBLE);
            lockedText.setText(R.string.locked_reputation);
            promptMessage.setText(MainApplication.getAppContext().getString(R.string.your_scoring));
            smiley.setLayoutManager(linearLayoutManager);
            adapter.showLockedSmiley();
        } else if (element.getReputationDataViewModel().isInserted()) {
            lockedLayout.setVisibility(View.GONE);
            promptMessage.setText(MainApplication.getAppContext().getString(R.string.your_scoring));
            smiley.setLayoutManager(linearLayoutManager);
            setSmiley(element, adapter);
        } else {
            smiley.setLayoutManager(gridLayout);
            adapter.showAllSmiley();
            promptMessage.setText(getPromptText(element));
        }

        if (element.getReputationDataViewModel().isEditable()) {
            changeButton.setVisibility(View.VISIBLE);
            changeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (changeButton.getText().equals(MainApplication.getAppContext().getString(R.string
                            .change))) {
                        adapter.showChangeSmiley(element.getReputationDataViewModel().getReviewerScore());
                        changeButton.setText(MainApplication.getAppContext().getString(R.string
                                .title_cancel));
                    } else {
                        setSmiley(element, adapter);
                        changeButton.setText(MainApplication.getAppContext().getString(R.string
                                .change));
                    }
                }
            });
        } else
            changeButton.setVisibility(View.GONE);

        if (element.getRevieweeBadgeSellerViewModel().getIsFavorited() != -1
                && element.getRole() ==
                InboxReputationItemViewModel.ROLE_SELLER) {
            favoriteButton.setVisibility(View.VISIBLE);
            setFavorite(element.getRevieweeBadgeSellerViewModel());
            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reputationListener.onFavoriteShopClicked(element.getShopId());
                }
            });
        } else {
            favoriteButton.setVisibility(View.GONE);
        }

        setSmileyOpponent(element);

    }

    private void goToInfoPage(InboxReputationDetailHeaderViewModel element) {
        if (element.getRole() == InboxReputationItemViewModel.ROLE_SELLER) {
            reputationListener.onGoToShopDetail(element.getShopId());
        } else {
            reputationListener.onGoToPeopleProfile(element.getUserId());
        }
    }

    private void setFavorite(RevieweeBadgeSellerViewModel revieweeBadgeSellerViewModel) {
        if (revieweeBadgeSellerViewModel.getIsFavorited() == 1) {
            MethodChecker.setBackground(favoriteButton, MethodChecker.getDrawable(favoriteButton
                    .getContext(), R.drawable.white_button_rounded));
            ImageHandler.loadImageWithIdWithoutPlaceholder(favoriteIcon, R.drawable.ic_done_24dp);
            favoriteText.setTextColor(MethodChecker.getColor(favoriteText.getContext(), R.color
                    .grey_500));
            favoriteText.setText(R.string.already_favorite);
        } else {
            MethodChecker.setBackground(favoriteButton, MethodChecker.getDrawable(favoriteButton
                    .getContext(), R.drawable.green_button_rounded));
            ImageHandler.loadImageWithIdWithoutPlaceholder(favoriteIcon, R.drawable.ic_new_action_plus);
            favoriteText.setTextColor(MethodChecker.getColor(favoriteText.getContext(), R.color
                    .white));
            favoriteText.setText(R.string.favorite_button);
        }
    }

    private void setSmileyOpponent(InboxReputationDetailHeaderViewModel element) {

        opponentSmileyText.setText(getOpponentSmileyPromptText(element));

        if (!element.getReputationDataViewModel().isShowRevieweeScore()
                && element.getReputationDataViewModel().
                getRevieweeScore() != NO_REPUTATION) {
            ImageHandler.loadImageWithIdWithoutPlaceholder(opponentSmiley, R.drawable.ic_done_24dp);
        } else {
            switch (element.getReputationDataViewModel().getRevieweeScore()) {
                case NO_REPUTATION:
                    ImageHandler.loadImageWithIdWithoutPlaceholder(opponentSmiley, R.drawable.ic_smiley_empty);
                    break;
                case SMILEY_BAD:
                    ImageHandler.loadImageWithIdWithoutPlaceholder(opponentSmiley, R.drawable.ic_smiley_bad);
                    break;
                case SMILEY_NEUTRAL:
                    ImageHandler.loadImageWithIdWithoutPlaceholder(opponentSmiley, R.drawable.ic_smiley_neutral);
                    break;
                case SMILEY_GOOD:
                    ImageHandler.loadImageWithIdWithoutPlaceholder(opponentSmiley, R.drawable.ic_smiley_good);
                    break;
            }

        }
    }

    private String getOpponentSmileyPromptText(InboxReputationDetailHeaderViewModel element) {
        if (element.getReputationDataViewModel().getRevieweeScore() == NO_REPUTATION)
            return element.getRole() == InboxReputationItemViewModel
                    .ROLE_SELLER ? MainApplication.getAppContext().getString(R.string
                    .seller_has_not_review) : MainApplication.getAppContext().getString(R.string
                    .buyer_has_not_review);
        else
            return element.getRole() == InboxReputationItemViewModel
                    .ROLE_SELLER ? MainApplication.getAppContext().getString(R.string
                    .score_from_seller) : MainApplication.getAppContext().getString(R.string
                    .score_from_buyer);
    }

    private void setSmiley(InboxReputationDetailHeaderViewModel element, ReputationAdapter adapter) {
        changeButton.setText(MainApplication.getAppContext().getString(R.string
                .change));
        switch (element.getReputationDataViewModel().getReviewerScore()) {
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

    private String getPromptText(InboxReputationDetailHeaderViewModel element) {
        return MainApplication.getAppContext().getString(R.string
                .reputation_prompt) + " " + element.getName() + "?";
    }

    public void setReputation(InboxReputationDetailHeaderViewModel element) {
        if (element.getRole() == InboxReputationItemViewModel.ROLE_BUYER) {
            userReputationView.setVisibility(View.VISIBLE);
            shopReputationView.setVisibility(View.GONE);
            userReputationView.setValue(
                    element.getRevieweeBadgeCustomerViewModel().getPositivePercentage(),
                    element.getRevieweeBadgeCustomerViewModel().getNoReputation() == 1,
                    element.getRevieweeBadgeCustomerViewModel().getPositive(),
                    element.getRevieweeBadgeCustomerViewModel().getNeutral(),
                    element.getRevieweeBadgeCustomerViewModel().getNegative()
            );
        } else {
            userReputationView.setVisibility(View.GONE);
            shopReputationView.setVisibility(View.VISIBLE);
            shopReputationView.setValue(
                    element.getRevieweeBadgeSellerViewModel().getReputationBadge().getSet(),
                    element.getRevieweeBadgeSellerViewModel().getReputationBadge().getLevel(),
                    String.valueOf(element.getRevieweeBadgeSellerViewModel().getScore()));

        }
    }
}
