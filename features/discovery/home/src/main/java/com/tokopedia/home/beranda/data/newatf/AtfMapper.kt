package com.tokopedia.home.beranda.data.newatf

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.datasource.local.entity.AtfCacheEntity
import com.tokopedia.home.beranda.data.newatf.banner.HomepageBannerMapper.asVisitable
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon
import com.tokopedia.home.beranda.domain.model.banner.BannerDataModel
import com.tokopedia.home.constant.AtfKey

object AtfMapper {
    fun mapToDomainAtfData(
        position: Int,
        data: com.tokopedia.home.beranda.data.model.AtfData
    ): AtfData {
        return AtfData(
            atfMetadata = AtfMetadata(
                id = data.id,
                position = position,
                name = data.name,
                component = data.component,
                param = data.param,
                isOptional = data.isOptional,
            ),
            isCache = false,
        )
    }

    fun mapRemoteToCache(
        position: Int,
        data: com.tokopedia.home.beranda.data.model.AtfData
    ): AtfCacheEntity {
        return AtfCacheEntity(
            id = data.id,
            position = position,
            name = data.name,
            component = data.component,
            param = data.param,
            isOptional = data.isOptional,
            status = AtfKey.STATUS_LOADING,
        )
    }

    fun mapToDomainAtfData(
        data: AtfCacheEntity,
    ): AtfData {
        return AtfData(
             atfMetadata = AtfMetadata(
                 id = data.id,
                 position = data.position,
                 name = data.name,
                 component = data.component,
                 param = data.param,
                 isOptional = data.isOptional,
            ),
            atfContent = data.getAtfContent(),
            isCache = true,
        )
    }

    private fun AtfCacheEntity.getAtfContent(): AtfContent? {
        return when(this.component) {
            AtfKey.TYPE_BANNER -> content?.getAtfContent<BannerDataModel>()
            AtfKey.TYPE_ICON -> content?.getAtfContent<DynamicHomeIcon>()
            else -> null
        }
    }

    private inline fun <reified T> String.getAtfContent(): T? {
        val gson = Gson()
        return gson.fromJson(this, T::class.java)
    }

    fun AtfDataList.mapToVisitableList(): List<Visitable<*>> {
        val visitables = mutableListOf<Visitable<*>>()
        listAtfData.forEachIndexed { index, value ->
            value.atfContent.run {
                when(this) {
                    is BannerDataModel -> visitables.add(this.asVisitable(index, value.isCache))
                }
            }
        }
        return visitables
    }
}
