package com.tokopedia.filter.newdynamicfilter.helper;

import com.tokopedia.filter.R;

public class RatingHelper {
    public static int getRatingDrawable(int ratingCount) {
        switch (ratingCount) {
            case 0:
                return R.drawable.ic_star_none;
            case 1:
                return R.drawable.ic_star_one;
            case 2:
                return R.drawable.ic_star_two;
            case 3:
                return R.drawable.ic_star_three;
            case 4:
                return R.drawable.ic_star_four;
            case 5:
                return R.drawable.ic_star_five;
            default:
                return R.drawable.ic_star_none;
        }
    }
}
