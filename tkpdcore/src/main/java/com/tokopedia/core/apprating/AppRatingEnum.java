package com.tokopedia.core.apprating;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.tokopedia.core.R;

/**
 * Created by okasurya on 1/10/18.
 */

public enum AppRatingEnum {
    EMPTY(0,0,0),
    WORST(R.drawable.ic_app_rating_1, R.string.app_rating_desc_worst, R.color.red_1),
    BAD(R.drawable.ic_app_rating_2, R.string.app_rating_desc_bad, R.color.red_1),
    ORDINARY(R.drawable.ic_app_rating_3, R.string.app_rating_desc_ordinary, R.color.yellow_1),
    GOOD(R.drawable.ic_app_rating_4, R.string.app_rating_desc_good, R.color.medium_green),
    BEST(R.drawable.ic_app_rating_5, R.string.app_rating_desc_super, R.color.medium_green);

    @DrawableRes
    private int drawableId;
    @StringRes
    private int stringId;
    @ColorRes
    private int colorId;

    private AppRatingEnum(int drawableId, int stringId, int colorId) {
        this.drawableId = drawableId;
        this.stringId = stringId;
        this.colorId = colorId;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public int getStringId() {
        return stringId;
    }

    public int getColorId() {
        return colorId;
    }
}
