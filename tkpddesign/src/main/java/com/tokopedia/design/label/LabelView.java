package com.tokopedia.design.label;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatResources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class LabelView extends BaseCustomView {
    private static final float MAX_WIDTH_PERCENT_CONTENT = 0.3f;

    private ImageView imageView;
    private TextView titleTextView;
    private TextView subTitleTextView;
    private TextView contentTextView;
    private TextView badgeTextView;
    private ImageView rightArrow;

    private Drawable imageDrawable;
    private int imageWidth;
    private int imageHeight;
    private int imageMarginRight;
    private String titleText;
    private String subTitleText;
    @ColorInt
    private int titleColorValue;
    private int contentTextStyleValue;

    private String contentText;
    private String badgeCounter;
    @ColorInt
    private int contentColorValue;
    private int titleTextStyleValue;

    private int maxLines;
    private float titleTextSize;
    private float subTitleTextSize;
    private float contentTextSize;
    private float contentMaxWidthPercentage;
    private int minTitleWidth;

    private boolean isArrowShown;
    private boolean enabled;

    public LabelView(Context context) {
        super(context);
        init();
    }

    public LabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.LabelView);
        try {
            final int drawableId = styledAttributes.getResourceId(R.styleable.LabelView_lv_image, -1);
            if (drawableId >= 0) {
                imageDrawable = AppCompatResources.getDrawable(getContext(), drawableId);
            }
            imageWidth = (int) styledAttributes.getDimension(R.styleable.LabelView_lv_image_width, getResources().getDimension(R.dimen.dp_32));
            imageHeight = (int) styledAttributes.getDimension(R.styleable.LabelView_lv_image_height, imageWidth);
            imageMarginRight = (int) styledAttributes.getDimension(R.styleable.LabelView_lv_image_margin_right, getResources().getDimension(R.dimen.dp_8));
            titleText = styledAttributes.getString(R.styleable.LabelView_lv_title);
            badgeCounter = styledAttributes.getString(R.styleable.LabelView_lv_badge);
            titleColorValue = styledAttributes.getColor(R.styleable.LabelView_lv_title_color, ContextCompat.getColor(getContext(), R.color.font_black_primary_70));
            subTitleText = styledAttributes.getString(R.styleable.LabelView_lv_sub_title);
            contentText = styledAttributes.getString(R.styleable.LabelView_lv_content);
            contentColorValue = styledAttributes.getColor(R.styleable.LabelView_lv_content_color, ContextCompat.getColor(getContext(), R.color.font_black_secondary_54));
            contentTextStyleValue = styledAttributes.getInt(R.styleable.LabelView_lv_content_text_style, Typeface.NORMAL);
            titleTextStyleValue = styledAttributes.getInt(R.styleable.LabelView_lv_title_text_style, Typeface.NORMAL);
            maxLines = styledAttributes.getInt(R.styleable.LabelView_lv_content_max_lines, 1);
            contentTextSize = styledAttributes.getDimension(R.styleable.LabelView_lv_content_text_size, getResources().getDimension(R.dimen.sp_16));
            contentMaxWidthPercentage = styledAttributes.getFloat(R.styleable.LabelView_lv_content_max_width_percentage, MAX_WIDTH_PERCENT_CONTENT);
            titleTextSize = styledAttributes.getDimension(R.styleable.LabelView_lv_title_text_size, contentTextSize);
            subTitleTextSize = styledAttributes.getDimension(R.styleable.LabelView_lv_sub_title_text_size, getResources().getDimension(R.dimen.sp_12));
            minTitleWidth = styledAttributes.getDimensionPixelSize(R.styleable.LabelView_lv_title_min_width, 0);
            isArrowShown = styledAttributes.getBoolean(R.styleable.LabelView_lv_show_arrow, false);
        } finally {
            styledAttributes.recycle();
        }
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_label_view, this);
        imageView = view.findViewById(R.id.image_view);
        titleTextView = view.findViewById(R.id.text_view_title);
        subTitleTextView = view.findViewById(R.id.text_view_sub_title);
        contentTextView = view.findViewById(R.id.text_view_content);
        rightArrow = view.findViewById(R.id.image_arrow);
        badgeTextView = view.findViewById(R.id.text_view_badge);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (imageDrawable != null) {
            imageView.setImageDrawable(imageDrawable);
            imageView.setVisibility(View.VISIBLE);
            titleTextView.setPadding(imageMarginRight, 0, 0, 0);
            subTitleTextView.setPadding(imageMarginRight, 0, 0, 0);
        }
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = imageWidth;
        layoutParams.height = imageHeight;
        imageView.setLayoutParams(layoutParams);
        titleTextView.setText(titleText);
        titleTextView.setTypeface(null, titleTextStyleValue);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        titleTextView.setMinWidth(minTitleWidth);
        subTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, subTitleTextSize);
        if (!TextUtils.isEmpty(subTitleText)) {
            subTitleTextView.setText(subTitleText);
            subTitleTextView.setVisibility(View.VISIBLE);
        } else {
            subTitleTextView.setVisibility(View.GONE);
        }
        contentTextView.setText(contentText);
        contentTextView.setTextColor(contentColorValue);
        contentTextView.setTypeface(null, contentTextStyleValue);
        contentTextView.setMaxLines(maxLines);
        contentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, contentTextSize);

        if (!TextUtils.isEmpty(badgeCounter)) {
            badgeTextView.setText(badgeCounter);
            badgeTextView.setVisibility(VISIBLE);
        } else {
            badgeTextView.setVisibility(GONE);
        }

        rightArrow.setVisibility(isArrowShown ? VISIBLE : GONE);

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);

        contentTextView.setMaxWidth((int)(dm.widthPixels * contentMaxWidthPercentage));

        invalidate();
        requestLayout();
    }

    public void resetContentText() {
        setContent(contentText);
        invalidate();
        requestLayout();
    }

    public boolean isContentDefault() {
        return contentTextView.getText().equals(contentText);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        setClickable(enabled);
        if (enabled) {
            titleTextView.setTextColor(titleColorValue);
            contentTextView.setTextColor(contentColorValue);
        } else {
            titleTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.font_black_disabled_38));
            contentTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.font_black_disabled_38));
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getTitle() {
        return titleTextView.getText().toString();
    }

    public void setTitle(String textTitle) {
        titleTextView.setText(textTitle);
        invalidate();
        requestLayout();
    }

    public void setSubTitle(String text) {
        subTitleTextView.setText(text);
        subTitleTextView.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        invalidate();
        requestLayout();
    }

    public void setTitleContentTypeFace(int typeFace) {
        titleTextView.setTypeface(null, typeFace);
        invalidate();
        requestLayout();
    }

    public void setContent(CharSequence textValue) {
        contentTextView.setText(textValue);
        invalidate();
        requestLayout();
    }

    public String getContent() {
        return contentTextView.getText().toString();
    }

    public void setBadgeCounter(String badge) {
        badgeTextView.setText(badge);
        badgeTextView.setVisibility(VISIBLE);
        invalidate();
        requestLayout();
    }

    public void setContentTypeface(int typefaceType) {
        contentTextView.setTypeface(null, typefaceType);
        invalidate();
        requestLayout();
    }

    public void setContentColorValue(@ColorInt int colorValue) {
        contentColorValue = colorValue;
        contentTextView.setTextColor(colorValue);
        invalidate();
        requestLayout();
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void showRightArrow(boolean hideArrow) {
        rightArrow.setVisibility(hideArrow ? GONE : VISIBLE);
        invalidate();
        requestLayout();
    }

    public void setImageResource(int imageResource) {
        if (imageResource >= 0){
            imageDrawable = AppCompatResources.getDrawable(getContext(), imageResource);
            imageView.setImageDrawable(imageDrawable);
            imageView.setVisibility(VISIBLE);
            titleTextView.setPadding(imageMarginRight, 0, 0, 0);
            subTitleTextView.setPadding(imageMarginRight, 0, 0, 0);
        } else {
            imageView.setVisibility(GONE);
            titleTextView.setPadding(0, 0, 0, 0);
            subTitleTextView.setPadding(0, 0, 0, 0);
        }
        invalidate();
        requestLayout();
    }
}