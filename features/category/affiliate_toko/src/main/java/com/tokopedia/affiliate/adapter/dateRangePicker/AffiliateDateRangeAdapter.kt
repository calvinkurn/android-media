package com.tokopedia.affiliate.adapter.dateRangePicker

import androidx.recyclerview.widget.AsyncDifferConfig
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapterDiffutil

class AffiliateDateRangeAdapter(asyncDifferConfig: AsyncDifferConfig<Visitable<*>>,adapterFactory:AffiliateDatePickerAdapterFactory):
    BaseListAdapterDiffutil<AffiliateDatePickerAdapterFactory>(asyncDifferConfig,adapterFactory) {
}