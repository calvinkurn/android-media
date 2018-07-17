//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tokopedia.instantloan.ddcollector;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseCollector implements InfoCollector {
    public abstract String getType();

    public Map<String, Object> buildPhoneInfo() {
        String type = this.getInfoType();
        Map phoneInfo = new HashMap();

        try {
            phoneInfo.put(type, this.getData());
        } catch (Exception e) {
            phoneInfo.put(type, (Object) null);
        }

        return phoneInfo;
    }

    public abstract Object getData();

    public String getInfoType() {
        return this.getType();
    }
}
