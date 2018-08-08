package com.tokopedia.challenges.view.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.activity.ChallengeDetailActivity;
import com.tokopedia.challenges.view.activity.SubmitDetailActivity;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;

class MySubmissionsViewHolder extends RecyclerView.ViewHolder {

    TextView tvTitle;
    TextView tvPoints;
    ImageView imgChallenge;
    ImageView imgLikes;
    TextView tvStatus;
    ImageView imgShare;

    private SubmissionResult submissionsResult;

    MySubmissionsViewHolder(View view) {
        super(view);
        tvTitle = view.findViewById(R.id.tv_title);
        tvPoints = view.findViewById(R.id.tv_points);
        imgChallenge = view.findViewById(R.id.img_challenge);
        imgLikes = view.findViewById(R.id.img_likes);
        tvStatus = view.findViewById(R.id.tv_status);
        imgShare = view.findViewById(R.id.img_share);
        view.setOnClickListener(view1 -> {
            Intent intent =new Intent(view1.getContext(), SubmitDetailActivity.class);
            intent.putExtra("submissionsResult", submissionsResult);
            view1.getContext().startActivity(intent);
        });
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
