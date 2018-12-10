package com.tokopedia.loyalty.domain.repository;

import android.text.TextUtils;

import com.tokopedia.loyalty.common.PopUpNotif;
import com.tokopedia.loyalty.common.TokoPointDrawerData;
import com.tokopedia.loyalty.domain.entity.response.Coupon;
import com.tokopedia.loyalty.domain.entity.response.CouponListDataResponse;
import com.tokopedia.loyalty.domain.entity.response.DigitalVoucherData;
import com.tokopedia.loyalty.domain.entity.response.GqlTokoPointDrawerDataResponse;
import com.tokopedia.loyalty.domain.entity.response.HachikoDrawerDataResponse;
import com.tokopedia.loyalty.domain.entity.response.TokoPointResponse;
import com.tokopedia.loyalty.domain.entity.response.ValidateRedeemCouponResponse;
import com.tokopedia.loyalty.domain.entity.response.VoucherResponse;
import com.tokopedia.loyalty.view.data.CouponData;
import com.tokopedia.loyalty.view.data.CouponViewModel;
import com.tokopedia.loyalty.view.data.CouponsDataWrapper;
import com.tokopedia.loyalty.view.data.EmptyMessage;
import com.tokopedia.loyalty.view.data.VoucherViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class TokoPointResponseMapper implements ITokoPointResponseMapper {

    @Inject
    public TokoPointResponseMapper() {
    }

    @Override
    public String sampleMapper(TokoPointResponse tokoplusResponse) {
        return tokoplusResponse.getStrResponse();
    }

    @Override
    public List<CouponData> convertCouponListData(CouponListDataResponse couponListDataResponse) {
        List<CouponData> couponDataList = new ArrayList<>();
        if (couponListDataResponse != null && couponListDataResponse.getCoupons() != null)
            for (Coupon coupon : couponListDataResponse.getCoupons()) {
                couponDataList.add(
                        new CouponData.Builder()
                                .id(coupon.getId())
                                .promoId(coupon.getPromoId())
                                .code(coupon.getCode())
                                .title(coupon.getTitle())
                                .subTitle(coupon.getSubTitle())
                                .description(coupon.getDescription())
                                .expired(coupon.getExpired())
                                .imageUrl(coupon.getImageUrl())
                                .imageUrlMobile(coupon.getImageUrlMobile())
                                .icon(coupon.getIcon())
                                .build()
                );
            }
        return couponDataList;
    }


    @Override
    public TokoPointDrawerData convertTokoplusPointDrawer(HachikoDrawerDataResponse dataResponse) {

        GqlTokoPointDrawerDataResponse tokoplusPointDrawerData = dataResponse.getGqlTokoPointDrawerDataResponse();

        PopUpNotif popUpNotif = new PopUpNotif();
        TokoPointDrawerData.UserTier userTier = new TokoPointDrawerData.UserTier();
        TokoPointDrawerData tokoPointDrawerData = new TokoPointDrawerData();

        if (tokoplusPointDrawerData != null
                && tokoplusPointDrawerData.getGqlTokoPointPopupNotif() != null &&
                !TextUtils.isEmpty(tokoplusPointDrawerData.getGqlTokoPointPopupNotif().getTitle())) {
            tokoPointDrawerData.setHasNotif(1);
        } else {
            tokoPointDrawerData.setHasNotif(0);
        }

        if (tokoplusPointDrawerData != null) {
            tokoPointDrawerData.setOffFlag(tokoplusPointDrawerData.getOffFlag() ? 1 : 0);
            tokoPointDrawerData.setMainPageUrl(tokoplusPointDrawerData.getGqlTokoPointUrl().getMainPageUrl());
            tokoPointDrawerData.setMainPageTitle("");
        }

        if (tokoplusPointDrawerData != null
                && tokoplusPointDrawerData.getGqlTokoPointStatus() != null &&
                tokoplusPointDrawerData.getGqlTokoPointStatus().getGqlTokoPointTier() != null) {
            userTier.setTierNameDesc(tokoplusPointDrawerData.getGqlTokoPointStatus().getGqlTokoPointTier().getNameDesc());
            userTier.setTierImageUrl(tokoplusPointDrawerData.getGqlTokoPointStatus().getGqlTokoPointTier().getImageUrl());
            tokoPointDrawerData.setUserTier(userTier);
        } else {
            tokoPointDrawerData.setUserTier(null);
        }

        if (tokoplusPointDrawerData != null
                && tokoplusPointDrawerData.getGqlTokoPointStatus() != null &&
                tokoplusPointDrawerData.getGqlTokoPointStatus().getGqlTokoPointPoints() != null) {
            userTier.setRewardPointsStr(tokoplusPointDrawerData.getGqlTokoPointStatus().getGqlTokoPointPoints().getRewardString());//getUserTier().getRewardPointsStr());

        } else {
            userTier.setRewardPointsStr("");
        }

        if (tokoplusPointDrawerData != null && tokoplusPointDrawerData.getGqlTokoPointPopupNotif() != null) {
            popUpNotif.setAppLink(tokoplusPointDrawerData.getGqlTokoPointPopupNotif().getAppLink());
            popUpNotif.setButtonText(tokoplusPointDrawerData.getGqlTokoPointPopupNotif().getButtonText());
            popUpNotif.setButtonUrl(tokoplusPointDrawerData.getGqlTokoPointPopupNotif().getButtonURL());
            popUpNotif.setImageUrl(tokoplusPointDrawerData.getGqlTokoPointPopupNotif().getImageURL());
            popUpNotif.setText(tokoplusPointDrawerData.getGqlTokoPointPopupNotif().getText());
            popUpNotif.setTitle(tokoplusPointDrawerData.getGqlTokoPointPopupNotif().getTitle());
            tokoPointDrawerData.setPopUpNotif(popUpNotif);
        } else {
            tokoPointDrawerData.setPopUpNotif(null);
        }

        if (dataResponse.getTokopointsSumCoupon() != null) {
            tokoPointDrawerData.setSumCoupon(dataResponse.getTokopointsSumCoupon().getSumCoupon());
            tokoPointDrawerData.setSumCouponStr(dataResponse.getTokopointsSumCoupon().getSumCouponStr());
        }
        return tokoPointDrawerData;
    }

    @Override
    public VoucherViewModel voucherViewModel(VoucherResponse voucherResponse, String voucherCode) {
        VoucherViewModel viewModel = new VoucherViewModel();
        viewModel.setAmount(voucherResponse.getVoucher().getVoucherAmountIdr());
        viewModel.setMessage(voucherResponse.getVoucher().getVoucherPromoDesc());
        viewModel.setCode(voucherCode);
        return viewModel;
    }

    @Override
    public CouponViewModel couponViewModel(VoucherResponse voucherResponse, String voucherCode, String couponTitle) {
        CouponViewModel viewModel = new CouponViewModel();
        viewModel.setAmount(voucherResponse.getVoucher().getVoucherAmountIdr());
        viewModel.setMessage(voucherResponse.getVoucher().getVoucherPromoDesc());
        viewModel.setCode(voucherCode);
        viewModel.setTitle(couponTitle);
        return viewModel;
    }

    @Override
    public VoucherViewModel digtialVoucherViewModel(DigitalVoucherData voucherResponse,
                                                    String voucherCode) {
        VoucherViewModel viewModel = new VoucherViewModel();
        viewModel.setAmount(voucherResponse.getAttributes().getDiscountAmount());
        viewModel.setMessage(voucherResponse.getAttributes().getMessage());
        viewModel.setCode(voucherCode);
        viewModel.setRawCashback(voucherResponse.getAttributes().getCashbackAmountPlain());
        viewModel.setRawDiscount(voucherResponse.getAttributes().getDiscountAmountPlain());
        return viewModel;
    }

    @Override
    public CouponViewModel digitalCouponViewModel(DigitalVoucherData voucherResponse,
                                                  String voucherCode, String couponTitle) {
        CouponViewModel viewModel = new CouponViewModel();
        viewModel.setAmount(voucherResponse.getAttributes().getDiscountAmount());
        viewModel.setMessage(voucherResponse.getAttributes().getMessage());
        viewModel.setCode(voucherCode);
        viewModel.setTitle(couponTitle);
        viewModel.setRawCashback(voucherResponse.getAttributes().getCashbackAmountPlain());
        viewModel.setRawDiscount(voucherResponse.getAttributes().getDiscountAmountPlain());
        return viewModel;
    }

    @Override
    public CouponsDataWrapper convertCouponsDataWraper(CouponListDataResponse couponListDataResponse) {
        CouponsDataWrapper wrapper = new CouponsDataWrapper();
        wrapper.setCoupons(convertCouponListData(couponListDataResponse));
        wrapper.setEmptyMessage(convertEmptyMessageData(couponListDataResponse));
        return wrapper;
    }

    private EmptyMessage convertEmptyMessageData(CouponListDataResponse couponListDataResponse) {
        EmptyMessage emptyMessage = null;
        if (couponListDataResponse != null && couponListDataResponse.getEmptyMessage() != null) {
            emptyMessage = new EmptyMessage();
            emptyMessage.setTitle(couponListDataResponse.getEmptyMessage().getTitle());
            emptyMessage.setSubTitle(couponListDataResponse.getEmptyMessage().getSubTitle());
        }
        return emptyMessage;
    }

    @Override
    public String getSuccessValidateRedeemMessage(ValidateRedeemCouponResponse response) {
        return response.getMessageSuccess();
    }
}
