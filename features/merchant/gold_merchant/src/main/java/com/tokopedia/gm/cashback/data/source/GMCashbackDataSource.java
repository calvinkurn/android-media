package com.tokopedia.gm.cashback.data.source;

import com.tokopedia.gm.cashback.data.model.RequestGetCashbackModel;
import com.tokopedia.seller.common.cashback.DataCashbackModel;
import com.tokopedia.seller.common.data.mapper.SimpleDataResponseMapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 10/2/17.
 */

public class GMCashbackDataSource {
    private final GMCashbackDataSourceCloud gmCashbackDataSourceCloud;

    @Inject
    public GMCashbackDataSource(GMCashbackDataSourceCloud gmCashbackDataSourceCloud) {
        this.gmCashbackDataSourceCloud = gmCashbackDataSourceCloud;
    }


    public Observable<List<DataCashbackModel>> getCashbackList(List<Long> productIds, String shopId) {
        RequestGetCashbackModel.DataRequestGetCashback dataRequestGetCashback = new RequestGetCashbackModel.DataRequestGetCashback(productIds, Long.parseLong(shopId));
        List<RequestGetCashbackModel.DataRequestGetCashback> requestGetCashbackModelList = new ArrayList<>();
        requestGetCashbackModelList.add(dataRequestGetCashback);
        RequestGetCashbackModel requestGetCashbackModel = new RequestGetCashbackModel(requestGetCashbackModelList);
        return gmCashbackDataSourceCloud.getCashbackList(requestGetCashbackModel)
                .map(new SimpleDataResponseMapper<List<DataCashbackModel>>())
                .map(new Func1<List<DataCashbackModel>, List<DataCashbackModel>>() {
                    @Override
                    public List<DataCashbackModel> call(List<DataCashbackModel> dataCashbackModels) {
                        if(dataCashbackModels == null){
                            return new ArrayList<DataCashbackModel>();
                        }else{
                            return dataCashbackModels;
                        }
                    }
                });
    }
}
