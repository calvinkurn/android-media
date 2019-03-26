package com.tokopedia.kol.feature.post.view.adapter.viewholder

import android.view.View
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.post.view.listener.KolPostListener
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel

/**
 * @author by nisie on 26/03/19.
 */
class KolPostDetailViewHolder(val kolView: View,
                              val viewListener: KolPostListener.View.ViewHolder,
                              val type: Type) :
        KolPostViewHolder(kolView, viewListener, type) {


    override fun bind(element: KolPostViewModel?) {
        super.bind(element)
        val footer = kolView.findViewById<View>(R.id.footer)
        footer.visibility = View.GONE
    }

}