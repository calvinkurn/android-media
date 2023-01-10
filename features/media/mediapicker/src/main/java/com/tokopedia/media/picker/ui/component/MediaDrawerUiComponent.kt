package com.tokopedia.media.picker.ui.component

import android.view.ViewGroup
import com.tokopedia.media.R
import com.tokopedia.media.common.utils.ParamCacheManager
import com.tokopedia.media.picker.analytics.gallery.GalleryAnalytics
import com.tokopedia.media.picker.ui.observer.stateOnAddPublished
import com.tokopedia.media.picker.ui.observer.stateOnChangePublished
import com.tokopedia.media.picker.ui.observer.stateOnRemovePublished
import com.tokopedia.media.picker.ui.widget.drawerselector.DrawerActionType
import com.tokopedia.media.picker.ui.widget.drawerselector.DrawerSelectionWidget
import com.tokopedia.picker.common.basecomponent.UiComponent
import com.tokopedia.picker.common.uimodel.MediaUiModel

class MediaDrawerUiComponent(
    parent: ViewGroup,
    private val param: ParamCacheManager,
    private val analytics: GalleryAnalytics
) : UiComponent(parent, R.id.drawer_selector), DrawerSelectionWidget.Listener {

    private val drawer = container() as DrawerSelectionWidget

    fun setListener() {
        drawer.setListener(this)
    }

    fun removeListener() {
        drawer.removeListener()
    }

    override fun onDrawerDataSetChanged(action: DrawerActionType) {
        if (!param.get().isMultipleSelectionType()) return

        when (action) {
            is DrawerActionType.Add -> stateOnAddPublished(action.media)
            is DrawerActionType.Remove -> stateOnRemovePublished(action.mediaToRemove)
            is DrawerActionType.Reorder -> stateOnChangePublished(action.data)
        }
    }

    override fun onDrawerItemClicked(media: MediaUiModel) {
        analytics.clickGalleryThumbnail()
    }
}
