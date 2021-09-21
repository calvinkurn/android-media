package com.tokopedia.logintest.common.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.logintest.R;

/**
 * Created by stevenfredian on 6/2/16.
 */
public class LoginTextView extends LinearLayout {

    int color;
    String customText;
    int textColor;
    int borderColor;
    int cornerSize;
    int borderSize;
    boolean imageEnabled;
    GradientDrawable shape;

    public LoginTextView(Context context) {
        super(context);
        init(context, null);
    }

    public LoginTextView(Context context, int color) {
        super(context);
        this.color = color;
        init(context, null);
    }

    public LoginTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LoginTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoginTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.custom_login_testapp_text_view, this);
        shape = new GradientDrawable();
        shape.setColor(Color.TRANSPARENT);
        if (attrs == null) {
            setDefaultShape();
        } else {
            setAttrs(context, attrs);
        }
        setUp();
    }

    private void setAttrs(Context context, AttributeSet attrs) {

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LoginTextView, 0, 0);
        int resourceId;
        try {

            customText = a.getString(R.styleable.LoginTextView_customText);
            textColor = a.getColor(R.styleable.LoginTextView_textColor, Color.WHITE);
            borderColor = a.getInt(R.styleable.LoginTextView_borderColor, 0);
            cornerSize = a.getInt(R.styleable.LoginTextView_cornerSizes, 3);
            borderSize = a.getInt(R.styleable.LoginTextView_borderSize, 1);
            imageEnabled = a.getBoolean(R.styleable.LoginTextView_imageEnabled, true);
            resourceId = a.getResourceId(R.styleable.LoginTextView_iconButton, 0);
        } finally {
            a.recycle();
        }

        if (!TextUtils.isEmpty(customText)) {
            ((TextView) findViewById(R.id.provider_name)).setText(customText);
        }

        ((TextView) findViewById(R.id.provider_name)).setTextColor(textColor);
        shape.setCornerRadii(new float[]{cornerSize, cornerSize, cornerSize, cornerSize
                , cornerSize, cornerSize, cornerSize, cornerSize});

        shape.setStroke(borderSize, borderColor);
        if (resourceId != 0) {
            Drawable drawable = AppCompatResources.getDrawable(context, resourceId);
            (findViewById(R.id.provider_image)).setBackground(drawable);
        }

        if (!imageEnabled || resourceId == 0) {
            (findViewById(R.id.provider_image)).setVisibility(GONE);
        }
    }

    @SuppressWarnings("deprecation")
    public void setUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(shape);
        } else {
            setBackgroundDrawable(shape);
        }
    }

    private void setDefaultShape() {
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[]{3, 3, 3, 3, 3, 3, 3, 3});
        shape.setColor(getBackgroundColor());
        if (getBackgroundColor() == Color.WHITE) shape.setStroke(1,
                MethodChecker.getColor(getContext(), R.color.black_38));
    }

    public void setText(String name) {
        TextView textView = findViewById(R.id.provider_name);
        textView.setText(name);
    }

    public void setImage(String image) {
        if(!TextUtils.isEmpty(image)) {
            ImageView imageView = findViewById(R.id.provider_image);
            ImageHandler.LoadImage(imageView, image);
        }
    }

    public void setColor(int color) {
        this.color = color;
        shape.setColor(color);
        setUp();
    }

    public int getBackgroundColor() {
        return color;
    }


    public void setBorderColor(int color) {
        shape.setStroke(1, color);
        setUp();
    }

    public void setRoundCorner(int cornerSize) {
        shape.setCornerRadii(new float[]{cornerSize, cornerSize, cornerSize, cornerSize
                , cornerSize, cornerSize, cornerSize, cornerSize});
        setUp();
    }

    public void setImageResource(int imageResource) {
        ImageView imageView = findViewById(R.id.provider_image);
        ImageHandler.loadImageWithIdWithoutPlaceholder(imageView, imageResource);
    }
}
