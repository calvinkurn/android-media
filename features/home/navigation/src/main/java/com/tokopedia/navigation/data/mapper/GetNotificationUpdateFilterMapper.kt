package com.tokopedia.navigation.data.mapper

import com.tokopedia.navigation.domain.pojo.NotificationUpdateFilter
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateFilterItemViewModel
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateFilterItemViewModel.FilterType
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateFilterSectionItemViewModel
import javax.inject.Inject

/**
 * @author : Steven 11/04/19
 */
class GetNotificationUpdateFilterMapper @Inject constructor(){
    open fun map(pojo: NotificationUpdateFilter) : ArrayList<NotificationUpdateFilterItemViewModel> {
        var item = pojo.pojo
        var list = arrayListOf<NotificationUpdateFilterItemViewModel>()

        var typeList = arrayListOf<NotificationUpdateFilterSectionItemViewModel>()
        for (notifFilterType in item.typeList.list) {
            typeList.add(NotificationUpdateFilterSectionItemViewModel(notifFilterType.name, notifFilterType.id))
        }
        val typeItem = NotificationUpdateFilterItemViewModel(FilterType.TYPE_ID.type,"Kategori", typeList)
        list.add(typeItem)

        var tagList = arrayListOf<NotificationUpdateFilterSectionItemViewModel>()
        for (notifTagType in item.tagList.list) {
            tagList.add(NotificationUpdateFilterSectionItemViewModel(notifTagType.tagName, notifTagType.tagId, notifTagType.tagKey))
        }
        val tagItem = NotificationUpdateFilterItemViewModel(FilterType.TAG_ID.type,"Notifikasi", tagList)
        list.add(tagItem)
        return list
    }

}