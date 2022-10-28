package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital_product_detail.data.mapper.DigitalMenuDetailUiMapper
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogMenuDetailRepository
import com.tokopedia.digital_product_detail.domain.usecase.GetRechargeCatalogMenuDetailUseCase
import com.tokopedia.common.topupbills.favoritepdp.domain.model.MenuDetailModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeCatalogMenuDetailRepositoryImpl @Inject constructor(
    private val getRechargeCatalogMenuDetailUseCase: GetRechargeCatalogMenuDetailUseCase,
    private val mapper: DigitalMenuDetailUiMapper,
    private val dispatchers: CoroutineDispatchers
): RechargeCatalogMenuDetailRepository {

    override suspend fun getMenuDetail(menuId: Int): MenuDetailModel = withContext(dispatchers.io) {
        val menuDetail = getRechargeCatalogMenuDetailUseCase.apply {
            setMenuDetailParams(menuId)
        }.executeOnBackground()

        return@withContext mapper.mapMenuDetailModel(menuDetail.catalogMenuDetailData)
    }
}