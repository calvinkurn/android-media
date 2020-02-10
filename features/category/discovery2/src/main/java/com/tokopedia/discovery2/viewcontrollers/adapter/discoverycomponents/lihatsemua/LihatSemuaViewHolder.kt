package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatsemua

import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class LihatSemuaViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView), View.OnClickListener {

    private lateinit var lihatSemuaViewModel: LihatSemuaViewModel
    private var lihatTextView: TextView
    private var lihatTitleTextView: TextView


    init {
        lihatTextView = itemView.findViewById(R.id.lihat_semua_tv)
        lihatTitleTextView = itemView.findViewById(R.id.title_tv)
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        lihatSemuaViewModel = discoveryBaseViewModel as LihatSemuaViewModel
        lihatTextView.setOnClickListener(this)

        lihatSemuaViewModel.getComponentData().observe(fragment.viewLifecycleOwner, Observer { componentItem ->
            lihatTitleTextView.text = componentItem.data?.get(0)?.title
            lihatTextView.text = componentItem.data?.get(0)?.buttonText
        })
    }

    override fun onClick(view: View?) {
        lihatSemuaViewModel.onButtonClicked()
    }
}