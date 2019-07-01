package com.tokopedia.home.beranda.presentation.view.adapter.viewholder

import android.widget.ImageView
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.GeolocationPromptViewModel
import com.tokopedia.unifyprinciples.Typography
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

class GeolocationPromptViewHolder(itemView :View, val homeCategoryListener: HomeCategoryListener) :
        AbstractViewHolder<GeolocationPromptViewModel>(itemView) {

    val remoteConfig = FirebaseRemoteConfigImpl(itemView.context)

    companion object {
        val LAYOUT = R.layout.layout_prompt_to_enable_geolocation
    }
    override fun bind(element: GeolocationPromptViewModel?) {
        val btnClose = itemView.findViewById<ImageView>(R.id.img_close)
        val tvTitle = itemView.findViewById<Typography>(R.id.typography_title)
        val tvDesc = itemView.findViewById<Typography>(R.id.typography_description)
        val tvPrompt = itemView.findViewById<Typography>(R.id.typography_prompt)

//        val titleValue = remoteConfig.getString(
//                RemoteConfigKey.HOME_GEOLOCATION_COMPONENT_TITLE,
//                itemView.context.resources.getString(R.string.discovery_home_title_geolocation_title)
//        )
//        val descValue = remoteConfig.getString(
//                RemoteConfigKey.HOME_GEOLOCATION_COMPONENT_DESCRIPTION,
//                itemView.context.resources.getString(R.string.discovery_home_title_geolocation_description)
//        )
//        val promptValue = remoteConfig.getString(
//                RemoteConfigKey.HOME_GEOLOCATION_COMPONENT_PROMPT,
//                itemView.context.resources.getString(R.string.discovery_home_title_geolocation_prompt)
//        )
//        tvTitle.text = titleValue
//        tvDesc.text = descValue
//        tvPrompt.text = promptValue

        tvPrompt.setOnClickListener {
            HomePageTracking.eventClickGeolocationComponent(itemView.context)
            homeCategoryListener.launchPermissionChecker()
        }

        btnClose.setOnClickListener {
            HomePageTracking.eventClickCloseGeolocationComponent(itemView.context)
            homeCategoryListener.onCloseGeolocationView()
        }
    }
}