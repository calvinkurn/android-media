package com.tokopedia.core.home.helper;

import android.content.res.Configuration;

/**
 * @author Kulomady on 11/22/16.
 */

public class ProductFeedHelper {
    // this value for main colum recyclerview
    public static final int LANDSCAPE_COLUMN_MAIN = 3;
    public static final int PORTRAIT_COLUMN_MAIN = 2;

    // this value for horizontal recyclerview
    public static final int LANDSCAPE_COLUMN_HEADER = 3;
    public static final int LANDSCAPE_COLUMN_FOOTER = 3;
    public static final int LANDSCAPE_COLUMN = 1;

    public static final int PORTRAIT_COLUMN_HEADER = 2;
    public static final int PORTRAIT_COLUMN_FOOTER = 2;
    public static final int PORTRAIT_COLUMN = 1;

    public static int calcColumnSize(int orientation) {
        int defaultColumnNumber = 1;
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                defaultColumnNumber = PORTRAIT_COLUMN_MAIN;
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                defaultColumnNumber = LANDSCAPE_COLUMN_MAIN;
                break;
        }
        return defaultColumnNumber;
    }

}
