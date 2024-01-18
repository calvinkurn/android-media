package com.tokopedia.shareexperience.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shareexperience.ui.adapter.diffutil.ShareExBottomSheetItemCallback
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactory
import timber.log.Timber

class ShareExBottomSheetAdapter(
    typeFactory: ShareExTypeFactory
) : BaseListAdapter<Visitable<in ShareExTypeFactory>, ShareExTypeFactory>(typeFactory) {

    fun updateItems(newList: List<Visitable<in ShareExTypeFactory>>) {
        try {
            val diffResult = DiffUtil.calculateDiff(
                ShareExBottomSheetItemCallback(visitables, newList)
            )
            visitables = newList
            diffResult.dispatchUpdatesTo(this)
        } catch (throwable: Throwable) {
            Timber.e(throwable)
        }
    }
}
