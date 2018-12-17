package com.tokopedia.challenges.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.adapter.util.StickHeaderItemDecoration;
import com.tokopedia.challenges.view.adapter.viewHolder.SubmissionViewHolder;
import com.tokopedia.challenges.view.customview.CustomVideoPlayer;
import com.tokopedia.challenges.view.fragments.ChallegeneSubmissionFragment;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.utils.MarkdownProcessor;
import com.tokopedia.challenges.view.utils.Utils;

import java.util.List;

/**
 * @author lalit.singh
 */
public class ChallengeMainDetailsAdapter extends RecyclerView.Adapter
        implements StickHeaderItemDecoration.StickyHeaderInterface {

    private final Context context;
    private final Result challengeResult;
    LayoutInflater inflater;
    private List<SubmissionResult> submissionWinnerResults;
    private List<SubmissionResult> allSubmissionList;
    boolean isPastChallenge;

    CustomVideoPlayer customVideoPlayer;
    private int VIDEO_POS = 0;

    boolean isLoaderVisible = true;

    LoadMoreListener loadMoreListener;

    Handler handler = new Handler();

    private int VIEW_CHALLENGE_DETAIL = 0, VIEW_HEADER = 1,
            VIEW_SUBMISSION = 2, VIEW_LOADER = 3, VIEW_CHALLENGE_DETAIL_PAST = 4;

    private String sortType = Utils.QUERY_PARAM_KEY_SORT_RECENT;

    SubmissionItemAdapter.INavigateToActivityRequest request;

    SubmissionViewHolder.SubmissionViewHolderListener listener;

    public ChallengeMainDetailsAdapter(Context context, Result result, LoadMoreListener loadMoreListener,
                                       boolean isPastChallenge, SubmissionItemAdapter.INavigateToActivityRequest request,
                                       SubmissionViewHolder.SubmissionViewHolderListener listener) {
        this.context = context;
        this.challengeResult = result;
        this.isPastChallenge = isPastChallenge;
        this.loadMoreListener = loadMoreListener;
        this.request = request;
        this.listener =  listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_CHALLENGE_DETAIL_PAST)
            return new ChallengeDetailPastViewHolder(inflater.inflate(R.layout.layout_challenge_details_past, parent, false));
        else if (viewType == VIEW_CHALLENGE_DETAIL)
            return new ChallengeDetailViewHolder(inflater.inflate(R.layout.layout_challenge_details, parent, false));
        else if (viewType == VIEW_HEADER)
            return new HeaderViewHolder(inflater.inflate(R.layout.item_header, parent, false));
        else if (viewType == VIEW_LOADER)
            return new FooterViewHolder(inflater.inflate(R.layout.loading_layout, parent, false));
        else
            return new SubmissionViewHolder(inflater.inflate(R.layout.submission_item, parent, false),
                    context, isPastChallenge);
    }

    public void setWinnerList(List<SubmissionResult> submissionWinnerResults, boolean isPastChallenge) {
        if (this.submissionWinnerResults == null) {
            this.submissionWinnerResults = submissionWinnerResults;
            this.isPastChallenge = isPastChallenge;
            notifyItemChanged(0);
        }
    }

    public void setSubmissionList(List<SubmissionResult> submissionResults) {
        if (allSubmissionList == null) {
            allSubmissionList = submissionResults;
            handler.postDelayed(() -> notifyItemRangeInserted(2, submissionResults.size()), 100L);
        } else {
            int startPosition = (allSubmissionList.size() - 1) + 2;
            allSubmissionList.addAll(submissionResults);
            (new Handler()).postDelayed(() -> notifyItemRangeChanged(2, allSubmissionList.size()), 100L);
        }
    }

    public void onViewScrolled() {
        if (customVideoPlayer != null)
            customVideoPlayer.hideMediaController();
    }

    public void onFragmentResume() {
        if (VIDEO_POS != 0) {
            if (customVideoPlayer != null) {
                customVideoPlayer.startPlay(VIDEO_POS, ChallegeneSubmissionFragment.isVideoPlaying);
                ChallegeneSubmissionFragment.isVideoPlaying = false;
            }

        }
    }

    public void onFragmentPause() {
        if (customVideoPlayer != null) {
            VIDEO_POS = customVideoPlayer.getPosition();
            if (customVideoPlayer.isVideoPlaying()) {
                ChallegeneSubmissionFragment.isVideoPlaying = true;
                customVideoPlayer.pause();
            } else
                ChallegeneSubmissionFragment.isVideoPlaying = false;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            if (holder instanceof ChallengeDetailViewHolder)
                populateDescription((ChallengeDetailViewHolder) holder);
            else
                populatePastDescription((ChallengeDetailPastViewHolder) holder);
        } else if (holder instanceof SubmissionViewHolder) {
            ((SubmissionViewHolder) holder).itemView.setTag(allSubmissionList.get(position - 2));
            ((SubmissionViewHolder) holder).bindData(allSubmissionList.get(position - 2), listener);
        } else if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder viewHolder = (HeaderViewHolder) holder;
            if (sortType.equals(Utils.QUERY_PARAM_KEY_SORT_RECENT)) {
                viewHolder.recentFirst.setBackgroundResource(R.drawable.bg_ch_bubble_selected);
                viewHolder.mostPointFirst.setBackgroundResource(R.drawable.bg_ch_bubble_default);
            } else {
                viewHolder.mostPointFirst.setBackgroundResource(R.drawable.bg_ch_bubble_selected);
                viewHolder.recentFirst.setBackgroundResource(R.drawable.bg_ch_bubble_default);
            }
        }

    }


    private void populatePastDescription(ChallengeDetailPastViewHolder viewHolder) {
        View itemView = viewHolder.itemView;
        if (itemView.getTag() == null) {
            viewHolder.challengeResult = challengeResult;
            itemView.setTag(true);
            viewHolder.tvShortDescription.setText(challengeResult.getDescription());
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

    private void populateDescription(ChallengeDetailViewHolder viewHolder) {
        View itemView = viewHolder.itemView;
        if (itemView.getTag() == null) {
            viewHolder.challengeResult = challengeResult;
            itemView.setTag(true);
            viewHolder.tvShortDescription.setText(challengeResult.getDescription());
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
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (challengeResult != null) {
            itemCount = itemCount + 2;
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
            if (isPastChallenge) {
                return VIEW_CHALLENGE_DETAIL_PAST;
            }
            return VIEW_CHALLENGE_DETAIL;
        } else if (position == 1) {
            return VIEW_HEADER;
        } else if (allSubmissionList == null && position == 2) {
            return VIEW_LOADER;
        } else {
            int loaderPosition = allSubmissionList.size() + 2;
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

    public void setSortingType(String sortType) {
        this.sortType = sortType;
        handler.postDelayed(() -> notifyItemChanged(1), 100);
    }

    private class ChallengeDetailPastViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final RecyclerView prizeRecyclerView, winnerRecyclerView;
        TextView tvShortDescription, tvSeeMoreLessButton;
        WebView fullDescriptionWebView;
        Result challengeResult;

        View containerPrize, containerWinners, containerVideo;

        CustomVideoPlayer customVideoPlayer;

        public ChallengeDetailPastViewHolder(View view) {
            super(view);
            tvShortDescription = view.findViewById(R.id.short_description);
            tvSeeMoreLessButton = view.findViewById(R.id.seemorebutton_description);
            fullDescriptionWebView = view.findViewById(R.id.markdownView);
            tvSeeMoreLessButton.setOnClickListener(this);
            containerPrize = view.findViewById(R.id.cl_awards);
            containerWinners = view.findViewById(R.id.cl_winners);
            prizeRecyclerView = view.findViewById(R.id.rv_awards);
            winnerRecyclerView = view.findViewById(R.id.rv_winners);
            containerVideo = view.findViewById(R.id.cl_video_player);
            customVideoPlayer = view.findViewById(R.id.video_player);
        }

        @Override
        public void onClick(View view) {
            if (fullDescriptionWebView.getVisibility() == View.GONE) {
                tvSeeMoreLessButton.setText(R.string.ch_see_less);
                MarkdownProcessor m = new MarkdownProcessor();
                String html = m.markdown(challengeResult.getDescription());
                fullDescriptionWebView.loadDataWithBaseURL("fake://", html, "text/html",
                        "UTF-8", null);
                tvShortDescription.setVisibility(View.GONE);
                fullDescriptionWebView.setVisibility(View.VISIBLE);
            } else {
                tvSeeMoreLessButton.setText(R.string.ch_see_more);
                tvShortDescription.setVisibility(View.VISIBLE);
                fullDescriptionWebView.setVisibility(View.GONE);
            }
        }
    }

    private class ChallengeDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final RecyclerView prizeRecyclerView;
        TextView tvShortDescription, tvSeeMoreLessButton;
        WebView fullDescriptionWebView;
        Result challengeResult;

        View containerPrize, containerVideo;

        CustomVideoPlayer customVideoPlayer;

        public ChallengeDetailViewHolder(View view) {
            super(view);
            tvShortDescription = view.findViewById(R.id.short_description);
            tvSeeMoreLessButton = view.findViewById(R.id.seemorebutton_description);
            fullDescriptionWebView = view.findViewById(R.id.markdownView);
            tvSeeMoreLessButton.setOnClickListener(this);
            containerPrize = view.findViewById(R.id.cl_awards);
            prizeRecyclerView = view.findViewById(R.id.rv_awards);
            containerVideo = view.findViewById(R.id.cl_video_player);
            customVideoPlayer = view.findViewById(R.id.video_player);
        }

        @Override
        public void onClick(View view) {
            if (fullDescriptionWebView.getVisibility() == View.GONE) {
                tvSeeMoreLessButton.setText(R.string.ch_see_less);
                MarkdownProcessor m = new MarkdownProcessor();
                String html = m.markdown(challengeResult.getDescription());
                fullDescriptionWebView.loadDataWithBaseURL("fake://", html, "text/html",
                        "UTF-8", null);
                tvShortDescription.setVisibility(View.GONE);
                fullDescriptionWebView.setVisibility(View.VISIBLE);
            } else {
                tvSeeMoreLessButton.setText(R.string.ch_see_more);
                tvShortDescription.setVisibility(View.VISIBLE);
                fullDescriptionWebView.setVisibility(View.GONE);
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


    @Override
    public int getHeaderPositionForItem(int itemPosition) {
        int headerPosition = 0;
        do {
            if (this.isHeader(itemPosition)) {
                headerPosition = itemPosition;
                break;
            }
            itemPosition -= 1;
        } while (itemPosition >= 0);
        return headerPosition;
    }

    @Override
    public int getHeaderLayout(int headerPosition) {
        return R.layout.item_header;
    }

    @Override
    public boolean isHeader(int itemPosition) {
        if (itemPosition == 1)
            return true;
        return false;
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView recentFirst, mostPointFirst;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            recentFirst = itemView.findViewById(R.id.tv_most_recent);
            mostPointFirst = itemView.findViewById(R.id.tv_buzz_points);
        }
    }

    public interface LoadMoreListener {
        void onLoadMoreStarts();
    }

}
