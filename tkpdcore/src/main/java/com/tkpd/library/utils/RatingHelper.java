package com.tkpd.library.utils;

import com.tokopedia.core2.R;

/**
 * Created by henrypriyono on 8/24/17.
 */

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
