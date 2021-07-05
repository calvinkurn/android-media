package com.tokopedia.feedcomponent.util;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.feedcomponent.R;
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.MediaItem;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder;
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.MultimediaGridViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.post.video.VideoViewModel;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;

import java.util.List;

/**
 * @author by yfsx on 13/06/19.
 */
public class FeedScrollListener {

    private static final int THRESHOLD_VIDEO_HEIGHT_SHOWN = 75;
    private static final String TYPE_VIDEO = "video";

    public static void onFeedScrolled(RecyclerView recyclerView, List<Visitable> list) {
        if (canAutoplayVideo(recyclerView)) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int firstPosition = layoutManager.findFirstVisibleItemPosition();
            int lastPosition = layoutManager.findLastVisibleItemPosition();
            for (int i = firstPosition; i <= lastPosition; i++) {
                if (isVideoCard(list, i)) {
                    VideoViewModel item = getVideoCardViewModel(list, i);
                    if (item != null) {
                        getVideoModelScrollListener(item, layoutManager, recyclerView, i);
                    } else {
                        MediaItem mediaItem = getVideoCardItemViewModel(list, i);
                        getVideoCardModelScrollListener(mediaItem, layoutManager, recyclerView, i);
                    }
                }
            }
        }
    }

    private static void getVideoModelScrollListener(VideoViewModel item, LinearLayoutManager layoutManager, RecyclerView recyclerView, int i) {
        Rect rvRect = new Rect();
        recyclerView.getGlobalVisibleRect(rvRect);
        Rect rowRect = new Rect();
        View view = layoutManager.findViewByPosition(i);
        if (view != null) {
            view.getGlobalVisibleRect(rowRect);
            Rect videoViewRect = new Rect();
            View imageView = view.findViewById(R.id.image);
            if (imageView != null) {
                view.findViewById(R.id.image).getGlobalVisibleRect(videoViewRect);
                int percentVideo;
                int visibleVideo;
                if (rowRect.bottom >= rvRect.bottom) {
                    visibleVideo = rvRect.bottom - videoViewRect.top;
                } else {
                    visibleVideo = videoViewRect.bottom - rvRect.top;
                }
                percentVideo = (visibleVideo * 100) / imageView.getHeight();
                boolean isStateChanged = false;
                if (percentVideo > THRESHOLD_VIDEO_HEIGHT_SHOWN) {
                    if (!item.getCanPlayVideo()) isStateChanged = true;
                    item.setCanPlayVideo(true);
                } else {
                    if (item.getCanPlayVideo()) isStateChanged = true;
                    item.setCanPlayVideo(false);
                }
                if (isStateChanged) {
                    recyclerView.getAdapter().notifyItemChanged(i, DynamicPostViewHolder.PAYLOAD_PLAY_VIDEO);
                }
            }
        }
    }

    private static void getVideoCardModelScrollListener(MediaItem item, LinearLayoutManager layoutManager, RecyclerView recyclerView, int i) {
        Rect rvRect = new Rect();
        recyclerView.getGlobalVisibleRect(rvRect);
        Rect rowRect = new Rect();
        View view = layoutManager.findViewByPosition(i);
        if (view != null) {
            view.getGlobalVisibleRect(rowRect);
            Rect videoViewRect = new Rect();
            View imageView = view.findViewById(R.id.image);
            imageView.getGlobalVisibleRect(videoViewRect);
            if (imageView != null && imageView.getHeight() != 0) {
                int percentVideo;
                int visibleVideo;
                if (rowRect.bottom >= rvRect.bottom) {
                    visibleVideo = rvRect.bottom - videoViewRect.top;
                } else {
                    visibleVideo = videoViewRect.bottom - rvRect.top;
                }
                percentVideo = (visibleVideo * 100) / imageView.getHeight();
                boolean isStateChanged = false;
                if (percentVideo > THRESHOLD_VIDEO_HEIGHT_SHOWN) {
                    if (!item.isCanPlayVideo()) isStateChanged = true;
                    item.setCanPlayVideo(true);
                } else {
                    if (item.isCanPlayVideo()) isStateChanged = true;
                    item.setCanPlayVideo(false);
                }
                if (isStateChanged && recyclerView.getAdapter() != null) {
                    recyclerView.getAdapter().notifyItemChanged(i, DynamicPostViewHolder.PAYLOAD_PLAY_VIDEO);
                }
            }
        }
    }

    private static boolean isVideoCard(List<Visitable> list, int position) {
        return list.size() > position
                && list.get(position) instanceof DynamicPostViewModel
                && ((DynamicPostViewModel) list.get(position)).getContentList().size() == 1
                && (((DynamicPostViewModel) list.get(position)).getContentList().get(0) instanceof VideoViewModel
                || ((((DynamicPostViewModel) list.get(position)).getContentList().get(0) instanceof MultimediaGridViewModel)
                && ((MultimediaGridViewModel) ((DynamicPostViewModel) list.get(position)).getContentList().get(0)).getMediaItemList().size() == 1
                && ((MultimediaGridViewModel) ((DynamicPostViewModel) list.get(position)).getContentList().get(0)).getMediaItemList().get(0).getType().equalsIgnoreCase(TYPE_VIDEO)));
    }

    private static VideoViewModel getVideoCardViewModel(List<Visitable> list, int position) {
        try {
            return (VideoViewModel) ((DynamicPostViewModel) list.get(position)).getContentList().get(0);
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return null;
    }

    private static MediaItem getVideoCardItemViewModel(List<Visitable> list, int position) {
        return ((MultimediaGridViewModel) ((DynamicPostViewModel) list.get(position)).getContentList().get(0)).getMediaItemList().get(0);
    }

    private static boolean canAutoplayVideo(RecyclerView recyclerView) {
        RemoteConfig config = new FirebaseRemoteConfigImpl(recyclerView.getContext());
        return config.getBoolean(RemoteConfigKey.CONFIG_AUTOPLAY_VIDEO_WIFI, false);
    }

}
