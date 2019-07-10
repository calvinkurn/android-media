package com.tokopedia.product.detail.view.fragment.partialview

import android.view.View
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import kotlinx.android.synthetic.main.partial_value_proposition_os.view.*

class PartialValuePropositionView(private val view: View, private val clickListener: View.OnClickListener) :
        View.OnClickListener by clickListener {

    var hideBackgroundResource: (() -> Unit)? = null

    companion object {
        fun build(_view: View, _listener: View.OnClickListener) = PartialValuePropositionView(_view, _listener)
    }

    fun renderData(goldOs: ShopInfo.GoldOS) {
        with(view) {
            if (goldOs.isOfficial == 1) {
                visible()
                hideBackgroundResource?.invoke()
            } else {
                gone()
            }
            view.container_ori.setOnClickListener(this@PartialValuePropositionView)
            view.container_guarantee.setOnClickListener(this@PartialValuePropositionView)
            view.container_ready.setOnClickListener(this@PartialValuePropositionView)
        }
    }

}