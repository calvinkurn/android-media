package com.tokopedia.core.product.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.tokopedia.core.R;
import com.tokopedia.core.product.model.goldmerchant.VideoData;

/**
 * Created by kris on 11/3/16. Tokopedia
 */

public class VideoLayout extends LinearLayout{

    private ProductVideoHorizontalScroll productVideoHorizontalScroll;

    public VideoLayout(Context context) {
        super(context);
        initView(context);
    }

    public VideoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public VideoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.youtube_video_list_place_holder, this, true);
        productVideoHorizontalScroll = (ProductVideoHorizontalScroll)
                findViewById(R.id.product_video_horizontal_scroll);
    }

    public void renderData(VideoData data) {
        productVideoHorizontalScroll.renderData(data);
    }

    public void destroyVideoLayoutProcess() {
        productVideoHorizontalScroll.destroyAllOnGoingYoutubeProcess();
    }

    public void refreshVideo() {
        productVideoHorizontalScroll.clearYoutubeVideo();
    }
}