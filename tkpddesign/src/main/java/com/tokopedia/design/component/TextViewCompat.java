package com.tokopedia.design.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.tokopedia.design.R;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

/**
 * Created by meyta on 06/03/18.
 *
 * fontSize = headline, subheadline, title big, title, small, micro
 * weight = regular, medium
 * theme = lightprimary, lightsecondary, lightdisabled, darkprimary, darksecondary, darkdisabled
 *
 * http://product.tkp.me/styles/typography/design
 */

public class TextViewCompat extends AppCompatTextView {

    public TextViewCompat(Context context) {
        super(context);
    }

    public TextViewCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public TextViewCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.TextViewCompat);

            configFontSize(attributeArray);
            configWeight(attributeArray);
            configTheme(context, attributeArray);
            configDrawable(context, attributeArray);

            attributeArray.recycle();
        }
    }

    private void configFontSize(TypedArray attributeArray) {
        int fontSize  = attributeArray.getInteger(R.styleable.TextViewCompat_fontSize, 5);
        if (fontSize == 1) { // headline
            this.setTextSize(COMPLEX_UNIT_SP, 34);
            this.setMinimumHeight(38);
        } else if (fontSize == 2) { // sub headline
            this.setTextSize(COMPLEX_UNIT_SP, 24);
            this.setMinimumHeight(28);
        } else if (fontSize == 3) { // title big
            this.setTextSize(COMPLEX_UNIT_SP, 20);
            this.setMinimumHeight(24);
        } else if (fontSize == 4) { // title
            this.setTextSize(COMPLEX_UNIT_SP, 17);
            this.setMinimumHeight(22);
        } else if (fontSize == 5) { // small
            this.setTextSize(COMPLEX_UNIT_SP, 14);
            this.setMinimumHeight(18);
        } else if (fontSize == 6) { // micro
            this.setTextSize(COMPLEX_UNIT_SP, 12);
            this.setMinimumHeight(16);
        }
    }

    private void configWeight(TypedArray attributeArray) {
        int weight = attributeArray.getInteger(R.styleable.TextViewCompat_weight, 1);
        if (weight == 1) {
            this.setTypeface(getTypeface(), Typeface.NORMAL);
        } else if (weight == 2) {
            this.setTypeface(getTypeface(), Typeface.BOLD);
        }
    }

    private void configTheme(Context context, TypedArray attributeArray) {
        int theme = attributeArray.getInteger(R.styleable.TextViewCompat_textViewTheme, 1);
        if (theme == 1) {
            this.setTextColor(ContextCompat.getColor(context, R.color.lightprimary));
        } else if (theme == 2) {
            this.setTextColor(ContextCompat.getColor(context, R.color.lightsecondary));
        } else if (theme == 3) {
            this.setTextColor(ContextCompat.getColor(context, R.color.lightdisabled));
        } else if (theme == 4) {
            this.setTextColor(ContextCompat.getColor(context, R.color.darkprimary));
        } else if (theme == 5) {
            this.setTextColor(ContextCompat.getColor(context, R.color.darksecondary));
        } else if (theme == 6) {
            this.setTextColor(ContextCompat.getColor(context, R.color.darkdisabled));
        }
    }

    private void configDrawable(Context context, TypedArray attributeArray) {
        Drawable drawableLeft = null;
        Drawable drawableRight = null;
        Drawable drawableBottom = null;
        Drawable drawableTop = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawableLeft = attributeArray.getDrawable(R.styleable.TextViewCompat_drawableLeftCompat);
            drawableRight = attributeArray.getDrawable(R.styleable.TextViewCompat_drawableRightCompat);
            drawableBottom = attributeArray.getDrawable(R.styleable.TextViewCompat_drawableBottomCompat);
            drawableTop = attributeArray.getDrawable(R.styleable.TextViewCompat_drawableTopCompat);
        } else {
            final int drawableLeftId = attributeArray.getResourceId(R.styleable.TextViewCompat_drawableLeftCompat, -1);
            final int drawableRightId = attributeArray.getResourceId(R.styleable.TextViewCompat_drawableRightCompat, -1);
            final int drawableBottomId = attributeArray.getResourceId(R.styleable.TextViewCompat_drawableBottomCompat, -1);
            final int drawableTopId = attributeArray.getResourceId(R.styleable.TextViewCompat_drawableTopCompat, -1);

            if (drawableLeftId != -1)
                drawableLeft = AppCompatResources.getDrawable(context, drawableLeftId);
            if (drawableRightId != -1)
                drawableRight = AppCompatResources.getDrawable(context, drawableRightId);
            if (drawableBottomId != -1)
                drawableBottom = AppCompatResources.getDrawable(context, drawableBottomId);
            if (drawableTopId != -1)
                drawableTop = AppCompatResources.getDrawable(context, drawableTopId);
        }
        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }


}