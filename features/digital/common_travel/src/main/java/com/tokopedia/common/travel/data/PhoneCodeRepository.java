package com.tokopedia.common.travel.data;

import com.tokopedia.common.travel.domain.IPhoneCodeRepository;
import com.tokopedia.flight.country.database.CountryPhoneCodeTable;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nabillasabbaha on 17/12/18.
 */
public class PhoneCodeRepository implements IPhoneCodeRepository {

    private PhoneCodeListDbSource phoneCodeListDbSource;

    @Inject
    public PhoneCodeRepository(PhoneCodeListDbSource phoneCodeListDbSource) {
        this.phoneCodeListDbSource = phoneCodeListDbSource;
    }

    @Override
    public Observable<List<CountryPhoneCodeTable>> getPhoneCodeList(String query) {
        return phoneCodeListDbSource.getPhoneCodeList(query);
    }
}
