package com.tokopedia.train.common.di.utils;

import android.app.Application;

import com.tokopedia.train.common.di.TrainComponent;

public class TrainComponentUtils {
    private static TrainComponent trainComponent;

    public static TrainComponent getTrainComponent(Application application) {
        return trainComponent;
    }
}
