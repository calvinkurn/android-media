package com.tokopedia.kol.feature.post.view.adapter.viewholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
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
    private final Context context;
    private BaseKolView baseKolView;
    private FrameLayout containerView;
    private ImageView reviewImage;
    private TextView tooltip;
    private View tooltipClickArea;
    private View topShadow;
    private Type type;

    public enum Type {
        PROFILE, FEED, EXPLORE, SHOP_PAGE
    }

    public KolPostViewHolder(View itemView,
                             KolPostListener.View.ViewHolder viewListener,
                             Type type) {
        super(itemView);
        this.viewListener = viewListener;
        this.type = type;
        analyticTracker = viewListener.getAbstractionRouter().getAnalyticTracker();
        context = itemView.getContext();
        topShadow = itemView.findViewById(R.id.top_shadow);
        containerView = itemView.findViewById(R.id.container_view);

        baseKolView = itemView.findViewById(R.id.base_kol_view);
        View view = baseKolView.inflateContentLayout(R.layout.kol_post_content);
        reviewImage = view.findViewById(R.id.image);
        tooltip = view.findViewById(R.id.tooltip);
        tooltipClickArea = view.findViewById(R.id.tooltip_area);
    }

    @Override
    public void bind(KolPostViewModel element) {
        if (element.isShowTopShadow() && getAdapterPosition() == 0) {
            topShadow.setVisibility(View.VISIBLE);
        } else {
            topShadow.setVisibility(View.GONE);
        }

        if (type == Type.EXPLORE) {
            containerView.setBackground(null);
            containerView.setBackgroundColor(
                    MethodChecker.getColor(context, R.color.white)
            );
        } else {
            containerView.setBackground(
                    MethodChecker.getDrawable(context, R.drawable.shadow_top_bottom)
            );
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
                context,
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
            element.setReviewUrlClickableSpan(getUrlClickableSpan(element));
        }

        setListener(element);

        baseKolView.bind(element);
        baseKolView.setViewListener(this, element);
    }

    public void onViewRecycled() {
        ImageHandler.clearImage(reviewImage);
        baseKolView.onViewRecycled();

        reviewImage.setImageDrawable(
                MethodChecker.getDrawable(context, R.drawable.ic_loading_image)
        );
    }

    @Override
    public void onAvatarClickListener(BaseKolViewModel element) {
        if (type != Type.PROFILE) {
            goToProfile(element);
        }
    }

    @Override
    public void onNameClickListener(BaseKolViewModel element) {
        if (type != Type.PROFILE) {
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

        if (element.getUserId() > 0) {
            viewListener.onGoToKolProfile(getAdapterPosition(),
                    String.valueOf(element.getUserId()),
                    element.getKolId()
            );
        } else {
            viewListener.onGoToKolProfileUsingApplink(
                    getAdapterPosition(),
                    element.getKolProfileUrl()
            );
        }
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
        } else if (type == Type.SHOP_PAGE) {
            analyticTracker.sendEventTracking(
                    KolEventTracking.Event.EVENT_SHOP_PAGE,
                    KolEventTracking.Category.SHOP_PAGE_FEED,
                    KolEventTracking.Action.SHOP_ITEM_CLICK,
                    String.valueOf(element.getKolId())
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

    private ClickableSpan getUrlClickableSpan(KolPostViewModel element) {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                tooltipAreaClicked(element);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(MethodChecker.getColor(context, R.color.tkpd_main_green));
            }
        };
    }
}
