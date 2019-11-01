package com.tokopedia.common.travel.domain;

import com.tokopedia.common.travel.database.CountryPhoneCodeTable;

import java.util.List;

import rx.Observable;

/**
 * Created by nabillasabbaha on 12/03/19.
 */
public interface IPhoneCodeRepository {

    Observable<List<CountryPhoneCodeTable>> getPhoneCodeList(String string);

    List<CountryPhoneCodeTable> getCountryById(String id);
}
