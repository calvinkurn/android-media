package com.tokopedia.challenges.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.view.analytics.ChallengesGaAnalyticsTracker;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.activity.SubmitDetailActivity;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.share.ShareBottomSheet;
import com.tokopedia.challenges.view.utils.Utils;

public class MySubmissionsViewHolder extends RecyclerView.ViewHolder {

    private TextView tvTitle;
    private TextView tvPoints;
    private ImageView imgChallenge;
    private ImageView imgLikes;
    private TextView tvStatus;
    private ImageView imgShare;
    private Context context;

    private SubmissionResult submissionsResult;
    private ChallengesGaAnalyticsTracker analytics;

    MySubmissionsViewHolder(Context context, View view) {
        super(view);
        this.context = context;
        tvTitle = view.findViewById(R.id.tv_title);
        tvPoints = view.findViewById(R.id.tv_points);
        imgChallenge = view.findViewById(R.id.img_challenge);
        imgLikes = view.findViewById(R.id.img_likes);
        tvStatus = view.findViewById(R.id.tv_status);
        imgShare = view.findViewById(R.id.img_share);
        analytics = new ChallengesGaAnalyticsTracker();
    }

    void bind(SubmissionResult challengesResult, ISubmissionsViewHolderListner ISubmissionsViewHolderListner) {
        this.submissionsResult = challengesResult;
        tvTitle.setText(challengesResult.getTitle());
        tvPoints.setText(String.valueOf(challengesResult.getPoints()));
        ImageHandler.loadImageWithoutPlaceholder(imgChallenge, Utils.getImageUrlForSubmission(challengesResult.getThumbnailUrl()), R.color.grey_1100);
        Utils.setTextViewBackground(context, tvStatus, submissionsResult.getStatus());
        imgLikes.setVisibility(View.GONE);
        imgShare.setVisibility(View.VISIBLE);

        if (Utils.STATUS_DECLINED.equalsIgnoreCase(submissionsResult.getStatus()) ||
                Utils.checkIsPastChallenge(submissionsResult.getCollection().getEndDate())) {
            imgShare.setVisibility(View.GONE);
            imgLikes.setVisibility(View.GONE);
        } else if (Utils.STATUS_APPROVED.equalsIgnoreCase(submissionsResult.getStatus())) {
            imgLikes.setVisibility(View.VISIBLE);
            if (submissionsResult.getMe().isLiked()) {
                imgLikes.setImageResource(R.drawable.ic_wishlist_checked);
            } else {
                imgLikes.setImageResource(R.drawable.ic_wishlist_unchecked);
            }
        }

        itemView.setOnClickListener(view1 -> {

            Intent intent = new Intent(context, SubmitDetailActivity.class);
            intent.putExtra(Utils.QUERY_PARAM_SUBMISSION_RESULT, submissionsResult);
            intent.putExtra(Utils.QUERY_PARAM_IS_PAST_CHALLENGE, Utils.checkIsPastChallenge(submissionsResult.getCollection().getEndDate()));
            context.startActivity(intent);
            analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_CHALLENGES,
                    ChallengesGaAnalyticsTracker.EVENT_CATEGORY_CHALLENGES_MYSUBMISSIONS,
                    ChallengesGaAnalyticsTracker.EVENT_ACTION_CLICK,
                    challengesResult.getCollection().getTitle());
        });
        imgShare.setOnClickListener(v -> {
            ShareBottomSheet.show(((AppCompatActivity) context).getSupportFragmentManager(), submissionsResult, false);
            analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_SHARE,
                    ChallengesGaAnalyticsTracker.EVENT_CATEGORY_CHALLENGES_MYSUBMISSIONS,
                    ChallengesGaAnalyticsTracker.EVENT_ACTION_SHARE,
                    challengesResult.getCollection().getTitle());
        });
        imgLikes.setOnClickListener(v -> {
            String action;

            ISubmissionsViewHolderListner.onLikeClick(submissionsResult);
            if (submissionsResult.getMe().isLiked()) {
                imgLikes.setImageResource(R.drawable.ic_wishlist_unchecked);
                action = ChallengesGaAnalyticsTracker.EVENT_ACTION_UNLIKE;
                submissionsResult.getMe().setLiked(false);
            } else {
                imgLikes.setImageResource(R.drawable.ic_wishlist_checked);
                action = ChallengesGaAnalyticsTracker.EVENT_ACTION_LIKE;
                submissionsResult.getMe().setLiked(true);
            }
            if (challengesResult.getCollection() != null) {
                analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_LIKE,
                        ChallengesGaAnalyticsTracker.EVENT_CATEGORY_CHALLENGES_MYSUBMISSIONS,
                        action,
                        challengesResult.getCollection().getTitle());
            }
        });
    }

    public interface ISubmissionsViewHolderListner {
        void onLikeClick(SubmissionResult challengesResult);
    }
}
