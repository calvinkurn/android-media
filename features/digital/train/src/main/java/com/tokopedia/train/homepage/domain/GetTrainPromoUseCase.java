package com.tokopedia.train.homepage.domain;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.homepage.data.entity.BannerDetail;
import com.tokopedia.train.homepage.presentation.model.TrainPromoAttributesViewModel;
import com.tokopedia.train.homepage.presentation.model.TrainPromoViewModel;
import com.tokopedia.train.homepage.presentation.model.TrainPromoViewModelMapper;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 23/07/18.
 */
public class GetTrainPromoUseCase extends UseCase<List<TrainPromoViewModel>> {
    private static final String PARAM_LANGUANGE = "languageId";
    private static final String PARAM_COUNTRY = "countryId";
    private static final String PARAM_DEVICE = "deviceId";
    private static final String PARAM_INSTANCE = "instance";

    private static final int DEFAULT_LANGUAGE = 0;
    private static final String DEFAULT_COUNTRY = "ID";
    private static final int DEFAULT_DEVICE = 5;
    private static final int DEFAULT_INSTANCE = 2;

    private TrainRepository trainRepository;
    private TrainPromoViewModelMapper trainPromoViewModelMapper;

    public GetTrainPromoUseCase(TrainRepository trainRepository, TrainPromoViewModelMapper trainPromoViewModelMapper) {
        this.trainRepository = trainRepository;
        this.trainPromoViewModelMapper = trainPromoViewModelMapper;
    }

    @Override
    public Observable<List<TrainPromoViewModel>> createObservable(RequestParams requestParams) {
        return trainRepository
                .getBanners(requestParams.getParameters())
                .map(new Func1<List<BannerDetail>, List<TrainPromoViewModel>>() {
                    @Override
                    public List<TrainPromoViewModel> call(List<BannerDetail> bannerDetails) {
                        return trainPromoViewModelMapper.transform(bannerDetails);
                    }
                });
    }

    public RequestParams create() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(PARAM_LANGUANGE, DEFAULT_LANGUAGE);
        requestParams.putString(PARAM_COUNTRY, DEFAULT_COUNTRY);
        requestParams.putInt(PARAM_DEVICE, DEFAULT_DEVICE);
        requestParams.putInt(PARAM_INSTANCE, DEFAULT_INSTANCE);
        return requestParams;
    }
}
