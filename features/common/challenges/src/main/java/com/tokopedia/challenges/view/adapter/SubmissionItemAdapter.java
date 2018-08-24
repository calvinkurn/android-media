package com.tokopedia.challenges.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.ChallengesAnalytics;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.di.ChallengesComponentInstance;
import com.tokopedia.challenges.view.activity.ChallengeDetailActivity;
import com.tokopedia.challenges.view.activity.SubmitDetailActivity;
import com.tokopedia.challenges.view.contractor.SubmissionAdapterContract;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.presenter.SubmissionAdapterPresenter;
import com.tokopedia.challenges.view.share.ShareBottomSheet;
import com.tokopedia.challenges.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SubmissionItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SubmissionAdapterContract.View {

    private List<SubmissionResult> categoryItems;
    private Context context;
    private static final int ITEM = 1;
    private static final int FOOTER = 2;
    @Inject
    SubmissionAdapterPresenter mPresenter;
    private INavigateToActivityRequest navigateToActivityRequest;
    private boolean isFooterAdded;
    private int orientation;
    private boolean isPastChallenge;
    private boolean isWinner;
    private ChallengesAnalytics analytics;


    public SubmissionItemAdapter(List<SubmissionResult> categoryItems, INavigateToActivityRequest navigateToActivityRequest, int orientation, boolean isPastChallenge) {
        if (categoryItems == null)
            this.categoryItems = new ArrayList<>();
        else
            this.categoryItems = categoryItems;
        this.navigateToActivityRequest = navigateToActivityRequest;
        this.orientation = orientation;
        this.isPastChallenge = isPastChallenge;
    }

    @Override
    public int getItemCount() {
        return (categoryItems == null) ? 0 : categoryItems.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        analytics = new ChallengesAnalytics(context);
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        RecyclerView.ViewHolder holder = null;
        View v;
        switch (viewType) {
            case ITEM:
                v = inflater.inflate(R.layout.submission_item, parent, false);
                holder = new ItemViewHolder(v);
                break;
            case FOOTER:
                v = inflater.inflate(R.layout.footer_layout, parent, false);
                holder = new FooterViewHolder(v);
                break;
            default:
                break;
        }

        ChallengesComponentInstance.getChallengesComponent(getActivity().getApplication()).inject(this);
        mPresenter.attachView(this);
        mPresenter.initialize();
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case ITEM:
                ((ItemViewHolder) holder).setIndex(position);
                ((ItemViewHolder) holder).bindData(categoryItems.get(position));
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        return (isLastPosition(position) && isFooterAdded) ? FOOTER : ITEM;
    }

    private boolean isLastPosition(int position) {
        return (position == categoryItems.size() - 1);
    }

    public void addFooter() {
        if (!isFooterAdded) {
            isFooterAdded = true;
            add(new SubmissionResult(), true);
        }
    }


    public void add(SubmissionResult item, boolean refreshItem) {
        categoryItems.add(item);
        if (refreshItem)
            notifyItemInserted(categoryItems.size() - 1);
    }

    public void clearList() {
        isFooterAdded = false;
        if (categoryItems != null)
            categoryItems.clear();
    }

    public void addAll(List<SubmissionResult> items, Boolean... refreshItems) {
        boolean refreshItem = true;
        if (refreshItems.length > 0)
            refreshItem = refreshItems[0];
        if (items != null) {
            for (SubmissionResult item : items) {
                add(item, refreshItem);
            }

        }
    }

    public void removeFooter() {
        if (isFooterAdded) {
            isFooterAdded = false;

            int position = categoryItems.size() - 1;
            SubmissionResult item = categoryItems.get(position);

            if (item != null) {
                categoryItems.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    @Override
    public Activity getActivity() {
        return (Activity) context;
    }

    @Override
    public RequestParams getParams() {
        return null;
    }

    @Override
    public void notifyDataSetChanged(int position) {

    }

    public void isWinnerLayout(boolean isWinner) {
        this.isWinner = isWinner;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View itemView;
        private ImageView submissionImage;
        private TextView tvBuzzPoints;
        private TextView submissionTitle;
        private ImageView ivFavourite;
        private ImageView ivShareVia;
        private TextView tvWinnerNumber;
        private int index;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            submissionImage = itemView.findViewById(R.id.iv_challenge);
            tvBuzzPoints = itemView.findViewById(R.id.tv_buzz_points);
            submissionTitle = itemView.findViewById(R.id.tv_submission_title);
            ivFavourite = itemView.findViewById(R.id.iv_like);
            ivShareVia = itemView.findViewById(R.id.iv_share);
            tvWinnerNumber = itemView.findViewById(R.id.tv_winner_number);
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                DisplayMetrics displaymetrics = new DisplayMetrics();
                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int devicewidth = (int) (displaymetrics.widthPixels / 1.2);
                itemView.getLayoutParams().width = devicewidth;
            }

        }

        public void bindData(final SubmissionResult productItem) {
            submissionTitle.setText(productItem.getTitle());
            ImageHandler.loadImage(context, submissionImage, productItem.getThumbnailUrl(), R.color.grey_1100, R.color.grey_1100);

            if (productItem.getMe() != null)
                setLikes(productItem.getMe().isLiked());
            if (productItem.getPoints() > 0) {
                tvBuzzPoints.setVisibility(View.VISIBLE);
                tvBuzzPoints.setText(String.valueOf(productItem.getPoints()));
            } else {
                itemView.findViewById(R.id.iv_buzz_points).setVisibility(View.GONE);
                tvBuzzPoints.setVisibility(View.GONE);
            }
            if (isWinner) {
                tvWinnerNumber.setVisibility(View.VISIBLE);
                tvWinnerNumber.setText(String.valueOf(getIndex() + 1));
            }
            itemView.setOnClickListener(this);
            ivShareVia.setOnClickListener(this);
            ivFavourite.setOnClickListener(this);
        }

        void setLikes(boolean isLiked) {
            categoryItems.get(getIndex()).getMe().setLiked(isLiked);
            if (isLiked) {
                ivFavourite.setImageResource(R.drawable.ic_wishlist_checked);
            } else {
                ivFavourite.setImageResource(R.drawable.ic_wishlist_unchecked);
            }
        }

        public void setIndex(int position) {
            this.index = position;
        }

        public int getIndex() {
            return this.index;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.iv_share) {
                analytics.sendEventChallenges(ChallengesAnalytics.EVENT_CLICK_SHARE,
                        ChallengesAnalytics.EVENT_CATEGORY_OTHER_SUBMISSION,
                        ChallengesAnalytics.EVENT_ACTION_SHARE,
                        categoryItems.get(getIndex()).getTitle());
                ShareBottomSheet.show(((AppCompatActivity) getActivity()).getSupportFragmentManager(), Utils.getApplinkPathForBranch(ChallengesUrl.AppLink.SUBMISSION_DETAILS, categoryItems.get(getIndex()).getId()), categoryItems.get(getIndex()).getTitle(), categoryItems.get(getIndex()).getSharing().getMetaTags().getOgUrl(), categoryItems.get(getIndex()).getSharing().getMetaTags().getOgTitle(), categoryItems.get(getIndex()).getSharing().getMetaTags().getOgImage(), categoryItems.get(getIndex()).getId(), Utils.getApplinkPathForBranch(ChallengesUrl.AppLink.SUBMISSION_DETAILS, categoryItems.get(getIndex()).getId()), false);
            } else if (v.getId() == R.id.iv_like) {
                String action = ChallengesAnalytics.EVENT_ACTION_LIKE;
                if (categoryItems.get(getIndex()).getMe().isLiked())
                    action = ChallengesAnalytics.EVENT_ACTION_UNLIKE;
                analytics.sendEventChallenges(ChallengesAnalytics.EVENT_CLICK_LIKE,
                        ChallengesAnalytics.EVENT_CATEGORY_OTHER_SUBMISSION,
                        action, categoryItems.get(getIndex()).getTitle());

                mPresenter.setSubmissionLike(categoryItems.get(getIndex()), getIndex());
                if (categoryItems.get(getIndex()).getMe() != null) {
                    if (categoryItems.get(getIndex()).getMe().isLiked()) {
                        setLikes(!categoryItems.get(getIndex()).getMe().isLiked());
                    } else {
                        setLikes(!categoryItems.get(getIndex()).getMe().isLiked());
                    }
                }

            } else {
                Intent detailsIntent = new Intent(context, SubmitDetailActivity.class);
                detailsIntent.putExtra("submissionsResult", categoryItems.get(getIndex()));
                detailsIntent.putExtra("isPastChallenge", isPastChallenge);
                navigateToActivityRequest.onNavigateToActivityRequest(detailsIntent, ChallengeDetailActivity.REQUEST_CODE_SUBMISSIONDETAILACTIVITY, getIndex());
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


    public void unsubscribeUseCase() {
        mPresenter.onDestroy();
    }

    public interface INavigateToActivityRequest {
        void onNavigateToActivityRequest(Intent intent, int requestCode, int position);
    }
}