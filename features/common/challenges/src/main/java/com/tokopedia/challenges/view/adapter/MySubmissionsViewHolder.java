package com.tokopedia.challenges.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.ChallengesAnalytics;
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
    private ChallengesAnalytics analytics;

    MySubmissionsViewHolder(Context context, View view) {
        super(view);
        this.context = context;
        tvTitle = view.findViewById(R.id.tv_title);
        tvPoints = view.findViewById(R.id.tv_points);
        imgChallenge = view.findViewById(R.id.img_challenge);
        imgLikes = view.findViewById(R.id.img_likes);
        tvStatus = view.findViewById(R.id.tv_status);
        imgShare = view.findViewById(R.id.img_share);
        analytics = new ChallengesAnalytics(context);
    }

    void bind(SubmissionResult challengesResult, ISubmissionsViewHolderListner ISubmissionsViewHolderListner) {
        this.submissionsResult = challengesResult;
        tvTitle.setText(challengesResult.getTitle());
        Drawable img = context.getResources().getDrawable(R.drawable.ic_buzz_points);
        tvPoints.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        tvPoints.setText(String.valueOf(challengesResult.getPoints()));
        ImageHandler.loadImageWithoutPlaceholder(imgChallenge, Utils.getImageUrlForSubmission(challengesResult.getThumbnailUrl()), R.color.grey_1100);
        Utils.setTextViewBackground(context, tvStatus, submissionsResult.getStatus());
        imgLikes.setVisibility(View.GONE);
        imgShare.setVisibility(View.VISIBLE);

        if (Utils.STATUS_APPROVED.equalsIgnoreCase(submissionsResult.getStatus())) {
            imgLikes.setVisibility(View.VISIBLE);
            if (submissionsResult.getMe().isLiked()) {
                imgLikes.setImageResource(R.drawable.ic_wishlist_checked);
            } else {
                imgLikes.setImageResource(R.drawable.ic_wishlist_unchecked);
            }
        } else if (Utils.STATUS_DECLINED.equalsIgnoreCase(submissionsResult.getStatus())) {
            imgShare.setVisibility(View.GONE);
        }

        itemView.setOnClickListener(view1 -> {

            Intent intent = new Intent(context, SubmitDetailActivity.class);
            intent.putExtra("submissionsResult", submissionsResult);
            context.startActivity(intent);
            analytics.sendEventChallenges(ChallengesAnalytics.EVENT_CLICK_CHALLENGES,
                    ChallengesAnalytics.EVENT_CATEGORY_MYSUBMISSIONS,
                    ChallengesAnalytics.EVENT_ACTION_CLICK,
                    challengesResult.getCollection().getTitle());
        });
        imgShare.setOnClickListener(v -> {
//            String mediaUrl;
//            boolean isVideo;
//            if (TextUtils.isEmpty(submissionsResult.getSharing().getAssets().getVideo())) {
//                mediaUrl = submissionsResult.getThumbnailUrl();
//                isVideo = false;
//            } else {
//                mediaUrl = submissionsResult.getSharing().getAssets().getVideo();
//                isVideo = true;
//            }
            ShareBottomSheet.show(((AppCompatActivity) context).getSupportFragmentManager(),submissionsResult);
         //   ShareBottomSheet.show(((AppCompatActivity) context).getSupportFragmentManager(), submissionsResult.getSharing().getMetaTags().getOgUrl(), submissionsResult.getTitle(), submissionsResult.getSharing().getMetaTags().getOgUrl(), submissionsResult.getSharing().getMetaTags().getOgTitle(), submissionsResult.getSharing().getMetaTags().getOgImage(), submissionsResult.getId(), Utils.getApplinkPathForBranch(ChallengesUrl.AppLink.SUBMISSION_DETAILS, submissionsResult.getId()), false, mediaUrl, submissionsResult.getCollection().getHashTag(), isVideo);
            analytics.sendEventChallenges(ChallengesAnalytics.EVENT_CLICK_SHARE,
                    ChallengesAnalytics.EVENT_CATEGORY_MYSUBMISSIONS,
                    ChallengesAnalytics.EVENT_ACTION_SHARE,
                    challengesResult.getCollection().getTitle());
        });
        imgLikes.setOnClickListener(v -> {
            String action = ChallengesAnalytics.EVENT_ACTION_LIKE;
            if (submissionsResult.getMe().isLiked())
                action = ChallengesAnalytics.EVENT_ACTION_UNLIKE;
            analytics.sendEventChallenges(ChallengesAnalytics.EVENT_CLICK_LIKE,
                    ChallengesAnalytics.EVENT_CATEGORY_MYSUBMISSIONS,
                    action,
                    challengesResult.getCollection().getTitle());
            ISubmissionsViewHolderListner.onLikeClick(submissionsResult);
            if (submissionsResult.getMe().isLiked()) {
                imgLikes.setImageResource(R.drawable.ic_wishlist_unchecked);
            } else {
                imgLikes.setImageResource(R.drawable.ic_wishlist_checked);
            }
        });
    }

    public interface ISubmissionsViewHolderListner {
        void onLikeClick(SubmissionResult challengesResult);
    }
}
