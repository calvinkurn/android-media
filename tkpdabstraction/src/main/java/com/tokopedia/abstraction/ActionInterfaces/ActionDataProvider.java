package com.tokopedia.abstraction.ActionInterfaces;

public interface ActionDataProvider<T,V> {
    T getData(int actionId, V dataObject);
}
