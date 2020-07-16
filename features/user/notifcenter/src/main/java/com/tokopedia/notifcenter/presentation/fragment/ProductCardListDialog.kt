package com.tokopedia.notifcenter.presentation.fragment

import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.mapper.MultipleProductCardMapper
import com.tokopedia.notifcenter.data.state.SourceMultipleProductView
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.presentation.BaseBottomSheet
import com.tokopedia.notifcenter.presentation.adapter.MultipleProductCardAdapter
import com.tokopedia.notifcenter.presentation.adapter.typefactory.product.MultipleProductCardFactoryImpl
import kotlinx.android.synthetic.main.dialog_notification_list_product.*

class ProductCardListDialog(
        private val element: NotificationItemViewBean,
        listener: NotificationItemListener
): BaseBottomSheet() {

    private val productCardFactory = MultipleProductCardFactoryImpl(
            sourceView = SourceMultipleProductView.BottomSheetDetail,
            listener = listener
    )

    private val productCardAdapter by lazy {
        MultipleProductCardAdapter(productCardFactory)
    }

    override fun contentView(): Int {
        return R.layout.dialog_notification_list_product
    }

    override fun renderView() {
        // dialog properties
        isFullpage = true

        // content
        txtTitle?.text = element.title
        txtDescription?.text = element.body

        lstProduct?.adapter = productCardAdapter
        productCardAdapter.insertData(
                MultipleProductCardMapper.map(element)
        )
    }

}