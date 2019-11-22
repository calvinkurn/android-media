/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tokopedia.translator.repository.source

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GetDataService {
    @GET("/translate_a/single")
    @Headers("Content-Type: application/json")
    fun getTranslatedString(
        @Query("client") client: String,
        @Query("sl") sl: String,
        @Query("tl") tl: String,
        @Query("dt") dt: String,
        @Query("oe") oe: String,
        @Query("ie") ie: String,
        @Query("q") q: String
    ): Call<String>

    @GET("https://translate.yandex.net/api/v1.5/tr.json/translate")
    @Headers("Content-Type: application/json")
    fun getTranslatedStringY(
        @Query("key") key: String?,
        @Query("text") text: String,
        @Query("lang") lang: String?,
        @Query("format") format: String
    ): Call<String>


    @GET("https://translate.yandex.net/api/v1.5/tr.json/detect")
    @Headers("Content-Type: application/json")
    fun checkApiKeyValidity(
        @Query("key") key: String?,
        @Query("text") text: String
    ): Call<String>

}