package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumber
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberData
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberItem
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberTrackingData
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoTrackingData

interface DigitalPDPTagihanListrikRepository : RechargeFavoriteNumberRepository,
    RechargeCatalogMenuDetailRepository,
    RechargeAddToCartRepository,
    RechargeCatalogOperatorSelectGroupRepository,
    RechargeCatalogProductInputMultiTabRepository,
    RechargeInquiryRepository