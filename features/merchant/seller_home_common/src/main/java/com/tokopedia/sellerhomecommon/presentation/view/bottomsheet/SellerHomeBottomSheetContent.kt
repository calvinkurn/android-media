package com.tokopedia.sellerhomecommon.presentation.view.bottomsheet

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.sellerhomecommon.databinding.ShcBottomSheetContentBinding
import com.tokopedia.sellerhomecommon.presentation.adapter.BottomSheetAdapterTypeFactory
import com.tokopedia.sellerhomecommon.presentation.model.BaseBottomSheetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BottomSheetContentUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BottomSheetListItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TooltipUiModel

/**
 * Created By @ilhamsuaib on 27/05/20
 */

class SellerHomeBottomSheetContent : LinearLayout {

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private var binding: ShcBottomSheetContentBinding? = null
    private var adapter: BaseListAdapter<BaseBottomSheetUiModel, BottomSheetAdapterTypeFactory>? =
        null

    private fun initView(context: Context) {
        binding = ShcBottomSheetContentBinding.inflate(
            LayoutInflater.from(context), this, true
        )

        if (null == adapter)
            adapter = BaseListAdapter(BottomSheetAdapterTypeFactory())

        binding?.rvBottomSheetContent?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@SellerHomeBottomSheetContent.adapter
        }
    }

    fun setTooltipData(tooltip: TooltipUiModel) {
        with(tooltip) {

            adapter?.data?.clear()

            if (content.isNotEmpty()) {
                adapter?.data?.add(BottomSheetContentUiModel(content))
            }

            if (list.isNotEmpty()) {
                adapter?.data?.addAll(list.mapIndexed { index, tooltipListItemUiModel ->
                    BottomSheetListItemUiModel(
                        tooltipListItemUiModel.title,
                        tooltipListItemUiModel.description,
                        index + 1 == list.size
                    )
                })
            }
        }

        binding?.rvBottomSheetContent?.post {
            adapter?.notifyDataSetChanged()
        }
    }
}