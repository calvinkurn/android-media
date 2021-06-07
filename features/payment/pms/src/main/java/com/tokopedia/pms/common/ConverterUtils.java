package com.tokopedia.pms.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 4/6/17.
 */

public class ConverterUtils {
    public static String[] convertCharSequenceToString(CharSequence[] charSequenceArray) {
        List<String> list = new ArrayList<>();
        for (CharSequence charSequence: charSequenceArray) {
            list.add(charSequence.toString());
        }
        return list.toArray(new String[list.size()]);
    }
}
