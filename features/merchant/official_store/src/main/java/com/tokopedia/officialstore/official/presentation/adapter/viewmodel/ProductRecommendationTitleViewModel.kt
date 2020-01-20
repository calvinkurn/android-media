package com.tokopedia.officialstore.official.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapterTypeFactory

class ProductRecommendationTitleViewModel (val title: String): Visitable<OfficialHomeAdapterTypeFactory> {

    override fun type(typeFactory: OfficialHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}