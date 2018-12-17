package com.tokopedia.challenges.view.adapter.viewHolder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.activity.ChallengeDetailActivity;
import com.tokopedia.challenges.view.activity.SubmitDetailActivity;
import com.tokopedia.challenges.view.analytics.ChallengesGaAnalyticsTracker;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.utils.Utils;

/**
 * @author lalit.singh
 */
public class SubmissionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public View itemView;
    private ImageView submissionImage;
    private TextView tvBuzzPoints;
    private TextView submissionTitle;
    private ImageView ivFavourite;
    private ImageView ivShareVia;
    private TextView tvWinnerNumber;

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
        tvWinnerNumber = itemView.findViewById(R.id.tv_winner_number);
    }

    public void bindData(final SubmissionResult productItem, SubmissionViewHolderListener listener) {
        this.listener = listener;
        submissionTitle.setText(productItem.getTitle());
        if (productItem.getCollection() != null &&
                Utils.checkIsPastChallenge(productItem.getCollection().getEndDate())) {
            ivFavourite.setVisibility(View.GONE);
            ivShareVia.setVisibility(View.GONE);
        } else {
            ivFavourite.setVisibility(View.VISIBLE);
            ivShareVia.setVisibility(View.VISIBLE);
        }

        ImageHandler.loadImage(context, submissionImage,
                Utils.getImageUrlForSubmission(productItem.getThumbnailUrl()),
                R.color.grey_1100, R.color.grey_1100);

        if (productItem.getMe() != null) {
            setLikes(productItem.getMe().isLiked());
        } else {
            setLikes(false);
        }
        tvBuzzPoints.setText(String.valueOf(productItem.getPoints()));
        if (isWinner) {
            int position = Utils.getWinnerPosition(productItem.getAwards());
            if (position != -1) {
                tvWinnerNumber.setVisibility(View.VISIBLE);
                tvWinnerNumber.setText(String.valueOf(position));
            }
        } else {
            tvWinnerNumber.setVisibility(View.GONE);
            tvWinnerNumber.setText("");

        }
        itemView.setOnClickListener(this);
        ivShareVia.setOnClickListener(this);
        ivFavourite.setOnClickListener(this);
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
        if (v.getId() == R.id.iv_like) {
            listener.onLike(result);
            if (result.getMe() != null) {
                setLikes(!result.getMe().isLiked());
                String action = result.getMe().isLiked() ?
                        ChallengesGaAnalyticsTracker.EVENT_ACTION_LIKE :
                        ChallengesGaAnalyticsTracker.EVENT_ACTION_UNLIKE;

                if (result.getCollection() != null) {
                   /* analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_LIKE,
                            ChallengesGaAnalyticsTracker.EVENT_CATEGORY_OTHER_SUBMISSION,
                            action, categoryItems.get(getIndex()).getCollection().getTitle());*/
                }
            }

        } else {
            Intent detailsIntent = new Intent(context, SubmitDetailActivity.class);
            detailsIntent.putExtra(Utils.QUERY_PARAM_SUBMISSION_RESULT, result);
            detailsIntent.putExtra(Utils.QUERY_PARAM_IS_PAST_CHALLENGE, isPastChallenge);
            listener.onNavigateToActivityRequest(detailsIntent, ChallengeDetailActivity.REQUEST_CODE_SUBMISSIONDETAILACTIVITY, 0);
        }
        /*if (v.getId() == R.id.iv_share) {
            ShareBottomSheet.show(((AppCompatActivity) getActivity()).getSupportFragmentManager(), categoryItems.get(getIndex()), false);
            if (categoryItems.get(getIndex()).getCollection() != null) {
                analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_SHARE,
                        ChallengesGaAnalyticsTracker.EVENT_CATEGORY_OTHER_SUBMISSION,
                        ChallengesGaAnalyticsTracker.EVENT_ACTION_SHARE,
                        categoryItems.get(getIndex()).getCollection().getTitle());
            }
        } else if (v.getId() == R.id.iv_like) {
            mPresenter.setSubmissionLike(categoryItems.get(getIndex()), getIndex());
            if (categoryItems.get(getIndex()).getMe() != null) {
                setLikes(!categoryItems.get(getIndex()).getMe().isLiked());
                String action = categoryItems.get(getIndex()).getMe().isLiked() ?
                        ChallengesGaAnalyticsTracker.EVENT_ACTION_LIKE :
                        ChallengesGaAnalyticsTracker.EVENT_ACTION_UNLIKE;

                if (categoryItems.get(getIndex()).getCollection() != null) {
                    analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_LIKE,
                            ChallengesGaAnalyticsTracker.EVENT_CATEGORY_OTHER_SUBMISSION,
                            action, categoryItems.get(getIndex()).getCollection().getTitle());
                }
            }

        } else {
            Intent detailsIntent = new Intent(context, SubmitDetailActivity.class);
            detailsIntent.putExtra(Utils.QUERY_PARAM_SUBMISSION_RESULT, categoryItems.get(getIndex()));
            detailsIntent.putExtra(Utils.QUERY_PARAM_IS_PAST_CHALLENGE, isPastChallenge);

            navigateToActivityRequest.onNavigateToActivityRequest(detailsIntent, ChallengeDetailActivity.REQUEST_CODE_SUBMISSIONDETAILACTIVITY, getIndex());
        }*/


    }

    public interface SubmissionViewHolderListener {
        void onLike(SubmissionResult result);
        void onNavigateToActivityRequest(Intent intent, int requestCode, int position);
    }
}