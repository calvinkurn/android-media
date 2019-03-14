package com.tokopedia.challenges.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.challenges.ChallengesModuleRouter;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.adapter.viewHolder.SubmissionViewHolder;
import com.tokopedia.challenges.view.customview.CustomVideoPlayer;
import com.tokopedia.challenges.view.fragments.ChallengeDetailsFragment;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.utils.Utils;

import java.util.List;

/**
 * @author lalit.singh
 */
public class ChallengeMainDetailsAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final Result challengeResult;
    private LayoutInflater inflater;
    private List<SubmissionResult> submissionWinnerResults;
    private List<SubmissionResult> allSubmissionList;
    private boolean isPastChallenge;

    private CustomVideoPlayer customVideoPlayer;
    private int VIDEO_POS = 0;

    private boolean isLoaderVisible = true;

    private LoadMoreListener loadMoreListener;

    Handler handler = new Handler();

    private int VIEW_CHALLENGE_DETAIL = 0, VIEW_SUBMISSION = 1, VIEW_LOADER = 2;

    private SubmissionItemAdapter.INavigateToActivityRequest request;

    private SubmissionViewHolder.SubmissionViewHolderListener listener;
    private OnChallengeDetailClickListener onChallengeDetailListener;

    private String tncText;

    public ChallengeMainDetailsAdapter(Context context, Result result, LoadMoreListener loadMoreListener,
                                       boolean isPastChallenge, SubmissionItemAdapter.INavigateToActivityRequest request,
                                       SubmissionViewHolder.SubmissionViewHolderListener listener,
                                       OnChallengeDetailClickListener onTncClickListener) {
        this.onChallengeDetailListener = onTncClickListener;
        this.context = context;
        this.challengeResult = result;
        this.isPastChallenge = isPastChallenge;
        this.loadMoreListener = loadMoreListener;
        this.request = request;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_CHALLENGE_DETAIL)
            return new ChallengeDetailViewHolder(inflater.inflate(R.layout.layout_challenge_details, parent, false));
        else if (viewType == VIEW_LOADER)
            return new FooterViewHolder(inflater.inflate(R.layout.loading_layout, parent, false));
        else
            return new SubmissionViewHolder(inflater.inflate(R.layout.submission_layout, parent, false),
                    context, isPastChallenge);
    }

    public void setWinnerList(List<SubmissionResult> submissionWinnerResults, boolean isPastChallenge) {
        if (this.submissionWinnerResults == null) {
            this.submissionWinnerResults = submissionWinnerResults;
            this.isPastChallenge = isPastChallenge;
            notifyItemChanged(0);
        }
    }

    public void setTnc(String tncText) {
        if (this.tncText == null) {
            this.tncText = tncText;
            notifyItemChanged(0);
        }
    }

    public void setSubmissionList(List<SubmissionResult> submissionResults) {
        if (allSubmissionList == null) {
            allSubmissionList = submissionResults;
            handler.postDelayed(() -> notifyItemRangeInserted(1, submissionResults.size()), 100L);
        } else {
            allSubmissionList.addAll(submissionResults);
            (new Handler()).postDelayed(() -> notifyItemRangeChanged(1, allSubmissionList.size()), 100L);
        }
    }

    public void onViewScrolled() {
        if (customVideoPlayer != null)
            customVideoPlayer.hideMediaController();
    }

    public void onFragmentResume() {
        if (VIDEO_POS != 0) {
            if (customVideoPlayer != null) {
                customVideoPlayer.startPlay(VIDEO_POS, ChallengeDetailsFragment.isVideoPlaying);
                ChallengeDetailsFragment.isVideoPlaying = false;
            }

        }
    }

    public void onFragmentPause() {
        if (customVideoPlayer != null) {
            VIDEO_POS = customVideoPlayer.getPosition();
            if (customVideoPlayer.isVideoPlaying()) {
                ChallengeDetailsFragment.isVideoPlaying = true;
                customVideoPlayer.pause();
            } else
                ChallengeDetailsFragment.isVideoPlaying = false;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            populateDescription((ChallengeDetailViewHolder) holder);
        } else if (holder instanceof SubmissionViewHolder) {
            ((SubmissionViewHolder) holder).itemView.setTag(allSubmissionList.get(position - 1));
            ((SubmissionViewHolder) holder).bindData(allSubmissionList.get(position - 1), listener);
        }
    }

    private void populateDescription(ChallengeDetailViewHolder viewHolder) {
        View itemView = viewHolder.itemView;
        if (itemView.getTag() == null) {
            viewHolder.challengeResult = challengeResult;
            itemView.setTag(true);
            String buzzPointText = ((ChallengesModuleRouter) context.getApplicationContext())
                    .getStringRemoteConfig(Utils.GENERATE_BUZZ_POINT_FIREBASE_KEY);
            if (TextUtils.isEmpty(buzzPointText)) {
                viewHolder.tvBuzzPoint.setVisibility(View.GONE);
                viewHolder.dividerBuzz.setVisibility(View.GONE);
            } else {
                viewHolder.tvBuzzPoint.setVisibility(View.VISIBLE);
                viewHolder.dividerBuzz.setVisibility(View.VISIBLE);
            }
            String description = challengeResult.getDescription();
            if (description.length() > 150) {
                description = description.substring(0, 150);
                viewHolder.tvShortDescription.setText(new StringBuilder().append(description).append("...").toString());
                viewHolder.tvShortDescription.setTag(true);
                viewHolder.tvShortDescription
                        .append(Html.fromHtml("<font color='#42b549'>Lebih</font>"));
            } else {
                viewHolder.tvShortDescription.setText(description);
            }

            if (challengeResult.getPrizes() != null
                    && challengeResult.getPrizes().size() > 0) {
                LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                viewHolder.prizeRecyclerView.setLayoutManager(mLayoutManager1);
                AwardAdapter awardAdapter = new AwardAdapter(challengeResult.getPrizes());
                viewHolder.prizeRecyclerView.setAdapter(awardAdapter);
                if (challengeResult.getSharing().getAssets() != null
                        && !TextUtils.isEmpty(challengeResult.getSharing().getAssets().getVideo())) {
                    customVideoPlayer = viewHolder.customVideoPlayer;
                    viewHolder.customVideoPlayer.setVideoThumbNail(challengeResult.getSharing().getAssets().getImage(),
                            challengeResult.getSharing().getAssets().getVideo(), false, null, false);
                } else {
                    viewHolder.containerVideo.setVisibility(View.GONE);
                }
            } else {
                viewHolder.containerPrize.setVisibility(View.GONE);
            }
        }

        if (viewHolder.ivTnc.getTag() == null && tncText != null) {
            viewHolder.ivTnc.setTag(true);
            viewHolder.ivTnc.setVisibility(View.VISIBLE);
        }

        if (isPastChallenge && viewHolder.containerWinners.getTag() == null)
            if (viewHolder.containerWinners.getVisibility() == View.GONE
                    && submissionWinnerResults != null &&
                    submissionWinnerResults.size() > 0) {
                viewHolder.containerWinners.setVisibility(View.VISIBLE);
                viewHolder.containerWinners.setTag(true);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                viewHolder.winnerRecyclerView.setLayoutManager(mLayoutManager);
                SubmissionItemAdapter winnerItemAdapter = new SubmissionItemAdapter(submissionWinnerResults
                        , request, LinearLayoutManager.HORIZONTAL, isPastChallenge);
                winnerItemAdapter.isWinnerLayout(true);
                viewHolder.winnerRecyclerView.setAdapter(winnerItemAdapter);
            }
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (challengeResult != null) {
            itemCount = itemCount + 1;
            if (isLoaderVisible)
                itemCount = itemCount + 1;
        }
        if (allSubmissionList != null) {
            itemCount = itemCount + allSubmissionList.size();
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_CHALLENGE_DETAIL;
        }
        if (allSubmissionList == null && position == 1) {
            return VIEW_LOADER;
        } else {
            int loaderPosition = allSubmissionList.size() + 1;
            if (position == loaderPosition)
                return VIEW_LOADER;
        }
        return VIEW_SUBMISSION;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder instanceof FooterViewHolder && loadMoreListener != null) {
            loadMoreListener.onLoadMoreStarts();
        }
    }

    public void showLoader() {
        if (!isLoaderVisible) {
            isLoaderVisible = true;
            int itemCount = getItemCount();
            notifyItemChanged(itemCount);
        }
    }

    public void clearList() {
        if (allSubmissionList == null || allSubmissionList.size() == 0)
            return;
        int removedItemSize = allSubmissionList.size();
        allSubmissionList.clear();
        isLoaderVisible = true;
        notifyItemRangeRemoved(2, removedItemSize);
        notifyItemChanged(2);

    }

    public void hideLoader() {
        if (isLoaderVisible) {
            isLoaderVisible = false;
            int itemCount = getItemCount();
            notifyItemRemoved(itemCount);
        }
    }

    private class ChallengeDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final RecyclerView prizeRecyclerView, winnerRecyclerView;
        TextView tvShortDescription, tvBuzzPoint;
        Result challengeResult;
        final ImageView ivTnc;

        View containerPrize, containerWinners, containerVideo, dividerBuzz;

        CustomVideoPlayer customVideoPlayer;

        public ChallengeDetailViewHolder(View view) {
            super(view);
            tvShortDescription = view.findViewById(R.id.short_description);
            tvShortDescription.setOnClickListener(this);
            containerPrize = view.findViewById(R.id.cl_awards);
            containerWinners = view.findViewById(R.id.cl_winners);
            prizeRecyclerView = view.findViewById(R.id.rv_awards);
            winnerRecyclerView = view.findViewById(R.id.rv_winners);
            containerVideo = view.findViewById(R.id.cl_video_player);
            customVideoPlayer = view.findViewById(R.id.video_player);
            tvBuzzPoint = view.findViewById(R.id.tv_how_buzz_points);
            tvBuzzPoint.setOnClickListener(this);

            dividerBuzz = view.findViewById(R.id.divider3);
            ivTnc = view.findViewById(R.id.iv_tnc);
            ivTnc.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            if (viewId == R.id.short_description && view.getTag() != null && onChallengeDetailListener != null) {
                onChallengeDetailListener.onShowChallengeDescription();
            } else if (viewId == R.id.tv_how_buzz_points && onChallengeDetailListener != null) {
                onChallengeDetailListener.onShowBuzzPointsText();
            } else if (viewId == R.id.iv_tnc && onChallengeDetailListener != null) {
                onChallengeDetailListener.onTncClick();
            }
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        View loadingLayout;

        private FooterViewHolder(View itemView) {
            super(itemView);
            loadingLayout = itemView.findViewById(R.id.loading_fl);
        }
    }

    public interface LoadMoreListener {
        void onLoadMoreStarts();
    }

    public interface OnChallengeDetailClickListener {
        void onTncClick();

        void onShowBuzzPointsText();

        void onShowChallengeDescription();
    }

}
