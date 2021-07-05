package com.tokopedia.design.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import android.util.AttributeSet;

import com.tokopedia.design.R;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

/**
 * Created by meta on 16/03/18.
 */

public class ButtonCompat extends AppCompatButton {

    public final static int PRIMARY = 1;
    public final static int SECONDARY = 2;
    public final static int TRANSACTION = 3;
    public final static int DISABLE = 4;
    public final static int WHITE_TRANSPARENT = 5;
    public final static int GHOST = 6;
    public final static int WHITE = 7;
    public final static int PRIMARY_DISABLED = 8;

    public final static int BIG = 4;
    public final static int MEDIUM = 5;
    public final static int SMALL = 6;

    private int mType;
    private int mSize;

    public ButtonCompat(Context context) {
        super(context);
    }

    public ButtonCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public ButtonCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.ButtonCompat);

            initImage(context, attributeArray);

            initUnifyType(attributeArray);

            attributeArray.recycle();
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        defineSize();
    }

    protected void initImage(Context context, TypedArray attributeArray) {
        Drawable drawableLeft = null;
        Drawable drawableRight = null;
        Drawable drawableBottom = null;
        Drawable drawableTop = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawableLeft = attributeArray.getDrawable(R.styleable.ButtonCompat_buttonDrawableLeftCompat);
            drawableRight = attributeArray.getDrawable(R.styleable.ButtonCompat_buttonDrawableRightCompat);
            drawableBottom = attributeArray.getDrawable(R.styleable.ButtonCompat_buttonDrawableBottomCompat);
            drawableTop = attributeArray.getDrawable(R.styleable.ButtonCompat_buttonDrawableTopCompat);
        } else {
            final int drawableLeftId = attributeArray.getResourceId(R.styleable.ButtonCompat_buttonDrawableLeftCompat, -1);
            final int drawableRightId = attributeArray.getResourceId(R.styleable.ButtonCompat_buttonDrawableRightCompat, -1);
            final int drawableBottomId = attributeArray.getResourceId(R.styleable.ButtonCompat_buttonDrawableBottomCompat, -1);
            final int drawableTopId = attributeArray.getResourceId(R.styleable.ButtonCompat_buttonDrawableTopCompat, -1);

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

    protected void initUnifyType(TypedArray attributeArray) {
        mType = attributeArray.getInteger(R.styleable.ButtonCompat_buttonCompatType, 0);
        mSize = attributeArray.getInteger(R.styleable.ButtonCompat_buttonCompatSize, 0);

        boolean textAllCaps = attributeArray.getBoolean(R.styleable.ButtonCompat_buttonCompatTextAllCaps, false);

        if (!textAllCaps) {
            setTransformationMethod(null);
        }

        defineType();
        defineSize();
    }

    private void initDraw(@ColorRes int textColor, @DrawableRes int backgroundDrawable) {
        this.setTextColor(ContextCompat.getColor(getContext(), textColor));
        this.setBackground(AppCompatResources.getDrawable(getContext(), backgroundDrawable));
    }

    private void initSize(float textSize, int height) {
        setMinHeight(height);
        setMinimumHeight(height);
        setTextSize(COMPLEX_UNIT_SP, textSize);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStateListAnimator(null);
        }
    }

    public int getButtonCompatType() {
        return mType;
    }

    public void setButtonCompatType(int mType) {
        this.mType = mType;
        defineType();
    }

    public int getButtonCompatSize() {
        return mSize;
    }

    public void setButtonCompatSize(int mSize) {
        this.mSize = mSize;
        defineSize();
    }

    private void defineType() {
        switch (mType) {
            case PRIMARY:
                initDraw(R.color.Unify_N0, R.drawable.bg_button_green);
                super.setEnabled(true);
                break;
            case PRIMARY_DISABLED:
                initDraw(com.tokopedia.unifyprinciples.R.color.Unify_N200, R.drawable.bg_button_green);
                super.setEnabled(false);
                break;
            case SECONDARY:
                initDraw(com.tokopedia.unifyprinciples.R.color.Unify_N200, R.drawable.bg_button_white_border);
                break;
            case TRANSACTION:
                initDraw(R.color.Unify_N0, R.drawable.bg_button_orange);
                break;
            case WHITE_TRANSPARENT:
                initDraw(R.color.Unify_N0, R.drawable.bg_button_white_transparent);
            case GHOST:
                initDraw(com.tokopedia.unifyprinciples.R.color.Unify_G500, R.drawable.bg_button_green_border);
                break;
            case WHITE:
                initDraw(com.tokopedia.unifyprinciples.R.color.Unify_N700_68, R.drawable.bg_button_white);
                break;
            case DISABLE:
                break;
        }
    }

    private void defineSize() {
        switch (mSize) { // this size initiate same as zeplin by px
            case BIG:
                initSize(getResources().getInteger(R.integer.button_big), getResources().getDimensionPixelSize(R.dimen.dp_48));
                break;
            case MEDIUM:
                initSize(getResources().getInteger(R.integer.button_medium), getResources().getDimensionPixelSize(R.dimen.dp_40));
                break;
            case SMALL:
                initSize(getResources().getInteger(R.integer.button_small), getResources().getDimensionPixelSize(R.dimen.dp_32));
                break;
        }
    }
}
