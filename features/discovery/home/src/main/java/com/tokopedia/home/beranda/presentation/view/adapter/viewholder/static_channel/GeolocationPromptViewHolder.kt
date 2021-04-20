package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.GeoLocationPromptDataModel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.stickylogin.common.utils.StripedUnderlineUtil
import com.tokopedia.unifyprinciples.Typography

class GeolocationPromptViewHolder(itemView :View, val homeCategoryListener: HomeCategoryListener?) :
        AbstractViewHolder<GeoLocationPromptDataModel>(itemView) {

    private val remoteConfig = FirebaseRemoteConfigImpl(itemView.context)
    private val btnClose = itemView.findViewById<ImageView>(R.id.img_close)
    private val tvTitle = itemView.findViewById<Typography>(R.id.typography_title)
    private val tvDesc = itemView.findViewById<Typography>(R.id.typography_description)

    companion object {
        val LAYOUT = R.layout.layout_prompt_to_enable_geolocation
    }
    override fun bind(element: GeoLocationPromptDataModel?) {
        val titleValue = remoteConfig.getString(
                RemoteConfigKey.HOME_GEOLOCATION_COMPONENT_TITLE,
                itemView.context.resources.getString(R.string.discovery_home_title_geolocation_title_default)
        )
        if (titleValue.isEmpty()) {
            val defaultTitle = itemView.context.resources.getText(R.string.discovery_home_title_geolocation_title_default)
            tvTitle.text = defaultTitle
        } else {
            tvTitle.text = MethodChecker.fromHtml(titleValue)
        }

        val descValue = remoteConfig.getString(
                RemoteConfigKey.HOME_GEOLOCATION_COMPONENT_DESCRIPTION,
                itemView.context.resources.getString(R.string.discovery_home_title_geolocation_description_default)
        )
        if (descValue.isEmpty()) {
            val defaultDesc = itemView.context.resources.getText(R.string.discovery_home_title_geolocation_description_default)
            tvDesc.text = defaultDesc
        } else {
            tvDesc.text = MethodChecker.fromHtml(descValue)
        }

        StripedUnderlineUtil.stripUnderlines(tvTitle)
        StripedUnderlineUtil.stripUnderlines(tvDesc)

        tvDesc.setOnClickListener {
            HomePageTracking.eventClickGeolocationComponent()
            homeCategoryListener?.launchPermissionChecker()
        }

        btnClose.setOnClickListener {
            HomePageTracking.eventClickCloseGeolocationComponent()
            homeCategoryListener?.onCloseGeolocationView()
        }
    }
}