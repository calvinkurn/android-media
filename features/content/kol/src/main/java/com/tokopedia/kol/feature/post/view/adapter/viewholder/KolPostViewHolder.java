package com.tokopedia.kol.feature.post.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.affiliatecommon.view.adapter.PostImageAdapter;
import com.tokopedia.kol.R;
import com.tokopedia.kol.analytics.KolEnhancedTracking;
import com.tokopedia.kol.analytics.KolEventTracking;
import com.tokopedia.kol.feature.post.view.listener.BaseKolListener;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.viewmodel.BaseKolViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;
import com.tokopedia.kol.feature.post.view.widget.BaseKolView;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 10/27/17.
 */

public class KolPostViewHolder extends AbstractViewHolder<KolPostViewModel>
        implements BaseKolListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.kol_post_layout;
    public static final int PAYLOAD_LIKE = 13;
    public static final int PAYLOAD_COMMENT = 14;
    public static final int PAYLOAD_FOLLOW = 15;

    private static final String DASH = "-";
    private static final String PARAM_COUNT = "{count}";
    private static final String SINGLE = "single";
    private static final String MULTIPLE = "multiple";
    private static final String PARAM_TYPE = "{type}";
    private static final int COUNT_SINGLE = 1;

    private final KolPostListener.View.ViewHolder viewListener;
    private final Context context;
    private PostImageAdapter adapter;
    private BaseKolView baseKolView;
    private FrameLayout containerView;
    private ViewPager imageViewPager;
    private TabLayout tabLayout;
    private TextView info;
    private TextView tooltip;
    private View addImageBtn;
    private View bottomSpace;
    private Type type;

    public KolPostViewHolder(View itemView,
                             KolPostListener.View.ViewHolder viewListener,
                             Type type) {
        super(itemView);
        this.viewListener = viewListener;
        this.type = type;
        context = itemView.getContext();
        containerView = itemView.findViewById(R.id.container_view);

        baseKolView = itemView.findViewById(R.id.base_kol_view);
        View view = baseKolView.inflateContentLayout(R.layout.kol_post_content);
        imageViewPager = view.findViewById(R.id.imageViewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        info = view.findViewById(R.id.info);
        tooltip = view.findViewById(R.id.tooltip);
        addImageBtn = view.findViewById(R.id.addImageBtn);
        bottomSpace = view.findViewById(R.id.bottom_space);
    }

    @Override
    public void bind(KolPostViewModel element) {
        setUpViewPager(element.getImageList());
        setListener(element);

        if (type == Type.EXPLORE) {
            containerView.setBackground(null);
            containerView.setBackgroundColor(
                    MethodChecker.getColor(context, R.color.white)
            );

            element.setReviewExpanded(true);
        } else {
            containerView.setBackground(
                    MethodChecker.getDrawable(context, R.drawable.card_shadow_top_bottom)
            );
        }

        if (TextUtils.isEmpty(element.getTagsCaption())) {
            tooltip.setVisibility(View.GONE);
        } else {
            tooltip.setVisibility(View.VISIBLE);
            tooltip.setText(element.getTagsCaption());
            element.setReviewUrlClickableSpan(getUrlClickableSpan(element));
        }

        if (TextUtils.isEmpty(element.getInfo())) {
            info.setVisibility(View.GONE);
        } else {
            info.setVisibility(View.VISIBLE);
            info.setText(element.getInfo());
        }

        addImageBtn.setVisibility(View.GONE);

        if (TextUtils.isEmpty(element.getReview())) {
            bottomSpace.setVisibility(View.VISIBLE);
        } else {
            bottomSpace.setVisibility(View.GONE);
        }

        baseKolView.bind(element);
        baseKolView.setViewListener(this, element);
    }

    @Override
    public void bind(KolPostViewModel element, @NonNull List<Object> payloads) {
        super.bind(element, payloads);
        switch ((int) payloads.get(0)) {
            case PAYLOAD_LIKE:
                baseKolView.bindLike(element.isLiked(), element.getTotalLike());
                break;
            case PAYLOAD_COMMENT:
                baseKolView.bindComment(element.getTotalComment());
                break;
            case PAYLOAD_FOLLOW:
                baseKolView.bindFollow(element.isFollowed(), element.isTemporarilyFollowed());
                break;
            default:
                bind(element);
                break;
        }
    }

    public void onViewRecycled() {
        baseKolView.onViewRecycled();
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
                TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                        KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                        KolEventTracking.Category.HOMEPAGE,
                        KolEventTracking.Action.FEED_UNFOLLOW_CONTENT,
                        generateKolEventLabel(true, element.getCardType())
                ));
            }

            viewListener.onUnfollowKolClicked(getAdapterPosition(), element.getUserId());
        } else {
            if (type == Type.FEED) {
                TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                        KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                        KolEventTracking.Category.HOMEPAGE,
                        KolEventTracking.Action.FEED_FOLLOW_CONTENT,
                        generateKolEventLabel(false, element.getCardType())
                ));
            }

            viewListener.onFollowKolClicked(getAdapterPosition(), element.getUserId());
        }
    }

    @Override
    public void onDescriptionClickListener(BaseKolViewModel element) {
        if (type == Type.FEED) {
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                    KolEventTracking.Category.HOMEPAGE,
                    KolEventTracking.Action.FEED_EXPAND_CONTENT,
                    generateKolEventLabel(element.isFollowed(), element.getCardType())
            ));
        }
    }

    @Override
    public void onLikeButtonClickListener(BaseKolViewModel element) {
        if (element.isLiked()) {
            if (type == Type.FEED) {
                TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                        KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                        KolEventTracking.Category.HOMEPAGE,
                        KolEventTracking.Action.FEED_UNLIKE_CONTENT,
                        generateKolEventLabel(element.isFollowed(), element.getCardType())
                ));
            }

            viewListener.onUnlikeKolClicked(
                    getAdapterPosition(),
                    element.getContentId(),
                    element.isMultipleContent(),
                    element.getActivityType()
            );
        } else {
            if (type == Type.FEED) {
                TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                        KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                        KolEventTracking.Category.HOMEPAGE,
                        KolEventTracking.Action.FEED_LIKE_CONTENT,
                        generateKolEventLabel(element.isFollowed(), element.getCardType())
                ));
            }
            viewListener.onLikeKolClicked(
                    getAdapterPosition(),
                    element.getContentId(),
                    element.isMultipleContent(),
                    element.getActivityType()
            );
        }
    }

    @Override
    public void onCommentClickListener(BaseKolViewModel element) {
        if (type == Type.FEED) {
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                    KolEventTracking.Category.HOMEPAGE,
                    KolEventTracking.Action.FEED_CLICK_CONTENT_COMMENT,
                    generateKolEventLabel(element.isFollowed(), element.getCardType())
            ));
        }

        viewListener.onGoToKolComment(
                getAdapterPosition(),
                element.getContentId(),
                element.isMultipleContent(),
                element.getActivityType()
        );
    }

    @Override
    public void onMenuClickListener(BaseKolViewModel element) {
        viewListener.onMenuClicked(getAdapterPosition(), element);
    }

    private void setUpViewPager(List<String> images) {
        adapter = new PostImageAdapter();
        adapter.setList(new ArrayList<>(images));
        imageViewPager.setAdapter(adapter);
        imageViewPager.setOffscreenPageLimit(adapter.getCount());
        tabLayout.setupWithViewPager(imageViewPager);
        tabLayout.setVisibility(adapter.getCount() > 1 ? View.VISIBLE : View.GONE);
    }

    private void setListener(final KolPostViewModel element) {
        tooltip.setOnClickListener(v -> {
            tooltipAreaClicked(element);
            viewListener.trackTooltipClick(
                    element.isMultipleContent(),
                    String.valueOf(element.getContentId()),
                    element.getActivityType(),
                    String.valueOf(getAdapterPosition())
            );
        });

        adapter.setClickListener(position -> {
            if (tooltip.getVisibility() == View.VISIBLE) {
                tooltipAreaClicked(element);
                viewListener.trackContentClick(
                        element.isMultipleContent(),
                        String.valueOf(element.getContentId()),
                        element.getActivityType(),
                        String.valueOf(getAdapterPosition())
                );
            }
        });

        addImageBtn.setOnClickListener(v -> viewListener.onEditClicked(
                element.isMultipleContent(),
                String.valueOf(element.getContentId()),
                element.getActivityType()
        ));
    }

    private void goToProfile(final BaseKolViewModel element) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                KolEventTracking.Category.HOMEPAGE,
                KolEventTracking.Action.FEED_CLICK_CONTENT_WRITER_NAME,
                generateKolEventLabel(element.isFollowed(), element.getCardType())
        ));

        if (element.getUserId() > 0) {
            viewListener.onGoToKolProfile(getAdapterPosition(),
                    String.valueOf(element.getUserId()),
                    element.getContentId()
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
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    KolEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                    KolEventTracking.Category.HOMEPAGE,
                    KolEventTracking.Action.FEED_CLICK_CONTENT_CTA,
                    generateKolEventLabel(element.isFollowed(), element.getCardType())
            ));

            promotionList.add(new KolEnhancedTracking.Promotion(
                    element.getContentId(),
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

            TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
                    KolEnhancedTracking.getKolClickTracking(promotionList)
            );

        } else if (type == Type.SHOP_PAGE) {
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    KolEventTracking.Event.EVENT_SHOP_PAGE,
                    KolEventTracking.Category.SHOP_PAGE_FEED,
                    KolEventTracking.Action.SHOP_ITEM_CLICK_DYNAMIC
                            .replace(PARAM_COUNT, element.getImageList().size() == COUNT_SINGLE ? SINGLE : MULTIPLE)
                            .replace(PARAM_TYPE, element.getTagsType()),
                    String.valueOf(element.getContentId())
            ));
        }

        viewListener.onOpenKolTooltip(
                getAdapterPosition(),
                element.getTrackingId(),
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

    public enum Type {
        PROFILE, FEED, EXPLORE, SHOP_PAGE
    }
}
