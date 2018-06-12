package com.tokopedia.kol.feature.post.view.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
    private TextView title;
    private TextView name;
    private ImageView avatar;
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
        likeButton = view.findViewById(R.id.like_button);
        title = view.findViewById(R.id.title);
        name = view.findViewById(R.id.name);
        avatar = view.findViewById(R.id.avatar);
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
        } else {
            title.setVisibility(View.VISIBLE);
            title.setText(MethodChecker.fromHtml(element.getTitle()));
        }

        if (element.isFollowed()) {
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

        UrlUtil.setTextWithClickableTokopediaUrl(kolText, getKolText(element));

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
    }

    public void setViewListener(final BaseKolListener viewListener, final BaseKolViewModel element) {
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onAvatarClickListener(element);
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onNameClickListener(element);
            }
        });

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onFollowButtonClickListener(element);
            }
        });

        kolText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kolText.getText().toString().endsWith(
                        kolText.getContext().getString(R.string.read_more_english))) {
                    UrlUtil.setTextWithClickableTokopediaUrl(kolText, element.getReview());
                    element.setReviewExpanded(true);

                    viewListener.onDescriptionClickListener(element);
                }
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onLikeButtonClickListener(element);
            }
        });

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onCommentClickListener(element);
            }
        });
    }

    public void onViewRecycled() {
        ImageHandler.clearImage(avatar);
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
