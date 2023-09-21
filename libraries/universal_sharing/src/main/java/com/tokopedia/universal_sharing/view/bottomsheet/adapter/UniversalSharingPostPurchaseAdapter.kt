package com.tokopedia.universal_sharing.view.bottomsheet.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.universal_sharing.view.bottomsheet.typefactory.UniversalSharingTypeFactory
import timber.log.Timber

class UniversalSharingPostPurchaseAdapter(
    typeFactory: UniversalSharingTypeFactory
) : BaseListAdapter<Visitable<in UniversalSharingTypeFactory>, UniversalSharingTypeFactory>(
    typeFactory
) {
    fun updateData(newList: List<Visitable<in UniversalSharingTypeFactory>>) {
        try {
            val diffResult = DiffUtil.calculateDiff(
                UniversalSharingPostPurchaseDiffUtilCallBack(
                    oldList = visitables,
                    newList = newList
                )
            )
            visitables = newList
            diffResult.dispatchUpdatesTo(this)
        } catch (throwable: Throwable) {
            Timber.e(throwable)
        }
    }
}
