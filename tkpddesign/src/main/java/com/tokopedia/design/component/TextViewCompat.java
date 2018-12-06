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
 * fontSize = headline, subHeadline, titleBig, title, small, micro
 * weight = regular, medium
 * textviewTheme = lightPrimary, lightSecondary, lightDisabled, darkPrimary, darkSecondary, darkDisabled
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
        if (fontSize == 1) {
            this.setTextSize(COMPLEX_UNIT_SP, getResources().getInteger(R.integer.headline));
            this.setMinimumHeight(getResources().getInteger(R.integer.headline_weight));
        } else if (fontSize == 2) {
            this.setTextSize(COMPLEX_UNIT_SP, getResources().getInteger(R.integer.sub_headline));
            this.setMinimumHeight(getResources().getInteger(R.integer.sub_headline_weight));
        } else if (fontSize == 3) {
            this.setTextSize(COMPLEX_UNIT_SP, getResources().getInteger(R.integer.title_big));
            this.setMinimumHeight(getResources().getInteger(R.integer.title_big_weight));
        } else if (fontSize == 4) {
            this.setTextSize(COMPLEX_UNIT_SP, getResources().getInteger(R.integer.title));
            this.setMinimumHeight(getResources().getInteger(R.integer.title_weight));
        } else if (fontSize == 5) {
            this.setTextSize(COMPLEX_UNIT_SP, getResources().getInteger(R.integer.small));
            this.setMinimumHeight(getResources().getInteger(R.integer.small_weight));
        } else if (fontSize == 6) {
            this.setTextSize(COMPLEX_UNIT_SP, getResources().getInteger(R.integer.micro));
            this.setMinimumHeight(getResources().getInteger(R.integer.micro_weight));
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
            this.setTextColor(ContextCompat.getColor(context, R.color.light_primary));
        } else if (theme == 2) {
            this.setTextColor(ContextCompat.getColor(context, R.color.light_secondary));
        } else if (theme == 3) {
            this.setTextColor(ContextCompat.getColor(context, R.color.light_disabled));
        } else if (theme == 4) {
            this.setTextColor(ContextCompat.getColor(context, R.color.dark_primary));
        } else if (theme == 5) {
            this.setTextColor(ContextCompat.getColor(context, R.color.dark_secondary));
        } else if (theme == 6) {
            this.setTextColor(ContextCompat.getColor(context, R.color.dark_disabled));
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