package com.tokopedia.train.reviewdetail;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.train.passenger.domain.model.TrainPaxPassenger;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;

/**
 * Created by Rizky on 02/07/18.
 */
public class TrainReviewDetailContract {

    interface View extends BaseListViewListener<TrainReviewPassengerInfoViewModel> {

    }

    public interface Presenter {

        void getPassengers(TrainSoftbook trainSoftbook);

    }

}
