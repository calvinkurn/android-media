package com.tokopedia.feedplus.view.adapter.viewholder.kol;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.image.SquareImageView;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.analytics.FeedEnhancedTracking;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.viewmodel.kol.PollOptionViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.PollViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by milhamj on 15/05/18.
 */

public class PollAdapter extends RecyclerView.Adapter<PollAdapter.ViewHolder> {
    private int rowNumber;
    private PollViewModel pollViewModel;
    private List<PollOptionViewModel> list;
    private FeedPlus.View.Polling viewListener;

    PollAdapter(int rowNumber, PollViewModel pollViewModel, FeedPlus.View.Polling viewListener) {
        this.rowNumber = rowNumber;
        this.pollViewModel = pollViewModel;
        this.list = new ArrayList<>();
        this.viewListener = viewListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.poll_content_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Context context = holder.imageView.getContext();
        final PollOptionViewModel element = list.get(position);

        if (element.getSelected() == PollOptionViewModel.DEFAULT) {
            holder.shadowLayer.setVisibility(View.GONE);
            holder.percent.setVisibility(View.GONE);
            holder.percentLayout.setVisibility(View.GONE);
            holder.progressBar.setProgress(0);
            holder.progressBar.setProgressDrawable(
                    MethodChecker.getDrawable(context, R.drawable.poll_option_image_default)
            );
        } else {
            holder.shadowLayer.setVisibility(View.VISIBLE);
            holder.percent.setVisibility(View.VISIBLE);
            holder.percentLayout.setVisibility(View.VISIBLE);
            holder.progressBar.setProgress(element.getPercentageInteger());
            if (element.getSelected() == PollOptionViewModel.SELECTED) {
                holder.progressBar.setProgressDrawable(
                        MethodChecker.getDrawable(context, R.drawable.poll_option_image_selected)
                );
            } else if (element.getSelected() == PollOptionViewModel.UNSELECTED) {
                holder.progressBar.setProgressDrawable(
                        MethodChecker.getDrawable(context, R.drawable.poll_option_image_unselected)
                );
            }
        }

        holder.option.setText(element.getOption());
        holder.percent.setText(element.getPercentage());
        ImageHandler.loadImageWithTarget(holder.imageView.getContext(),
                element.getImageUrl(),
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap arg0, GlideAnimation<? super Bitmap> arg1) {
                        holder.imageView.setImageBitmap(arg0);
                        holder.imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                holder.shadowLayer.setLayoutParams(new RelativeLayout.LayoutParams(
                                        holder.imageView.getHeight(),
                                        holder.imageView.getWidth())
                                );

                                if (element.getSelected() == PollOptionViewModel.DEFAULT) {
                                    holder.shadowLayer.setVisibility(View.GONE);
                                } else {
                                    holder.shadowLayer.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                    }
                });

        holder.itemView.setOnClickListener(view -> {
            String trackingPromoCode;
            if (pollViewModel.isVoted()) {
                viewListener.onGoToLink(element.getRedirectLink());
                trackingPromoCode = pollViewModel.getKolProfileUrl();
            } else {
                viewListener.onVoteOptionClicked(rowNumber, pollViewModel.getPollId(), element.getOptionId());
                trackingPromoCode = FeedEnhancedTracking.Promotion.TRACKING_EMPTY;
            }

            viewListener.trackEEPoll(element, trackingPromoCode, rowNumber, pollViewModel);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<PollOptionViewModel> list) {
        int sizeBefore = list.size();
        this.list.addAll(list);
        notifyItemRangeInserted(sizeBefore, list.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private ProgressBar progressBar;
        private TextView option;
        private TextView percent;
        private SquareImageView imageView;
        private View percentLayout;
        private View shadowLayer;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            shadowLayer = itemView.findViewById(R.id.shadow_layer);
            progressBar = itemView.findViewById(R.id.progress_bar);
            option = itemView.findViewById(R.id.text_view);
            imageView = itemView.findViewById(R.id.imageView);
            percent = itemView.findViewById(R.id.percent);
            percentLayout = itemView.findViewById(R.id.percent_layout);
        }
    }
}
