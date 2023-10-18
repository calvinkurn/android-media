package com.tokopedia.sellerpersona.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.view.model.PersonaTypeLoadingUiModel

/**
 * Created by @ilhamsuaib on 09/02/23.
 */

class PersonaTypeLoadingViewHolder(
    itemView: View
) : AbstractViewHolder<PersonaTypeLoadingUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_persona_type_loading_state
    }

    override fun bind(element: PersonaTypeLoadingUiModel) {}
}