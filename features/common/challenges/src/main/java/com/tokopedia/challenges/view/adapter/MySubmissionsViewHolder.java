package com.tokopedia.challenges.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.ChallengesModuleRouter;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.view.activity.SubmitDetailActivity;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.share.ShareBottomSheet;

class MySubmissionsViewHolder extends RecyclerView.ViewHolder {

    private TextView tvTitle;
    private TextView tvPoints;
    private ImageView imgChallenge;
    private ImageView imgLikes;
    private TextView tvStatus;
    private ImageView imgShare;
    private Context context;

    private SubmissionResult submissionsResult;

    MySubmissionsViewHolder(Context context, View view) {
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
        //imgShare.setOnClickListener(v -> ((ChallengesModuleRouter) (((Activity) context).getApplication())).shareChallenge(context, ChallengesUrl.AppLink.CHALLENGES_DETAILS, submissionsResult.getTitle(), submissionsResult.getThumbnailUrl(), submissionsResult.getSharing().getMetaTags().getOgUrl(), submissionsResult.getSharing().getMetaTags().getOgTitle(), submissionsResult.getSharing().getMetaTags().getOgImage()));
        imgShare.setOnClickListener(v -> ShareBottomSheet.show(((AppCompatActivity) context).getSupportFragmentManager(), ChallengesUrl.AppLink.CHALLENGES_DETAILS, submissionsResult.getTitle(), submissionsResult.getSharing().getMetaTags().getOgUrl(), submissionsResult.getSharing().getMetaTags().getOgTitle(), submissionsResult.getSharing().getMetaTags().getOgImage()));

    }

    void bind(SubmissionResult challengesResult) {
        this.submissionsResult = challengesResult;
        tvTitle.setText(challengesResult.getTitle());
        tvPoints.setText(String.valueOf(challengesResult.getPoints()));
        //imgLikes
        //  tvStatus.setText(submissionsResult.getSubmissionCount());
        ImageHandler.loadImage(context, imgChallenge, challengesResult.getThumbnailUrl(), R.color.grey_1100, R.color.grey_1100);
    }

}
