package com.tokopedia.scp_rewards_widgets.views.rewardsview

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.scp_rewards_widgets.common.VerticalSpacing
import com.tokopedia.scp_rewards_widgets.model.MedalRewardsModel
import com.tokopedia.scp_rewards_widgets.model.RewardsErrorModel

class MedalRewardsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr:Int = 0
): FrameLayout(context, attrs, defStyleAttr) {

    companion object{
        private const val COUPON_IMG = "https://images.tokopedia.net/img/cache/576x192/uqilkZ/2023/5/16/ae716f14-f1c8-431f-bf84-03f9adf74075.png"
        private const val TEXT = "*Bisa dipakai hingga 17 Mei 2023"
        private const val error = "https://img.freepik.com/free-vector/glitch-error-404-page_23-2148105404.jpg"
        private const val ITEM_VERTICAL_SPACING = 16
    }
    private var rewardsRv: RecyclerView? = null
    private val rewardsAdapter:BaseAdapter<RewardsViewTypeFactory> by lazy {
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
        if(heightMode == MeasureSpec.UNSPECIFIED){
            rewardsRv?.isNestedScrollingEnabled = false
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


    private fun setupRecyclerView(){
        rewardsRv?.apply {
            adapter = rewardsAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(VerticalSpacing(ITEM_VERTICAL_SPACING))
        }
    }

    private fun setupDummyList(){
        val list = listOf(
            RewardsErrorModel(
                imageUrl = error
            ),
            MedalRewardsModel(
                isActive = true,
                status = "active",
                imageUrl = COUPON_IMG,
                statusDescription = TEXT
            ),
            MedalRewardsModel(
                isActive = false,
                status = "inactive",
                imageUrl = COUPON_IMG,
                statusDescription = TEXT
            ),
            MedalRewardsModel(
                isActive = false,
                status = "expired",
                imageUrl = COUPON_IMG,
                statusDescription = TEXT
            ),
            MedalRewardsModel(
                isActive = false,
                status = "used",
                imageUrl = COUPON_IMG,
                statusDescription = TEXT
            ),
            MedalRewardsModel()
        )
        rewardsAdapter.setVisitables(list)
    }

    fun setErrorState(error:RewardsErrorModel){
        rewardsAdapter.setVisitables(
            listOf(
                error
            )
        )
    }

    fun renderCoupons(data:List<MedalRewardsModel>){
        rewardsAdapter.setVisitables(data)
    }

}
