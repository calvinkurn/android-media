package com.tokopedia.product.manage.feature.etalase.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.product.manage.feature.etalase.view.adapter.factory.EtalaseListAdapterFactory
import com.tokopedia.product.manage.feature.etalase.view.adapter.viewholder.EtalaseViewHolder

class EtalaseAdapter(
    listener: EtalaseViewHolder.OnClickListener
): BaseAdapter<EtalaseListAdapterFactory>(EtalaseListAdapterFactory(listener))