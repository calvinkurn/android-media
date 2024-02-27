package com.tokopedia.homenav.mainnav.view.adapter.viewholder.buyagain

import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.mainnav.view.datamodel.buyagain.BuyAgainUiModel

class BuyAgainViewHolder(view: View) : AbstractViewHolder<BuyAgainUiModel>(view) {

    override fun bind(element: BuyAgainUiModel?) {
        if (element == null) return

        Toast.makeText(itemView.context, "hi!!", Toast.LENGTH_SHORT).show()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_transaction_buy_again
    }
}
