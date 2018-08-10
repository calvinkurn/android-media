package com.tokopedia.challenges.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.ChallengesModuleRouter;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.activity.SubmitDetailActivity;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;

class MySubmissionsViewHolder extends RecyclerView.ViewHolder {

    TextView tvTitle;
    TextView tvPoints;
    ImageView imgChallenge;
    ImageView imgLikes;
    TextView tvStatus;
    ImageView imgShare;
    Context context;

    private SubmissionResult submissionsResult;

    MySubmissionsViewHolder(Context context,View view) {
        super(view);
        this.context = context;
        tvTitle = view.findViewById(R.id.tv_title);
        tvPoints = view.findViewById(R.id.tv_points);
        imgChallenge = view.findViewById(R.id.img_challenge);
        imgLikes = view.findViewById(R.id.img_likes);
        tvStatus = view.findViewById(R.id.tv_status);
        imgShare = view.findViewById(R.id.img_share);
        view.setOnClickListener(view1 -> {
            Intent intent =new Intent(view1.getContext(), SubmitDetailActivity.class);
            intent.putExtra("submissionsResult", submissionsResult);
            view.getContext().startActivity(intent);
        });
        imgShare.setOnClickListener(v -> ((ChallengesModuleRouter)(((Activity)context).getApplication())).shareChallenge(context,"tokopedia://referral", submissionsResult.getSharing().getMetaTags().getOgTitle(),submissionsResult.getSharing().getMetaTags().getOgImage()));
    }

    void bind(SubmissionResult challengesResult) {
        this.submissionsResult = challengesResult;
        tvTitle.setText(challengesResult.getTitle());
        tvPoints.setText(String.valueOf(challengesResult.getPoints()));
        //imgLikes
      //  tvStatus.setText(submissionsResult.getSubmissionCount());
        ImageHandler.loadImageAndCache(imgChallenge, challengesResult.getThumbnailUrl());
    }

}
