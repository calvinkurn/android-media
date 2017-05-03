package com.tokopedia.core.base.utils;

import rx.functions.Func1;

public class ErrorCheck<T> implements Func1<T, T> {
    @Override
    public T call(T serviceModel) {
        if (serviceModel == null) {
            throw new RuntimeException("Cache is empty");
        } else {
            return serviceModel;
        }
    }
}