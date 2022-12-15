package com.tokopedia.search.result.product.inspirationcarousel

import com.tokopedia.search.result.product.QueryKeyProvider
import javax.inject.Inject

class InspirationCarouselViewDelegate @Inject constructor(
    queryKeyProvider: QueryKeyProvider,
) : InspirationCarouselView,
    QueryKeyProvider by queryKeyProvider {

}
