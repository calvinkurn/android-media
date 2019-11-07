package com.tokopedia.product.detail.usecase

import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

class GetProductAllDataUseCase @Inject constructor(private val getProductInfoP1UseCase: GetProductInfoP1UseCase)
    : UseCase<Result<ProductInfo.Response>>() {

    override suspend fun executeOnBackground(): Result<ProductInfo.Response> {
        var dataP1: Result<ProductInfo.Response> = Success(ProductInfo.Response())

        supervisorScope {
            try {
                getProductInfoP1UseCase.params = GetProductInfoP1UseCase.createParams(179098219, "", "")
                dataP1 = Success(getProductInfoP1UseCase.executeOnBackground())

            } catch (e: Throwable) {
                dataP1 = Fail(e)
            }
        }

        return dataP1
    }
}