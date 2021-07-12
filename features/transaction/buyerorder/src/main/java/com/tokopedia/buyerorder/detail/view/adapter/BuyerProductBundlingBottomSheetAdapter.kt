package com.tokopedia.buyerorder.detail.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.buyerorder.detail.view.adapter.typefactory.BuyerProductBundlingAdapterFactory
import com.tokopedia.buyerorder.detail.view.adapter.uimodel.BuyerBundlingProductUiModel
import com.tokopedia.buyerorder.detail.view.adapter.uimodel.BuyerNormalProductUiModel

class BuyerProductBundlingBottomSheetAdapter(bundleProductItems: List<BuyerBundlingProductUiModel>,
                                             normalProductItems: List<BuyerNormalProductUiModel>,
                                             adapterTypeFactory: BuyerProductBundlingAdapterFactory)
    : BaseAdapter<BuyerProductBundlingAdapterFactory>(adapterTypeFactory, bundleProductItems + normalProductItems)