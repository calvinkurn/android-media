package com.tokopedia.similarsearch.helper;

import com.tokopedia.similarsearch.R;

public class RatingHelper {
    public static int getRatingDrawable(int param) {
        switch (param) {
            case 0:
                return R.drawable.ss_ic_star_none;
            case 1:
                return R.drawable.ss_ic_star_one;
            case 2:
                return R.drawable.ss_ic_star_two;
            case 3:
                return R.drawable.ss_ic_star_three;
            case 4:
                return R.drawable.ss_ic_star_four;
            case 5:
                return R.drawable.ss_ic_star_five;
            default:
                return R.drawable.ss_ic_star_none;
        }
    }
}
