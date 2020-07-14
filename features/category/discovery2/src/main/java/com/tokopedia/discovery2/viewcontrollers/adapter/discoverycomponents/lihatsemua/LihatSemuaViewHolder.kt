package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatsemua

import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment

class LihatSemuaViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private lateinit var lihatSemuaViewModel: LihatSemuaViewModel
    private var lihatTextView: TextView = itemView.findViewById(R.id.lihat_semua_tv)
    private var lihatTitleTextView: TextView = itemView.findViewById(R.id.title_tv)


    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        lihatSemuaViewModel = discoveryBaseViewModel as LihatSemuaViewModel

        lihatSemuaViewModel.getComponentData().observe(fragment.viewLifecycleOwner, Observer { componentItem ->
            componentItem.data?.firstOrNull()?.let { data ->
                lihatTitleTextView.text = data.title
                lihatTextView.text = data.buttonText
                lihatTextView.setOnClickListener {
                    onClick(data)
                }
            }
        })
    }

    private fun onClick(data: DataItem) {
        lihatSemuaViewModel.navigate(fragment.activity, data.btnApplink)
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackLihatSemuaClick(data.name)
    }

}