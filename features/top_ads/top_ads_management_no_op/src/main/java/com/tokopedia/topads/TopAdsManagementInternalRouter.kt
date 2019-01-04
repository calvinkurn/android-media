package com.tokopedia.topads

import android.content.Context
import android.content.Intent
import android.widget.Toast;
import com.tokopedia.topads.dashboard.view.activity.TopAdsAddingPromoOptionActivity
import com.tokopedia.topads.dashboard.view.activity.TopAdsDetailShopActivity
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupNewPromoActivity
import com.tokopedia.topads.group.view.activity.TopAdsGroupAdListActivity
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordAdListActivity
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordNewChooseGroupActivity
import com.tokopedia.topads.product.view.activity.TopAdsProductAdListActivity

object TopAdsManagementInternalRouter {

    @JvmStatic
    fun getTopAdsDetailShopIntent(context: Context): Intent {
        Toast.makeText(context, "TopAds Detail Shop", Toast.LENGTH_SHORT).show()
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse("http://www.getTopAdsDetailShopIntent.com"))
        return intent
    }

    @JvmStatic
    fun getTopAdsKeywordListIntent(context: Context): Intent {
        Toast.makeText(context, "TopAds Keyword List", Toast.LENGTH_SHORT).show()
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse("http://www.getTopAdsKeywordListIntent.com"))
        return intent
    }

    @JvmStatic
    fun getTopAdsAddingPromoOptionIntent(context: Context): Intent {
        Toast.makeText(context, "TopAds Option Add Promo", Toast.LENGTH_SHORT).show()
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse("http://www.getTopAdsAddingPromoOptionIntent.com"))
        return intent
    }

    @JvmStatic
    fun getTopAdsProductAdListIntent(context: Context): Intent {
        Toast.makeText(context, "TopAds Product List", Toast.LENGTH_SHORT).show()
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse("http://www.getTopAdsProductAdListIntent.com"))
        return intent
    }

    @JvmStatic
    fun getTopAdsGroupAdListIntent(context: Context): Intent {
        Toast.makeText(context, "TopAds Group List", Toast.LENGTH_SHORT).show()
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse("http://www.getTopAdsGroupAdListIntent.com"))
        return intent
    }

    @JvmStatic
    fun getTopAdsGroupNewPromoIntent(context: Context): Intent {
        Toast.makeText(context, "TopAds Add Group", Toast.LENGTH_SHORT).show()
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse("http://www.getTopAdsGroupNewPromoIntent.com"))
        return intent
    }

    @JvmStatic
    fun getTopAdsKeywordNewChooseGroupIntent(context: Context, isPositive: Boolean, groupId: String? =null): Intent {
        Toast.makeText(context, "TopAds Add Keyword", Toast.LENGTH_SHORT).show()
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse("http://www.getTopAdsKeywordNewChooseGroupIntent.com"))
        return intent
    }
}
