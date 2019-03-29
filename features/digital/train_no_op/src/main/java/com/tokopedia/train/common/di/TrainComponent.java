package com.tokopedia.train.common.di;

import com.tokopedia.train.common.domain.TrainRepository;

public interface TrainComponent {

    TrainRepository trainRepository();
}