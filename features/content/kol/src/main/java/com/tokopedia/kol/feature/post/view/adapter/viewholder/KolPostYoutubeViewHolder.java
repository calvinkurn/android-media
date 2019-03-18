package com.tokopedia.kol.feature.post.view.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.kol.R;
import com.tokopedia.kol.analytics.KolEnhancedTracking;
import com.tokopedia.kol.analytics.KolEventTracking;
import com.tokopedia.kol.feature.post.view.activity.KolPostYouTubeActivity;
import com.tokopedia.kol.feature.post.view.listener.BaseKolListener;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.viewmodel.BaseKolViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostYoutubeViewModel;
import com.tokopedia.kol.feature.post.view.widget.BaseKolView;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.youtubeutils.common.YoutubeInitializer;
import com.tokopedia.youtubeutils.common.YoutubePlayerConstant;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * @author by yfsx on 28/06/17.
 */

public class KolPostYoutubeViewHolder extends AbstractViewHolder<KolPostYoutubeViewModel>
        implements BaseKolListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.kol_post_youtube_layout;

    private static final String DASH = "-";

    private final KolPostListener.View.ViewHolder viewListener;
    private final Context context;
    private BaseKolView baseKolView;
    private ImageView ivPlay;
    private ProgressBar loadingBar;
    private YouTubeThumbnailLoader youTubeThumbnailLoader;
    private YouTubeThumbnailView thumbnailView;
    private TextView tooltip;
    private View tooltipClickArea;
    private View topShadow;
    private Type type;

    public enum Type {
        PROFILE, FEED, SHOP_PAGE
    }

    public KolPostYoutubeViewHolder(View itemView,
                                    KolPostListener.View.ViewHolder viewListener,
                                    Type type) {
        super(itemView);
        this.viewListener = viewListener;
        this.type = type;
        context = itemView.getContext();
        topShadow = itemView.findViewById(R.id.top_shadow);
        baseKolView = itemView.findViewById(R.id.base_kol_view);
        View view = baseKolView.inflateContentLayout(R.layout.kol_post_content_youtube);
        View mainView = view.findViewById(R.id.main_view);
        ivPlay = view.findViewById(R.id.iv_play);
        loadingBar = view.findViewById(R.id.progress_bar);
        thumbnailView = view.findViewById(R.id.view_youtube_thumbnail);
        thumbnailView.setMinimumWidth(mainView.getWidth());
        tooltip = view.findViewById(R.id.tooltip);
        tooltipClickArea = view.findViewById(R.id.tooltip_area);
    }

    @Override
    public void bind(KolPostYoutubeViewModel element) {
        ivPlay.setVisibility(GONE);
        destroyReleaseProcess();
        if (type == Type.PROFILE && getAdapterPosition() == 0) {
            topShadow.setVisibility(View.VISIBLE);
        } else {
            topShadow.setVisibility(View.GONE);
        }
        try {
            thumbnailView.initialize(YoutubePlayerConstant.GOOGLE_API_KEY,
                    YoutubeInitializer.videoThumbnailInitializer(element.getYoutubeLink(), new YoutubeInitializer.OnVideoThumbnailInitialListener() {
                        @Override
                        public void onSuccessInitializeThumbnail(YouTubeThumbnailLoader loader, YouTubeThumbnailView youTubeThumbnailView) {
                            thumbnailView.setVisibility(VISIBLE);
                            thumbnailView.setOnClickListener(onYoutubeThumbnailClickedListener(element));
                            youTubeThumbnailLoader = loader;
                            loader.release();
                            destroyReleaseProcess();
                            ivPlay.setVisibility(VISIBLE);
                            loadingBar.setVisibility(GONE);
                        }

                        @Override
                        public void onErrorInitializeThumbnail(String error) {
                            destroyReleaseProcess();
                            ivPlay.setVisibility(VISIBLE);
                            ivPlay.setOnClickListener(onYoutubeThumbnailClickedListener(element));
                            loadingBar.setVisibility(GONE);
                        }
                    }));
        } catch (Exception e) {
            loadingBar.setVisibility(VISIBLE);
            thumbnailView.setVisibility(GONE);
        }

        if (TextUtils.isEmpty(element.getTagsCaption())) {
            tooltipClickArea.setVisibility(View.GONE);
        } else {
            tooltip.setText(element.getTagsCaption());
            tooltipClickArea.setVisibility(View.VISIBLE);
            tooltipClickArea.setOnClickListener(v -> tooltipAreaClicked(element));
            element.setReviewUrlClickableSpan(getUrlClickableSpan(element));
        }

        baseKolView.bind(element);
        baseKolView.setViewListener(this, element);
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
            viewListener.onUnfollowKolClicked(getAdapterPosition(), element.getUserId());
        } else {

            viewListener.onFollowKolClicked(getAdapterPosition(), element.getUserId());
        }
    }

    @Override
    public void onDescriptionClickListener(BaseKolViewModel element) {
    }

    @Override
    public void onLikeButtonClickListener(BaseKolViewModel element) {
        if (element.isLiked()) {
            viewListener.onUnlikeKolClicked(
                    getAdapterPosition(),
                    element.getContentId(),
                    element.isMultipleContent(),
                    element.getActivityType()
            );
        } else {
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
        viewListener.onGoToKolComment(
                getAdapterPosition(),
                element.getContentId(),
                element.isMultipleContent(),
                element.getActivityType()
        );
    }

    @Override
    public void onMenuClickListener(BaseKolViewModel element) {

    }

    private void tooltipAreaClicked(KolPostYoutubeViewModel element) {
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

        }

        viewListener.onOpenKolTooltip(
                getAdapterPosition(),
                "",
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

    private void goToProfile(final BaseKolViewModel element) {
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


    private View.OnClickListener onYoutubeThumbnailClickedListener(KolPostYoutubeViewModel element) {
        return v -> {
            Intent intent = KolPostYouTubeActivity.getInstance(
                    viewListener.getContext(),
                    element.getYoutubeLink()
            );
            viewListener.getContext().startActivity(intent);

            doClickPlayTracking(element);
        };
    }

    private void doClickPlayTracking(KolPostYoutubeViewModel element) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                KolEventTracking.Event.EVENT_CLICK_FEED,
                KolEventTracking.Category.CONTENT_FEED,
                KolEventTracking.Action.CLICK_YOUTUBE_VIDEO,
                String.valueOf(element.getContentId())
        ));

        List<KolEnhancedTracking.Promotion> promotionList = new ArrayList<>();

        promotionList.add(new KolEnhancedTracking.Promotion(
                element.getContentId(),
                KolEnhancedTracking.Promotion.createContentNameAnnouncement(
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
    }

    private void destroyReleaseProcess() {
        if (youTubeThumbnailLoader != null) youTubeThumbnailLoader.release();
    }

    private ClickableSpan getUrlClickableSpan(KolPostYoutubeViewModel element) {
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
