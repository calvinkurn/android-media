package com.tokopedia.flight.passenger.domain.model;

import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.passenger.data.db.FlightPassengerTable;

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

    public FlightBookingPassengerViewModel transform(FlightPassengerTable savedPassengerEntity) {
        FlightBookingPassengerViewModel flightBookingPassengerViewModel = new FlightBookingPassengerViewModel();
        if (savedPassengerEntity.getBirthdate() != null && !savedPassengerEntity.getBirthdate().isEmpty()) {
            flightBookingPassengerViewModel.setPassengerBirthdate(
                    FlightDateUtil.formatDate(
                            FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                            FlightDateUtil.DEFAULT_FORMAT,
                            savedPassengerEntity.getBirthdate()
                    )
            );
        }

        if (savedPassengerEntity.getPassportNo() != null && !savedPassengerEntity.getPassportNo().isEmpty()) {
            flightBookingPassengerViewModel.setPassportNumber(savedPassengerEntity.getPassportNo());
        }

        if (savedPassengerEntity.getPassportExpiry() != null && !savedPassengerEntity.getPassportExpiry().isEmpty()) {
            flightBookingPassengerViewModel.setPassportExpiredDate(
                    FlightDateUtil.formatDate(
                            FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                            FlightDateUtil.DEFAULT_FORMAT,
                            savedPassengerEntity.getPassportExpiry()
                    )
            );
        }

        if (savedPassengerEntity.getPassportNationality() != null && !savedPassengerEntity.getPassportNationality().isEmpty()) {
            FlightBookingPhoneCodeViewModel passportNationality = new FlightBookingPhoneCodeViewModel();
            passportNationality.setCountryId(savedPassengerEntity.getPassportNationality());
            flightBookingPassengerViewModel.setPassportNationality(passportNationality);
        }

        if (savedPassengerEntity.getPassportCountry() != null && !savedPassengerEntity.getPassportCountry().isEmpty()) {
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

    public List<FlightBookingPassengerViewModel> transform(List<FlightPassengerTable> savedPassengerEntityList) {
        List<FlightBookingPassengerViewModel> flightBookingPassengerViewModelList = new ArrayList<>();

        for (FlightPassengerTable savedPassengerEntity : savedPassengerEntityList) {
            flightBookingPassengerViewModelList.add(this.transform(savedPassengerEntity));
        }

        return flightBookingPassengerViewModelList;
    }
}

