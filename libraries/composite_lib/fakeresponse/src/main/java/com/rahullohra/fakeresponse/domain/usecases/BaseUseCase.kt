package com.rahullohra.fakeresponse.domain.usecases

import com.rahullohra.fakeresponse.data.models.Either
import com.rahullohra.fakeresponse.domain.repository.BaseRepository
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

open class BaseUseCase<T:BaseRepository>(t:T) {

    fun isJSONValid(test: String): Either<Exception, Boolean> {
        try {
            JSONObject(test)
        } catch (ex: JSONException) {
            JSONArray(test)
        } catch (ex1: JSONException) {
            return Either.Left(ex1)
        }
        return Either.Right(true)
    }
}