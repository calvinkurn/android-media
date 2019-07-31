package com.tokopedia.sellerapp.dashboard.view.widget;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.tokopedia.sellerapp.R;

/**
 * Created by hendry on 2019-06-16.
 */
public class RoundGradientProgressBar extends RoundCornerProgressBar {
    private int[] progressColors;

    public RoundGradientProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundGradientProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected GradientDrawable createGradientDrawable(int color) {
        GradientDrawable gradientDrawable;
        if (progressColors == null) {
            progressColors = new int[]{ContextCompat.getColor(getContext(), R.color.lightish_green),
                    ContextCompat.getColor(getContext(), R.color.tkpd_main_green)};
        }
        boolean existInProgressColor = false;
        for (int colorItem : progressColors) {
            if (color == colorItem) {
                existInProgressColor = true;
                break;
            }
        }
        if (existInProgressColor) {
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, progressColors);
        } else {
            gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(color);
        }
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        return gradientDrawable;
    }

    public void setProgressColor(int[] colorProgress) {
        super.setProgressColor(colorProgress[0]);
    }
}

