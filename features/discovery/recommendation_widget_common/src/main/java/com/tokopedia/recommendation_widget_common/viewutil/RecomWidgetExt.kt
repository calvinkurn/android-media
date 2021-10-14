package com.tokopedia.recommendation_widget_common.viewutil

import android.content.Context
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.recommendation_widget_common.presenter.RecommendationViewModel

/**
 * Created by yfsx on 13/10/21.
 */
fun <T> T.initViewModel(context: () -> Context) =
    RecomWidgetViewModelDelegate<RecommendationViewModel>(context)

fun RecommendationViewModel.updateRecomWidgetQtyItemWithMiniCart(theContext: Context) {
    theContext.let {
        this.getMiniCart(
            shopId = ChooseAddressUtils.getLocalizingAddressData(it)?.shop_id ?: "",
            pageName = ""
        )
    }
}
