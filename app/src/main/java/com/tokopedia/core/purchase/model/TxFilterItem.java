package com.tokopedia.core.purchase.model;

/**
 * Created by Angga.Prasetiyo on 25/04/2016.
 */
public class TxFilterItem {
    private static final String TAG = TxFilterItem.class.getSimpleName();
    private String id;
    private String name;

    public TxFilterItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
