package com.tokopedia.vouchercreation.voucherlist.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.vouchercreation.voucherlist.model.ui.ShareVoucherUiModel
import com.tokopedia.vouchercreation.voucherlist.view.adapter.factory.ShareVoucherFactoryImpl

/**
 * Created By @ilhamsuaib on 28/04/20
 */

class ShareVoucherAdapter(onItemClickListener: (ShareVoucherUiModel) -> Unit) : BaseAdapter<ShareVoucherFactoryImpl>(ShareVoucherFactoryImpl(onItemClickListener))