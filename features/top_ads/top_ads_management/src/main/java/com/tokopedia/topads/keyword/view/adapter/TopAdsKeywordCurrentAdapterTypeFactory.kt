package com.tokopedia.topads.keyword.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.R
import com.tokopedia.topads.common.view.adapter.TopAdsListAdapterTypeFactory
import com.tokopedia.topads.keyword.view.adapter.viewholder.TopAdsKeywordAddViewHolder
import com.tokopedia.topads.keyword.view.model.KeywordAd

class TopAdsKeywordCurrentAdapterTypeFactory: TopAdsListAdapterTypeFactory<KeywordAd>(){

    override fun type(keywordAd: KeywordAd): Int {
        return R.layout.item_top_ads_keyword_add_new
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            R.layout.item_top_ads_keyword_add_new -> TopAdsKeywordAddViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}