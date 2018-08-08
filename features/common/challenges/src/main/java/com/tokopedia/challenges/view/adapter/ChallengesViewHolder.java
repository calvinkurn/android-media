package com.tokopedia.challenges.view.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.view.activity.ChallengeDetailActivity;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.R;

class ChallengesViewHolder extends RecyclerView.ViewHolder {

    TextView tvTitle;
    TextView tvHastags;
    ImageView imgChallenge;
    TextView tvTimeRemaining;
    TextView tvStatus;
    ImageView imgShare;

    private Result challengesResult;

    ChallengesViewHolder(View view) {
        super(view);
        tvTitle = view.findViewById(R.id.tv_title);
        tvHastags = view.findViewById(R.id.tv_hastags);
        imgChallenge = view.findViewById(R.id.img_challenge);
        tvTimeRemaining = view.findViewById(R.id.tv_time_remaining);
        tvStatus = view.findViewById(R.id.tv_status);
        imgShare = view.findViewById(R.id.img_share);
        view.setOnClickListener(view1 -> {
            Intent intent =new Intent(view1.getContext(), ChallengeDetailActivity.class);
            intent.putExtra("challengesResult",challengesResult);
            view1.getContext().startActivity(intent);
        });
    }

    void bind(Result challengesResult) {
        this.challengesResult = challengesResult;
        tvTitle.setText(challengesResult.getTitle());
        tvHastags.setText(challengesResult.getHashTag());

        if(challengesResult.getMe().getSubmissionCounts().getApproved()>0){
            tvStatus.setText("Approved");
        }else if(challengesResult.getMe().getSubmissionCounts().getDeclined()>0){
            tvStatus.setText("Declined");
        }else if(challengesResult.getMe().getSubmissionCounts().getWaiting()>0){
            tvStatus.setText("Waiting");
        }else{
            tvTimeRemaining.setText(challengesResult.getEndDate());
            tvTimeRemaining.setVisibility(View.VISIBLE);
            tvStatus.setVisibility(View.GONE);
        }
        ImageHandler.loadImageAndCache(imgChallenge, challengesResult.getThumbnailUrl());
    }

}
