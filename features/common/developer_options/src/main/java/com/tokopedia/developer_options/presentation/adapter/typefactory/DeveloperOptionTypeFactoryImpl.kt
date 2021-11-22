package com.tokopedia.developer_options.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.presentation.model.AccessTokenUiModel
import com.tokopedia.developer_options.presentation.model.PdpDevUiModel
import com.tokopedia.developer_options.presentation.model.SystemNonSystemAppsUiModel
import com.tokopedia.developer_options.presentation.viewholder.AccessTokenViewHolder
import com.tokopedia.developer_options.presentation.viewholder.PdpDevViewHolder
import com.tokopedia.developer_options.presentation.viewholder.SystemNonSystemAppsViewHolder

class DeveloperOptionTypeFactoryImpl(
    private val pdpDevListener: PdpDevViewHolder.PdpDevListener,
    private val accessTokenListener: AccessTokenViewHolder.AccessTokenListener,
    private val systemNonSystemAppsListener: SystemNonSystemAppsViewHolder.SystemNonSystemAppsListener
):  BaseAdapterTypeFactory(), DeveloperOptionTypeFactory {

    override fun type(uiModel: PdpDevUiModel): Int = PdpDevViewHolder.LAYOUT
    override fun type(uiModel: AccessTokenUiModel): Int = AccessTokenViewHolder.LAYOUT
    override fun type(uiModel: SystemNonSystemAppsUiModel): Int = SystemNonSystemAppsViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            PdpDevViewHolder.LAYOUT -> PdpDevViewHolder(view, pdpDevListener)
            AccessTokenViewHolder.LAYOUT -> AccessTokenViewHolder(view, accessTokenListener)
            SystemNonSystemAppsViewHolder.LAYOUT -> SystemNonSystemAppsViewHolder(view, systemNonSystemAppsListener)
            else -> super.createViewHolder(view, type)
        }
    }

}