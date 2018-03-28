package com.tokopedia.gamification.cracktoken;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;

/**
 * Created by nakama on 23/03/18.
 */

public class MaskedHeightImageView extends android.support.v7.widget.AppCompatImageView {
    private double percentMasked;
    private Rect maskedRect;

    Bitmap bitmap;

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

    private void init() {
        percentMasked = 100;
    }

    public void setPercentMasked(double percentMasked) {
        this.percentMasked = percentMasked;
        invalidate();
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
            maskedRect.set(0, (int) (  bitmap.getHeight() * percentMasked / 100), bitmap.getWidth(), bitmap.getHeight() );
            canvas.drawBitmap(bitmap,maskedRect, maskedRect, null);
            canvas.restoreToCount(saveCount);
        } else {
            super.onDraw(canvas);
        }
    }
}
