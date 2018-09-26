package com.tokopedia.train.homepage.domain;

import com.tokopedia.train.homepage.presentation.model.TrainPromoAttributesViewModel;
import com.tokopedia.train.homepage.presentation.model.TrainPromoViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by nabillasabbaha on 23/07/18.
 */
public class GetTrainPromoUseCase extends UseCase<List<TrainPromoViewModel>> {

    public GetTrainPromoUseCase() {
    }

    @Override
    public Observable<List<TrainPromoViewModel>> createObservable(RequestParams requestParams) {
        //TODO =============== : this is dummy data for list promo, delete this after sync with API
        TrainPromoAttributesViewModel trainPromoAttributesViewModel = new TrainPromoAttributesViewModel(
                "Beli Tiket Pesawat dengan Kartu Kredit mandiri, Cashback hingga 300 Ribu",
                "https://www.tokopedia.com/promo", "", "AYOTERBANG");
        List<TrainPromoViewModel> trainPromoViewModelList = new ArrayList<>();
        TrainPromoViewModel trainPromoViewModel1 = new TrainPromoViewModel("31", trainPromoAttributesViewModel);
        TrainPromoViewModel trainPromoViewModel2 = new TrainPromoViewModel("32", trainPromoAttributesViewModel);
        TrainPromoViewModel trainPromoViewModel3 = new TrainPromoViewModel("33", trainPromoAttributesViewModel);
        trainPromoViewModelList.add(trainPromoViewModel1);
        trainPromoViewModelList.add(trainPromoViewModel2);
        trainPromoViewModelList.add(trainPromoViewModel3);

        return Observable.just(trainPromoViewModelList);
    }
}
