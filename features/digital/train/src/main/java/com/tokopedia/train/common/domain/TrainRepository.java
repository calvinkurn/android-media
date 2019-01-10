package com.tokopedia.train.common.domain;

import com.tokopedia.train.checkout.data.entity.TrainCheckoutEntity;
import com.tokopedia.train.homepage.data.entity.BannerDetail;
import com.tokopedia.train.homepage.presentation.model.TrainPromoViewModel;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.reviewdetail.data.entity.TrainCheckVoucherEntity;
import com.tokopedia.train.search.domain.FilterParam;
import com.tokopedia.train.search.presentation.model.AvailabilityKeySchedule;
import com.tokopedia.train.search.presentation.model.FilterSearchData;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.train.seat.data.entity.TrainChangeSeatEntity;
import com.tokopedia.train.seat.data.entity.TrainSeatMapEntity;
import com.tokopedia.train.seat.domain.model.TrainPassengerSeat;
import com.tokopedia.train.seat.domain.model.request.ChangeSeatMapRequest;
import com.tokopedia.train.station.domain.model.TrainStation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * @author by alvarisi on 3/5/18.
 */

public interface TrainRepository {

    Observable<List<TrainStation>> getPopularStations();

    Observable<List<TrainStation>> getStationsByKeyword(String keyword);

    Observable<TrainStation> getStationByStationCode(String stationCode);

    Observable<List<TrainStation>> getStationCitiesByKeyword(String keyword);

    Observable<List<AvailabilityKeySchedule>> getSchedule(Map<String, Object> mapParam, int scheduleVariant);

    Observable<List<TrainScheduleViewModel>> getAvailabilitySchedule(Map<String, Object> mapParam);

    Observable<List<TrainScheduleViewModel>> getFilteredAndSortedSchedule(FilterParam filterParam, int sortOptionId, int scheduleVariant);

    Observable<TrainScheduleViewModel> getDetailSchedule(String idSchedule);

    Observable<Integer> getCountSchedule(FilterSearchData filterSearchData, int scheduleVariant,
                                         String arrivalTimestampSelected);

    Observable<List<TrainSeatMapEntity>> getSeat(HashMap<String, Object> parameters);

    Observable<List<TrainStation>> getAllStations();

    Observable<List<TrainChangeSeatEntity>> changeSeats(List<ChangeSeatMapRequest> requests);

    Observable<TrainSoftbook> doSoftBookTrainTicket(Map<String, Object> mapParam);

    Observable<TrainCheckVoucherEntity> checkVoucher(HashMap<String, Object> parameters);

    Observable<TrainCheckoutEntity> checkout(HashMap<String, Object> parameters);

    Observable<List<BannerDetail>> getBanners(HashMap<String, Object> parameters);
}
