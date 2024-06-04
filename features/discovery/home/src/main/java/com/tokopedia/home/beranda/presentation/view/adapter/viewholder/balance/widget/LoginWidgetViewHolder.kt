package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.widget

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.databinding.LayoutDynamicBalanceLoginWidgetBinding
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.home.R as homeR

/**
 * Created by frenzel
 */
class LoginWidgetViewHolder (
    itemView: View,
    private val listener: HomeCategoryListener
) : AbstractViewHolder<LoginWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = homeR.layout.layout_dynamic_balance_login_widget
    }

    private var binding: LayoutDynamicBalanceLoginWidgetBinding? by viewBinding()

    override fun bind(element: LoginWidgetUiModel) {
        binding?.loginWidget?.bind(listener)
    }
}
