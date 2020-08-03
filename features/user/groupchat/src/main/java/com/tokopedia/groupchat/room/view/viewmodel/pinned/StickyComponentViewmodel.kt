package com.tokopedia.groupchat.room.view.viewmodel.pinned

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import kotlinx.android.parcel.Parcelize

/**
 * @author by yfsx on 14/02/19.
 */
@Parcelize
data class StickyComponentViewModel constructor(val componentId : String = "",
                                     val componentType: String = "",
                                     val imageUrl: String = "",
                                     val title: String = "",
                                     val subtitle: String = "",
                                     val redirectUrl : String = "",
                                     val stickyTime : Int = 0,
                                     val relatedButton : Int = 0,
                                     var attributeData : String = "") : Visitable<Any>, Parcelable {

    companion object {
        const val TYPE_PRODUCT = "product"
    }

    override fun type(typeFactory: Any?): Int {
        return 0
    }
}