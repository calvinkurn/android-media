package com.tokopedia.otp.validator.domain.usecase

import com.tokopedia.usecase.coroutines.UseCase

/**
 * @author rival
 * @created on 9/12/2019
 */

abstract class ValidatorUseCase<T : Any> : UseCase<T>() {

    protected val params = mutableMapOf<String, Any>()

    fun createParams(params: Map<String, Any>) {
        this.params.clear()
        this.params.putAll(params)
    }
}