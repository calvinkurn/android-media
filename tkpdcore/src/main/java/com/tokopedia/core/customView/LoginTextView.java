package com.tokopedia.core.customView;

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
import android.widget.Space;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.util.MethodChecker;

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
    private Drawable drawable;
    int backgroundColor;
    private float myDefaultPadding;
    private int padding;
    private int height;


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
        LayoutInflater.from(context).inflate(R.layout.provider_login_text_view, this);
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
        try {

            padding = (int) (a.getDimension(R.styleable.LoginTextView_btn_padding, myDefaultPadding));
            height = (int) a.getDimension(R.styleable.LoginTextView_btn_height, getResources().getDimension(R.dimen.btn_height));
            customText = a.getString(R.styleable.LoginTextView_customText);
            textColor = a.getColor(R.styleable.LoginTextView_textColor, Color.WHITE);
            borderColor = a.getInt(R.styleable.LoginTextView_borderColor, 0);
            cornerSize = a.getInt(R.styleable.LoginTextView_cornerSize, 3);
            borderSize = a.getInt(R.styleable.LoginTextView_borderSize, 1);
            imageEnabled = a.getBoolean(R.styleable.LoginTextView_imageEnabled, true);
            drawable = a.getDrawable(R.styleable.LoginTextView_iconButton);
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
        if (drawable != null)
            (findViewById(R.id.provider_image)).setBackgroundDrawable(drawable);

        if (!imageEnabled || drawable == null) {
            (findViewById(R.id.provider_image)).setVisibility(GONE);
            Space space = (Space) findViewById(R.id.space);
            space.setVisibility(GONE);
        }
    }

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

    private int getInverseColor(int color) {
        double y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000;
        return y >= 128 ? Color.BLACK : Color.WHITE;
    }

    public void setTextLogin(String name) {
        TextView textView = (TextView) findViewById(R.id.provider_name);
        textView.setTextColor(getInverseColor(getBackgroundColor()));
        textView.setText(String.format("Masuk Dengan %s", name));
    }

    public void setTextRegister(String name) {
        TextView textView = (TextView) findViewById(R.id.provider_name);
        textView.setTextColor(getInverseColor(getBackgroundColor()));
        textView.setText(String.format("Daftar Dengan %s", name));
    }

    public void setText(String name) {
        TextView textView = (TextView) findViewById(R.id.provider_name);
        textView.setText(name);
    }

    public void setImage(String image) {
        if(!TextUtils.isEmpty(image)) {
            ImageView imageView = (ImageView) findViewById(R.id.provider_image);
            ImageHandler.LoadImage(imageView, image);
        }
    }

    public void setTextColorInverse() {
        int temp;
        temp = getBackgroundColor();
        TextView textView = (TextView) findViewById(R.id.provider_name);
        textView.setTextColor(getInverseColor(temp));
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

    public void setImageVisibility(int visibility) {
        ImageView imageView = (ImageView) findViewById(R.id.provider_image);
        imageView.setVisibility(visibility);
    }

    public void setTextVisibility(int visibility) {
        TextView textView = (TextView) findViewById(R.id.provider_name);
        textView.setVisibility(visibility);
        Space space = (Space) findViewById(R.id.space);
        space.setVisibility(visibility);
    }

    public void setImageNextToText() {
        TextView textView = (TextView) findViewById(R.id.provider_name);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
    }

    public void setImageResource(int imageResource) {
        ImageView imageView = (ImageView) findViewById(R.id.provider_image);
        ImageHandler.loadImageWithIdWithoutPlaceholder(imageView, imageResource);
    }
}
