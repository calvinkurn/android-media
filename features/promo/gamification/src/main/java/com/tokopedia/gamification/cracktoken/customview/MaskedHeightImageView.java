package com.tokopedia.gamification.cracktoken.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.FloatProperty;
import android.util.IntProperty;
import android.util.Property;
import android.view.View;

/**
 * Created by nakama on 23/03/18.
 */

public class MaskedHeightImageView extends androidx.appcompat.widget.AppCompatImageView {
    private int percentMasked;
    private Rect maskedRect;
    private Bitmap bitmap;

    public MaskedHeightImageView(Context context) {
        super(context);
        init();
    }

    public MaskedHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MaskedHeightImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public static final Property<MaskedHeightImageView, Integer> MASKED_PERCENT = new IntProperty<MaskedHeightImageView>("percentMasked") {
        @Override
        public void setValue(MaskedHeightImageView object, int value) {
            object.setPercentMasked(value);
        }

        @Override
        public Integer get(MaskedHeightImageView object) {
            return object.getPercentMasked();
        }
    };

    private void init() {
        percentMasked = 100;
    }

    public void reset(){
        init();
        invalidate();
    }
    public void setPercentMasked(int percentMasked) {
        this.percentMasked = percentMasked;
        invalidate();
    }

    public int getPercentMasked() {
        return percentMasked;
    }

    public boolean isFullyHiddenByMask(){
        return percentMasked == 0;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        bitmap = bm;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap != null && !bitmap.isRecycled()) {
            final int saveCount = canvas.getSaveCount();
            canvas.save();

            canvas.translate(getPaddingLeft(), getPaddingTop());

            if (getImageMatrix() != null) {
                canvas.concat(getImageMatrix());
            }
            if (maskedRect==null) {
                maskedRect = new Rect();
            }
            maskedRect.set(0, 0, bitmap.getWidth(), (int)((1f - percentMasked / 100f) * bitmap.getHeight()));
            canvas.drawBitmap(bitmap,maskedRect, maskedRect, null);
            canvas.restoreToCount(saveCount);
        } else {
            super.onDraw(canvas);
        }
    }
}
