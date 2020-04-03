package com.tokopedia.notifcenter.presentation.fragment

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.mapper.MultipleProductCardMapper
import com.tokopedia.notifcenter.data.state.SourceMultipleProductView
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.presentation.BaseBottomSheetDialog
import com.tokopedia.notifcenter.presentation.adapter.MultipleProductCardAdapter
import com.tokopedia.notifcenter.presentation.adapter.typefactory.product.MultipleProductCardFactoryImpl
import com.tokopedia.unifyprinciples.Typography

class NotificationProductCardDialog(
        context: Context,
        fragmentManager: FragmentManager,
        val listener: NotificationItemListener
): BaseBottomSheetDialog<NotificationItemViewBean>(context, fragmentManager) {

    private val txtTitle = container?.findViewById<Typography>(R.id.txt_title)
    private val txtDescription = container?.findViewById<Typography>(R.id.txt_description)
    private val lstProducts = container?.findViewById<RecyclerView>(R.id.lst_products)

    private val productCardAdapter by lazy {
        val factory = MultipleProductCardFactoryImpl(
                sourceView = SourceMultipleProductView.BottomSheetDetail,
                listener = listener
        )
        MultipleProductCardAdapter(factory)
    }

    override fun resourceId(): Int {
        return R.layout.dialog_notification_list_product
    }

    override fun show(element: NotificationItemViewBean) {
        setFullPage(true)

        txtTitle?.text = element.title
        txtDescription?.text = element.body

        //product list item
        lstProducts?.adapter = productCardAdapter
        productCardAdapter.insertData(
                MultipleProductCardMapper.map(element)
        )
    }

}