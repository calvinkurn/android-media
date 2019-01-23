package com.tokopedia.train.common.di;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.common.domain.TrainRepositoryImpl;

public class TrainModule {

    public TrainModule() {
    }

    public TrainRepository provideTrainRepository() {
        return new TrainRepositoryImpl();
    }
}
