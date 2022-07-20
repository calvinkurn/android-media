package com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.typefactory.BuyerProductBundlingAdapterFactory
import com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.uimodel.BuyerNormalProductUiModel

class BuyerProductBundlingBottomSheetAdapter(normalProductItems: List<BuyerNormalProductUiModel>,
                                             adapterTypeFactory: BuyerProductBundlingAdapterFactory
) : BaseAdapter<BuyerProductBundlingAdapterFactory>(adapterTypeFactory, normalProductItems)