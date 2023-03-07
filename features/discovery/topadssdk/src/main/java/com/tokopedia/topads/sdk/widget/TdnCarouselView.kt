package com.tokopedia.topads.sdk.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.TopAdsConstants.TdnBannerConstants.TYPE_VERTICAL_CAROUSEL
import com.tokopedia.topads.sdk.decoration.TdnCarouselItemDecoration
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.view.adapter.TdnCarouselAdapter
import com.tokopedia.unifycomponents.BaseCustomView

class TdnCarouselView : BaseCustomView {

    private var tdnCarouselAdapter: TdnCarouselAdapter? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.layout_widget_tdn_carousel_view, this)
    }

    fun setCarouselModel(
        topAdsImageViewModel: List<TopAdsImageViewModel>,
        onTdnBannerClicked: (applink: String) -> Unit,
        cornerRadius: Int,
        onLoadFailed: () -> Unit,
        onTdnBannerImpressed: () -> Unit
    ) {
        tdnCarouselAdapter = TdnCarouselAdapter(onTdnBannerClicked, cornerRadius, onLoadFailed, onTdnBannerImpressed)
        val tdnRv = findViewById<RecyclerView>(R.id.tdnCarouselRv)
        tdnRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        if (topAdsImageViewModel.firstOrNull()?.layoutType != TYPE_VERTICAL_CAROUSEL){
            val snapHelper: SnapHelper = PagerSnapHelper()
            tdnRv.onFlingListener = null
            snapHelper.attachToRecyclerView(tdnRv)
        }
        tdnRv.removeAllItemDecoration()
        tdnRv.addItemDecoration(TdnCarouselItemDecoration())
        tdnRv.adapter = tdnCarouselAdapter
        tdnCarouselAdapter?.setList(topAdsImageViewModel)
    }
}

private fun RecyclerView.removeAllItemDecoration() {
    if (this.itemDecorationCount > Int.ZERO)
        for (i in Int.ZERO until this.itemDecorationCount) {
            this.removeItemDecorationAt(i)
        }
}
