package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.ConvertResourceIdUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class ConvertResourceIdViewHolder(
    private val view: View
) : AbstractViewHolder<ConvertResourceIdUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_resource_id_to_resource_name

        const val PREFIX_RESOURCE_ID = "#0x"
        const val RADIX_DEFAULT = 16
    }

    override fun bind(element: ConvertResourceIdUiModel?) {
        val btnConvertResourceId = itemView.findViewById<UnifyButton>(R.id.convertResourceBtn)
        val tvInputResourceId = itemView.findViewById<TextFieldUnify>(R.id.tvInputResourceId)
        val tvResultResourceName = itemView.findViewById<Typography>(R.id.tvResultResourceName)

        btnConvertResourceId.setOnClickListener {
            val resourceName = convertResourceIdToResourceName(tvInputResourceId.textFieldInput.text.toString())
            if (resourceName.isNotBlank()) {
                tvResultResourceName.text = resourceName
                tvResultResourceName.show()
            } else {
                tvResultResourceName.hide()
            }
        }
    }

    private fun convertResourceIdToResourceName(resourceId: String): String {
        return try {
            val parseResourceId = if (resourceId.startsWith(PREFIX_RESOURCE_ID)) {
                val resourceIdSplit = resourceId.split(PREFIX_RESOURCE_ID).getOrNull(Int.ONE).orEmpty()
                Integer.parseInt(resourceIdSplit, RADIX_DEFAULT)
            } else {
                Integer.parseInt(resourceId, RADIX_DEFAULT)
            }
            view.resources.getResourceName(parseResourceId)
        } catch (e: Exception) {
            e.localizedMessage.orEmpty()
        }
    }
}