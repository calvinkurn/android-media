package com.tokopedia.tkpd.beranda.data.mapper;

import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashModel;
import com.tokopedia.core.drawer2.data.pojo.toppoints.TopPointsModel;
import com.tokopedia.tkpd.beranda.domain.model.saldo.HomeSaldoModel;

import rx.functions.Func2;

/**
 * Created by errysuprayogi on 12/5/17.
 */
public class SaldoDataMapper implements Func2<TokoCashModel, TopPointsModel, HomeSaldoModel> {
    @Override
    public HomeSaldoModel call(TokoCashModel tokoCashModel, TopPointsModel topPointsModel) {
        HomeSaldoModel homeSaldoModel = new HomeSaldoModel();
        if (tokoCashModel.isSuccess() && tokoCashModel.getTokoCashData() != null) {
            homeSaldoModel.setTokoCashData(tokoCashModel.getTokoCashData());
        }
        if (topPointsModel.isSuccess() && topPointsModel.getTopPointsData() != null) {
            homeSaldoModel.setTopPointsData(topPointsModel.getTopPointsData());
        }
        return homeSaldoModel;
    }
}
