package com.tokopedia.exploreCategory.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.affiliate_toko.R
import com.tokopedia.exploreCategory.adapter.AffiliateAdapter
import com.tokopedia.exploreCategory.adapter.AffiliateAdapterFactory
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.AffiliateProductCardVHViewModel
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.AffiliateShareVHViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.affiliate_home_fragment_layout.*
import java.util.ArrayList

class AffiliatePromotionBottomSheet : BottomSheetUnify(){
    private var contentView: View? = null
    private val adapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory())

    companion object {

        fun newInstance(): AffiliatePromotionBottomSheet {
            return AffiliatePromotionBottomSheet().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun init() {
        showCloseIcon = true
        showKnob = false
        setTitle(getString(R.string.affiliate_where_to_promote))
        contentView = View.inflate(context,
                R.layout.affiliate_promotion_bottom_sheet, null)

        contentView?.findViewById<RecyclerView>(R.id.share_rv)?.let {
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter.setVisitables(ArrayList())
            it.layoutManager = layoutManager
            it.adapter = adapter
        }

        adapter.addShareOptions()
        setChild(contentView)
    }

}
