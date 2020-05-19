package com.tokopedia.notifcenter.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.mapper.MultipleProductCardMapper
import com.tokopedia.notifcenter.data.state.SourceMultipleProductView
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.presentation.adapter.MultipleProductCardAdapter
import com.tokopedia.notifcenter.presentation.adapter.typefactory.product.MultipleProductCardFactoryImpl
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.dialog_notification_list_product.*

class ProductCardListDialog(
        private val element: NotificationItemViewBean,
        listener: NotificationItemListener
): BottomSheetUnify() {

    private val productCardFactory = MultipleProductCardFactoryImpl(
            sourceView = SourceMultipleProductView.BottomSheetDetail,
            listener = listener
    )

    private val productCardAdapter by lazy {
        MultipleProductCardAdapter(productCardFactory)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val contentView = View.inflate(
                requireContext(),
                R.layout.dialog_notification_list_product,
                null
        )
        setChild(contentView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFullpage = true
        renderView()
    }

    private fun renderView() {
        txtTitle?.text = element.title
        txtDescription?.text = element.body

        //product list item
        lstProduct?.adapter = productCardAdapter
        productCardAdapter.insertData(
                MultipleProductCardMapper.map(element)
        )
    }

}