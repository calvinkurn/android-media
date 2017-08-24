package com.tokopedia.posapp.base.data;

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

    List<S> getAllData(ConditionGroup conditions);

    List<S> getAllData();

    boolean isTableEmpty();
}
