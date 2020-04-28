package com.tokopedia.product.detail.view.viewholder

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationCarouselDataModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography


class AddToCartDoneRecommendationCarouselViewHolder(
        itemView: View,
        private val recommendationListener: RecommendationListener
) : AbstractViewHolder<AddToCartDoneRecommendationCarouselDataModel>(itemView) {

    private val viewPager = itemView.findViewById<ViewPager2>(R.id.view_pager_image)
    private val titleWidget = itemView.findViewById<Typography>(R.id.title_recom)
    private val productName = itemView.findViewById<Typography>(R.id.product_name)
    private val shopBadges = itemView.findViewById<ImageView>(R.id.shop_badge)
    private val shopLocation = itemView.findViewById<Typography>(R.id.shop_location)
    private val ratingCount = itemView.findViewById<Typography>(R.id.rating_count)
    private val reviewCount = itemView.findViewById<Typography>(R.id.review_count)
    private val freeOngkirImage = itemView.findViewById<ImageView>(R.id.image_free_ongkir)
    private val ticker = itemView.findViewById<Ticker>(R.id.ticker)
    private val price = itemView.findViewById<Typography>(R.id.price)
    private val addToCartButton = itemView.findViewById<UnifyButton>(R.id.btn_add_to_cart)
    private val adapter = RecommendationCarouselAdapter()
    private val containerContent = itemView.findViewById<View>(R.id.content_layout)

    // animation
    private val slideLeftOut = ObjectAnimator.ofFloat(containerContent, "translationX", 0f, -100f)
    private val slideLeftIn = ObjectAnimator.ofFloat(containerContent, "translationX", -100f, 0f)
    private val slideRightIn = ObjectAnimator.ofFloat(containerContent, "translationX", 100f, 0f)
    private val slideRightOut = ObjectAnimator.ofFloat(containerContent, "translationX", 0f, 100f)
    private val alphaOut = ObjectAnimator.ofFloat(containerContent, "alpha", 1f, 0f)
    private val alphaIn = ObjectAnimator.ofFloat(containerContent, "alpha", 0f, 1f)
    private val animatorSet = AnimatorSet()
    private val reversedAnimatorSet = AnimatorSet()
    private lateinit var animationListener: Animator.AnimatorListener
    private var viewPager2PageChangeCallback: ViewPager2.OnPageChangeCallback? = null

    private var previousPosition = -1
    private var currentPosition = 0
    private var model: AddToCartDoneRecommendationCarouselDataModel ?= null

    private val pageMarginPx = itemView.resources.getDimensionPixelOffset(R.dimen.dp_20)
    private val offsetPx = itemView.resources.getDimensionPixelOffset(R.dimen.dp_80)
    companion object {
        val LAYOUT_RES = R.layout.add_to_cart_done_recommendation_carousel_layout
    }

    init {
        configAnimation()
    }

    override fun bind(element: AddToCartDoneRecommendationCarouselDataModel) {
        try{
            with(itemView) {
                model = element
                titleWidget.text = element.recommendationWidget.title
                val products = element.recommendationWidget.recommendationItemList
                val parentPosition = adapterPosition
                adapter.setItem(products)

                viewPager.adapter = adapter
                with(viewPager) {
                    clipToPadding = false
                    clipChildren = false
                    offscreenPageLimit = 3
                }
                if(viewPager2PageChangeCallback == null){
                    viewPager2PageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            setItemDetail(position)
                        }
                    }
                    viewPager.registerOnPageChangeCallback(viewPager2PageChangeCallback as ViewPager2.OnPageChangeCallback)
                }
                viewPager.setPageTransformer(ViewPager2PageTransformation())
                visible()
            }
        }catch (exception: Exception){
            exception.printStackTrace()
        }
    }

    private fun setItemDetail(position: Int){
        model?.let { model ->
            currentPosition = position
            val recommendation = model.recommendationWidget.recommendationItemList[position]
            productName.show()
            shopLocation.show()
            reviewCount.show()
            ratingCount.show()
            freeOngkirImage.visibility = if (recommendation.isFreeOngkirActive) View.VISIBLE else View.GONE
            shopBadges.visibility = if (recommendation.badgesUrl.isNotEmpty()) View.VISIBLE else View.GONE
            ticker.visibility = if (model.shopId != -1) View.VISIBLE else View.GONE
            price.show()


            if(previousPosition > position && previousPosition != -1){
                reversedAnimatorSet.start()
            } else{
                animatorSet.start()
            }
            previousPosition = position
        }
    }

    private fun configAnimation(){
        slideLeftOut.duration = 200
        slideLeftOut.interpolator = FastOutLinearInInterpolator()
        slideLeftIn.duration = 200
        slideLeftIn.interpolator = FastOutLinearInInterpolator()

        slideRightOut.duration = 200
        slideRightOut.interpolator = FastOutLinearInInterpolator()
        slideRightIn.duration = 200
        slideRightIn.interpolator = FastOutLinearInInterpolator()

        alphaOut.duration = 200
        alphaIn.duration = 200

        animatorSet.play(slideLeftOut).with(alphaOut)
        animatorSet.play(slideRightIn).with(alphaIn).after(180)

        reversedAnimatorSet.play(slideRightOut).with(alphaOut)
        reversedAnimatorSet.play(slideLeftIn).with(alphaIn).after(180)

        animationListener = object : Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {}

            @SuppressLint("SyntheticAccessor")
            override fun onAnimationEnd(animation: Animator?) {
                val recommendation = model?.recommendationWidget?.recommendationItemList?.get(currentPosition)
                recommendation?.let {
                    productName.text = recommendation.name
                    shopLocation.text = recommendation.location
                    reviewCount.text = "(${recommendation.countReview})"
                    ratingCount.text = recommendation.rating.toString()
                    shopBadges.loadImage(recommendation.badgesUrl.firstOrNull() ?: "")
                    freeOngkirImage.loadImage(recommendation.freeOngkirImageUrl)
                    price.text = recommendation.price
                    ticker.tickerType = if(recommendation.shopId == model?.shopId) Ticker.TYPE_INFORMATION else Ticker.TYPE_ANNOUNCEMENT
                    ticker.setTextDescription(getString(if(recommendation.shopId == model?.shopId) R.string.ticker_atc_done_some_store else R.string.ticker_atc_done_different_store))
                }
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        }

        slideLeftOut.addListener(animationListener)
        slideRightOut.addListener(animationListener)
    }

    inner class RecommendationCarouselAdapter : RecyclerView.Adapter<RecommendationCarouselImageViewHolder>(){
        private var list: List<RecommendationItem> = listOf()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationCarouselImageViewHolder {
            return RecommendationCarouselImageViewHolder(parent)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: RecommendationCarouselImageViewHolder, position: Int) {
            holder.bind(list[position])
        }

        fun setItem(list: List<RecommendationItem>){
            this.list = list
            notifyDataSetChanged()
        }
    }

    inner class RecommendationCarouselImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context).inflate(R.layout.add_to_cart_done_recommendation_carousel_image_item, parent, false))
        fun bind(recommendation: RecommendationItem){
            itemView.findViewById<ImageView>(R.id.carousel_image)?.loadImageRounded(
                    recommendation.imageUrl,
                    8f
            )
        }
    }

    inner class ViewPager2PageTransformation : ViewPager2.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            val absPos = Math.abs(position)
            page.apply {
                val viewPager = page.parent.parent as ViewPager2
                val offset = position * -(2 * offsetPx + pageMarginPx)
                if (viewPager.orientation == ORIENTATION_HORIZONTAL) {
                    if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                        page.translationX = -offset
                    } else {
                        page.translationX = offset
                    }
                } else {
                    page.translationY = offset
                }
                scaleY = if (absPos >= 1) {
                    0.5f
                } else {
                    (0.5f - 1) * absPos + 1
                }
                scaleX = if (absPos >= 1) {
                    0.5f
                } else {
                    (0.5f - 1) * absPos + 1
                }

            }
            when {
                position < -1 ->
                    page.alpha = 0.5f
                position <= 1 -> {
                    page.alpha = Math.max(0.5f, 1 - Math.abs(position))
                }
                else -> page.alpha = 0.5f
            }
        }
    }
}