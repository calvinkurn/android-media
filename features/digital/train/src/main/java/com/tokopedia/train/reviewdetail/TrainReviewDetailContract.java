package com.tokopedia.train.reviewdetail;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;

import java.util.List;

/**
 * Created by Rizky on 02/07/18.
 */
public class TrainReviewDetailContract {

    interface View extends BaseListViewListener<TrainReviewPassengerInfoViewModel> {

        void showScheduleTrips(TrainScheduleViewModel first, TrainScheduleViewModel second);

    }

    public interface Presenter {

        void getPassengers(TrainSoftbook trainSoftbook, String origin, String destination);

        void getScheduleDetail(String departureScheduleId, String returnScheduleId);

    }

}
