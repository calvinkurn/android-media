package com.tokopedia.design.label.selection;

/**
 * Created by nathan on 10/29/17.
 */

public class SelectionItem<T> {

    private String key;
    private T value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
