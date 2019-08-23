package com.tokopedia.imagepicker.picker.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.VideoView;

import com.tokopedia.imagepicker.R;

public class VideoPlayerView extends VideoView {
    private boolean shouldMeasure = true;
    private int mVideoWidth = 0;
    private int mVideoHeight = 0;

    public VideoPlayerView(Context context) {
        super(context);
    }

    public VideoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public VideoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VideoPlayerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray attributeArray = getContext().obtainStyledAttributes(attrs, R.styleable.VideoPlayerView);
        shouldMeasure = attributeArray.getBoolean(R.styleable.VideoPlayerView_onMeasure, true);
        attributeArray.recycle();
    }

    public void setSize(int width, int height){
        mVideoHeight = height;
        mVideoWidth = width;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (shouldMeasure){
            int width = View.getDefaultSize(mVideoWidth, widthMeasureSpec);
            int height = View.getDefaultSize(mVideoHeight, heightMeasureSpec);
            if (mVideoWidth > 0 && mVideoHeight > 0) {
                if (mVideoWidth * height > width * mVideoHeight) {
                    height = width * mVideoHeight / mVideoWidth;
                } else if (mVideoWidth * height < width * mVideoHeight) {
                    width = height * mVideoWidth / mVideoHeight;
                }
            }
            setMeasuredDimension(width, height);
        } else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
