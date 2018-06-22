package com.tokopedia.events.view.utils;

import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.events.R2;

import butterknife.BindView;

/**
 * Created by pranaymohapatra on 28/11/17.
 */

public class ImageTextViewHolder {

    @BindView(R2.id.image_holder_small)
    ImageView imageHolderSmall;

    @BindView(R2.id.textview_holder_small)
    TextView textViewHolder;

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
