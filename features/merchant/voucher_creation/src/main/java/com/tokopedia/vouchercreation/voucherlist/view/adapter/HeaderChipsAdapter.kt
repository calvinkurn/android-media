package com.tokopedia.vouchercreation.voucherlist.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.vouchercreation.voucherlist.model.ui.BaseHeaderChipUiModel
import com.tokopedia.vouchercreation.voucherlist.view.adapter.factory.HeaderChipFactoryImpl

/**
 * Created By @ilhamsuaib on 20/04/20
 */

class HeaderChipsAdapter(
        onClick: (element: BaseHeaderChipUiModel) -> Unit
) : BaseAdapter<HeaderChipFactoryImpl>(HeaderChipFactoryImpl(onClick)) {

    var items = listOf<BaseHeaderChipUiModel>()
        private set
        get() = visitables.filterIsInstance<BaseHeaderChipUiModel>()
}