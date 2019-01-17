package com.tokopedia.network

import kotlinx.coroutines.experimental.CompletableDeferred
import kotlinx.coroutines.experimental.Deferred
import retrofit2.*
import retrofit2.adapter.rxjava.HttpException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class CoroutineCallAdapterFactory private constructor(): CallAdapter.Factory() {

    companion object {
        @JvmStatic @JvmName("create")
        operator fun invoke() = CoroutineCallAdapterFactory()
    }

    override fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit): CallAdapter<*>? {
        if (Deferred::class.java != getRawType(returnType)) return null

        if (returnType !is ParameterizedType){
            throw IllegalStateException(
                    "Deferred return type must be parameterized as Deferred<Foo> or Deferred<out Foo>")
        }

        val responseType = getParameterUpperBound(0, returnType)
        val rawDeferredType = getRawType(responseType)
        return if (rawDeferredType == Response::class.java) {
            if (responseType !is ParameterizedType) {
                throw IllegalStateException(
                        "Response must be parameterized as Response<Foo> or Response<out Foo>")
            }
            ResponseCallAdapter<Any>(getParameterUpperBound(0, responseType))
        } else {
            BodyCallAdapter<Any>(responseType)
        }
    }

    private class BodyCallAdapter<T>(private val responseType: Type): CallAdapter<Deferred<T>> {

        override fun <R : Any?> adapt(call: Call<R>): Deferred<T> {
            val deferred = CompletableDeferred<T>()

            deferred.invokeOnCompletion {
                if (deferred.isCancelled){
                    call.cancel()
                }
            }

            call.enqueue(object : Callback<R> {
                override fun onFailure(call: Call<R>?, t: Throwable) {
                    deferred.cancel(t)
                }

                override fun onResponse(call: Call<R>, response: Response<R>) {

                    if (response.isSuccessful){
                        deferred.complete(response.body() as T)
                    } else {
                        deferred.cancel(HttpException(response))
                    }
                }
            })

            return deferred
        }

        override fun responseType(): Type = responseType
    }

    private class ResponseCallAdapter<T>(private val responseType: Type): CallAdapter<Deferred<Response<T>>> {

        override fun <R : Any?> adapt(call: Call<R>): Deferred<Response<T>> {
            val deferred = CompletableDeferred<Response<T>>()

            deferred.invokeOnCompletion {
                if (deferred.isCancelled){
                    call.cancel()
                }
            }

            call.enqueue(object : Callback<R> {
                override fun onFailure(call: Call<R>?, t: Throwable) {
                    deferred.cancel(t)
                }

                override fun onResponse(call: Call<R>, response: Response<R>) {

                    if (response.isSuccessful){
                        deferred.complete(response as Response<T>)
                    } else {
                        deferred.cancel(HttpException(response))
                    }
                }
            })

            return deferred
        }

        override fun responseType(): Type = responseType
    }
}