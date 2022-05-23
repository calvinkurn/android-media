package com.tokopedia.createpost.producttag.analytic

import com.tokopedia.createpost.producttag.analytic.product.ProductTagAnalytic
import com.tokopedia.createpost.producttag.analytic.srp.SRPProductTagAnalytic
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 23, 2022
 */
class ContentProductTagAnalytic @Inject constructor(
    private val srpProductTagAnalytic: SRPProductTagAnalytic,
    private val productTagAnalytic: ProductTagAnalytic,
): SRPProductTagAnalytic by srpProductTagAnalytic,
    ProductTagAnalytic by productTagAnalytic