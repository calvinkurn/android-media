/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tokopedia.home.beranda.helper

data class Result<out T>(val status: Status, val data: T?, val error: Throwable?) {
    companion object {
        fun <T> success(data: T?): Result<T> {
            return Result(
                status = Status.SUCCESS,
                data = data,
                error = null
            )
        }

        fun <T> error(error: Throwable, data: T? = null): Result<T> {
            return Result(
                status = Status.ERROR,
                data = data,
                error = error
            )
        }

        fun <T> errorPagination(error: Throwable, data: T? = null): Result<T> {
            return Result(
                status = Status.ERROR_PAGINATION,
                data = data,
                error = error
            )
        }

        fun <T> errorAtf(error: Throwable, data: T? = null): Result<T> {
            return Result(
                status = Status.ERROR_ATF,
                data = data,
                error = error
            )
        }

        fun <T> errorGeneral(error: Throwable, data: T? = null): Result<T> {
            return Result(
                status = Status.ERROR_GENERAL,
                data = data,
                error = error
            )
        }

        fun <T> errorNewAtfMechanism(error: Throwable, data: T? = null): Result<T> {
            return Result(
                status = Status.ERROR_ATF_NEW,
                data = data,
                error = error
            )
        }
    }

    enum class Status {
        SUCCESS,
        ERROR,
        ERROR_PAGINATION,
        LOADING,
        ERROR_ATF,
        ERROR_GENERAL,
        ERROR_ATF_NEW
    }

    fun Status.isLoading() = this == Status.LOADING

    fun Status.isError() = this == Status.ERROR

    fun Status.isSuccess() = this == Status.SUCCESS
}
