package com.tokopedia.design.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.tokopedia.design.R;

/**
 * Created by meyta on 07/03/18.
 */

public class EditTextCompat extends AppCompatEditText {

    public EditTextCompat(Context context) {
        super(context);
    }

    public EditTextCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public EditTextCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.EditTextCompat);

            Drawable drawableLeft = null;
            Drawable drawableRight = null;
            Drawable drawableBottom = null;
            Drawable drawableTop = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawableLeft = attributeArray.getDrawable(R.styleable.EditTextCompat_editTextDrawableLeftCompat);
                drawableRight = attributeArray.getDrawable(R.styleable.EditTextCompat_editTextDrawableRightCompat);
                drawableBottom = attributeArray.getDrawable(R.styleable.EditTextCompat_editTextDrawableBottomCompat);
                drawableTop = attributeArray.getDrawable(R.styleable.EditTextCompat_editTextDrawableTopCompat);
            } else {
                final int drawableLeftId = attributeArray.getResourceId(R.styleable.EditTextCompat_editTextDrawableLeftCompat, -1);
                final int drawableRightId = attributeArray.getResourceId(R.styleable.EditTextCompat_editTextDrawableRightCompat, -1);
                final int drawableBottomId = attributeArray.getResourceId(R.styleable.EditTextCompat_editTextDrawableBottomCompat, -1);
                final int drawableTopId = attributeArray.getResourceId(R.styleable.EditTextCompat_editTextDrawableTopCompat, -1);

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
            attributeArray.recycle();
        }
    }
}
