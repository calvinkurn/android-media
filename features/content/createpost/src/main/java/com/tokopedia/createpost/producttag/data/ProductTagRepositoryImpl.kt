package com.tokopedia.createpost.producttag.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.createpost.producttag.domain.repository.ProductTagRepository
import com.tokopedia.createpost.producttag.domain.usecase.GetFeedLastTaggedProductUseCase
import com.tokopedia.createpost.producttag.view.uimodel.LastTaggedProductUiModel
import com.tokopedia.createpost.producttag.view.uimodel.mapper.ProductTagUiModelMapper
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
class ProductTagRepositoryImpl @Inject constructor(
    private val getFeedLastTaggedProductUseCase: GetFeedLastTaggedProductUseCase,
    private val mapper: ProductTagUiModelMapper,
    private val dispatchers: CoroutineDispatchers,
) : ProductTagRepository {

    override suspend fun getLastTaggedProducts(
        authorId: String,
        authorType: String,
        cursor: String,
        limit: Int
    ): LastTaggedProductUiModel {
        return withContext(dispatchers.io) {
            val response = getFeedLastTaggedProductUseCase.apply {
                setRequestParams(GetFeedLastTaggedProductUseCase.createParams(
                    authorId = authorId,
                    authorType = authorType,
                    cursor = cursor,
                    limit = limit,
                ))
            }.executeOnBackground()

            mapper.mapLastTaggedProduct(response)
        }
    }
}