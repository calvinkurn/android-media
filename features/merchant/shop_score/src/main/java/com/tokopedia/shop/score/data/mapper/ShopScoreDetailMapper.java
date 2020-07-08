package com.tokopedia.shop.score.data.mapper;

import android.content.Context;
import androidx.annotation.NonNull;

import com.tokopedia.shop.score.data.model.detail.ShopScoreDetailItemServiceModel;
import com.tokopedia.shop.score.data.model.detail.ShopScoreDetailServiceModel;
import com.tokopedia.shop.score.domain.model.ShopScoreDetailDomainModel;
import com.tokopedia.shop.score.domain.model.ShopScoreDetailItemDomainModel;
import com.tokopedia.shop.score.domain.model.ShopScoreDetailSummaryDomainModel;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailMapper
        implements Func1<ShopScoreDetailServiceModel, ShopScoreDetailDomainModel> {

    private final Context context;
    private final UserSessionInterface sessionHandler;

    @Inject
    public ShopScoreDetailMapper(Context context, UserSessionInterface sessionHandler) {
        this.context = context;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public ShopScoreDetailDomainModel call(ShopScoreDetailServiceModel serviceModel) {
        ShopScoreDetailDomainModel dataDomain = new ShopScoreDetailDomainModel();

        List<ShopScoreDetailItemDomainModel> dataDomainItemModels =
                mapShopScoreDetailItemDomainModels(serviceModel);
        dataDomain.setItemModels(dataDomainItemModels);

        ShopScoreDetailSummaryDomainModel summaryModel =
                mapShopScoreDetailSummaryDomainModels(serviceModel);
        dataDomain.setSummaryModel(summaryModel);

        int state = mapShopScoreDetailStateDomainModels(serviceModel);
        dataDomain.setState(state);

        return dataDomain;
    }

    private int mapShopScoreDetailStateDomainModels(ShopScoreDetailServiceModel serviceModel) {
        if (sessionHandler.isGoldMerchant()) {
            if (isScoreHigherThanLimit(serviceModel)) {
                return ShopScoreDetailDomainModel.GOLD_MERCHANT_QUALIFIED_BADGE;
            } else {
                return ShopScoreDetailDomainModel.GOLD_MERCHANT_NOT_QUALIFIED_BADGE;
            }
        } else {
            if (isScoreHigherThanLimit(serviceModel)) {
                return ShopScoreDetailDomainModel.NOT_GOLD_MERCHANT_QUALIFIED_BADGE;
            } else {
                return ShopScoreDetailDomainModel.NOT_GOLD_MERCHANT_NOT_QUALIFIED_BADGE;
            }
        }
    }

    private boolean isScoreHigherThanLimit(
            ShopScoreDetailServiceModel shopScoreDetailServiceModel
    ) {
        return shopScoreDetailServiceModel.getData().getSumData().getValue() >=
                shopScoreDetailServiceModel.getData().getBadgeScore();
    }

    private ShopScoreDetailSummaryDomainModel mapShopScoreDetailSummaryDomainModels(
            ShopScoreDetailServiceModel serviceModel
    ) {
        ShopScoreDetailSummaryDomainModel summaryDomainModel =
                new ShopScoreDetailSummaryDomainModel();
        summaryDomainModel.setValue(serviceModel.getData().getSumData().getValue());
        String color = serviceModel.getData().getSumData().getColor();
        summaryDomainModel.setColor(ColorUtil.formatColor(color));
        summaryDomainModel.setText(serviceModel.getData().getSumData().getText());
        return summaryDomainModel;
    }

    @NonNull
    private List<ShopScoreDetailItemDomainModel> mapShopScoreDetailItemDomainModels(
            ShopScoreDetailServiceModel serviceModel
    ) {
        List<ShopScoreDetailItemDomainModel> dataDomainItemModels = new ArrayList<>();
        for (ShopScoreDetailItemServiceModel data : serviceModel.getData().getData()) {
            ShopScoreDetailItemDomainModel dataServiceModel = dataDomainModelMap(data);
            dataDomainItemModels.add(dataServiceModel);
        }
        return dataDomainItemModels;
    }

    private ShopScoreDetailItemDomainModel dataDomainModelMap(
            ShopScoreDetailItemServiceModel data
    ) {
        ShopScoreDetailItemDomainModel domainModel = new ShopScoreDetailItemDomainModel();
        domainModel.setTitle(data.getTitle());
        domainModel.setValue(data.getValue());
        domainModel.setDescription(data.getDescription());
        domainModel.setProgressBarColor(ColorUtil.formatColorWithAlpha(data.getColor()));
        return domainModel;
    }
}
