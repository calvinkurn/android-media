package com.tokopedia.vouchercreation.shop.voucherlist.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.ShareVoucherUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory.ShareVoucherFactoryImpl

/**
 * Created By @ilhamsuaib on 28/04/20
 */

class ShareVoucherAdapter(onItemClickListener: (ShareVoucherUiModel) -> Unit) : BaseAdapter<ShareVoucherFactoryImpl>(ShareVoucherFactoryImpl(onItemClickListener))