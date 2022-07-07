package com.tokopedia.affiliate.adapter.bottomSheetsAdapter

import androidx.recyclerview.widget.AsyncDifferConfig
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapterDiffutil

class AffiliateBottomSheetAdapter(asyncDifferConfig: AsyncDifferConfig<Visitable<*>>, adapterFactory:AffiliateBottomSheetAdapterFactory):
    BaseListAdapterDiffutil<AffiliateBottomSheetAdapterFactory>(asyncDifferConfig,adapterFactory) {
}