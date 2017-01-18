package com.tokopedia.core.product.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.tokopedia.core.R;
import com.tokopedia.core.product.model.goldmerchant.VideoData;

import java.util.List;

/**
 * Created by kris on 11/3/16. Tokopedia
 */

public class ProductVideoHorizontalScroll extends HorizontalScrollView {

    private LinearLayout placeHolder;

    private List<YoutubeThumbnailViewHolder> youtubeThumbnailList;

    public ProductVideoHorizontalScroll(Context context) {
        super(context);
        initView(context);
    }

    public ProductVideoHorizontalScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ProductVideoHorizontalScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.horizontal_scroll_youtube_view, this, true);
        placeHolder = (LinearLayout) findViewById(R.id.youtube_video_place_holder);
    }

    public void renderData(VideoData data) {
        if(YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(getContext().getApplicationContext())
                .equals(YouTubeInitializationResult.SUCCESS)) {
            for (int i = 0; i < data.getVideo().size(); i++) {
                placeHolder.addView(new YoutubeThumbnailViewHolder(getContext(), data, i));
            }
        } else {
            for(int i = 0; i < data.getVideo().size(); i++) {
                placeHolder.addView(new YoutubeWebViewThumbnail(getContext(),
                        data.getVideo().get(i).getUrl()));
            }
        }
    }

    public void destroyAllOnGoingYoutubeProcess() {
        for (int i = 0; i < youtubeThumbnailList.size(); i++) {
            youtubeThumbnailList.get(i).destroyReleaseProcess();
        }
    }
}