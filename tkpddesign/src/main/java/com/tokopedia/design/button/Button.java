package com.tokopedia.design.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.tokopedia.design.R;

/**
 * Created by meyta on 1/25/18.
 *
 * How to use?
 * > xml
 * app:size=big (big, medium, small)
 * app:type=primary (primary, secondary)
 * android:enable=false (true, false)
 *
 * > java
 * button.setType(Button.PRIMARY); (PRIMARY, SECONDARY)
 * button.setSize(Button.BIG); (BIG, MEDIUM, SMALL)
 *
 * for more http://product.tkp.me/components/button/design
 */

public class Button extends AppCompatButton {

    public final static int PRIMARY = 1;
    public final static int SECONDARY = 2;

    public final static int BIG = 3;
    public final static int MEDIUM = 4;
    public final static int SMALL = 5;

    private int mType;
    private int mSize;

    public Button(Context context) {
        super(context);
        init();
    }

    public Button(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public Button(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.Button, 0, 0);
        try {
            mType = a.getInteger(R.styleable.Button_type, 0);
            mSize = a.getInteger(R.styleable.Button_size, 0);
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
        defineType();
        defineSize();

        if (!isEnabled()) {
            initDraw(R.color.grey_350, R.drawable.bg_button_disabled);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            initDraw(R.color.grey_350, R.drawable.bg_button_disabled);
        }
    }

    private void initDraw(@ColorRes int textColor, @DrawableRes int backgroundDrawable) {
        this.setTextColor(ContextCompat.getColor(getContext(), textColor));
        this.setBackground(ContextCompat.getDrawable(getContext(), backgroundDrawable));
    }

    private void initSize(float textSize, int height) {
        this.setTextSize(textSize);
    }

    public int getType() {
        return mType;
    }

    public void setType(int mType) {
        this.mType = mType;
        defineType();
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int mSize) {
        this.mSize = mSize;
        defineSize();
    }

    private void defineType() {
        switch (mType) {
            case PRIMARY:
                initDraw(R.color.white, R.drawable.bg_button_primary);
                break;
            case SECONDARY:
                initDraw(R.color.grey_500, R.drawable.bg_button_secondary);
                break;
            default:
                break;

        }
    }

    private void defineSize() {
        switch (mSize) { // this size initiate same as zeplin by px
            case BIG:
                initSize(14, 48);
                break;
            case MEDIUM:
                initSize(13, 40);
                break;
            case SMALL:
                initSize(11, 32);
                break;
            default:
                initSize(13, 40); // default value
                break;
        }
    }
}