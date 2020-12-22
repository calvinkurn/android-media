package com.tokopedia.top_ads_headline.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.top_ads_headline.R
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.headline_preview_bottomsheet.*

class HeadlinePreviewBottomSheet : BottomSheetUnify() {
    private var headlineName: String = ""
    private var cpmModel: CpmModel? = null

    companion object {
        fun newInstance(headlineName: String, cpmModel: CpmModel?): HeadlinePreviewBottomSheet {
            return HeadlinePreviewBottomSheet().apply {
                this.headlineName = headlineName
                this.cpmModel = cpmModel
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = View.inflate(context, R.layout.headline_preview_bottomsheet, null)
        setChild(contentView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetTitle = getString(R.string.topads_headline_preview_bottomsheet_header, headlineName)
        setTitle(bottomSheetTitle)
        cpmModel?.let {
            topAdsBannerView.displayAds(it)
        }
    }
}