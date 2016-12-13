package com.tokopedia.core.product.customview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by kris on 11/22/16. Tokopedia
 */

public class YoutubeWebViewThumbnail extends RelativeLayout{

    /*@Bind(R2.id.thumbnail_webview)
    WebView thumbnailWebView;*/

    @BindView(R2.id.thumbnail)
    ImageView thumbnail;


    public YoutubeWebViewThumbnail(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public YoutubeWebViewThumbnail(Context context) {
        super(context);
    }

    public YoutubeWebViewThumbnail(Context context, String videoId) {
        super(context);
        initView(context, videoId);
    }

    public YoutubeWebViewThumbnail(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initView (Context context, String videoId) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.youtube_thumbnail_webview, this, true);
        ButterKnife.bind(this);

        Glide.with(getContext())
                .load("http://img.youtube.com/vi/"+videoId+"/1.jpg").into(thumbnail);

        setOnClickListener(onThumnailClickedListener(videoId));
    }

    private OnClickListener onThumnailClickedListener(final String videoId) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + videoId)));
            }
        };
    }

}