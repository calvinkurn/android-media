package com.tokopedia.imagepicker.picker.instagram.view.adapter;

import android.view.View;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.picker.instagram.view.model.InstagramErrorLoginModel;

/**
 * Created by zulfikarrahman on 5/7/18.
 */

public class InstagramErrorLoginViewHolder extends AbstractViewHolder<InstagramErrorLoginModel> {
    public static final int LAYOUT = R.layout.layout_instagram_error_login;
    private final Button loginInstagram;

    public InstagramErrorLoginViewHolder(View itemView) {
        super(itemView);
        loginInstagram = itemView.findViewById(com.tokopedia.abstraction.R.id.button_retry);
    }

    @Override
    public void bind(final InstagramErrorLoginModel element) {
        loginInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InstagramErrorLoginModel.ListenerLoginInstagram listenerLoginInstagram = element.getListenerLoginInstagram();
                if(listenerLoginInstagram != null){
                    listenerLoginInstagram.onClickLoginInstagram();
                }
            }
        });
    }
}
