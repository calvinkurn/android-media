package com.tokopedia.kol.feature.post.view.adapter.viewholder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.project.youtubeutils.common.YoutubeInitializer;
import com.project.youtubeutils.common.YoutubePlayerConstant;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.kol.R;
import com.tokopedia.kol.feature.post.view.activity.KolPostYouTubeActivity;
import com.tokopedia.kol.feature.post.view.listener.BaseKolListener;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.viewmodel.BaseKolViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostYoutubeViewModel;
import com.tokopedia.kol.feature.post.view.widget.BaseKolView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * @author by yfsx on 28/06/17.
 */

public class KolPostYoutubeViewHolder extends AbstractViewHolder<KolPostYoutubeViewModel>
        implements BaseKolListener, YoutubeInitializer.OnVideoThumbnailInitialListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.kol_post_youtube_layout;

    private static final String DASH = "-";

    private final KolPostListener.View.ViewHolder viewListener;
    private BaseKolView baseKolView;
    private ImageView ivPlay;
    private ProgressBar loadingBar;
    private RelativeLayout mainView;
    private YouTubeThumbnailLoader youTubeThumbnailLoader;
    private YouTubeThumbnailView thumbnailView;
    private View topShadow;
    private Type type;

    public enum Type {
        PROFILE, FEED
    }

    public KolPostYoutubeViewHolder(View itemView,
                                    KolPostListener.View.ViewHolder viewListener,
                                    Type type) {
        super(itemView);
        this.viewListener = viewListener;
        this.type = type;
        topShadow = itemView.findViewById(R.id.top_shadow);

        baseKolView = itemView.findViewById(R.id.base_kol_view);
        View view = baseKolView.inflateContentLayout(R.layout.kol_post_content_youtube);
        mainView = view.findViewById(R.id.main_view);
        ivPlay = view.findViewById(R.id.iv_play);
        loadingBar = view.findViewById(R.id.progress_bar);
        thumbnailView = view.findViewById(R.id.view_youtube_thumbnail);
        thumbnailView.setMinimumWidth(mainView.getWidth());
    }

    @Override
    public void bind(KolPostYoutubeViewModel element) {
        ivPlay.setVisibility(GONE);
        baseKolView.bind(element);

        if (type == Type.PROFILE && getAdapterPosition() == 0) {
            topShadow.setVisibility(View.VISIBLE);
        } else {
            topShadow.setVisibility(View.GONE);
        }
        baseKolView.setViewListener(this, element);
        thumbnailView.initialize(YoutubePlayerConstant.GOOGLE_API_KEY,
                YoutubeInitializer.videoThumbnailInitializer(element.getYoutubeLink(), this));
        thumbnailView.setOnClickListener(onYoutubeThumbnailClickedListener(element.getYoutubeLink()));
    }

    @Override
    public void onSuccessInitializeThumbnail(YouTubeThumbnailLoader loader, YouTubeThumbnailView youTubeThumbnailView) {
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
        loadingBar.setVisibility(GONE);
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
            viewListener.onUnlikeKolClicked(getAdapterPosition(), element.getKolId());
        } else {
            viewListener.onLikeKolClicked(getAdapterPosition(), element.getKolId());
        }
    }

    @Override
    public void onCommentClickListener(BaseKolViewModel element) {

        viewListener.onGoToKolComment(getAdapterPosition(), element.getKolId());
    }

    private void goToProfile(final BaseKolViewModel element) {
        viewListener.onGoToKolProfile(getAdapterPosition(),
                String.valueOf(element.getUserId()),
                element.getKolId()
        );
    }


    private View.OnClickListener onYoutubeThumbnailClickedListener(String videoUrl) {
        return v -> {
            Intent intent = KolPostYouTubeActivity.getInstance(viewListener.getContext(), videoUrl);
            viewListener.getContext().startActivity(intent);
        };
    }

    private void destroyReleaseProcess() {
        if (youTubeThumbnailLoader != null) youTubeThumbnailLoader.release();
    }

}
