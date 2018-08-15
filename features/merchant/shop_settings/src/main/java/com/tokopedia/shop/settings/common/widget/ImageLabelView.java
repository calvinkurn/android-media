package com.tokopedia.shop.settings.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.shop.settings.R;

/**
 * Created by hendry on 15/08/18.
 */

public class ImageLabelView extends FrameLayout {
    private String titleText;
    private float titleTextSize;
    private int titleTextStyleValue;
    private TextView titleTextView;
    @ColorInt
    private int titleColorValue;

    ImageView imageView;
    private int drawableRes;
    private TextView tvContent;

    public ImageLabelView(Context context) {
        super(context);
        init(null);
    }

    public ImageLabelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ImageLabelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImageLabelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        applyAttrs(attrs);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_label_view_image,
                this, true);
        titleTextView = view.findViewById(R.id.tvTitle);
        titleTextView.setText(titleText);
        titleTextView.setTypeface(null, titleTextStyleValue);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        titleTextView.setTextColor(titleColorValue);
        imageView = view.findViewById(R.id.imageView);
        tvContent = view.findViewById(R.id.tvContent);
        setImage(drawableRes);
    }

    private void applyAttrs(AttributeSet attrs) {
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.ImageLabelView);
        try {
            titleText = styledAttributes.getString(R.styleable.ImageLabelView_ilv_title);
            titleColorValue = styledAttributes.getColor(R.styleable.ImageLabelView_ilv_title_color, ContextCompat.getColor(getContext(), R.color.font_black_primary_70));
            titleTextStyleValue = styledAttributes.getInt(R.styleable.ImageLabelView_ilv_title_text_style, Typeface.NORMAL);
            titleTextSize = styledAttributes.getDimension(R.styleable.ImageLabelView_ilv_title_text_size, getResources().getDimension(R.dimen.sp_16));
            drawableRes = styledAttributes.getResourceId(R.styleable.TitleCardView_tcv_loading_layout, 0);
        } finally {
            styledAttributes.recycle();
        }
    }

    public void setImage(int drawableRes) {
        this.drawableRes = drawableRes;
        if (drawableRes == 0) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setImageResource(drawableRes);
            imageView.setVisibility(View.VISIBLE);
        }
    }

    public void setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            titleTextView.setVisibility(View.GONE);
        } else {
            titleTextView.setText(title);
            titleTextView.setVisibility(View.VISIBLE);
        }
    }

    public void setContent(String content) {
        if (TextUtils.isEmpty(content)) {
            tvContent.setVisibility(View.GONE);
        } else {
            tvContent.setText(content);
            tvContent.setVisibility(View.VISIBLE);
        }
    }
}
