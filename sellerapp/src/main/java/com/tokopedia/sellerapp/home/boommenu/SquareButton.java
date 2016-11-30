package com.tokopedia.sellerapp.home.boommenu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.sellerapp.R;

/**
 * Created by normansyahputa on 9/9/16.
 */

public class SquareButton extends FrameLayout {

    ImageView image;
    TextView text;
    private CircleButton.OnCircleButtonClickListener onCircleButtonClickListener;
    private int index;
    private RelativeLayout container;

    public SquareButton(Context context) {
        this(context, null);
    }

    public SquareButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.square_button, this, true);

        container = (RelativeLayout) findViewById(R.id.square_button_container);
        image = (ImageView) findViewById(R.id.image);
        text = (TextView) findViewById(R.id.text);
    }

    public void setOnCircleButtonClickListener(
            final CircleButton.OnCircleButtonClickListener onCircleButtonClickListener,
            final int index
    ){
        this.onCircleButtonClickListener = onCircleButtonClickListener;
        this.index = index;
        container.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onCircleButtonClickListener.onClick(index);
                    }
                }
        );
    }

    public void setColor(int pressedColor, int normalColor) {
        // TODO currently do nothing
//        Util.getInstance().setCircleButtonStateListDrawable(
//                imageButton, radius, pressedColor, normalColor);
    }

    public void setDrawable(Drawable drawable) {
        if (image != null) image.setImageDrawable(drawable);
    }

    public void setText(String text) {
        if (this.text != null) this.text.setText(text);
    }

    public TextView getTextView(){
        return text;
    }

    public ImageView getImageView(){
        return image;
    }
}
