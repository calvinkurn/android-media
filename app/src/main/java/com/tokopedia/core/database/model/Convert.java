package com.tokopedia.core.database.model;


import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by m.normansyah on 2/2/16.
 */
public interface Convert<E extends NetworkModel,T extends BaseModel> {

    /**
     *
     * @param e object network
     * @return object of db
     */
    T toDb(E e);

    /**
     *
     * @param t object db
     * @return object of network
     */
    E toNetwork(T t);

}
