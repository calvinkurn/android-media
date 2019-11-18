package com.tokopedia.v2.home.ui.adapter.delegate.staticwidgets

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.utils.StripedUnderlineUtil
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.v2.home.base.adapterdelegate.ModelViewType
import com.tokopedia.v2.home.base.adapterdelegate.ViewTypeDelegateAdapter
import com.tokopedia.v2.home.base.adapterdelegate.inflate
import com.tokopedia.v2.home.model.vo.GeoLocationDataModel

class GeolocationDelegateAdapter : ViewTypeDelegateAdapter {
    override fun isForViewType(item: ModelViewType): Boolean {
        return item is GeoLocationDataModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return GeolocationViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ModelViewType) {
        holder as GeolocationViewHolder
        holder.bind(item as GeoLocationDataModel)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ModelViewType, payload: List<Any>) {
        /* do nothing */
    }

    inner class GeolocationViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(parent.inflate(R.layout.layout_prompt_to_enable_geolocation)){
        val remoteConfig = FirebaseRemoteConfigImpl(itemView.context)
        fun bind(item: GeoLocationDataModel){
            val btnClose = itemView.findViewById<ImageView>(R.id.img_close)
            val tvTitle = itemView.findViewById<Typography>(R.id.typography_title)
            val tvDesc = itemView.findViewById<Typography>(R.id.typography_description)

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
                HomePageTracking.eventClickGeolocationComponent(itemView.context)

            }

            btnClose.setOnClickListener {
                HomePageTracking.eventClickCloseGeolocationComponent(itemView.context)

            }
        }
    }
}