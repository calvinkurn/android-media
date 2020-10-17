package com.tokopedia.design.component;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;

/**
 * Created by meyta on 1/29/18.
 */

public class FloatingButton extends BaseCustomView {

    private ButtonCompat button;

    private String mText;
    private int mType;
    private int mSize;
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
            mType = a.getInteger(R.styleable.FloatingButton_floatButtonType, 0);
            mSize = a.getInteger(R.styleable.FloatingButton_floatButtonSize, 0);
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

    private void init() {
        if (button == null)
            button = new ButtonCompat(getContext());

        button.setButtonCompatType(mType);
        button.setButtonCompatSize(mSize);
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

    public void setButtonType(int mType) {
        if (button != null)
            this.button.setButtonCompatType(mType);
    }

    public void setButtonSize(int mSize) {
        if (button != null)
            this.button.setButtonCompatSize(mSize);
    }

    public ButtonCompat getButton() {
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
        background.getPaint().setColor(ContextCompat.getColor(getContext(), R.color.Unify_N0));

        return new LayerDrawable(new Drawable[]{shapeDrawable1, shapeDrawable2,
                shapeDrawable3, shapeDrawable4, shapeDrawable5, background});
    }
}
