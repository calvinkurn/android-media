package com.tokopedia.topads.keyword.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModelDatum
import com.tokopedia.topads.keyword.helper.KeywordTypeMapper
import com.tokopedia.topads.keyword.view.model.KeywordAd
import kotlinx.android.synthetic.main.item_top_ads_keyword_add_new.view.*

class TopAdsKeywordAddViewHolder(val view: View): AbstractViewHolder<KeywordAd>(view) {

    fun bind(data: AddKeywordDomainModelDatum) {
        itemView.apply {
            title.text = data.keywordTag
            type.text = KeywordTypeMapper.mapToKeywordName(itemView.context, data.keyWordTypeId)
        }
    }

    override fun bind(element: KeywordAd) {
        itemView.apply {
            title.text = element.keywordTag
            type.text = element.keywordTypeDesc
            delete.visibility = View.GONE

        }
    }
}