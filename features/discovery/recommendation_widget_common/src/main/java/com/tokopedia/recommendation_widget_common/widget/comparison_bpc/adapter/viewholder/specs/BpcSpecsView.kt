package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.viewholder.specs

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.recommendation_widget_common.databinding.ViewSpecsBinding

/**
 * Created by Frenzel
 */
class BpcSpecsView: FrameLayout  {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        binding = ViewSpecsBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var binding: ViewSpecsBinding? = null

    fun setSpecsInfo(bpcSpecsListModel: BpcSpecsListModel) {
        binding?.run {
            rvSpecs.layoutManager = LinearLayoutManager(context)
            rvSpecs.adapter = BpcSpecsAdapter(bpcSpecsListModel)
            rvSpecs.suppressLayout(true)
        }
    }
}
