package com.tokopedia.topads

import android.content.Context
import android.content.Intent
import com.tokopedia.topads.dashboard.view.activity.TopAdsAddingPromoOptionActivity
import com.tokopedia.topads.dashboard.view.activity.TopAdsDetailShopActivity
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupNewPromoActivity
import com.tokopedia.topads.group.view.activity.TopAdsGroupAdListActivity
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordAdListActivity
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordNewChooseGroupActivity
import com.tokopedia.topads.product.view.activity.TopAdsProductAdListActivity
import com.tokopedia.topads.dashboard.view.activity.TopAdsDetailProductActivity

object TopAdsManagementInternalRouter {

    @JvmStatic
    fun getTopAdsDetailShopIntent(context: Context) = Intent(context, TopAdsDetailShopActivity::class.java)

    @JvmStatic
    fun getTopAdsKeywordListIntent(context: Context) = Intent(context, TopAdsKeywordAdListActivity::class.java)

    @JvmStatic
    fun getTopAdsAddingPromoOptionIntent(context: Context) = Intent(context, TopAdsAddingPromoOptionActivity::class.java)

    @JvmStatic
    fun getTopAdsProductAdListIntent(context: Context) = Intent(context, TopAdsProductAdListActivity::class.java)

    @JvmStatic
    fun getTopAdsGroupAdListIntent(context: Context) = Intent(context, TopAdsGroupAdListActivity::class.java)

    @JvmStatic
    fun getTopAdsGroupNewPromoIntent(context: Context) = Intent(context, TopAdsGroupNewPromoActivity::class.java)

    @JvmStatic
    fun getTopAdsKeywordNewChooseGroupIntent(context: Context, isPositive: Boolean, groupId: String? =null) =
            TopAdsKeywordNewChooseGroupActivity.createIntent(context, isPositive, groupId)

    @JvmStatic
    fun getTopAdsDetailProductIntent(context: Context) = Intent(context, TopAdsDetailProductActivity::class.java)
}