package com.tokopedia.shop.info.view.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfoShipment;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfoShipmentPackage;
import com.tokopedia.shop.info.view.model.ShopInfoLogisticViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 12/14/17.
 */

public class ShopInfoLogisticViewModelMapper {

    public ShopInfoLogisticViewModelMapper() {
    }

    public List<Visitable> transform(List<ShopInfoShipment> shopInfoShipmentList) {
        List<Visitable> visitableList = new ArrayList<>();
        for (ShopInfoShipment shopInfoShipment : shopInfoShipmentList) {
            ShopInfoLogisticViewModel logisticViewModel = new ShopInfoLogisticViewModel();
            logisticViewModel.setShipmentImage(shopInfoShipment.getShipmentImage());
            logisticViewModel.setShipmentName(shopInfoShipment.getShipmentName());
            String packagName = "";
            for (ShopInfoShipmentPackage shipmentPackage : shopInfoShipment.getShipmentPackage()) {
                if (!TextUtils.isEmpty(packagName)) {
                    packagName += ", ";
                }
                packagName += shipmentPackage.getProductName();
            }
            logisticViewModel.setShipmentPackage(packagName);
            visitableList.add(logisticViewModel);
        }
        return visitableList;
    }
}
