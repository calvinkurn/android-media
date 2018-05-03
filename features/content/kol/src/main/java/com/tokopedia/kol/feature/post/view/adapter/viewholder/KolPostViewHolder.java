package com.tokopedia.kol.feature.post.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.kol.R;
import com.tokopedia.kol.analytics.KolEnhancedTracking;
import com.tokopedia.kol.common.util.TimeConverter;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.util.KolGlideRequestListener;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;

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
    private final KolPostListener.View.ViewHolder viewListener;
    private TextView title;
    private TextView name;
    private ImageView avatar;
    private TextView label;
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
    private View topSeparator;
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
        title = itemView.findViewById(R.id.title);
        name = itemView.findViewById(R.id.name);
        avatar = itemView.findViewById(R.id.avatar);
        label = itemView.findViewById(R.id.label);
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
        topSeparator = itemView.findViewById(R.id.separator2);
    }

    @Override
    public void bind(KolPostViewModel element) {
        if (type == Type.PROFILE) {
            title.setVisibility(View.GONE);
            if (getAdapterPosition() == 0 ) {
                topShadow.setVisibility(View.VISIBLE);
            } else {
                topShadow.setVisibility(View.GONE);
            }
        } else if (type == Type.FEED) {
            if (TextUtils.isEmpty(element.getTitle())) {
                title.setVisibility(View.GONE);
            } else {
                title.setVisibility(View.VISIBLE);
                title.setText(MethodChecker.fromHtml(element.getTitle()));
            }
        }

        name.setText(MethodChecker.fromHtml(element.getName()));
        ImageHandler.loadImageCircle2(avatar.getContext(), avatar, element.getAvatar());

        if (element.isFollowed()) {
            //TODO milhamj unify this time
            label.setText(TimeConverter.generateTime(label.getContext(), element.getTime()));
        } else {
            label.setText(element.getLabel());
        }

        if (element.isFollowed() && !element.isTemporarilyFollowed()) {
            followButton.setVisibility(View.GONE);
            topSeparator.setVisibility(View.GONE);
        } else if (element.isFollowed() && element.isTemporarilyFollowed()) {
            followButton.setVisibility(View.VISIBLE);
            followButton.setBackground(MethodChecker.getDrawable(followButton.getContext(),
                    R.drawable.bg_button_white_border));
            followText.setText(R.string.following);
            followText.setTextColor(MethodChecker.getColor(followText.getContext(),
                    R.color.black_54));
            topSeparator.setVisibility(View.VISIBLE);
        } else {
            followButton.setVisibility(View.VISIBLE);
            followButton.setBackground(MethodChecker.getDrawable(followButton.getContext(),
                    R.drawable.bg_button_green));
            followText.setTextColor(MethodChecker.getColor(followText.getContext(),
                    R.color.white));
            followText.setText(R.string.action_follow_english);
            topSeparator.setVisibility(View.VISIBLE);
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
                if (type == Type.FEED) {
                    //TODO milhamj tracking
//                    UnifyTracking.eventKolContentGoToProfilePage(element.isFollowed(), element.getTagsType());
                    viewListener.onGoToKolProfile(element.getPage(),
                            getAdapterPosition(),
                            String.valueOf(element.getUserId()),
                            element.getContentId()
                    );
                }
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UnifyTracking.eventKolContentGoToProfilePage(element.isFollowed(), element.getTagsType());
                viewListener.onGoToKolProfile(element.getPage(),
                        getAdapterPosition(),
                        String.valueOf(element.getUserId()),
                        element.getContentId()
                );
            }
        });

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (element.isFollowed()) {
//                    UnifyTracking.eventKolContentUnfollowClick(element.getTagsType());
                    viewListener.onUnfollowKolClicked(element.getPage(), getAdapterPosition(),
                            element.getUserId());
                } else {
//                    UnifyTracking.eventKolContentFollowClick(element.getTagsType());
                    viewListener.onFollowKolClicked(element.getPage(), getAdapterPosition(),
                            element.getUserId());
                }
            }
        });

        kolText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kolText.getText().toString().endsWith(
                        kolText.getContext().getString(R.string.read_more_english))) {
//                    UnifyTracking.eventKolContentReadMoreClick(element.isFollowed(), element.getTagsType());
                    kolText.setText(element.getReview());
                    element.setReviewExpanded(true);
                }
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (element.isLiked()) {
//                    UnifyTracking.eventKolContentUnlike(element.isFollowed(), element.getTagsType());
                    viewListener.onUnlikeKolClicked(element.getPage(), getAdapterPosition(),
                            element.getId());
                } else {
//                    UnifyTracking.eventKolContentLike(element.isFollowed(), element.getTagsType());
                    viewListener.onLikeKolClicked(element.getPage(), getAdapterPosition(), element.getId());
                }
            }
        });

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UnifyTracking.eventKolContentCommentClick(element.isFollowed(), element.getTagsType());
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
                            + "<font color='#42b549'><b>"
                            + kolText.getContext().getString(R.string.read_more_english)
                            + "</b></font>");
        } else {
            return MethodChecker.fromHtml(element.getReview().replaceAll("(\r\n|\n)", "<br />"));
        }
    }

    private void tooltipAreaClicked(KolPostViewModel element) {
        List<KolEnhancedTracking.Promotion> promotionList = new ArrayList<>();
        promotionList.add(new KolEnhancedTracking.Promotion(
                element.getId(),
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
//        Feed tracking
//        UnifyTracking.eventKolContentCtaClick(element.isFollowed(), element.getTagsType());
//        List<FeedEnhancedTracking.Promotion> list = new ArrayList<>();
//        list.add(new FeedEnhancedTracking.Promotion(
//                element.getId(),
//                FeedEnhancedTracking.Promotion.createContentName(
//                        element.getTagsType(),
//                        element.getCardType())
//                ,
//                element.getName().equals("")? "-" : element.getName(),
//                getAdapterPosition(),
//                element.getLabel().equals("")? "-" : element.getLabel(),
//                element.getContentId(),
//                element.getContentLink().equals("")? "-" : element.getContentLink()
//        ));

        viewListener.getAbstractionRouter()
                .getAnalyticTracker()
                .sendEnhancedEcommerce(KolEnhancedTracking.getKolClickTracking(promotionList));

        viewListener.onOpenKolTooltip(element.getPage(), getAdapterPosition(),
                element.getContentLink());
    }

}
