package com.tokopedia.abstraction.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rizky on 19/04/18.
 */

public class HexValidator {

    private static final String HEX_PATTERN = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";

    /**
     * Validate hex with regular expression
     *
     * @param hex hex for validation
     * @return true valid hex, false invalid hex
     */
    public static boolean validate(final String hex) {
        Pattern pattern = Pattern.compile(HEX_PATTERN);
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();
    }

}