package com.tokopedia.core.database.o2o;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by okasurya on 8/23/17.
 */

public interface DbManagerOperation<T extends BaseModel, S>  {
    void store(T data);

    void update(T data);

    void delete(ConditionGroup conditions);

    void deleteAll();

    S first(ConditionGroup conditions);

    S first();

    List<S> getListData(ConditionGroup conditions);

    List<S> getListData(int offset, int limit);

    List<S> getAllData();

    boolean isTableEmpty();
}
