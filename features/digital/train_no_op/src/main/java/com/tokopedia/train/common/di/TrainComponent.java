package com.tokopedia.train.common.di;

import com.tokopedia.train.common.domain.TrainRepository;

/**
 * @author by alvarisi on 2/19/18.
 */
public interface TrainComponent {

    TrainRepository trainRepository();
}