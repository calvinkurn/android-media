package com.tokopedia.kol.feature.post.view.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.kol.R;
import com.tokopedia.kol.common.util.TimeConverter;
import com.tokopedia.kol.common.util.UrlUtil;
import com.tokopedia.kol.feature.post.view.listener.BaseKolListener;
import com.tokopedia.kol.feature.post.view.viewmodel.BaseKolViewModel;

/**
 * @author by milhamj on 09/05/18.
 */

public class BaseKolView extends BaseCustomView {

    private static final int MAX_CHAR = 175;

    private FrameLayout contentLayout;
    private TextView kolText;
    private ImageView likeIcon;
    private TextView likeText;
    private TextView commentText;
    private View commentButton;
    private View likeButton;
    private View menuButton;
    private TextView title;
    private TextView name;
    private ImageView avatar;
    private ImageView badge;
    private TextView label;
    private TextView followText;
    private View followButton;
    private View topSeparator;

    public BaseKolView(@NonNull Context context) {
        super(context);
        init();
    }

    public BaseKolView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseKolView(@NonNull Context context, @Nullable AttributeSet attrs,
                       int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.base_kol, this);
        contentLayout = view.findViewById(R.id.content);
        kolText = view.findViewById(R.id.kol_text);
        likeIcon = view.findViewById(R.id.like_icon);
        likeText = view.findViewById(R.id.like_text);
        commentText = view.findViewById(R.id.comment_text);
        commentButton = view.findViewById(R.id.comment_button);
        menuButton = view.findViewById(R.id.menu_button);
        likeButton = view.findViewById(R.id.like_button);
        title = view.findViewById(R.id.title);
        name = view.findViewById(R.id.name);
        avatar = view.findViewById(R.id.avatar);
        badge = view.findViewById(R.id.kol_badge);
        label = view.findViewById(R.id.label);
        followText = view.findViewById(R.id.follow_text);
        followButton = view.findViewById(R.id.follow_button);
        topSeparator = view.findViewById(R.id.separator2);
    }

    public View inflateContentLayout(int layoutRes) {
        View contentView = inflate(getContext(), layoutRes, null);
        contentLayout.addView(contentView);
        return contentView;
    }

    public void bind(BaseKolViewModel element) {
        name.setText(MethodChecker.fromHtml(element.getName()));
        ImageHandler.loadImageCircle2(avatar.getContext(), avatar, element.getAvatar());

        if (TextUtils.isEmpty(element.getTitle())) {
            title.setVisibility(View.GONE);
            topSeparator.setVisibility(View.GONE);
        } else {
            title.setVisibility(View.VISIBLE);
            title.setText(MethodChecker.fromHtml(element.getTitle()));
            topSeparator.setVisibility(View.VISIBLE);
        }

        if (element.isFollowed()) {
            label.setText(TimeConverter.generateTime(label.getContext(), element.getTime()));
        } else {
            label.setText(element.getLabel());
        }

        if (!TextUtils.isEmpty(element.getReview())) {
            kolText.setVisibility(View.VISIBLE);
            setKolText(element);
        } else {
            kolText.setVisibility(View.GONE);
        }

        bindLike(element.isLiked(), element.getTotalLike());
        bindComment(element.getTotalComment());
        bindFollow(element.isFollowed(), element.isTemporarilyFollowed());

        commentButton.setVisibility(element.isShowComment() ? View.VISIBLE : View.GONE);
        likeButton.setVisibility(element.isShowLike() ? View.VISIBLE : View.GONE);
        badge.setVisibility(element.isKol() ? View.VISIBLE : View.GONE);
        menuButton.setVisibility(shouldShowMenu(element) ? View.VISIBLE : View.GONE);
    }

    public void setViewListener(final BaseKolListener viewListener, final BaseKolViewModel element) {
        avatar.setOnClickListener(v -> viewListener.onAvatarClickListener(element));

        name.setOnClickListener(v -> viewListener.onNameClickListener(element));

        followButton.setOnClickListener(v -> viewListener.onFollowButtonClickListener(element));

        kolText.setOnClickListener(v -> {
            if (kolText.getText().toString().endsWith(
                    kolText.getContext().getString(R.string.read_more_english))) {
                element.setReviewExpanded(true);
                setKolText(element);

                viewListener.onDescriptionClickListener(element);
            }
        });

        likeButton.setOnClickListener(v -> viewListener.onLikeButtonClickListener(element));

        commentButton.setOnClickListener(v -> viewListener.onCommentClickListener(element));

        menuButton.setOnClickListener(v -> viewListener.onMenuClickListener(element));
    }

    public void onViewRecycled() {
        ImageHandler.clearImage(avatar);
    }

    public void bindLike(boolean isLiked, int totalLike) {
        if (isLiked) {
            ImageHandler.loadImageWithIdWithoutPlaceholder(likeIcon, R.drawable.ic_thumb_green);
            likeText.setText(String.valueOf(totalLike));
            likeText.setTextColor(MethodChecker.getColor(likeText.getContext(), R.color
                    .tkpd_main_green));

        } else if (totalLike > 0) {
            ImageHandler.loadImageWithIdWithoutPlaceholder(likeIcon, R.drawable.ic_thumb);
            likeText.setText(String.valueOf(totalLike));
            likeText.setTextColor(MethodChecker.getColor(likeText.getContext(), R.color
                    .black_54));
        } else {
            ImageHandler.loadImageWithIdWithoutPlaceholder(likeIcon, R.drawable.ic_thumb);
            likeText.setText(R.string.action_like);
            likeText.setTextColor(MethodChecker.getColor(likeIcon.getContext(), R.color
                    .black_54));
        }
    }

    public void bindComment(int totalComment) {
        if (totalComment == 0) {
            commentText.setText(R.string.comment);
        } else {
            commentText.setText(String.valueOf(totalComment));
        }
    }

    public void bindFollow(boolean isFollowed, boolean isTemporarilyFollowed) {
        if (isFollowed && !isTemporarilyFollowed) {
            followButton.setVisibility(View.GONE);
        } else if (isFollowed && isTemporarilyFollowed) {
            followButton.setVisibility(View.VISIBLE);
            followButton.setBackground(MethodChecker.getDrawable(followButton.getContext(),
                    R.drawable.bg_button_white_border));
            followText.setText(R.string.following);
            followText.setTextColor(MethodChecker.getColor(followText.getContext(),
                    R.color.black_54));
        } else {
            followButton.setVisibility(View.VISIBLE);
            followButton.setBackground(MethodChecker.getDrawable(followButton.getContext(),
                    R.drawable.bg_button_green));
            followText.setTextColor(MethodChecker.getColor(followText.getContext(),
                    R.color.white));
            followText.setText(R.string.action_follow_english);
        }
    }

    private boolean shouldShowMenu(BaseKolViewModel baseKolViewModel) {
        return baseKolViewModel.isDeletable() || baseKolViewModel.isReportable();
    }

    private void setKolText(final BaseKolViewModel element) {
        if (element.getReviewUrlClickableSpan() != null) {
            UrlUtil.setTextWithClickableTokopediaUrl(kolText,
                    getKolText(element),
                    element.getReviewUrlClickableSpan());
        } else {
            UrlUtil.setTextWithClickableTokopediaUrl(kolText, getKolText(element));
        }
    }

    private String getKolText(BaseKolViewModel element) {
        if (!element.isReviewExpanded() && MethodChecker.fromHtml(element.getReview()).length() >
                MAX_CHAR) {
            String subDescription = MethodChecker.fromHtml(element.getReview()).toString().substring(0,
                    MAX_CHAR);
            return subDescription.replaceAll("(\r\n|\n)", "<br />") + "... "
                    + "<font color='#42b549'><b>"
                    + kolText.getContext().getString(R.string.read_more_english)
                    + "</b></font>";
        } else {
            return element.getReview().replaceAll("(\r\n|\n)", "<br />");
        }
    }
}
