package com.tokopedia.challenges.view.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class CustomVideoView extends VideoView {
    private int width;
    private int height;

    public CustomVideoView(Context context) {
        super(context);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setVideoDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int videoWidth = getDefaultSize(this.width, widthMeasureSpec);
        int videoHeight = getDefaultSize(this.height, heightMeasureSpec);

        if (width > 0 && height > 0) {
            if (width * videoHeight > videoWidth * height) {
                videoHeight = videoWidth * height / width;
            } else if (width * videoHeight < videoWidth * height) {
                videoWidth = videoHeight * width / height;
            } else {

            }
        }
        setMeasuredDimension(videoWidth, videoHeight);
    }
}
