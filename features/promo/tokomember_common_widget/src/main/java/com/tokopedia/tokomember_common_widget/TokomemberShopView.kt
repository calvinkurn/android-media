package com.tokopedia.tokomember_common_widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.unifyprinciples.Typography
import android.widget.FrameLayout
import com.tokopedia.tokomember_common_widget.model.TokomemberShopCardModel
import com.tokopedia.tokomember_common_widget.util.MemberType
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify

class TokomemberShopView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    companion object {
        const val REQUEST_CODE = 121
        const val RESULT_CODE_OK = 1
    }

    lateinit var cvShop: CardUnify
    lateinit var ivShopIcon: ImageUnify
    lateinit var tvShopName: Typography
    lateinit var tvShopMemberName: Typography
    lateinit var tvShopStartDateValue: Typography
    lateinit var tvShopStartDateLabel: Typography
    lateinit var tvShopEndDateLabel: Typography
    lateinit var tvShopEndDateValue: Typography
    lateinit var containerBadge: FrameLayout
    lateinit var tvShopType: Typography


    @MemberType
    var shopType: Int = MemberType.PREMIUM

    init {
        View.inflate(context, R.layout.tm_shop_view, this)
        initViews()
        setClicks()
    }

    private fun initViews() {
        cvShop = this.findViewById(R.id.cvShop)
        ivShopIcon = this.findViewById(R.id.ivShopIcon)
        tvShopName = this.findViewById(R.id.tvShopName)
        tvShopMemberName = this.findViewById(R.id.tvShopMemberName)
        tvShopStartDateLabel = this.findViewById(R.id.tvShopStartDateLabel)
        tvShopStartDateValue = this.findViewById(R.id.tvShopStartDateValue)
        tvShopEndDateLabel = this.findViewById(R.id.tvShopEndDateLabel)
        tvShopEndDateValue = this.findViewById(R.id.tvShopEndDateValue)
        containerBadge = this.findViewById(R.id.containerBadge)
        tvShopType = this.findViewById(R.id.tvShopType)
    }

    private fun setClicks() {

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    fun setData(

    ) {

    }

    private fun setShopCardData(tokomemberShopViewModel: TokomemberShopCardModel) {
        tvShopName.text = tokomemberShopViewModel.shopName
        tvShopMemberName.text = tokomemberShopViewModel.shopMemberName
        tvShopStartDateValue.text = tokomemberShopViewModel.shopName
        tvShopEndDateValue.text = tokomemberShopViewModel.shopName
        shopType = tokomemberShopViewModel.shopType
    }

    fun sendImpressionTrackerForPdp() {

    }
}