package com.tokopedia.analytic_constant;

import com.google.android.gms.common.util.VisibleForTesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataLayer {
    @VisibleForTesting
    public static Map<String, Object> mapOf(Object... var0) {
        if (var0.length % 2 != 0) {
            throw new IllegalArgumentException("expected even number of key-value pairs");
        } else {
            HashMap var1 = new HashMap();

            for(int var2 = 0; var2 < var0.length; var2 += 2) {
                if (!(var0[var2] instanceof String)) {
                    String var3 = String.valueOf(var0[var2]);
                    throw new IllegalArgumentException((new StringBuilder(21 + String.valueOf(var3).length())).append("key is not a string: ").append(var3).toString());
                }

                var1.put((String)var0[var2], var0[var2 + 1]);
            }

            return var1;
        }
    }

    @VisibleForTesting
    public static List<Object> listOf(Object... var0) {
        ArrayList var1 = new ArrayList();

        for(int var2 = 0; var2 < var0.length; ++var2) {
            var1.add(var0[var2]);
        }

        return var1;
    }
}
