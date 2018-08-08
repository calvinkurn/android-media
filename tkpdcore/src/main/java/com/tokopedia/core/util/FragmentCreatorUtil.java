package com.tokopedia.core.util;

import android.app.Fragment;
import android.content.Context;


/**
 * Created by ricoharisin on 11/9/16.
 */

public class FragmentCreatorUtil {

    public static Fragment getFragment(Context context, String className) {
        return Fragment.instantiate(context, className);
    }




}
