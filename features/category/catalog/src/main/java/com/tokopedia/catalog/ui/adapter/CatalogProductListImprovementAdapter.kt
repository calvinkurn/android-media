package com.tokopedia.catalog.ui.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.RecyclerView
import com.tkpd.atcvariant.view.adapter.AtcVariantAdapterTypeFactoryImpl
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapterDiffutil

class CatalogProductListImprovementAdapter(
    asyncDifferConfig: AsyncDifferConfig<Visitable<*>>,
    baseListAdapterTypeFactory: CatalogProductListAdapterFactoryImpl
) : BaseListAdapterDiffutil<CatalogProductListAdapterFactoryImpl>(asyncDifferConfig, baseListAdapterTypeFactory) {


}
