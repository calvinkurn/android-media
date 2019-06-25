package com.tokopedia.events.view.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.events.R;

/**
 * Created by pranaymohapatra on 28/11/17.
 */

public class ImageTextViewHolder extends LinearLayout {

    ImageView imageHolderSmall;
    TextView textViewHolder;
    private Context mContext;

    public ImageTextViewHolder(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public ImageTextViewHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ImageTextViewHolder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = inflate(mContext, R.layout.image_textview_holder, this);
        imageHolderSmall = view.findViewById(R.id.image_holder_small);
        textViewHolder = view.findViewById(R.id.textview_holder_small);
    }

    public void setImage(int resID) {
        imageHolderSmall.setImageResource(resID);
    }

    public void setTextView(String label) {
        textViewHolder.setText(label);
    }

    public void setTextColor(int color) {
        textViewHolder.setTextColor(color);
    }

    public void setTextSize(int size) {
        textViewHolder.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setImageTint(int tintColor) {
        imageHolderSmall.setColorFilter(tintColor);
    }
}
