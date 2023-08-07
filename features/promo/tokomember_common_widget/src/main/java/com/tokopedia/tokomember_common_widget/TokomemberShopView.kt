package com.tokopedia.tokomember_common_widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.unifyprinciples.Typography
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokomember_common_widget.model.TokomemberShopCardModel
import com.tokopedia.tokomember_common_widget.util.MemberType
import com.tokopedia.unifycomponents.ImageUnify
import kotlinx.android.synthetic.main.tm_shop_view.view.*

const val PREMIUM = "Premium"
const val VIP = "VIP"
class TokomemberShopView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    lateinit var cvShop: FrameLayout
    lateinit var ivShopIcon: ImageUnify
    lateinit var tvShopName: Typography
    lateinit var tvShopMemberName: Typography
    lateinit var tvShopStartDateValue: Typography
    lateinit var tvShopStartDateLabel: Typography
    lateinit var tvShopEndDateLabel: Typography
    lateinit var tvShopEndDateValue: Typography
    lateinit var containerBadge: FrameLayout
    lateinit var tvShopType: Typography
    private var mBackgroundImageUrl =""
    private var mCardShopName =""

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

    fun getCardBackgroundImageUrl() = mBackgroundImageUrl
    fun getCardShopName() = mCardShopName

    fun setShopCardData(tokomemberShopViewModel: TokomemberShopCardModel) {
        mBackgroundImageUrl = tokomemberShopViewModel.backgroundImgUrl
        mCardShopName = tokomemberShopViewModel.shopName
        tvShopName.text = mCardShopName
        shopType = tokomemberShopViewModel.shopType
        ivShopIcon.loadImage(tokomemberShopViewModel.shopIconUrl)
        ivShopContainer.loadImage(mBackgroundImageUrl)

        when(shopType){
            MemberType.PREMIUM ->{
                tvDescMembership.text = "Preview kartu member Premium (Level 1)."
                containerBadge.background = context?.getDrawable(R.drawable.tm_dash_badge_premium)
                tvShopType.text = PREMIUM
                tvShopType.setTextColor(ContextCompat.getColor(context,com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
            }

            MemberType.VIP ->{
                tvDescMembership.text = "Preview kartu member VIP (Level 2)."
                containerBadge.background = context?.getDrawable(R.drawable.tm_dash_badge_vip)
                tvShopType.text = VIP
                tvShopType.setTextColor(ContextCompat.getColor(context,com.tokopedia.unifyprinciples.R.color.Unify_YN400))
            }
        }
    }

}