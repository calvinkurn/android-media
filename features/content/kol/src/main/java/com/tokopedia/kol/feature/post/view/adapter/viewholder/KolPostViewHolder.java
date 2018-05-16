package com.tokopedia.kol.feature.post.view.adapter.viewholder;

import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.kol.R;
import com.tokopedia.kol.analytics.KolEnhancedTracking;
import com.tokopedia.kol.analytics.KolEventTracking;
import com.tokopedia.kol.feature.post.view.listener.BaseKolListener;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.viewmodel.BaseKolViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;
import com.tokopedia.kol.feature.post.view.widget.BaseKolView;

import java.util.ArrayList;
import java.util.List;
/**
 * @author by nisie on 10/27/17.
 */

public class KolPostViewHolder extends AbstractViewHolder<KolPostViewModel>
        implements BaseKolListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.kol_post_layout;

    private static final String DASH = "-";

    private final KolPostListener.View.ViewHolder viewListener;
    private final AnalyticTracker analyticTracker;
    private BaseKolView baseKolView;
    private ImageView reviewImage;
    private TextView tooltip;
    private View tooltipClickArea;
    private View topShadow;
    private Type type;

    public enum Type {
        PROFILE, FEED
    }

    public KolPostViewHolder(View itemView,
                             KolPostListener.View.ViewHolder viewListener,
                             Type type) {
        super(itemView);
        this.viewListener = viewListener;
        this.type = type;
        analyticTracker = viewListener.getAbstractionRouter().getAnalyticTracker();
        topShadow = itemView.findViewById(R.id.top_shadow);

        baseKolView = itemView.findViewById(R.id.base_kol_view);
        View view = baseKolView.inflateContentLayout(R.layout.kol_post_content);
        reviewImage = view.findViewById(R.id.image);
        tooltip = view.findViewById(R.id.tooltip);
        tooltipClickArea = view.findViewById(R.id.tooltip_area);
    }

    @Override
    public void bind(KolPostViewModel element) {
        baseKolView.bind(element);

        if (type == Type.PROFILE && getAdapterPosition() == 0) {
            topShadow.setVisibility(View.VISIBLE);
        } else {
            topShadow.setVisibility(View.GONE);
        }

        reviewImage.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        ViewTreeObserver viewTreeObserver = reviewImage.getViewTreeObserver();
                        viewTreeObserver.removeOnGlobalLayoutListener(this);

                        reviewImage.setMaxHeight(reviewImage.getWidth());
                        reviewImage.requestLayout();
                    }
                }
        );

        ImageHandler.loadImageWithTarget(
                reviewImage.getContext(),
                element.getKolImage(),
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        reviewImage.setImageBitmap(resource);
                    }
                }
        );

        if (TextUtils.isEmpty(element.getTagsCaption())) {
            tooltipClickArea.setVisibility(View.GONE);
        } else {
            tooltipClickArea.setVisibility(View.VISIBLE);
            tooltip.setText(element.getTagsCaption());
        }

        setListener(element);
        baseKolView.setViewListener(this, element);
    }

    public void onViewRecycled() {
        ImageHandler.clearImage(reviewImage);
        baseKolView.onViewRecycled();

        reviewImage.setImageDrawable(
                MethodChecker.getDrawable(
                        reviewImage.getContext(),
                        R.drawable.ic_loading_image)
        );
    }

    @Override
    public void onAvatarClickListener(BaseKolViewModel element) {
        if (type == Type.FEED) {
            goToProfile(element);
        }
    }

    @Override
    public void onNameClickListener(BaseKolViewModel element) {
        if (type == Type.FEED) {
            goToProfile(element);
        }
    }

    @Override
    public void onFollowButtonClickListener(BaseKolViewModel element) {
        if (element.isFollowed()) {
            if (type == Type.FEED) {
                analyticTracker.sendEventTracking(
                        KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                        KolEventTracking.Category.HOMEPAGE,
                        KolEventTracking.Action.FEED_UNFOLLOW_CONTENT,
                        generateKolEventLabel(true, element.getCardType())
                );
            }

            viewListener.onUnfollowKolClicked(getAdapterPosition(), element.getUserId());
        } else {
            if (type == Type.FEED) {
                analyticTracker.sendEventTracking(
                        KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                        KolEventTracking.Category.HOMEPAGE,
                        KolEventTracking.Action.FEED_FOLLOW_CONTENT,
                        generateKolEventLabel(false, element.getCardType())
                );
            }

            viewListener.onFollowKolClicked(getAdapterPosition(), element.getUserId());
        }
    }

    @Override
    public void onDescriptionClickListener(BaseKolViewModel element) {
        if (type == Type.FEED) {
            analyticTracker.sendEventTracking(
                    KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                    KolEventTracking.Category.HOMEPAGE,
                    KolEventTracking.Action.FEED_EXPAND_CONTENT,
                    generateKolEventLabel(element.isFollowed(), element.getCardType())
            );
        }
    }

    @Override
    public void onLikeButtonClickListener(BaseKolViewModel element) {
        if (element.isLiked()) {
            if (type == Type.FEED) {
                analyticTracker.sendEventTracking(
                        KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                        KolEventTracking.Category.HOMEPAGE,
                        KolEventTracking.Action.FEED_UNLIKE_CONTENT,
                        generateKolEventLabel(element.isFollowed(), element.getCardType())
                );
            }

            viewListener.onUnlikeKolClicked(getAdapterPosition(), element.getKolId());
        } else {
            if (type == Type.FEED) {
                analyticTracker.sendEventTracking(
                        KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                        KolEventTracking.Category.HOMEPAGE,
                        KolEventTracking.Action.FEED_LIKE_CONTENT,
                        generateKolEventLabel(element.isFollowed(), element.getCardType())
                );
            }

            viewListener.onLikeKolClicked(getAdapterPosition(), element.getKolId());
        }
    }

    @Override
    public void onCommentClickListener(BaseKolViewModel element) {
        if (type == Type.FEED) {
            analyticTracker.sendEventTracking(
                    KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                    KolEventTracking.Category.HOMEPAGE,
                    KolEventTracking.Action.FEED_CLICK_CONTENT_COMMENT,
                    generateKolEventLabel(element.isFollowed(), element.getCardType())
            );
        }

        viewListener.onGoToKolComment(getAdapterPosition(), element.getKolId());
    }

    private void setListener(final KolPostViewModel element) {
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

    private void goToProfile(final BaseKolViewModel element) {
        analyticTracker.sendEventTracking(
                KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                KolEventTracking.Category.HOMEPAGE,
                KolEventTracking.Action.FEED_CLICK_CONTENT_WRITER_NAME,
                generateKolEventLabel(element.isFollowed(), element.getCardType())
        );

        viewListener.onGoToKolProfile(getAdapterPosition(),
                String.valueOf(element.getUserId()),
                element.getKolId()
        );
    }

    private void tooltipAreaClicked(KolPostViewModel element) {
        List<KolEnhancedTracking.Promotion> promotionList = new ArrayList<>();
        if (type == Type.FEED) {
            analyticTracker.sendEventTracking(
                    KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                    KolEventTracking.Category.HOMEPAGE,
                    KolEventTracking.Action.FEED_CLICK_CONTENT_CTA,
                    generateKolEventLabel(element.isFollowed(), element.getCardType())
            );

            promotionList.add(new KolEnhancedTracking.Promotion(
                    element.getKolId(),
                    KolEnhancedTracking.Promotion.createContentNameFeed(
                            element.getTagsType(),
                            element.getCardType()),
                    TextUtils.isEmpty(element.getName()) ? DASH :
                            element.getName(),
                    getAdapterPosition(),
                    TextUtils.isEmpty(element.getLabel()) ? DASH :
                            element.getLabel(),
                    element.getTagsId(),
                    TextUtils.isEmpty(element.getTagsLink()) ? DASH :
                            element.getTagsLink(),
                    Integer.valueOf(!TextUtils.isEmpty(viewListener.getUserSession().getUserId()) ?
                            viewListener.getUserSession().getUserId() : "0")
            ));

            analyticTracker.sendEnhancedEcommerce(
                    KolEnhancedTracking.getKolClickTracking(promotionList)
            );

        } else if (type == Type.PROFILE) {
            promotionList.add(new KolEnhancedTracking.Promotion(
                    element.getKolId(),
                    KolEnhancedTracking.Promotion.createContentNameKolPost(
                            element.getTagsType()),
                    TextUtils.isEmpty(element.getName()) ? DASH :
                            element.getName(),
                    getAdapterPosition(),
                    TextUtils.isEmpty(element.getLabel()) ? DASH :
                            element.getLabel(),
                    element.getTagsId(),
                    TextUtils.isEmpty(element.getTagsLink()) ? DASH :
                            element.getTagsLink(),
                    Integer.valueOf(!TextUtils.isEmpty(viewListener.getUserSession().getUserId()) ?
                            viewListener.getUserSession().getUserId() : "0")
            ));

            analyticTracker.sendEnhancedEcommerce(
                    KolEnhancedTracking.getKolClickTracking(promotionList)
            );
        }

        viewListener.onOpenKolTooltip(getAdapterPosition(),
                element.getTagsLink()
        );
    }

    private String generateKolEventLabel(boolean isFollowed, String type) {
        String contentType = isFollowed ?
                KolEventTracking.EventLabel.FEED_CONTENT_TYPE_FOLLOWED :
                KolEventTracking.EventLabel.FEED_CONTENT_TYPE_RECOMMENDED;

        String campaignType = type + KolEventTracking.EventLabel.FEED_CAMPAIGN_TYPE_SUFFIX;
        return contentType + " - " + campaignType;
    }
}
