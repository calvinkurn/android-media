package com.tokopedia.digital.product.model;

import java.util.List;

/**
 * Created by Rizky on 12/18/17.
 */

public class PassDataSingleton<T> {

    private static PassDataSingleton instance;

    public synchronized static <T> PassDataSingleton <T> get() {
        if (instance == null) {
            instance = new PassDataSingleton();
        }
        return instance;
    }

    private int sync = 0;

    private T largeData;

    public int setLargeData(T largeData) {
        this.largeData = largeData;
        return ++sync;
    }

    public T getLargeData(int request) {
        return (request == sync) ? largeData : null;
    }

}
