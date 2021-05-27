package com.tokopedia.logintest.common.view.banner.domain.usecase

import com.tokopedia.usecase.coroutines.UseCase

/**
 * @author rival
 * @created on 20/02/2020
 */

abstract class BaseDynamicBannerUseCase<T : Any> : UseCase<T>() {

    protected val params = mutableMapOf<String, Any>()

    fun createParams(params: Map<String, Any>) {
        this.params.clear()
        this.params.putAll(params)
    }
}