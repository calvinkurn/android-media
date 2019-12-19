package com.tokopedia.train.passenger.data;

import androidx.annotation.IntDef;

import static com.tokopedia.train.passenger.data.TrainPassengerTitle.NONA;
import static com.tokopedia.train.passenger.data.TrainPassengerTitle.NYONYA;
import static com.tokopedia.train.passenger.data.TrainPassengerTitle.TUAN;

/**
 * Created by nabillasabbaha on 26/06/18.
 */
@IntDef({TUAN, NYONYA, NONA})
public @interface TrainPassengerTitle {

    int TUAN = 1;
    int NYONYA = 2;
    int NONA = 3;
}
