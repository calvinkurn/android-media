package com.tkpd.remoteresourcerequest.view;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tkpd.remoteresourcerequest.view.ImageDensityType.SUPPORT_MULTIPLE_DPI;
import static com.tkpd.remoteresourcerequest.view.ImageDensityType.SUPPORT_NO_DPI;
import static com.tkpd.remoteresourcerequest.view.ImageDensityType.SUPPORT_SINGLE_DPI;


/**
 * Created by sandeepgupta-xps on 6/22/20.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({SUPPORT_MULTIPLE_DPI, SUPPORT_SINGLE_DPI, SUPPORT_NO_DPI})
public @interface ImageDensityType {
    int SUPPORT_MULTIPLE_DPI = 0;
    int SUPPORT_SINGLE_DPI = 1;
    int SUPPORT_NO_DPI = 2;
}

