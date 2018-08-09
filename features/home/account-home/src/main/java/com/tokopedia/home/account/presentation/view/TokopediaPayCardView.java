package com.tokopedia.home.account.presentation.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.AccountHomeRouter;

/**
 * @author okasurya on 7/18/18.
 */
public class TokopediaPayCardView extends BaseCustomView {
    private static final String KEY_IMAGE_HOST = "image_host";
    private static final String TOKOPEDIA_PAY_BG_NAME = "bg_tokopedia_pay.png";

    private LinearLayout layoutAction;
    private TextView actionText;
    private TextView textAmountLeft;
    private TextView textDescLeft;
    private TextView textAmountRight;
    private TextView textDesctRight;
    private LinearLayout layoutLeft;
    private LinearLayout layoutRight;
    private View container;

    public TokopediaPayCardView(@NonNull Context context) {
        super(context);
        init();
    }

    public TokopediaPayCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TokopediaPayCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_tokopedia_pay_card, this);
        container = view.findViewById(R.id.container);
        layoutAction = view.findViewById(R.id.layout_action);
        actionText = view.findViewById(R.id.text_action);
        textAmountLeft = view.findViewById(R.id.text_amount_left);
        textDescLeft = view.findViewById(R.id.text_desc_left);
        textAmountRight = view.findViewById(R.id.text_amount_right);
        textDesctRight = view.findViewById(R.id.text_desc_right);
        layoutLeft = view.findViewById(R.id.layout_left);
        layoutRight = view.findViewById(R.id.layout_right);

        ImageHandler.loadImageBitmap2(getContext(),
                getBackgroundImageUri(),
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(getContext().getResources(), resource);
                        container.setBackground(drawable);
                    }
                });
    }

    private String getBackgroundImageUri() {
        String imageUrl = AccountConstants.Url.IMAGE_URL;
        if(getContext().getApplicationContext() instanceof AccountHomeRouter) {
            imageUrl = ((AccountHomeRouter) getContext().getApplicationContext())
                    .getStringRemoteConfig(KEY_IMAGE_HOST, AccountConstants.Url.CDN_URL);
            imageUrl = imageUrl + AccountConstants.Url.CDN_IMAGE_PATH;
        }

        return imageUrl + TOKOPEDIA_PAY_BG_NAME;
    }

    public void setActionText(@NonNull String text) {
        this.actionText.setText(text);
    }

    public void setTextAmountLeft(@NonNull String text) {
        this.textAmountLeft.setText(text);
    }

    public void setTextDescLeft(@NonNull String text) {
        this.textDescLeft.setText(text);
    }

    public void setTextAmountRight(@NonNull String text) {
        this.textAmountRight.setText(text);
    }

    public void setTextDesctRight(@NonNull String text) {
        textDesctRight.setText(text);
    }

    public void setActionTextClickListener(View.OnClickListener listener) {
        layoutAction.setOnClickListener(listener);
    }

    public void setLeftItemClickListener(View.OnClickListener listener) {
        layoutLeft.setOnClickListener(listener);
    }

    public void setRightItemClickListener(View.OnClickListener listener) {
        layoutRight.setOnClickListener(listener);
    }
}
