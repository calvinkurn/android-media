package com.tokopedia.kol.feature.post.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.kol.R;
import com.tokopedia.kol.analytics.KolEnhancedTracking;
import com.tokopedia.kol.analytics.KolEventTracking;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.util.KolGlideRequestListener;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;
import com.tokopedia.kol.feature.post.view.widget.BaseKolView;

import java.util.ArrayList;
import java.util.List;
/**
 * @author by nisie on 10/27/17.
 */

public class KolPostViewHolder extends AbstractViewHolder<KolPostViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.kol_post_layout;

    private static final String DASH = "-";

    private static final int MAX_CHAR = 175;
    private final Context context;
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
        context = itemView.getContext();
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
        if (type == Type.PROFILE && getAdapterPosition() == 0) {
            topShadow.setVisibility(View.VISIBLE);
        }

        ImageHandler.loadImageWithRequestListener(reviewImage,
                element.getKolImage(),
                new KolGlideRequestListener());

        if (TextUtils.isEmpty(element.getProductTooltip())) {
            tooltipClickArea.setVisibility(View.GONE);
        } else {
            tooltipClickArea.setVisibility(View.VISIBLE);
            tooltip.setText(element.getProductTooltip());
        }
    }

    private void setListener(final KolPostViewModel element) {
//        avatar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (type == Type.FEED) {
//                    goToProfile(element);
//                }
//            }
//        });
//
//        name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (type == Type.FEED) {
//                    goToProfile(element);
//                }
//            }
//        });
//
//        followButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (element.isFollowed()) {
//                    if (type == Type.FEED) {
//                        analyticTracker.sendEventTracking(
//                                KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
//                                KolEventTracking.Category.HOMEPAGE,
//                                KolEventTracking.Action.FEED_UNFOLLOW_CONTENT,
//                                generateKolEventLabel(true, element.getCardType())
//                        );
//                    }
//
//                    viewListener.onUnfollowKolClicked(element.getPage(), getAdapterPosition(),
//                            element.getUserId());
//                } else {
//                    if (type == Type.FEED) {
//                        analyticTracker.sendEventTracking(
//                                KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
//                                KolEventTracking.Category.HOMEPAGE,
//                                KolEventTracking.Action.FEED_FOLLOW_CONTENT,
//                                generateKolEventLabel(false, element.getCardType())
//                        );
//                    }
//
//                    viewListener.onFollowKolClicked(element.getPage(), getAdapterPosition(),
//                            element.getUserId());
//                }
//            }
//        });
//
//        kolText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (kolText.getText().toString().endsWith(
//                        kolText.getContext().getString(R.string.read_more_english))) {
//
//                    if (type == Type.FEED) {
//                        analyticTracker.sendEventTracking(
//                                KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
//                                KolEventTracking.Category.HOMEPAGE,
//                                KolEventTracking.Action.FEED_EXPAND_CONTENT,
//                                generateKolEventLabel(element.isFollowed(), element.getCardType())
//                        );
//                    }
//
//                    kolText.setText(element.getReview());
//                    element.setReviewExpanded(true);
//                }
//            }
//        });
//
//        likeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (element.isLiked()) {
//                    if (type == Type.FEED) {
//                        analyticTracker.sendEventTracking(
//                                KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
//                                KolEventTracking.Category.HOMEPAGE,
//                                KolEventTracking.Action.FEED_UNLIKE_CONTENT,
//                                generateKolEventLabel(element.isFollowed(), element.getCardType())
//                        );
//                    }
//
//                    viewListener.onUnlikeKolClicked(element.getPage(), getAdapterPosition(),
//                            element.getId());
//                } else {
//                    if (type == Type.FEED) {
//                        analyticTracker.sendEventTracking(
//                                KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
//                                KolEventTracking.Category.HOMEPAGE,
//                                KolEventTracking.Action.FEED_LIKE_CONTENT,
//                                generateKolEventLabel(element.isFollowed(), element.getCardType())
//                        );
//                    }
//
//                    viewListener.onLikeKolClicked(element.getPage(),
//                            getAdapterPosition(),
//                            element.getId());
//                }
//            }
//        });
//
//        commentButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (type == Type.FEED) {
//                    analyticTracker.sendEventTracking(
//                            KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
//                            KolEventTracking.Category.HOMEPAGE,
//                            KolEventTracking.Action.FEED_CLICK_CONTENT_COMMENT,
//                            generateKolEventLabel(element.isFollowed(), element.getCardType())
//                    );
//                }
//
//
//                viewListener.onGoToKolComment(element.getPage(), getAdapterPosition(), element);
//            }
//        });
//
//        tooltipClickArea.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tooltipAreaClicked(element);
//            }
//        });
//
//        reviewImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (tooltipClickArea.getVisibility() == View.VISIBLE)
//                    tooltipAreaClicked(element);
//            }
//        });

    }

    private Spanned getReadMoreText(KolPostViewModel element) {
        if (!element.isReviewExpanded() && MethodChecker.fromHtml(element.getReview()).length() >
                MAX_CHAR) {
            String subDescription = MethodChecker.fromHtml(element.getReview()).toString().substring(0,
                    MAX_CHAR);
            return MethodChecker.fromHtml(
                    subDescription.replaceAll("(\r\n|\n)", "<br />") + "... "
                            + "<font color='#42b549'><b>"
                            + context.getString(R.string.read_more_english)
                            + "</b></font>");
        } else {
            return MethodChecker.fromHtml(element.getReview().replaceAll("(\r\n|\n)", "<br />"));
        }
    }

    private void goToProfile (final KolPostViewModel element) {
        analyticTracker.sendEventTracking(
                KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                KolEventTracking.Category.HOMEPAGE,
                KolEventTracking.Action.FEED_CLICK_CONTENT_WRITER_NAME,
                generateKolEventLabel(element.isFollowed(), element.getCardType())
        );

        viewListener.onGoToKolProfile(element.getPage(),
                getAdapterPosition(),
                String.valueOf(element.getUserId()),
                element.getContentId()
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
                    element.getContentId(),
                    TextUtils.isEmpty(element.getContentLink()) ? DASH :
                            element.getContentLink(),
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
                    element.getContentId(),
                    TextUtils.isEmpty(element.getContentLink()) ? DASH :
                            element.getContentLink(),
                    Integer.valueOf(!TextUtils.isEmpty(viewListener.getUserSession().getUserId()) ?
                            viewListener.getUserSession().getUserId() : "0")
            ));

            analyticTracker.sendEnhancedEcommerce(
                    KolEnhancedTracking.getKolClickTracking(promotionList)
            );
        }

        viewListener.onOpenKolTooltip(element.getPage(),
                getAdapterPosition(),
                element.getContentLink()
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
