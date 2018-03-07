package com.tokopedia.tkpdcontent.feature.profile.view.adapter.viewholder;

import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tkpdcontent.R;
import com.tokopedia.tkpdcontent.analytics.KolTracking;
import com.tokopedia.tkpdcontent.feature.profile.view.listener.KolPostListener;
import com.tokopedia.tkpdcontent.feature.profile.view.viewmodel.KolPostViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 10/27/17.
 */

public class KolPostViewHolder extends AbstractViewHolder<KolPostViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.kol_post_layout;

    private static final int MAX_CHAR = 250;
    private final KolPostListener.View viewListener;
    private TextView name;
    private ImageView avatar;
    private TextView label;
    private ImageView followIcon;
    private TextView followText;
    private View followButton;
    private ImageView reviewImage;
    private TextView tooltip;
    private View tooltipClickArea;
    private TextView kolText;
    private ImageView likeIcon;
    private TextView likeText;
    private TextView commentText;
    private View commentButton;
    private View likeButton;
    private View topShadow;
    private RelativeLayout containerView;

    public KolPostViewHolder(View itemView, KolPostListener.View viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        name = itemView.findViewById(R.id.name);
        avatar = itemView.findViewById(R.id.avatar);
        label = itemView.findViewById(R.id.label);
        followIcon = itemView.findViewById(R.id.follow_icon);
        followText = itemView.findViewById(R.id.follow_text);
        followButton = itemView.findViewById(R.id.follow_button);
        reviewImage = itemView.findViewById(R.id.image);
        tooltip = itemView.findViewById(R.id.tooltip);
        tooltipClickArea = itemView.findViewById(R.id.tooltip_area);
        kolText = itemView.findViewById(R.id.kol_text);
        likeIcon = itemView.findViewById(R.id.like_icon);
        likeText = itemView.findViewById(R.id.like_text);
        commentText = itemView.findViewById(R.id.comment_text);
        commentButton = itemView.findViewById(R.id.comment_button);
        likeButton = itemView.findViewById(R.id.like_button);
        topShadow = itemView.findViewById(R.id.top_shadow);
        containerView = itemView.findViewById(R.id.container_view);
    }

    @Override
    public void bind(KolPostViewModel element) {
        if (getAdapterPosition() == 0 ) {
            topShadow.setVisibility(View.VISIBLE);
        } else {
            topShadow.setVisibility(View.GONE);
        }

        name.setText(MethodChecker.fromHtml(element.getName()));
        ImageHandler.loadImageCircle2(avatar.getContext(), avatar, element.getAvatar());

        if (element.isFollowed()) {
            label.setText(element.getTime());
        } else {
            label.setText(element.getLabel());
        }

        if (element.isFollowed() && !element.isTemporarilyFollowed()) {
            followButton.setVisibility(View.GONE);
        } else if (element.isFollowed() && element.isTemporarilyFollowed()) {
            followButton.setVisibility(View.VISIBLE);
            followText.setText(R.string.following);
            followText.setTextColor(MethodChecker.getColor(followText.getContext(),
                    R.color.black_54));
            ImageHandler.loadImageWithIdWithoutPlaceholder(followIcon, R.drawable.ic_tick);
        } else {
            followButton.setVisibility(View.VISIBLE);
            ImageHandler.loadImageWithIdWithoutPlaceholder(followIcon, R.drawable.ic_plus_green);
            followText.setTextColor(MethodChecker.getColor(followText.getContext(),
                    R.color.green_500));
            followText.setText(R.string.action_follow);
        }

        ImageHandler.loadImageWithTarget(
                reviewImage.getContext(),
                element.getKolImage(),
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        reviewImage.setImageBitmap(resource);
                    }
                });

        if (TextUtils.isEmpty(element.getProductTooltip())) {
            tooltipClickArea.setVisibility(View.GONE);
        } else {
            tooltipClickArea.setVisibility(View.VISIBLE);
            tooltip.setText(element.getProductTooltip());
        }

        kolText.setText(getKolText(element));

        if (element.isLiked()) {
            ImageHandler.loadImageWithIdWithoutPlaceholder(likeIcon, R.drawable.ic_thumb_green);
            likeText.setText(String.valueOf(element.getTotalLike()));
            likeText.setTextColor(MethodChecker.getColor(likeText.getContext(), R.color
                    .tkpd_main_green));

        } else if (element.getTotalLike() > 0) {
            ImageHandler.loadImageWithIdWithoutPlaceholder(likeIcon, R.drawable.ic_thumb);
            likeText.setText(String.valueOf(element.getTotalLike()));
            likeText.setTextColor(MethodChecker.getColor(likeText.getContext(), R.color
                    .black_54));
        } else {
            ImageHandler.loadImageWithIdWithoutPlaceholder(likeIcon, R.drawable.ic_thumb);
            likeText.setText(R.string.action_like);
            likeText.setTextColor(MethodChecker.getColor(likeIcon.getContext(), R.color
                    .black_54));
        }

        if (element.getTotalComment() == 0) {
            commentText.setText(R.string.comment);
        } else {
            commentText.setText(String.valueOf(element.getTotalComment()));
        }

        commentButton.setVisibility(element.isShowComment() ? View.VISIBLE : View.GONE);
        setListener(element);
    }

    private void setListener(final KolPostViewModel element) {
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToKolProfile(element.getPage(), getAdapterPosition(), element
                        .getKolProfileUrl());
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToKolProfile(element.getPage(), getAdapterPosition(), element
                        .getKolProfileUrl());
            }
        });

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (element.isFollowed()) {
                    viewListener.onUnfollowKolClicked(element.getPage(), getAdapterPosition(),
                            element.getUserId());
                } else {
                    viewListener.onFollowKolClicked(element.getPage(), getAdapterPosition(),
                            element.getUserId());
                }
            }
        });

        kolText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kolText.getText().toString().endsWith(kolText.getContext().getString(R
                        .string.read_more_english))) {
                    kolText.setText(element.getReview());
                    element.setReviewExpanded(true);
                }
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (element.isLiked()) {
                    viewListener.onUnlikeKolClicked(element.getPage(), getAdapterPosition(),
                            element.getId());
                } else {
                    viewListener.onLikeKolClicked(element.getPage(), getAdapterPosition(), element.getId());
                }
            }
        });

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToKolComment(element.getPage(), getAdapterPosition(), element);
            }
        });

        tooltipClickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tooltipAreaClicked(element);
            }
        });

        reviewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tooltipClickArea.getVisibility() == View.VISIBLE)
                    tooltipAreaClicked(element);
            }
        });

    }

    private Spanned getKolText(KolPostViewModel element) {
        if (!element.isReviewExpanded() && MethodChecker.fromHtml(element.getReview()).length() >
                MAX_CHAR) {
            String subDescription = MethodChecker.fromHtml(element.getReview()).toString().substring(0,
                    MAX_CHAR);
            return MethodChecker.fromHtml(
                    subDescription.replaceAll("(\r\n|\n)", "<br />") + "... "
                            + "<font color='#42b549'>"
                            + kolText.getContext().getString(R.string.read_more_english)
                            + "</font>");
        } else {
            return MethodChecker.fromHtml(element.getReview());
        }
    }

    private void tooltipAreaClicked(KolPostViewModel element) {
        List<KolTracking.Promotion> promotionList = new ArrayList<>();
        promotionList.add(new KolTracking.Promotion(
                element.getId(),
                KolTracking.Promotion.createContentNameKolPost(
                        element.getTagsType()),
                TextUtils.isEmpty(element.getName()) ? "-" :
                        element.getName(),
                getAdapterPosition(),
                TextUtils.isEmpty(element.getLabel()) ? "-" :
                        element.getLabel(),
                element.getContentId(),
                TextUtils.isEmpty(element.getContentLink()) ? "-" :
                        element.getContentLink(),
                Integer.valueOf(!TextUtils.isEmpty(viewListener.getUserSession().getUserId()) ?
                        viewListener.getUserSession().getUserId() : "0")
        ));
        viewListener.getAbstractionRouter()
                .getAnalyticTracker()
                .sendEnhancedEcommerce(KolTracking.getKolClickTracking(promotionList));

        viewListener.onOpenKolTooltip(element.getPage(), getAdapterPosition(),
                element.getContentLink());
    }

}
