package com.tokopedia.topads.common.view.adapter.etalase.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.view.adapter.etalase.viewmodel.EtalaseShimerViewModel

/**
 * Author errysuprayogi on 11,November,2019
 */

class EtalaseShimerViewHolder(view: View): EtalaseViewHolder<EtalaseShimerViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_edit_select_shimering_adapter_etalase_list_item
    }

}