package com.tokopedia.createpost.producttag.analytic

import com.tokopedia.createpost.producttag.analytic.product.ProductTagAnalytic
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 23, 2022
 */
class ContentProductTagAnalytic @Inject constructor(
    private val productTagAnalytic: ProductTagAnalytic,
): ProductTagAnalytic by productTagAnalytic