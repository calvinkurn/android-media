package com.tokopedia.kol.feature.post.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.kol.R;
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
        implements BaseKolListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.kol_post_youtube_layout;

    private final static String GOOGLE_API_KEY = "AIzaSyCRkgwGBe8ZxjcK07Cnl3Auf72BpgA6lLo";
    private static final String DASH = "-";

//    private final YouTubeThumbnailLoadInProcess youTubeThumbnailLoadInProcess;
    private YouTubeThumbnailLoader youTubeThumbnailLoader;
    private final KolPostListener.View.ViewHolder viewListener;
    private BaseKolView baseKolView;
    private ImageView ivPlay;
    private ProgressBar loadingBar;
    private RelativeLayout mainView;
    private YouTubeThumbnailView thumbnailView;
    private View topShadow;
    private Type type;

    public enum Type {
        PROFILE, FEED
    }

    public KolPostYoutubeViewHolder(View itemView,
                                    KolPostListener.View.ViewHolder viewListener,
                                    Type type
//                                    YouTubeThumbnailLoadInProcess youTubeThumbnailLoadInProcess
    ) {
        super(itemView);
        this.viewListener = viewListener;
        this.type = type;
        topShadow = itemView.findViewById(R.id.top_shadow);
//        this.youTubeThumbnailLoadInProcess = youTubeThumbnailLoadInProcess;

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
        thumbnailView.initialize(GOOGLE_API_KEY,
                thumbnailInitializedListener(element.getYoutubeLink()));
//        if(youTubeThumbnailLoadInProcess != null)
//            youTubeThumbnailLoadInProcess.onIntializationStart();
        thumbnailView.setOnClickListener(onYoutubeThumbnailClickedListener(element.getYoutubeLink()));
    }

    private YouTubeThumbnailView
            .OnInitializedListener thumbnailInitializedListener(final String youtubeVideoId) {
        return new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView,
                                                final YouTubeThumbnailLoader loader) {
//                if(youTubeThumbnailLoadInProcess != null)
//                    youTubeThumbnailLoadInProcess.onIntializationComplete();
                youTubeThumbnailLoader = loader;
                loader.setVideo(youtubeVideoId);
                loader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader
                        .OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView,
                                                  String s) {
                        loader.release();
                        ivPlay.setVisibility(VISIBLE);
                        loadingBar.setVisibility(GONE);
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView,
                                                 YouTubeThumbnailLoader.ErrorReason errorReason) {
                        ivPlay.setVisibility(VISIBLE);
                        loadingBar.setVisibility(GONE);

                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView,
                                                YouTubeInitializationResult result) {
//                if(youTubeThumbnailLoadInProcess != null)
//                    youTubeThumbnailLoadInProcess.onIntializationComplete();
                loadingBar.setVisibility(GONE);
            }
        };
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
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(viewListener.getContext(), YoutubeIntermediaryActivity.class);
//                intent.putExtra(YoutubeIntermediaryActivity.EXTRA_YOUTUBE_VIDEO_URL, videoUrl);
//                viewListener.getContext().startActivity(intent);
            }
        };
    }

    public void destroyReleaseProcess() {
        youTubeThumbnailLoader.release();
    }

//    public interface YouTubeThumbnailLoadInProcess {
//        public void onIntializationStart();
//        public void onIntializationComplete();
//    }
}
