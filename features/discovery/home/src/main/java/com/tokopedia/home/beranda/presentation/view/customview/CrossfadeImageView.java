package com.tokopedia.home.beranda.presentation.view.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.tokopedia.home.R;

public class CrossfadeImageView extends Toolbar {

    ImageView img1;
    BitmapDrawable currentBitmapDrawable;

    public CrossfadeImageView(Context context) {
        super(context);
        init(context, null);
    }

    public CrossfadeImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CrossfadeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void inflateResource(Context context) {
        inflate(context, R.layout.layout_crossfade_image_view, this);
    }

    protected void init(Context context, @Nullable AttributeSet attrs) {
        inflateResource(context);
        img1 = findViewById(R.id.img1);
    }

    public void showImage(Bitmap bitmap) {
        if (currentBitmapDrawable == null){
            img1.setImageBitmap(bitmap);
            currentBitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        } else {
            TransitionDrawable td = new TransitionDrawable( new Drawable[] {
                    currentBitmapDrawable,
                    new BitmapDrawable(getResources(), bitmap)
            });
            img1.setImageDrawable(td);
            td.startTransition(250);
            currentBitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        }
    }

    public ImageView getImg1(){
        return img1;
    }
}