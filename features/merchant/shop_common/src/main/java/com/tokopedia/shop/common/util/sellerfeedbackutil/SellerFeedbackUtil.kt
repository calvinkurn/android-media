package com.tokopedia.shop.common.util.sellerfeedbackutil

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created By @ilhamsuaib on 14/10/21
 */

class SellerFeedbackUtil(
    context: Context
) {
    companion object {
        private const val SHARED_PREF_NAME = "seller_feedback_pref"
        private const val SELECTED_PAGE = "seller_feedback_selected_fragment"

        /*These constants value refers to the seller feedback,
        com.tokopedia.sellerfeedback.presentation.util.ScreenShootPageHelper*/
        const val SELLER_HOME_PAGE = "seller_home"
        const val PRODUCT_MANAGE_PAGE = "product_manage"
        const val CHAT_PAGE = "top_chat"
        const val SOM_PAGE = "som"
        const val ADD_PRODUCT = "add_product"
        const val EDIT_PRODUCT = "edit_product"
    }

    private val sharedPref by lazy {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    suspend fun setSelectedPage(page: String) {
        withContext(Dispatchers.IO) {
            val spe = sharedPref.edit()
            spe.putString(SELECTED_PAGE, page)
            spe.apply()
        }
    }
}