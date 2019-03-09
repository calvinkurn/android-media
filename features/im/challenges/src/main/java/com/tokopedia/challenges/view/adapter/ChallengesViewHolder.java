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
import com.tokopedia.challenges.view.activity.ChallengeDetailsActivity;
import com.tokopedia.challenges.view.analytics.ChallengesGaAnalyticsTracker;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.share.ShareBottomSheet;
import com.tokopedia.challenges.view.utils.RemainingDaysFormatter;
import com.tokopedia.challenges.view.utils.Utils;

class ChallengesViewHolder extends RecyclerView.ViewHolder {

    private final ChallengesGaAnalyticsTracker analytics;
    private TextView tvTitle;
    private TextView tvHastags;
    private ImageView imgChallenge;
    private TextView tvTimeRemaining;
    private TextView tvStatus;
    private ImageView imgShare;

    private Context context;

    ChallengesViewHolder(Context context, View view) {
        super(view);
        this.context = context;
        tvTitle = view.findViewById(R.id.tv_title);
        tvHastags = view.findViewById(R.id.tv_hastags);
        imgChallenge = view.findViewById(R.id.img_challenge);
        tvTimeRemaining = view.findViewById(R.id.tv_time_remaining);
        tvStatus = view.findViewById(R.id.tv_status);
        imgShare = view.findViewById(R.id.img_share);
        analytics = new ChallengesGaAnalyticsTracker(context);
    }

    void bind(Result challengesResult, boolean isPastChallenge) {
        tvTitle.setText(challengesResult.getTitle());
        tvHastags.setText(challengesResult.getHashTag());
        tvTimeRemaining.setVisibility(View.GONE);
        tvStatus.setVisibility(View.VISIBLE);
        imgShare.setVisibility(View.VISIBLE);
        if (isPastChallenge) {
            Utils.setTextViewBackground(context, tvStatus, Utils.STATUS_COMPLETED);
            imgShare.setVisibility(View.INVISIBLE);
        } else if (challengesResult.getMe().getSubmissionCounts().getApproved() > 0 || challengesResult.getMe().getSubmissionCounts().getWaiting() > 0) {
            Utils.setTextViewBackground(context, tvStatus, Utils.STATUS_PARTICIPATED);
        } else {
            tvTimeRemaining.setText(getEndDate(challengesResult.getEndDate()));
            tvTimeRemaining.setVisibility(View.VISIBLE);
            tvStatus.setVisibility(View.GONE);
        }
        ImageHandler.loadImageWithoutPlaceholder(imgChallenge, Utils.getImageUrl(challengesResult.getThumbnailUrl()), R.color.grey_1100);

        itemView.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, ChallengeDetailsActivity.class);
            intent.putExtra(Utils.QUERY_PARAM_SUBMISSION_RESULT, challengesResult);
            intent.putExtra(Utils.QUERY_PARAM_IS_PAST_CHALLENGE, isPastChallenge);
            context.startActivity(intent);
            if (isPastChallenge) {
                analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_CHALLENGES,
                        ChallengesGaAnalyticsTracker.EVENT_CATEGORY_CHALLENGES_PAST_CHALLENGES,
                        ChallengesGaAnalyticsTracker.EVENT_ACTION_CLICK,
                        challengesResult.getTitle());
            } else {
                analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_CHALLENGES,
                        ChallengesGaAnalyticsTracker.EVENT_CATEGORY_CHALLENGES_ACTIVE_CHALLENEGES,
                        ChallengesGaAnalyticsTracker.EVENT_ACTION_CLICK,
                        challengesResult.getTitle());
            }
        });

        imgShare.setOnClickListener(v -> {
            ShareBottomSheet.show(((AppCompatActivity) context).getSupportFragmentManager(), challengesResult, false);
            if (isPastChallenge) {
                analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_SHARE,
                        ChallengesGaAnalyticsTracker.EVENT_CATEGORY_CHALLENGES_PAST_CHALLENGES,
                        ChallengesGaAnalyticsTracker.EVENT_ACTION_SHARE,
                        challengesResult.getTitle());
            } else {
                analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_SHARE,
                        ChallengesGaAnalyticsTracker.EVENT_CATEGORY_CHALLENGES_ACTIVE_CHALLENEGES,
                        ChallengesGaAnalyticsTracker.EVENT_ACTION_SHARE,
                        challengesResult.getTitle());
            }
        });
    }

    private String getEndDate(String time) {
        RemainingDaysFormatter daysFormatter = new RemainingDaysFormatter(System.currentTimeMillis(), Utils.convertUTCToMillis(time));

        int day = (int) daysFormatter.getRemainingDays();
        long hours = daysFormatter.getRemainingHours();
        long minute = daysFormatter.getRemainingMinutes();

        if (day > 0) {
            return String.format(context.getResources().getString(R.string.ch_text_remaining_days), day);
        } else if (hours > 0) {
            return String.format(context.getResources().getString(R.string.ch_text_remaining_hours), hours);
        } else if (minute < 0)
            return String.format(context.getResources().getString(R.string.ch_text_remaining_minutes), minute);
        return String.format(context.getResources().getString(R.string.ch_text_remaining_days), day);
    }
}
