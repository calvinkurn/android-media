package com.tokopedia.flight.passenger.domain.model;

import android.text.TextUtils;

import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight_dbflow.FlightPassengerDB;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by furqan on 23/02/18.
 */

public class ListPassengerViewModelMapper {

    @Inject
    public ListPassengerViewModelMapper() {
    }

    public FlightBookingPassengerViewModel transform(FlightPassengerDB savedPassengerEntity) {
        FlightBookingPassengerViewModel flightBookingPassengerViewModel = new FlightBookingPassengerViewModel();
        if (savedPassengerEntity.getBirthdate() != null && !TextUtils.isEmpty(savedPassengerEntity.getBirthdate())) {
            flightBookingPassengerViewModel.setPassengerBirthdate(
                    FlightDateUtil.formatDate(
                            FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                            FlightDateUtil.DEFAULT_FORMAT,
                            savedPassengerEntity.getBirthdate()
                    )
            );
        }

        if (savedPassengerEntity.getPassportNo() != null && !TextUtils.isEmpty(savedPassengerEntity.getPassportNo())) {
            flightBookingPassengerViewModel.setPassportNumber(savedPassengerEntity.getPassportNo());
        }

        if (savedPassengerEntity.getPassportExpiry() != null && !TextUtils.isEmpty(savedPassengerEntity.getPassportExpiry())) {
            flightBookingPassengerViewModel.setPassportExpiredDate(savedPassengerEntity.getPassportExpiry());
        }

        if (savedPassengerEntity.getPassportNationality() != null && !TextUtils.isEmpty(savedPassengerEntity.getPassportNationality())) {
            FlightBookingPhoneCodeViewModel passportNationality = new FlightBookingPhoneCodeViewModel();
            passportNationality.setCountryId(savedPassengerEntity.getPassportNationality());
            flightBookingPassengerViewModel.setPassportNationality(passportNationality);
        }

        if (savedPassengerEntity.getPassportCountry() != null && !TextUtils.isEmpty(savedPassengerEntity.getPassportCountry())) {
            FlightBookingPhoneCodeViewModel passportIssuerCountry = new FlightBookingPhoneCodeViewModel();
            passportIssuerCountry.setCountryId(savedPassengerEntity.getPassportCountry());
            flightBookingPassengerViewModel.setPassportIssuerCountry(passportIssuerCountry);
        }

        flightBookingPassengerViewModel.setPassengerFirstName(savedPassengerEntity.getFirstName());
        flightBookingPassengerViewModel.setPassengerLastName(savedPassengerEntity.getLastName());
        flightBookingPassengerViewModel.setPassengerTitleId(savedPassengerEntity.getTitleId());
        flightBookingPassengerViewModel.setPassengerId(savedPassengerEntity.getPassengerId());

        return flightBookingPassengerViewModel;
    }

    public List<FlightBookingPassengerViewModel> transform(List<FlightPassengerDB> savedPassengerEntityList) {
        List<FlightBookingPassengerViewModel> flightBookingPassengerViewModelList = new ArrayList<>();

        for (FlightPassengerDB savedPassengerEntity : savedPassengerEntityList) {
            flightBookingPassengerViewModelList.add(this.transform(savedPassengerEntity));
        }

        return flightBookingPassengerViewModelList;
    }
}

