
package com.tokopedia.tkpd.database;


import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by Angga.Prasetiyo on 27/04/2016.
 */
public interface DBOperation<T extends BaseModel> {

    void store();

    void delete(String key);

    void deleteAll();

    boolean isExpired(long time);

    T getData(String key);

    List<T> getDataList(String key);

    String getValueString(String key);

    @SuppressWarnings("unchecked")
    <Z> Z getConvertObjData(String key, Class<Z> clazz);
}
