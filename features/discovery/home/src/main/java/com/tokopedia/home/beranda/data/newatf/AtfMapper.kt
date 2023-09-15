package com.tokopedia.home.beranda.data.newatf

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.datasource.local.entity.AtfCacheEntity
import com.tokopedia.home.beranda.data.newatf.banner.HomepageBannerMapper.asVisitable
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon
import com.tokopedia.home.beranda.domain.model.banner.BannerDataModel
import com.tokopedia.home.constant.AtfKey

object AtfMapper {
    fun com.tokopedia.home.beranda.data.model.AtfData.mapRemoteAtfData(): AtfData {
        return AtfData(
            atfMetadata = AtfMetadata(
                id = id,
                name = name,
                component = component,
                param = param,
                isOptional = isOptional,
            ),
            isCache = false,
        )
    }

    fun AtfCacheEntity.mapCachedAtfData(): AtfData {
        return AtfData(
             atfMetadata = AtfMetadata(
                id = id,
                name = name,
                component = component,
                param = param,
                isOptional = isOptional,
            ),
            atfContent = this.getAtfContent(),
            isCache = true,
        )
    }

    fun AtfCacheEntity.getAtfContent(): AtfContent? {
        return when(this.component) {
            AtfKey.TYPE_BANNER -> content?.getAtfContent<BannerDataModel>()
            AtfKey.TYPE_ICON -> content?.getAtfContent<DynamicHomeIcon>()
            else -> null
        }
    }

    inline fun <reified T> String.getAtfContent(): T? {
        val gson = Gson()
        return gson.fromJson(this, T::class.java)
    }

    fun AtfDataList.mapToVisitableList(): List<Visitable<*>> {
        val visitables = mutableListOf<Visitable<*>>()
        listAtfData.forEachIndexed { index, value ->
            value.atfContent.run {
                when(this) {
                    is BannerDataModel -> this.asVisitable(index, value.isCache)
                }
            }
        }
        return visitables
    }
}
