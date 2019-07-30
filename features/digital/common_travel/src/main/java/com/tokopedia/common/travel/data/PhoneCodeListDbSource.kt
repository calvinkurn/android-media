package com.tokopedia.common.travel.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.travel.data.entity.FlightCountryEntity
import com.tokopedia.flight.country.database.CountryPhoneCodeDao
import com.tokopedia.flight.country.database.CountryPhoneCodeTable
import rx.Observable
import java.io.IOException
import java.nio.charset.Charset
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 12/03/19.
 */
class PhoneCodeListDbSource @Inject
constructor(private val countryPhoneCodeDao: CountryPhoneCodeDao,
            private val phoneCodeListMapper: PhoneCodeListMapper,
            @param:ApplicationContext private val context: Context) {

    val data: List<FlightCountryEntity>
        get() {
            val g = Gson()
            val dataResponseType = object : TypeToken<DataResponse<List<FlightCountryEntity>>>() {

            }.type
            val dataResponse = g.fromJson<DataResponse<List<FlightCountryEntity>>>(loadJSONFromAsset(), dataResponseType)
            return dataResponse.data
        }

    fun dataFromJson(): Observable<List<CountryPhoneCodeTable>> {
        return Observable.just(true)
                .map { countryPhoneCodeDao.findAllPhoneCodes() }
                .flatMap { it ->
                    if (it.isEmpty()) {
                        return@flatMap Observable.just(data)
                                .map<List<CountryPhoneCodeTable>> { flightCountryData -> phoneCodeListMapper.mapEntitiesToTables(flightCountryData) }
                                .map<List<Long>> { airportCountryTables -> countryPhoneCodeDao.insertAll(airportCountryTables) }
                                .map<List<CountryPhoneCodeTable>> { result -> countryPhoneCodeDao.findAllPhoneCodes() }
                    } else {
                        return@flatMap Observable.just(true)
                                .map { countryPhoneCodeDao.findAllPhoneCodes() }
                    }
                }
    }

    fun loadJSONFromAsset(): String? {
        var json: String? = null
        try {
            val `is` = context.assets.open(FLIGHT_SEARCH_AIRPORT_JSON)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }

    fun getPhoneCodeList(keyword: String): Observable<List<CountryPhoneCodeTable>> {
        val query = "%$keyword%"
        return if (query == "") {
            dataFromJson()
        } else {
            dataFromJson()
                    .map { countryPhoneCodeDao.getPhoneCodeByKeyword(query) }
        }
    }

    fun getAirportByCountryId(id: String): Observable<CountryPhoneCodeTable> {
        val query = "%$id%"
        return dataFromJson()
                .map { countryPhoneCodeDao.getCountryIdByKeyword(query) }
    }

    companion object {
        val FLIGHT_SEARCH_AIRPORT_JSON = "travel_country_list.json"
    }
}