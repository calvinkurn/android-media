package com.tokopedia.flight.bookingV2.presentation.viewmodel.mapper

import com.tokopedia.flight.booking.data.cloud.entity.Amenity
import com.tokopedia.flight.booking.data.cloud.entity.Voucher
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingCartData
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingVoucherViewModel
import com.tokopedia.flight.booking.view.viewmodel.mapper.FlightBookingAmenityViewModelMapper
import com.tokopedia.flight.booking.view.viewmodel.mapper.FlightInsuranceViewModelMapper
import com.tokopedia.flight.bookingV2.data.entity.GetCartEntity
import javax.inject.Inject

/**
 * @author by furqan on 08/03/19
 */
class FlightBookingCartDataMapper @Inject constructor(private val flightBookingAmenityViewModelMapper: FlightBookingAmenityViewModelMapper,
                                                      private val flightInsuranceViewModelMapper: FlightInsuranceViewModelMapper) {

    fun transform(flightBookingCartData: FlightBookingCartData?, entity: GetCartEntity?): FlightBookingCartData {
        val data = flightBookingCartData ?: FlightBookingCartData()
        if (entity != null) {
            data.id = entity.cartId
            data.refreshTime = entity.attributes.flight.repriceTime
            data.isDomestic = entity.attributes.flight.isDomestic
            data.isMandatoryDob = entity.attributes.flight.mandatoryDob
            data.voucherViewModel = transform(entity.attributes.voucher)

            for (item in entity.attributes.flight.journeys) {
                // replace price
                if (data.departureTrip != null && data.departureTrip.id != null &&
                        item.id.equals(data.departureTrip.id, true)) {
                    data.departureTrip.adultNumericPrice = item.totalPerPax.adultTotal.toInt()
                    data.departureTrip.childNumericPrice = item.totalPerPax.childTotal.toInt()
                    data.departureTrip.infantNumericPrice = item.totalPerPax.infantTotal.toInt()
                } else if (data.returnTrip != null && data.returnTrip.id != null &&
                        item.id.equals(data.returnTrip.id, true)) {
                    data.returnTrip.adultNumericPrice = item.totalPerPax.adultTotal.toInt()
                    data.returnTrip.childNumericPrice = item.totalPerPax.childTotal.toInt()
                    data.returnTrip.infantNumericPrice = item.totalPerPax.infantTotal.toInt()
                }
            }

            if (entity.attributes.flight.amenities.isNotEmpty()) {
                val luggageMetaViewModels = arrayListOf<FlightBookingAmenityMetaViewModel>()
                val mealMetaViewModels = arrayListOf<FlightBookingAmenityMetaViewModel>()
                for (item in entity.attributes.flight.amenities) {
                    when (item.type) {
                        Amenity.MEAL -> {
                            val mealMetaViewModel = FlightBookingAmenityMetaViewModel()
                            mealMetaViewModel.arrivalId = item.arrivalId
                            mealMetaViewModel.departureId = item.departureId
                            mealMetaViewModel.key = item.key
                            mealMetaViewModel.journeyId = item.journeyId
                            mealMetaViewModel.description = item.description
                            mealMetaViewModel.amenities = flightBookingAmenityViewModelMapper.transform(item)

                            mealMetaViewModels.add(mealMetaViewModel)
                        }
                        Amenity.LUGGAGE -> {
                            val luggageMetaViewModel = FlightBookingAmenityMetaViewModel()
                            luggageMetaViewModel.arrivalId = item.arrivalId
                            luggageMetaViewModel.departureId = item.departureId
                            luggageMetaViewModel.key = item.key
                            luggageMetaViewModel.journeyId = item.journeyId
                            luggageMetaViewModel.description = item.description
                            luggageMetaViewModel.amenities = flightBookingAmenityViewModelMapper.transform(item)

                            luggageMetaViewModels.add(luggageMetaViewModel)
                        }
                    }
                }
                data.luggageViewModels = luggageMetaViewModels
                data.mealViewModels = mealMetaViewModels
            } else {
                data.luggageViewModels = arrayListOf()
                data.mealViewModels = arrayListOf()
            }

            if (entity.attributes.insurances.isNotEmpty()) {
                val insuranceViewModels = flightInsuranceViewModelMapper.transform(entity.attributes.insurances)
                data.insurances = insuranceViewModels
            } else {
                data.insurances = arrayListOf()
            }

            data.newFarePrices = entity.attributes.newPrice
        }
        return data
    }

    private fun transform(voucher: Voucher): FlightBookingVoucherViewModel {
        val data = FlightBookingVoucherViewModel()

        data.isEnableVoucher = voucher.enableVoucher
        data.isCouponActive = voucher.isCouponActive
        data.defaultPromoTab = voucher.defaultPromoTab
        if (voucher.autoApply != null) {
            data.isAutoapplySuccess = voucher.autoApply.isSuccess
            data.code = voucher.autoApply.code
            data.isCoupon = voucher.autoApply.isCoupon
            data.discountAmount = voucher.autoApply.discountAmount.toDouble()
            data.discountPrice = voucher.autoApply.discountPrice
            data.discountedAmount = voucher.autoApply.discountedAmount.toDouble()
            data.discountedPrice = voucher.autoApply.discountedPrice
            data.titleDescription = voucher.autoApply.titleDescription
            data.messageSuccess = voucher.autoApply.messageSuccess
            data.promoId = voucher.autoApply.promoId
        }

        return data
    }

}