package com.tokopedia.purchase_platform.common.base;

import java.util.List;

import javax.inject.Inject;

import dagger.Module;

/**
 * @author anggaprasetiyo on 23/02/18.
 */
@Module
public class MapperUtil implements IMapperUtil {

    @Inject
    public MapperUtil() {
    }

    @Override
    public String convertToString(List<String> stringList) {
        if (isEmpty(stringList)) {
            return "";
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            int length = stringList.size();
            for (int i = 0; i < length; i++) {
                String s = stringList.get(i);
                stringBuilder.append(s);
                if (length > 1 && i < length - 1) {
                    stringBuilder.append(", ");
                }
            }
            return stringBuilder.toString();
        }
    }

    @Override
    public boolean isEmpty(List<?> objectList) {
        return objectList == null || objectList.isEmpty();
    }

    @Override
    public boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    @Override
    public boolean isEmpty(Object object) {
        return object == null;
    }
}
