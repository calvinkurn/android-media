package com.tokopedia.flight.booking.view.viewmodel.mapper;

import com.tokopedia.flight.booking.data.cloud.entity.Amenity;
import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.booking.data.cloud.entity.Voucher;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingCartData;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingVoucherViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightInsuranceViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by alvarisi on 11/15/17.
 */

public class FlightBookingCartDataMapper {
    private FlightBookingAmenityViewModelMapper flightBookingAmenityViewModelMapper;
    private FlightInsuranceViewModelMapper flightInsuranceViewModelMapper;

    @Inject
    public FlightBookingCartDataMapper(FlightBookingAmenityViewModelMapper flightBookingAmenityViewModelMapper,
                                       FlightInsuranceViewModelMapper flightInsuranceViewModelMapper) {
        this.flightBookingAmenityViewModelMapper = flightBookingAmenityViewModelMapper;
        this.flightInsuranceViewModelMapper = flightInsuranceViewModelMapper;
    }

    public FlightBookingCartData transform(FlightBookingCartData data, CartEntity entity) {
        if (entity != null) {
            if (data == null) {
                data = new FlightBookingCartData();
            }
            data.setId(entity.getId());
            data.setRefreshTime(entity.getAttribute().getFlightAttribute().getRefreshTime());
            data.setDomestic(entity.getAttribute().getFlightAttribute().isDomestic() );
            if (entity.getAttribute().getFlightAttribute().getAmenities() != null) {
                List<FlightBookingAmenityMetaViewModel> luggageMetaViewModels = new ArrayList<>();
                List<FlightBookingAmenityMetaViewModel> mealMetaViewModels = new ArrayList<>();
                for (Amenity amenity : entity.getAttribute().getFlightAttribute().getAmenities()) {
                    switch (amenity.getType()) {
                        case Amenity.MEAL:
                            FlightBookingAmenityMetaViewModel mealMetaViewModel = new FlightBookingAmenityMetaViewModel();
                            mealMetaViewModel.setArrivalId(amenity.getArrivalId());
                            mealMetaViewModel.setDepartureId(amenity.getDepartureId());
                            mealMetaViewModel.setKey(amenity.getKey());
                            mealMetaViewModel.setJourneyId(amenity.getJourneyId());
                            mealMetaViewModel.setDescription(amenity.getDescription());
                            mealMetaViewModel.setAmenities(flightBookingAmenityViewModelMapper.transform(amenity));
                            mealMetaViewModels.add(mealMetaViewModel);
                            break;
                        case Amenity.LUGGAGE:
                            FlightBookingAmenityMetaViewModel luggageMetaViewModel = new FlightBookingAmenityMetaViewModel();
                            luggageMetaViewModel.setArrivalId(amenity.getArrivalId());
                            luggageMetaViewModel.setDepartureId(amenity.getDepartureId());
                            luggageMetaViewModel.setKey(amenity.getKey());
                            luggageMetaViewModel.setJourneyId(amenity.getJourneyId());
                            luggageMetaViewModel.setDescription(amenity.getDescription());
                            luggageMetaViewModel.setAmenities(flightBookingAmenityViewModelMapper.transform(amenity));
                            luggageMetaViewModels.add(luggageMetaViewModel);
                            break;
                    }
                }
                data.setLuggageViewModels(luggageMetaViewModels);
                data.setMealViewModels(mealMetaViewModels);
                data.setMandatoryDob(entity.getAttribute().getFlightAttribute().isMandatoryDob());
                data.setVoucherViewModel(transform(entity.getAttribute().getFlightAttribute().getVoucher()));
            } else {
                data.setLuggageViewModels(new ArrayList<FlightBookingAmenityMetaViewModel>());
                data.setMealViewModels(new ArrayList<FlightBookingAmenityMetaViewModel>());
            }
            if (entity.getInsurances() != null && entity.getInsurances().size() > 0) {
                List<FlightInsuranceViewModel> insuranceViewModels = flightInsuranceViewModelMapper.transform(entity.getInsurances());
                data.setInsurances(insuranceViewModels);
            } else {
                data.setInsurances(new ArrayList<FlightInsuranceViewModel>());
            }
            data.setNewFarePrices(entity.getAttribute().getFlightAttribute().getNewPrices());
        }
        return data;
    }

    private FlightBookingVoucherViewModel transform(Voucher voucher) {
        FlightBookingVoucherViewModel data = new FlightBookingVoucherViewModel();

        data.setEnableVoucher(voucher.getEnableVoucher());
        data.setIsCouponActive(voucher.getIsCouponActive());
        data.setDefaultPromoTab(voucher.getDefaultPromoTab());
        if (voucher.getAutoApply() != null) {
            data.setAutoapplySuccess(voucher.getAutoApply().getIsSuccess());
            data.setCode(voucher.getAutoApply().getCode());
            data.setIsCoupon(voucher.getAutoApply().getIsCoupon());
            data.setDiscountAmount(voucher.getAutoApply().getDiscountAmount());
            data.setDiscountPrice(voucher.getAutoApply().getDiscountPrice());
            data.setDiscountedAmount(voucher.getAutoApply().getDiscountedAmount());
            data.setDiscountedPrice(voucher.getAutoApply().getDiscountedPrice());
            data.setTitleDescription(voucher.getAutoApply().getTitleDescription());
            data.setMessageSuccess(voucher.getAutoApply().getMessageSuccess());
            data.setPromoId(voucher.getAutoApply().getPromoId());
        }

        return data;
    }

    public FlightBookingCartData transform(CartEntity entity) {
        FlightBookingCartData data = null;
        return transform(null, entity);
    }
}
