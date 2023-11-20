package com.tokopedia.scp_rewards_widgets.recommendation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.scp_rewards_widgets.common.GridSpacingItemDecoration
import com.tokopedia.scp_rewards_widgets.databinding.WidgetRecommendedMedalsBinding
import com.tokopedia.scp_rewards_widgets.medal.MedalCallbackListener
import com.tokopedia.scp_rewards_widgets.medal.MedalItem
import com.tokopedia.scp_rewards_widgets.medal.MedalViewTypeFactory
import com.tokopedia.scp_rewards_widgets.model.RecommendedMedalSectionModel
import com.tokopedia.unifycomponents.toPx

private const val VERTICAL_SPACING = 8
private const val HORIZONTAL_SPACING = 30

class WidgetRecommendedMedals(private val context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs), MedalCallbackListener {

    private var listener: MedalCallbackListener? = null

    private val binding = WidgetRecommendedMedalsBinding.inflate(LayoutInflater.from(context), this)

    private val medalsAdapter: BaseAdapter<MedalViewTypeFactory> by lazy {
        BaseAdapter(MedalViewTypeFactory(this))
    }

    init {
        binding.rvRecommendedMedals.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = medalsAdapter
            addItemDecoration(
                GridSpacingItemDecoration(
                    HORIZONTAL_SPACING.toPx(),
                    VERTICAL_SPACING.toPx(),
                    false
                )
            )
            isNestedScrollingEnabled = false
        }
    }

    fun bindData(sectionData: RecommendedMedalSectionModel) {
        binding.apply {
            tvRecommendedMedalsTitle.text = sectionData.title
            val medalList = sectionData.medalList
            if (medalList.isNullOrEmpty()) {
                this@WidgetRecommendedMedals.hide()
            } else {
                val placeHolderList = mutableListOf<MedalItem>().apply { addAll(medalList) }
                val remainder = medalList.size % 3
                if (remainder != 0) {
                    repeat(3 - remainder) {
                        placeHolderList.add(MedalItem(isPlaceHolder = true))
                    }
                }
                medalsAdapter.setVisitables(placeHolderList.toList())
            }
        }
    }

    fun attachMedalClickListener(listener: MedalCallbackListener) {
        this.listener = listener
    }

    override fun onMedalClick(medalItem: MedalItem) {
        listener?.onMedalClick(medalItem)
    }
}
