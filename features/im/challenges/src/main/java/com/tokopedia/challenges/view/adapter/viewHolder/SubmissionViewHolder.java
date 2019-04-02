package com.tokopedia.challenges.view.adapter.viewHolder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.analytics.ChallengesGaAnalyticsTracker;
import com.tokopedia.challenges.view.customview.MExpandableTextView;
import com.tokopedia.challenges.view.model.User;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.utils.Utils;

/**
 * @author lalit.singh
 */
public class SubmissionViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, MExpandableTextView.OnExpandListener {

    private final ChallengesGaAnalyticsTracker analytics;
    public View itemView;
    private ImageView submissionImage;
    private TextView tvBuzzPoints;
    private TextView tvParticipantUserName;
    private TextView submissionTitle;
    private ImageView ivFavourite;
    private ImageView ivShareVia;
    private ImageView ivUserAvatar;
    private ImageView ivVideoIcon;
    private TextView tvWinnerNumber;
    private MExpandableTextView tvPostDescription;

    private Context context;
    private boolean isWinner;
    private boolean isPastChallenge;

    private SubmissionViewHolderListener listener;


    public SubmissionViewHolder(View itemView, Context context, boolean isPastChallenge) {
        super(itemView);
        this.context = context;
        this.itemView = itemView;
        this.isPastChallenge = isPastChallenge;
        submissionImage = itemView.findViewById(R.id.iv_challenge);
        tvBuzzPoints = itemView.findViewById(R.id.tv_buzz_points);
        submissionTitle = itemView.findViewById(R.id.tv_submission_title);
        ivFavourite = itemView.findViewById(R.id.iv_like);
        ivShareVia = itemView.findViewById(R.id.iv_share);
        ivUserAvatar = itemView.findViewById(R.id.iv_userAvatar);
        ivVideoIcon = itemView.findViewById(R.id.play_icon);
        tvParticipantUserName = itemView.findViewById(R.id.tv_participantUserName);
        tvWinnerNumber = itemView.findViewById(R.id.tv_winner_number);
        tvPostDescription = itemView.findViewById(R.id.tv_submission_description);
        itemView.findViewById(R.id.cl_participant).setVisibility(View.VISIBLE);
        analytics = new ChallengesGaAnalyticsTracker();
    }

    public void bindData(final SubmissionResult submissionResult, SubmissionViewHolderListener listener) {
        this.listener = listener;
        submissionTitle.setText(submissionResult.getTitle());
        if (submissionResult.getCollection() != null &&
                Utils.checkIsPastChallenge(submissionResult.getCollection().getEndDate())) {
            ivFavourite.setVisibility(View.GONE);
            ivShareVia.setVisibility(View.GONE);
        } else {
            ivFavourite.setVisibility(View.VISIBLE);
            ivShareVia.setVisibility(View.VISIBLE);
            ivShareVia.setOnClickListener(this);
        }

        ImageHandler.loadImage(context, submissionImage,
                Utils.getImageUrlForSubmission(submissionResult.getThumbnailUrl()),
                R.color.grey_1100, R.color.grey_1100);

        if (submissionResult.getMe() != null) {
            setLikes(submissionResult.getMe().isLiked());
        } else {
            setLikes(false);
        }
        tvBuzzPoints.setText(String.valueOf(submissionResult.getPoints()));
        if (isWinner) {
            int position = Utils.getWinnerPosition(submissionResult.getAwards());
            if (position != -1) {
                tvWinnerNumber.setVisibility(View.VISIBLE);
                tvWinnerNumber.setText(String.valueOf(position));
            }
        } else {
            tvWinnerNumber.setVisibility(View.GONE);
            tvWinnerNumber.setText("");
        }

        if (submissionResult.getMedia() != null && submissionResult.getMedia().get(0).getVideo() != null
                && submissionResult.getMedia().get(0).getVideo().getSources() != null) {
            ivVideoIcon.setVisibility(View.VISIBLE);
            submissionImage.setOnClickListener(this);
        } else {
            ivVideoIcon.setVisibility(View.INVISIBLE);
            submissionImage.setOnClickListener(null);
        }

        if (!TextUtils.isEmpty(submissionResult.getDescription())) {
            tvPostDescription.setVisibility(View.VISIBLE);
            tvPostDescription.setTag(submissionResult);
            tvPostDescription.setText(submissionResult.getDescription());
            if (submissionResult.isExpanded()) {
                tvPostDescription.expand();
            } else {
                tvPostDescription.collapse();
            }
            tvPostDescription.setListener(this);
        } else {
            tvPostDescription.setVisibility(View.GONE);
        }
        itemView.setOnClickListener(this);
        ivShareVia.setOnClickListener(this);
        ivFavourite.setOnClickListener(this);
        setUserData(submissionResult.getUser());
    }

    private void setUserData(User user) {
        ImageHandler.loadImageCircle2(context, ivUserAvatar, user.getThumbnailImage(), R.drawable.ic_big_notif_customerapp);
        tvParticipantUserName.setText(user.getTitle());
    }

    private void setLikes(boolean isLiked) {
        if (((SubmissionResult) itemView.getTag()).getMe() != null) {
            ((SubmissionResult) itemView.getTag()).getMe().setLiked(isLiked);
            if (isLiked) {
                ivFavourite.setImageResource(R.drawable.ic_wishlist_checked);
            } else {
                ivFavourite.setImageResource(R.drawable.ic_wishlist_unchecked);
            }
        }
    }

    @Override
    public void onClick(View v) {
        SubmissionResult result = (SubmissionResult) itemView.getTag();
        if (v.getId() == R.id.iv_challenge) {
            listener.openVideoView(result);
        } else if (v.getId() == R.id.iv_like) {
            listener.onLike(result);
            if (result.getMe() != null) {
                setLikes(!result.getMe().isLiked());
                String action = result.getMe().isLiked() ?
                        ChallengesGaAnalyticsTracker.EVENT_ACTION_LIKE :
                        ChallengesGaAnalyticsTracker.EVENT_ACTION_UNLIKE;
                if (result.getCollection() != null) {
                    analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_LIKE,
                            ChallengesGaAnalyticsTracker.EVENT_CHALLENGE_OTHER_SUBMISSION,
                            action, result.getCollection().getTitle());
                }
            }

        } else if (v.getId() == R.id.iv_share) {
            listener.onShareClick(result);
            if (result.getCollection() != null) {
                analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_SHARE,
                        ChallengesGaAnalyticsTracker.EVENT_CHALLENGE_OTHER_SUBMISSION,
                        ChallengesGaAnalyticsTracker.EVENT_ACTION_SHARE,
                        result.getCollection().getTitle());
            }
        }
    }

    @Override
    public void onExpand() {
        if (tvPostDescription != null && tvPostDescription.getTag() != null) {
            SubmissionResult submissionResult = (SubmissionResult) tvPostDescription.getTag();
            submissionResult.setExpanded(true);
        }
    }

    @Override
    public void onCollapsed() {
        if (tvPostDescription != null && tvPostDescription.getTag() != null) {
            SubmissionResult submissionResult = (SubmissionResult) tvPostDescription.getTag();
            submissionResult.setExpanded(false);
        }
    }

    public interface SubmissionViewHolderListener {
        void onLike(SubmissionResult result);

        void onShareClick(SubmissionResult result);

        void onNavigateToActivityRequest(Intent intent, int requestCode, int position);

        void openVideoView(SubmissionResult submissionResult);
    }

}
