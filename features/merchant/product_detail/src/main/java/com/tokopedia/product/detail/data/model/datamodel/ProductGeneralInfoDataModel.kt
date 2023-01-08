package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductGeneralInfoDataModel(
    val name: String = "",
    val type: String = "",
    var applink: String = "",
    var title: String = "",
    var isApplink: Boolean = false,
    val parentIcon: String = "",
    var subtitle: String = "",
    val lightIcon: String = "",
    val darkIcon: String = "",
    var isPlaceholder: Boolean = false,

    var additionalIcon: String = "",
    var additionalDesc: String = "",
    var separator: String = "",
) : DynamicPdpDataModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductGeneralInfoDataModel) {
            applink == newData.applink &&
                title == newData.title &&
                isApplink == newData.isApplink &&
                parentIcon == newData.parentIcon &&
                subtitle == newData.subtitle &&
                additionalIcon == newData.additionalIcon &&
                additionalDesc == newData.additionalDesc &&
                separator == newData.separator
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        return null
    }

    /**
     * get icon url from 3 field (dark, light or default)
     * icon field is old version
     */
    fun getIconUrl(isDarkModel: Boolean): String {
        val iconUrl = if (isDarkModel) {
            darkIcon
        } else {
            lightIcon
        }

        return iconUrl.ifBlank { parentIcon }
    }

    val shouldRenderContent
        get() = !isPlaceholder && (subtitle.isNotBlank() || title.isNotBlank())

    val shouldTopSeparatorShowing
        get() = separator == ProductCustomInfoDataModel.SEPARATOR_BOTH || separator == ProductCustomInfoDataModel.SEPARATOR_TOP

    val shouldBottomSeparatorShowing
        get() = separator == ProductCustomInfoDataModel.SEPARATOR_BOTH || separator == ProductCustomInfoDataModel.SEPARATOR_BOTTOM
}
