package com.tokopedia.groupchat.vote.view.adapter.viewholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.image.SquareImageView;
import com.tokopedia.groupchat.R;
import com.tokopedia.groupchat.chatroom.view.listener.ChannelVoteContract;
import com.tokopedia.groupchat.vote.view.model.VoteViewModel;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class VoteImageViewHolder extends AbstractViewHolder<VoteViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.vote_option_image;
    private ChannelVoteContract.View.VoteOptionListener viewListener;
    private ProgressBar progressBar;
    private TextView option;
    private TextView percent;
    private SquareImageView imageView;
    private View icon;
    private View percentLayout;
    private View shadowLayer;

    public VoteImageViewHolder(View itemView, ChannelVoteContract.View.VoteOptionListener viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        shadowLayer = itemView.findViewById(R.id.shadow_layer);
        progressBar = itemView.findViewById(R.id.progress_bar);
        option = itemView.findViewById(R.id.text_view);
        imageView = itemView.findViewById(com.tokopedia.design.R.id.imageView);
        percent = itemView.findViewById(R.id.percent);
        percentLayout = itemView.findViewById(R.id.percent_layout);
        icon = itemView.findViewById(R.id.icon);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            params.height = itemView.getContext().getResources().getDimensionPixelSize(R.dimen
                    .vote_icon_size);
            imageView.setLayoutParams(params);
        }
    }

    @Override
    public void bind(final VoteViewModel element) {
        Context context = itemView.getContext();
        if (element.getSelected() == VoteViewModel.DEFAULT) {
            shadowLayer.setVisibility(View.GONE);
            percent.setVisibility(View.GONE);
            percentLayout.setVisibility(View.GONE);
            progressBar.setProgress(0);
            progressBar.setProgressDrawable(MethodChecker.getDrawable(context, R.drawable.vote_option_image_default));
            icon.setVisibility(View.GONE);
        } else {
            shadowLayer.setVisibility(View.VISIBLE);
            percent.setVisibility(View.VISIBLE);
            percentLayout.setVisibility(View.VISIBLE);
            progressBar.setProgress(element.getPercentageInteger());
            if (element.getSelected() == VoteViewModel.SELECTED) {
                icon.setVisibility(View.VISIBLE);
                progressBar.setProgressDrawable(MethodChecker.getDrawable(context, R.drawable.vote_option_image_selected));
            } else if (element.getSelected() == VoteViewModel.UNSELECTED) {
                icon.setVisibility(View.GONE);
                progressBar.setProgressDrawable(MethodChecker.getDrawable(context, R.drawable.vote_option_image_unselected));
            }
        }

        option.setText(element.getOption());
        percent.setText(element.getPercentage());
        ImageHandler.loadImageWithTarget(imageView.getContext(), element.getUrl(), new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                imageView.setImageBitmap(resource);
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        shadowLayer.setLayoutParams(new RelativeLayout.LayoutParams(imageView.getHeight(),
                                imageView.getWidth()));

                        if (element.getSelected() == VoteViewModel.DEFAULT) {
                            shadowLayer.setVisibility(View.GONE);
                        } else {
                            shadowLayer.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onVoteOptionClicked(element);
            }
        });
    }
}
