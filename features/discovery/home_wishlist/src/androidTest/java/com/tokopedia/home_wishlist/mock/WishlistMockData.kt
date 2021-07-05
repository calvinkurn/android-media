package com.tokopedia.home_wishlist.mock

import android.content.Context
import com.tokopedia.instrumentation.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.RestMockUtil
import com.tokopedia.test.application.util.setupRestMockResponse

/**
 * Created by yfsx on 1/21/21.
 */
class WishlistMockData: MockModelConfig() {
    companion object{

        const val KEY_CONTAINS_WISHLIST = "getWishlist"
        const val KEY_CONTAINS_ADD_TO_CART = "cart"
        const val KEY_CONTAINS_DELETE_WISHLIST = "wishlist_remove"
        const val KEY_CONTAINS_BANNER = "https://ta.tokopedia.com/v1.3/display?item=1&user_id=108956738&inventory_id=6&page_token=&ep=banner&device=android&dimen_id=3"
    }
    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_CONTAINS_ADD_TO_CART,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_wishlist_addtocart),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_CONTAINS_DELETE_WISHLIST,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_wishlist_delete),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_CONTAINS_WISHLIST,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_wishlist),
                FIND_BY_CONTAINS)

        return this
    }
}