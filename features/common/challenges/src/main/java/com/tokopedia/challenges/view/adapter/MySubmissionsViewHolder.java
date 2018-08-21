package com.tokopedia.challenges.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.view.activity.SubmitDetailActivity;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.share.ShareBottomSheet;
import com.tokopedia.challenges.view.utils.RemainingDaysFormatter;
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

    MySubmissionsViewHolder(Context context, View view, ISubmissionsViewHolderListner ISubmissionsViewHolderListner) {
        super(view);
        this.context = context;
        tvTitle = view.findViewById(R.id.tv_title);
        tvPoints = view.findViewById(R.id.tv_points);
        imgChallenge = view.findViewById(R.id.img_challenge);
        imgLikes = view.findViewById(R.id.img_likes);
        tvStatus = view.findViewById(R.id.tv_status);
        imgShare = view.findViewById(R.id.img_share);
        view.setOnClickListener(view1 -> {
            Intent intent = new Intent(view1.getContext(), SubmitDetailActivity.class);
            intent.putExtra("submissionsResult", submissionsResult);
            view.getContext().startActivity(intent);
        });
        //imgShare.setOnClickListener(v -> ((ChallengesModuleRouter) (((Activity) context).getApplication())).generateBranchUrlForChallenge(context, ChallengesUrl.AppLink.CHALLENGES_DETAILS, submissionsResult.getTitle(), submissionsResult.getThumbnailUrl(), submissionsResult.getSharing().getMetaTags().getOgUrl(), submissionsResult.getSharing().getMetaTags().getOgTitle(), submissionsResult.getSharing().getMetaTags().getOgImage()));
        imgShare.setOnClickListener(v -> ShareBottomSheet.show(((AppCompatActivity) context).getSupportFragmentManager(), submissionsResult.getSharing().getMetaTags().getOgUrl(), submissionsResult.getTitle(), submissionsResult.getSharing().getMetaTags().getOgUrl(), submissionsResult.getSharing().getMetaTags().getOgTitle(), submissionsResult.getSharing().getMetaTags().getOgImage(), submissionsResult.getId(), Utils.getApplinkPathForBranch(ChallengesUrl.AppLink.SUBMISSION_DETAILS, submissionsResult.getId()), false));
        imgLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ISubmissionsViewHolderListner.onLikeClick(submissionsResult);
                if (submissionsResult.getMe().isLiked()) {
                    imgLikes.setImageResource(R.drawable.ic_wishlist_unchecked);
                } else {
                    imgLikes.setImageResource(R.drawable.ic_wishlist_checked);
                }
            }
        });
    }

    void bind(SubmissionResult challengesResult) {
        this.submissionsResult = challengesResult;
        tvTitle.setText(challengesResult.getTitle());
        tvPoints.setText(String.valueOf(challengesResult.getPoints()));
        //imgLikes
        tvStatus.setText(submissionsResult.getStatus());

        if ("Approved".equalsIgnoreCase(submissionsResult.getStatus())) {
            tvStatus.setBackgroundResource(R.drawable.bg_round_solid_green_radius_huge);
            tvStatus.setTextColor(context.getResources().getColor(R.color.tkpd_main_green));
        } else if ("Declined".equalsIgnoreCase(submissionsResult.getStatus())) {
            tvStatus.setBackgroundResource(R.drawable.bg_round_solid_red_radius_huge);
            tvStatus.setTextColor(context.getResources().getColor(R.color.red_200));
        } else if ("Waiting".equalsIgnoreCase(submissionsResult.getStatus())) {
            tvStatus.setBackgroundResource(R.drawable.bg_round_solid_gray_radius_huge);
            tvStatus.setTextColor(context.getResources().getColor(R.color.orange_300));
        }
        if (submissionsResult.getMe().isLiked()) {
            imgLikes.setImageResource(R.drawable.ic_wishlist_unchecked);
        } else {
            imgLikes.setImageResource(R.drawable.ic_wishlist_checked);
        }
        ImageHandler.loadImage(context, imgChallenge, challengesResult.getThumbnailUrl(), R.color.grey_1100, R.color.grey_1100);
    }

    public interface ISubmissionsViewHolderListner {
        void onLikeClick(SubmissionResult challengesResult);
    }
}
