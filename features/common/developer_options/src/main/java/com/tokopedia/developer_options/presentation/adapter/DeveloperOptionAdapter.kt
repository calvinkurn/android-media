package com.tokopedia.developer_options.presentation.adapter

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.adapter.typefactory.DeveloperOptionTypeFactoryImpl
import com.tokopedia.developer_options.presentation.model.AccessTokenUiModel
import com.tokopedia.developer_options.presentation.model.OptionItemUiModel
import com.tokopedia.developer_options.presentation.model.PdpDevUiModel
import com.tokopedia.developer_options.presentation.model.SystemNonSystemAppsUiModel
import java.util.*

class DeveloperOptionAdapter(
    typeFactory: DeveloperOptionTypeFactoryImpl,
    differ: DeveloperOptionDiffer,
    context: Context
) : BaseDeveloperOptionAdapter<Visitable<*>, DeveloperOptionTypeFactoryImpl>(typeFactory, differ) {

    private val defaultItems = listOf(
        PdpDevUiModel(context.getString(com.tokopedia.developer_options.R.string.pdp_dev)),
        AccessTokenUiModel(context.getString(com.tokopedia.developer_options.R.string.access_token)),
        SystemNonSystemAppsUiModel(context.getString(com.tokopedia.developer_options.R.string.system_apps_non_system_apps))
    )

    fun searchItem(text: String) {
        val newItems = mutableListOf<OptionItemUiModel>()
        defaultItems.forEach {
            if (it.text.lowercase().contains(text.lowercase())) {
                newItems.add(it)
            }
        }
        submitList(newItems)
    }

    fun setDefaultItem() {
        submitList(defaultItems)
    }
}