package com.tokopedia.content.common.util.sidesheet;

import androidx.annotation.RestrictTo;

/**
 * @author by astidhiyaa on 13/06/23
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
final class SheetUtils {

    private SheetUtils() {}

    static boolean isSwipeMostlyHorizontal(float xVelocity, float yVelocity) {
        return Math.abs(xVelocity) > Math.abs(yVelocity);
    }
}
