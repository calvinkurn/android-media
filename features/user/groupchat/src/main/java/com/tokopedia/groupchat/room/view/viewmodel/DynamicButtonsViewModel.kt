package com.tokopedia.groupchat.room.view.viewmodel

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import kotlinx.android.parcel.Parcelize

/**
 * @author by nisie on 22/02/19.
 */
@Parcelize
class DynamicButtonsViewModel constructor(var floatingButton: DynamicButton = DynamicButton(),
                                          var listDynamicButton: ArrayList<DynamicButton> = ArrayList(),
                                          var interactiveButton: InteractiveButton = InteractiveButton())
    : Visitable<Any>, Parcelable {

    companion object {

        const val TYPE = "dynamic_button"
        const val TYPE_REDIRECT_EXTERNAL = "external"
        const val TYPE_OVERLAY_CTA = "overlay_cta"
        const val TYPE_OVERLAY_WEBVIEW = "overlay_webview"

    }

    override fun type(typeFactory: Any): Int {
        return 0
    }

}
