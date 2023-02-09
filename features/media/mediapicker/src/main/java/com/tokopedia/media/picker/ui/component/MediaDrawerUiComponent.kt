package com.tokopedia.media.picker.ui.component

import android.view.ViewGroup
import com.tokopedia.media.R
import com.tokopedia.media.picker.analytics.gallery.GalleryAnalytics
import com.tokopedia.media.picker.ui.publisher.PickerEventBus
import com.tokopedia.media.picker.ui.widget.drawerselector.DrawerActionType
import com.tokopedia.media.picker.ui.widget.drawerselector.DrawerSelectionWidget
import com.tokopedia.picker.common.basecomponent.UiComponent
import com.tokopedia.picker.common.cache.PickerCacheManager
import com.tokopedia.picker.common.uimodel.MediaUiModel

class MediaDrawerUiComponent(
    parent: ViewGroup,
    private val param: PickerCacheManager,
    private val analytics: GalleryAnalytics,
    private val eventBus: PickerEventBus
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
            is DrawerActionType.Add -> eventBus.addMediaEvent(action.media)
            is DrawerActionType.Remove -> eventBus.removeMediaEvent(action.mediaToRemove)
            is DrawerActionType.Reorder -> eventBus.notifyDataOnChangedEvent(action.data)
        }
    }

    override fun onDrawerItemClicked(media: MediaUiModel) {
        analytics.clickGalleryThumbnail()
    }
}
