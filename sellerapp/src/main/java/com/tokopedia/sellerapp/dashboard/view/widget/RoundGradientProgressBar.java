package com.tokopedia.sellerapp.dashboard.view.widget;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.tokopedia.seller.R;

public class RoundGradientProgressBar extends RoundCornerProgressBar {
    private GradientDrawable gradientDrawable;

    public RoundGradientProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundGradientProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int[] colors = {ContextCompat.getColor(context, R.color.lightish_green),
                ContextCompat.getColor(context, R.color.tkpd_main_green)};
        gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,colors);
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
    }

    protected GradientDrawable createGradientDrawable(int color) {
        return gradientDrawable;
    }
}
