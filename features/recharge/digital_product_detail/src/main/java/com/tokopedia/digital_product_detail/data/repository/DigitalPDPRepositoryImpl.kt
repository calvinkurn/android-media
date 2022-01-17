package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPRepository
import com.tokopedia.digital_product_detail.domain.repository.RechargeCatalogRepository
import javax.inject.Inject

class DigitalPDPRepositoryImpl @Inject constructor(
    private val rechargeCatalogRepo: RechargeCatalogRepository
): DigitalPDPRepository,
        RechargeCatalogRepository by rechargeCatalogRepo