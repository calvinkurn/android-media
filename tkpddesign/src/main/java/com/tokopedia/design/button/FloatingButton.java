package com.tokopedia.design.button;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.ContextThemeWrapper;
import android.util.AttributeSet;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;

/**
 * Created by meyta on 1/29/18.
 *
 * How to use?
 * xml :
 * app:style, app:buttonTextAllCaps, app:text
 * app:shadow, app:shadowLeft, app:shadowTop, app:shadowRight, app:shadowBottom
 *
 * java :
 * button setStyle(R.style.Button_Primary), setTextAllCaps(boolean), setText(string)
 * setShadowEffect(left, top, right, bottom)
 *
 * for more http://product.tkp.me/components/button/design
 */

public class FloatingButton extends BaseCustomView {

    private android.widget.Button button;

    private String mText;
    private int mStyle;
    private boolean mTextAllCaps;

    private boolean isShadow;
    private boolean isShadowLeft, isShadowTop, isShadowRight, isShadowBottom;

    public FloatingButton(@NonNull Context context) {
        super(context);
        init();
    }

    public FloatingButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FloatingButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.FloatingButton, 0, 0);
        try {
            mText = a.getString(R.styleable.FloatingButton_text);
            mStyle = a.getResourceId(R.styleable.FloatingButton_style, 0);
            mTextAllCaps = a.getBoolean(R.styleable.FloatingButton_buttonTextAllCaps, false);
            isShadow = a.getBoolean(R.styleable.FloatingButton_shadow, false);
            isShadowLeft = a.getBoolean(R.styleable.FloatingButton_shadowLeft, false);
            isShadowTop = a.getBoolean(R.styleable.FloatingButton_shadowTop, false);
            isShadowRight = a.getBoolean(R.styleable.FloatingButton_shadowRight, false);
            isShadowBottom = a.getBoolean(R.styleable.FloatingButton_shadowBottom, false);
        } finally {
            a.recycle();
        }

        init();
    }

    @SuppressLint("RestrictedApi")
    private void init() {
        if (button == null)
            button = new android.widget.Button(new ContextThemeWrapper(this.getContext(), mStyle), null, 0);

        button.setText(mText);
        button.setAllCaps(mTextAllCaps);

        this.addView(button);

        if (isShadow) {
            this.setShadowEffect(true, true, true, true);
            this.setBackground(createShadow());
            this.setPadding(toDp(24), toDp(24), toDp(24), toDp(24));
        } else {
            setShadowEffect(isShadowLeft, isShadowTop, isShadowRight, isShadowBottom);
            this.setBackground(createShadow());
            this.setPadding(toDp(isShadowLeft ? 24 : 16), toDp(isShadowTop ? 24 : 16), toDp(isShadowRight ? 24 : 16), toDp(isShadowBottom ? 24 : 16));
        }
    }

    public void setTextAllCaps(boolean enable) {
        if (button != null)
            this.button.setAllCaps(enable);
    }

    public void setText(String text) {
        if (button != null)
            this.button.setText(text);
    }

    public android.widget.Button getButton() {
        return button;
    }

    private int toDp(int number) {
        return (int) (number*Resources.getSystem().getDisplayMetrics().density + 0.5f);
    }

    /**
     * Define shadow
     */
    private int paddingLeft, paddingTop, paddingRight, paddingBottom;

    public void setShadowEffect(boolean left, boolean top, boolean right, boolean bottom) {
        paddingLeft = (left) ? 2 : 0;
        paddingTop = (top) ? 2 : 0;
        paddingRight = (right) ? 2: 0;
        paddingBottom = (bottom) ? 2: 0;
    }

    private LayerDrawable createShadow() {
        ShapeDrawable shapeDrawable1 = new ShapeDrawable();
        shapeDrawable1.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        shapeDrawable1.getPaint().setColor(ContextCompat.getColor(getContext(), R.color.shadow_1));

        ShapeDrawable shapeDrawable2 = new ShapeDrawable();
        shapeDrawable2.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        shapeDrawable2.getPaint().setColor(ContextCompat.getColor(getContext(), R.color.shadow_2));

        ShapeDrawable shapeDrawable3 = new ShapeDrawable();
        shapeDrawable3.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        shapeDrawable3.getPaint().setColor(ContextCompat.getColor(getContext(), R.color.shadow_3));

        ShapeDrawable shapeDrawable4 = new ShapeDrawable();
        shapeDrawable4.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        shapeDrawable4.getPaint().setColor(ContextCompat.getColor(getContext(), R.color.shadow_4));

        ShapeDrawable shapeDrawable5 = new ShapeDrawable();
        shapeDrawable5.setPadding(1, 1, 1,1);
        shapeDrawable5.getPaint().setColor(ContextCompat.getColor(getContext(), R.color.shadow_5));

        ShapeDrawable background = new ShapeDrawable();
        background.getPaint().setColor(ContextCompat.getColor(getContext(), R.color.white));

        return new LayerDrawable(new Drawable[]{shapeDrawable1, shapeDrawable2,
                shapeDrawable3, shapeDrawable4, shapeDrawable5, background});
    }
}
