package com.tokopedia.feedplus.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.viewmodel.EmptyFeedBeforeLoginModel;

/**
 * Created by meyta on 1/29/18.
 */

public class EmptyFeedBeforeLoginViewHolder extends AbstractViewHolder<EmptyFeedBeforeLoginModel> {

    private static final String ANDROID_IMAGE_URL = "https://ecs7.tokopedia.net/img/android";
    private static final String FINISH_IMAGE_NAME = "ic_empty_feed_nonlogin";
    private static final String IMAGE_URL_FORMAT = "%s/%s/%s/%s.png";

    @LayoutRes public static final int LAYOUT = R.layout.list_feed_before_login;

    public EmptyFeedBeforeLoginViewHolder(View itemView, final FeedPlus.View viewListener) {
        super(itemView);
        String imageUrl = String.format(IMAGE_URL_FORMAT,
                ANDROID_IMAGE_URL,
                FINISH_IMAGE_NAME,
                DisplayMetricUtils.getScreenDensity(itemView.getContext()),
                FINISH_IMAGE_NAME
        );

        Button button = itemView.findViewById(R.id.btn_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onGoToLogin();
            }
        });
        ImageView imageView = itemView.findViewById(R.id.iv_image);
        ImageHandler.loadImage2(imageView, imageUrl, R.drawable.ic_loading_image);
    }

    @Override
    public void bind(EmptyFeedBeforeLoginModel element) {}
}