package com.tokopedia.flight.country.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.flight.country.FlightCountryListMapper
import com.tokopedia.flight.country.database.FlightAirportCountryDao
import com.tokopedia.flight.country.database.FlightAirportCountryTable
import rx.Observable
import java.io.IOException
import java.nio.charset.Charset
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 12/03/19.
 */
class FlightCountryListDbSource @Inject
constructor(private val flightAirportCountryDao: FlightAirportCountryDao,
            private val flightCountryListMapper: FlightCountryListMapper,
            @param:ApplicationContext private val context: Context) {

    val data: List<FlightCountryEntity>
        get() {
            val g = Gson()
            val dataResponseType = object : TypeToken<DataResponse<List<FlightCountryEntity>>>() {

            }.type
            val dataResponse = g.fromJson<DataResponse<List<FlightCountryEntity>>>(loadJSONFromAsset(), dataResponseType)
            return dataResponse.data
        }

    fun dataFromJson(): Observable<List<FlightAirportCountryTable>> {
        return Observable.just(true)
                .map { flightAirportCountryDao.findAllPhoneCodes() }
                .flatMap { it ->
                    if (it.isEmpty()) {
                        return@flatMap Observable.just(data)
                                .map<List<FlightAirportCountryTable>> { flightCountryData -> flightCountryListMapper.mapEntitiesToTables(flightCountryData) }
                                .map<List<Long>> { airportCountryTables -> flightAirportCountryDao.insertAll(airportCountryTables) }
                                .map<List<FlightAirportCountryTable>> { result -> flightAirportCountryDao.findAllPhoneCodes() }
                    } else {
                        return@flatMap Observable.just(true)
                                .map { flightAirportCountryDao.findAllPhoneCodes() }
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

    fun getPhoneCodeList(keyword: String): Observable<List<FlightAirportCountryTable>> {
        val query = "%$keyword%"
        return if (query == "") {
            dataFromJson()
        } else {
            dataFromJson()
                    .map { flightAirportCountryDao.getPhoneCodeByKeyword(query) }
        }
    }

    fun getAirportByCountryId(id: String): Observable<FlightAirportCountryTable> {
        val query = "%$id%"
        return dataFromJson()
                .map { flightAirportCountryDao.getCountryIdByKeyword(query) }
    }

    companion object {
        val FLIGHT_SEARCH_AIRPORT_JSON = "flight_search_airport.json"
    }
}