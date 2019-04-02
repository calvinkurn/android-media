package com.tokopedia.feedplus.view.adapter.viewholder.kol;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.analytics.FeedAnalytics;
import com.tokopedia.feedplus.view.analytics.FeedEnhancedTracking;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.viewmodel.kol.KolRecommendItemViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.KolRecommendationViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 10/30/17.
 */

public class KolRecommendationAdapter extends RecyclerView.Adapter<KolRecommendationAdapter
        .ViewHolder> {

    private final FeedPlus.View.Kol kolViewListener;
    private final FeedAnalytics analytics;
    private KolRecommendationViewModel data;

    public KolRecommendationAdapter(FeedPlus.View.Kol kolViewListener, FeedAnalytics analytics) {
        this.kolViewListener = kolViewListener;
        this.analytics = analytics;
        ArrayList<KolRecommendItemViewModel> list = new ArrayList<>();
        this.data = new KolRecommendationViewModel("", "", "", list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView avatar;
        private TextView name;
        private TextView label;
        private TextView followButton;
        private View mainView;

        public ViewHolder(View itemView) {
            super(itemView);
            mainView = itemView;
            avatar = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.name);
            label = itemView.findViewById(R.id.label);
            followButton = itemView.findViewById(R.id.follow_button);

            avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToProfilePage(getAdapterPosition());
                }
            });

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToProfilePage(getAdapterPosition());
                }
            });

            mainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToProfilePage(getAdapterPosition());
                }
            });

            followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KolRecommendItemViewModel kolItem = data.getListRecommend().get(getAdapterPosition());
                    if (kolItem.isFollowed()) {
                        analytics.eventKolRecommendationUnfollowClick(kolItem.getLabel(), kolItem.getName());
                        kolViewListener.onUnfollowKolFromRecommendationClicked(
                                data.getRowNumber(),
                                kolItem.getId(),
                                getAdapterPosition()
                        );
                        kolItem.setFollowed(false);
                        notifyItemChanged(getAdapterPosition());
                    } else {
                        String userId = kolViewListener.getUserSession().getUserId();

                        analytics.eventKolRecommendationFollowClick(kolItem.getLabel(), kolItem.getName());

                        List<FeedEnhancedTracking.Promotion> list = new ArrayList<>();
                        KolRecommendItemViewModel recItem = data.getListRecommend().get(getAdapterPosition());
                        list.add(new FeedEnhancedTracking.Promotion(
                                recItem.getId(),
                                FeedEnhancedTracking.Promotion.createContentNameRecommendation(),
                                recItem.getName().equals("") ? "-" : recItem.getName(),
                                data.getRowNumber(),
                                recItem.getLabel().equals("") ? "-" : recItem.getLabel(),
                                recItem.getId(),
                                recItem.getUrl().equals("") ? "-" : recItem.getUrl()));
                        analytics.eventTrackingEnhancedEcommerce(FeedEnhancedTracking
                                .getClickTracking(list,
                                        Integer.parseInt(userId)
                                ));

                        kolViewListener.onFollowKolFromRecommendationClicked(
                                data.getRowNumber(),
                                kolItem.getId(),
                                getAdapterPosition()
                        );
                        kolItem.setFollowed(true);
                        notifyItemChanged(getAdapterPosition());
                    }
                }
            });

        }
    }

    private void navigateToProfilePage(int adapterPosition) {
        KolRecommendItemViewModel kolItem = data.getListRecommend().get(adapterPosition);
        analytics.eventKolRecommendationGoToProfileClick(kolItem.getLabel(), kolItem.getName());
        kolViewListener.onGoToKolProfileFromRecommendation(data.getRowNumber(),
                adapterPosition,
                String.valueOf(kolItem.getId()));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kol_recommendation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageHandler.loadImageCircle2(holder.avatar.getContext(), holder.avatar, data.getListRecommend().get(position).getImageUrl());
        holder.name.setText(MethodChecker.fromHtml(data.getListRecommend().get(position).getName()));
        holder.label.setText(data.getListRecommend().get(position).getLabel());

        setFollowing(holder, position);
        setMargin(holder, position);
    }

    private void setFollowing(ViewHolder holder, int position) {
        if (data.getListRecommend().get(position).isFollowed()) {
            holder.followButton.setText(holder.followButton.getContext().getString(R.string.following));
            MethodChecker.setBackground(holder.followButton, MethodChecker.getDrawable(
                    holder.followButton.getContext()
                    , R.drawable.btn_share_transaparent));
            holder.followButton.setTextColor(MethodChecker.getColor(holder.followButton.getContext(), R
                    .color.tkpd_main_green));
        } else {
            holder.followButton.setTextColor(MethodChecker.getColor(holder.followButton.getContext(), R
                    .color.white));
            holder.followButton.setText(holder.followButton.getContext().getString(R.string.action_follow_english));
            MethodChecker.setBackground(holder.followButton, MethodChecker.getDrawable(
                    holder.followButton.getContext(), R.drawable.green_button_rounded_unify));
        }
    }

    private void setMargin(ViewHolder holder, int position) {
        if (holder.mainView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams =
                    (ViewGroup.MarginLayoutParams) holder.mainView.getLayoutParams();
            Resources resources = holder.mainView.getContext().getResources();

            if (position == 0) {
                marginLayoutParams.leftMargin = (int) resources.getDimension(R.dimen.dp_16);
            } else if (position == getItemCount() - 1) {
                marginLayoutParams.rightMargin = (int) resources.getDimension(R.dimen.dp_16);
            } else {
                marginLayoutParams.leftMargin = 0;
                marginLayoutParams.rightMargin = (int) resources.getDimension(R.dimen.dp_10);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.getListRecommend().size();
    }

    public void setData(KolRecommendationViewModel data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public KolRecommendationViewModel getData() {
        return data;
    }


}
