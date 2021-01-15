package com.tokopedia.home.account.presentation.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.home.account.AccountHomeUrl;
import com.tokopedia.home.account.R;

/**
 * @author okasurya on 7/18/18.
 */
public class TokopediaPayCardView extends BaseCustomView {
    private static final String TOKOPEDIA_PAY_BG_NAME = "bg_tokopedia_pay.png";

    private LinearLayout layoutAction;
    private TextView actionText;
    private TextView textAmountLeft;
    private TextView textDescLeft;
    private TextView textAmountCentre;
    private TextView textDescCentre;
    private TextView textAmountRight;
    private TextView textDesctRight;
    private LinearLayout layoutLeft;
    private LinearLayout layoutCentre;
    private LinearLayout layoutRight;
    private View container;
    private ImageView iconLeft, iconCentre, iconRight;

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
        textAmountCentre = view.findViewById(R.id.text_amount_centre);
        textDescCentre = view.findViewById(R.id.text_desc_centre);
        textAmountRight = view.findViewById(R.id.text_amount_right);
        textDesctRight = view.findViewById(R.id.text_desc_right);
        layoutLeft = view.findViewById(R.id.layout_left);
        layoutCentre = view.findViewById(R.id.layout_centre);
        layoutRight = view.findViewById(R.id.layout_right);
        iconLeft = view.findViewById(R.id.card_icon_left);
        iconCentre = view.findViewById(R.id.card_icon_centre);
        iconRight = view.findViewById(R.id.card_icon_right);
    }

    public void setBackgroundImage(String url){
        ImageHandler.loadImageBitmap2(getContext(),
                url + TOKOPEDIA_PAY_BG_NAME,
                new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Drawable drawable = new BitmapDrawable(getContext().getResources(), resource);
                        container.setBackground(drawable);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public void setActionText(@NonNull String text) {
        this.actionText.setText(text);
    }

    public void setTextAmountLeft(@NonNull String text) {
        this.textAmountLeft.setText(text);
    }

    public void setTextAmountCentre(@NonNull String text) {
        this.textAmountCentre.setText(text);
    }

    public void setAmountColorLeft(@ColorRes int color) {
        this.textAmountLeft.setTextColor(ContextCompat.getColor(getContext(), color));
    }

    public void setTextDescLeft(@NonNull String text) {
        this.textDescLeft.setText(text);
    }

    public void setTextAmountRight(@NonNull String text) {
        setTextAmountRight(text, false);
    }

    public void setTextAmountRight(@NonNull String text, boolean isImportant) {
        if (isImportant) {
            this.textAmountRight.setTextColor(ContextCompat.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_G400));
        } else {
            this.textAmountRight.setTextColor(ContextCompat.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
        }

        this.textAmountRight.setText(text);
    }

    public void setTextDesctCentre(@NonNull String text) {
        textDescCentre.setText(text);
    }

    public void setTextDesctRight(@NonNull String text) {
        textDesctRight.setText(text);
    }

    public void setIconLeft(String url) {
        if (!URLUtil.isValidUrl(url)) {
            return;
        }

        ImageHandler.loadImageFitCenter(iconLeft.getContext(), iconLeft, url);
    }

    public void setIconCentre(String url) {
        if (!URLUtil.isValidUrl(url)) {
            return;
        }

        ImageHandler.loadImageFitCenter(iconCentre.getContext(), iconCentre, url);
    }

    public void setIconRight(String url) {
        if (!URLUtil.isValidUrl(url)) {
            return;
        }

        ImageHandler.loadImageFitCenter(iconRight.getContext(), iconRight, url);
    }

    public void setActionTextClickListener(View.OnClickListener listener) {
        layoutAction.setOnClickListener(listener);
    }

    public void setLeftItemClickListener(View.OnClickListener listener) {
        layoutLeft.setOnClickListener(listener);
    }

    public void setCentreItemClickListener(View.OnClickListener listener) {
        layoutCentre.setOnClickListener(listener);
    }

    public void setRightItemClickListener(View.OnClickListener listener) {
        layoutRight.setOnClickListener(listener);
    }

    public void setCenterLayoutVisibility(int visibility) {
        layoutCentre.setVisibility(visibility);
    }
}
