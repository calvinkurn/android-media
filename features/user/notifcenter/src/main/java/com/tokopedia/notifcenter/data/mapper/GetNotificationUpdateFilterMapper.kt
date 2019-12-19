package com.tokopedia.notifcenter.data.mapper

import com.tokopedia.notifcenter.domain.model.NotificationFilterSection
import com.tokopedia.notifcenter.domain.pojo.NotificationUpdateFilter
import com.tokopedia.notifcenter.presentation.view.viewmodel.NotificationUpdateFilterViewBean
import com.tokopedia.notifcenter.presentation.view.viewmodel.NotificationUpdateFilterViewBean.FilterType
import com.tokopedia.notifcenter.presentation.view.viewmodel.NotificationUpdateFilterSectionViewBean
import javax.inject.Inject

/**
 * @author : Steven 11/04/19
 */
class GetNotificationUpdateFilterMapper @Inject constructor(){
    open fun map(pojo: NotificationUpdateFilter) : ArrayList<NotificationUpdateFilterViewBean> {
        var item = pojo.pojo
        var list = arrayListOf<NotificationUpdateFilterViewBean>()

        var typeList = arrayListOf<NotificationUpdateFilterSectionViewBean>()
        for (notifFilterType in item.typeList.list) {
            typeList.add(NotificationUpdateFilterSectionViewBean(notifFilterType.name, notifFilterType.id))
        }
        val typeItem = NotificationUpdateFilterViewBean(FilterType.TYPE_ID.type,"Kategori", typeList)
        list.add(typeItem)

        var tagList = arrayListOf<NotificationUpdateFilterSectionViewBean>()
        for (notifTagType in item.tagList.list) {
            tagList.add(NotificationUpdateFilterSectionViewBean(notifTagType.tagName, notifTagType.tagId, notifTagType.tagKey))
        }
        val tagItem = NotificationUpdateFilterViewBean(FilterType.TAG_ID.type,"Notifikasi", tagList)
        list.add(tagItem)
        return list
    }

    open fun mapToFilter(pojo: NotificationUpdateFilter) : ArrayList<NotificationFilterSection> {
        var item = pojo.pojo
        var list = arrayListOf<NotificationFilterSection>()

        var typeList = arrayListOf<NotificationUpdateFilterSectionViewBean>()
        for (notifFilterType in item.typeList.list) {
            typeList.add(NotificationUpdateFilterSectionViewBean(notifFilterType.name, notifFilterType.id))
        }
        val typeItem = NotificationFilterSection(FilterType.TYPE_ID.type,"Kategori", typeList)
        list.add(typeItem)

        var tagList = arrayListOf<NotificationUpdateFilterSectionViewBean>()
        for (notifTagType in item.tagList.list) {
            tagList.add(NotificationUpdateFilterSectionViewBean(notifTagType.tagName, notifTagType.tagId, notifTagType.tagKey))
        }
        val tagItem = NotificationFilterSection(FilterType.TAG_ID.type,"Notifikasi", tagList)
        list.add(tagItem)
        return list
    }
}