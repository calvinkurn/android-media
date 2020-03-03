package com.tokopedia.network

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import retrofit2.*
import retrofit2.adapter.rxjava.HttpException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Retrofit 2.6.0 is now coroutine support.
 * https://proandroiddev.com/suspend-what-youre-doing-retrofit-has-now-coroutines-support-c65bd09ba067
 * Change the interface to suspend (instead of Deferred<T>) and this can be removed from retrofit addCallAdapterFactory
 * example:
 *
 * @GET("api/me")
 * fun getUser(): Deferred<User>
 *
 *     to
 *
 * @GET("api/me")
 * suspend fun getUser(): User
 */
@Deprecated("Retrofit 2.6.0 is now coroutine support.")
class CoroutineCallAdapterFactory private constructor(): CallAdapter.Factory() {

    companion object {
        @JvmStatic @JvmName("create")
        operator fun invoke() = CoroutineCallAdapterFactory()
    }

    override fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
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

    private class BodyCallAdapter<T>(private val responseType: Type): CallAdapter<T, Deferred<T>> {

        override fun adapt(call: Call<T>): Deferred<T> {
            val deferred = CompletableDeferred<T>()

            deferred.invokeOnCompletion {
                if (deferred.isCancelled){
                    call.cancel()
                }
            }

            call.enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>?, t: Throwable) {
                    deferred.cancel()
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {

                    if (response.isSuccessful){
                        deferred.complete(response.body() as T)
                    } else {
                        deferred.cancel()
                    }
                }
            })

            return deferred
        }

        override fun responseType(): Type = responseType
    }

    private class ResponseCallAdapter<T>(private val responseType: Type): CallAdapter<T, Deferred<Response<T>>> {

        override fun adapt(call: Call<T>): Deferred<Response<T>> {
            val deferred = CompletableDeferred<Response<T>>()

            deferred.invokeOnCompletion {
                if (deferred.isCancelled){
                    call.cancel()
                }
            }

            call.enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>?, t: Throwable) {
                    deferred.cancel()
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {

                    if (response.isSuccessful){
                        deferred.complete(response)
                    } else {
                        deferred.cancel()
                    }
                }
            })

            return deferred
        }

        override fun responseType(): Type = responseType
    }
}