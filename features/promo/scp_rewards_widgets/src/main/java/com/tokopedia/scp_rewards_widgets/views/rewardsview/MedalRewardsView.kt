package com.tokopedia.scp_rewards_widgets.views.rewardsview

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.scp_rewards_widgets.constants.CouponStatus
import com.tokopedia.scp_rewards_widgets.model.MedalRewardsModel
import com.tokopedia.scp_rewards_widgets.model.RewardsErrorModel

class MedalRewardsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var rewardsRv: RecyclerView? = null
    private val rewardsAdapter: BaseAdapter<RewardsViewTypeFactory> by lazy {
        BaseAdapter(RewardsViewTypeFactory())
    }

    init {
        rewardsRv = RecyclerView(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
        }
        addView(rewardsRv)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setupRecyclerView()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (heightMode == MeasureSpec.UNSPECIFIED) {
            rewardsRv?.isNestedScrollingEnabled = false
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


    private fun setupRecyclerView() {
        rewardsRv?.apply {
            adapter = rewardsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    fun renderCoupons(data: List<MedalRewardsModel>, onErrorAction: () -> Unit) {
        if (isErrorCase(data)) {
            setErrorState(
                RewardsErrorModel(
                    imageUrl = data.last().imageUrl,
                    status = data.last().status,
                    statusDescription = data.last().statusDescription,
                    isActive = data.last().isActive
                )
            )
            onErrorAction()
        } else {
            rewardsAdapter.setVisitables(data)
        }
    }

    private fun isErrorCase(data: List<MedalRewardsModel>): Boolean {
        return data.size == 1 &&
            (data.last().status == CouponStatus.ERROR || data.last().status == CouponStatus.HIDDEN)
    }

    private fun setErrorState(error: RewardsErrorModel) {
        rewardsAdapter.setVisitables(listOf(error))
    }

}
