package com.tokopedia.gm.featured.domain.mapper;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.gm.featured.data.model.GMFeaturedProductDataModel;
import com.tokopedia.seller.common.featuredproduct.GMFeaturedProductDomainModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * Created by normansyahputa on 9/7/17.
 */

public class GMFeaturedProductMapper implements Func1<GMFeaturedProductDataModel, GMFeaturedProductDomainModel> {

    @Inject
    public GMFeaturedProductMapper() {
    }

    public static GMFeaturedProductDomainModel convert(GMFeaturedProductDataModel GMFeaturedProductDataModel) {
        GMFeaturedProductDomainModel GMFeaturedProductDomainModel
                = new GMFeaturedProductDomainModel();
        ArrayList<GMFeaturedProductDomainModel.Datum> data = new ArrayList<>();
        for (GMFeaturedProductDataModel.Datum datum : GMFeaturedProductDataModel.getData()) {
            GMFeaturedProductDomainModel.Datum res = new GMFeaturedProductDomainModel.Datum();
            res.setProductId(datum.getProductId());
            res.setCashback(datum.isCashback());

            res.setCashbackDetail(convert(datum.getCashbackDetail()));
            res.setImageUri(datum.getImageUri());
            res.setName(datum.getName());
            res.setPreorder(datum.isPreorder());
            res.setPrice(datum.getPrice());
            res.setReturnable(datum.isReturnable());
            res.setUri(datum.getUri());
            res.setWholesale(datum.isWholesale());
            res.setBadges(convert(datum.getBadges()));
            res.setLabels(convert2(datum.getLabels()));
            res.setWholesaleDetail(convert3(datum.getWholesaleDetail()));

            data.add(res);
        }

        GMFeaturedProductDomainModel.setData(data);
        return GMFeaturedProductDomainModel;
    }

    private static List<GMFeaturedProductDomainModel.WholesaleDetail> convert3(List<GMFeaturedProductDataModel.WholesaleDetail> wholesaleDetail) {
        List<GMFeaturedProductDomainModel.WholesaleDetail> details = new ArrayList<>();

        if (!CommonUtils.checkCollectionNotNull(details))
            return details;

        for (int i = 0; i < wholesaleDetail.size(); i++) {
            GMFeaturedProductDataModel.WholesaleDetail detail = wholesaleDetail.get(i);

            GMFeaturedProductDomainModel.WholesaleDetail wholesaleDetail1 = new GMFeaturedProductDomainModel.WholesaleDetail();

            wholesaleDetail1.setPrice(detail.getPrice());
            wholesaleDetail1.setQty(detail.getQty());

            details.add(wholesaleDetail1);
        }

        return details;
    }

    private static List<GMFeaturedProductDomainModel.Label> convert2(List<GMFeaturedProductDataModel.Label> labels) {
        List<GMFeaturedProductDomainModel.Label> details = new ArrayList<>();

        if (!CommonUtils.checkCollectionNotNull(labels))
            return details;

        for (int i = 0; i < labels.size(); i++) {
            GMFeaturedProductDataModel.Label detail = labels.get(i);

            GMFeaturedProductDomainModel.Label wholesaleDetail1 = new GMFeaturedProductDomainModel.Label();

            wholesaleDetail1.setTitle(detail.getTitle());
            wholesaleDetail1.setColor(detail.getColor());

            details.add(wholesaleDetail1);
        }

        return details;
    }

    public static GMFeaturedProductDomainModel.CashbackDetail convert(GMFeaturedProductDataModel.CashbackDetail cashbackDetail) {
        GMFeaturedProductDomainModel.CashbackDetail cashbackDetail1 = new GMFeaturedProductDomainModel.CashbackDetail();
        cashbackDetail1.setCashbackPercent(cashbackDetail.getCashbackPercent());
        cashbackDetail1.setCashbackStatus(cashbackDetail.getCashbackStatus());
        cashbackDetail1.setCashbackValue(cashbackDetail.getCashbackValue());
        cashbackDetail1.setIsCashbackExpired(cashbackDetail.getIsCashbackExpired());
        return cashbackDetail1;
    }

    public static List<GMFeaturedProductDomainModel.Badge> convert(List<GMFeaturedProductDataModel.Badge> data) {
        List<GMFeaturedProductDomainModel.Badge> badges = new ArrayList<>();

        if (!CommonUtils.checkCollectionNotNull(data))
            return badges;

        for (int i = 0; i < data.size(); i++) {
            GMFeaturedProductDataModel.Badge badge = data.get(i);

            GMFeaturedProductDomainModel.Badge badge1 = new GMFeaturedProductDomainModel.Badge();
            badge1.setImageUrl(badge.getImageUrl());
            badge1.setTitle(badge.getTitle());
            badges.add(badge1);
        }
        return badges;
    }

    @Override
    public GMFeaturedProductDomainModel call(GMFeaturedProductDataModel GMFeaturedProductDataModel) {
        return convert(GMFeaturedProductDataModel);
    }
}
