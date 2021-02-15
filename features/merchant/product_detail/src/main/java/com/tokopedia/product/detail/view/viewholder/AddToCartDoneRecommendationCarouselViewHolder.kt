package com.tokopedia.product.detail.view.viewholder

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DimenRes
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationCarouselDataModel
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationItemDataModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import kotlin.math.abs

class AddToCartDoneRecommendationCarouselViewHolder(
        itemView: View,
        private val recommendationListener: RecommendationListener,
        private val addToCartDoneAddedProductListener: AddToCartDoneAddedProductViewHolder.AddToCartDoneAddedProductListener
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
    private val containerContent = itemView.findViewById<View>(R.id.content_layout)
    private val adapter = RecommendationCarouselAdapter()

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
    private val itemDecoration = HorizontalMarginItemDecoration(
            itemView.context,
            R.dimen.viewpager_current_item_horizontal_margin
    )

    private var previousPosition = -1
    private var currentPosition = 0
    private var model: AddToCartDoneRecommendationCarouselDataModel ?= null

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
                            configDetailRecommendation(position)
                        }
                    }
                    viewPager.registerOnPageChangeCallback(viewPager2PageChangeCallback as ViewPager2.OnPageChangeCallback)
                }
                viewPager.addItemDecoration(itemDecoration)
                viewPager.setPageTransformer(ViewPager2PageTransformation())
                visible()
            }
        }catch (exception: Exception){
            exception.printStackTrace()
        }
    }

    private fun configViewVisibility(dataModel: AddToCartDoneRecommendationItemDataModel){
        productName.show()
        shopLocation.show()
        reviewCount.show()
        ratingCount.show()
        freeOngkirImage.visibility = if (dataModel.recommendationItem.isFreeOngkirActive) View.VISIBLE else View.GONE
        shopBadges.visibility = if (dataModel.recommendationItem.badgesUrl.isNotEmpty()) View.VISIBLE else View.GONE
        ticker.visibility = if (model?.shopId != -1) View.VISIBLE else View.GONE
    }

    private fun configDetailRecommendation(position: Int){
        model?.let { model ->
            currentPosition = position
            val recommendation = model.recommendationWidget.recommendationItemList[position]
            configViewVisibility(recommendation)
            when {
                previousPosition == -1 -> {
                    setRecommendationItemToView(recommendation)
                }
                previousPosition > position -> {
                    reversedAnimatorSet.start()
                }
                else -> {
                    animatorSet.start()
                }
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
                    setRecommendationItemToView(it)
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

    private fun setRecommendationItemToView(dataModel: AddToCartDoneRecommendationItemDataModel){
        productName.text = dataModel.recommendationItem.name
        shopLocation.text = dataModel.recommendationItem.location
        reviewCount.text = "(${dataModel.recommendationItem.countReview})"
        ratingCount.text = dataModel.recommendationItem.rating.toString()
        shopBadges.loadIcon(dataModel.recommendationItem.badgesUrl.firstOrNull() ?: "")
        freeOngkirImage.loadIcon(dataModel.recommendationItem.freeOngkirImageUrl)
        ticker.tickerType = if(dataModel.recommendationItem.shopId == model?.shopId) Ticker.TYPE_INFORMATION else Ticker.TYPE_ANNOUNCEMENT
        ticker.setTextDescription(getString(if(dataModel.recommendationItem.shopId == model?.shopId) R.string.ticker_atc_done_some_store else R.string.ticker_atc_done_different_store))
        addToCartDoneAddedProductListener.onRecommendationItemSelected(dataModel, dataModel.recommendationItem.position)
    }

    inner class RecommendationCarouselAdapter : RecyclerView.Adapter<RecommendationCarouselImageViewHolder>(){
        private var list: List<AddToCartDoneRecommendationItemDataModel> = listOf()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationCarouselImageViewHolder {
            return RecommendationCarouselImageViewHolder(parent)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: RecommendationCarouselImageViewHolder, position: Int) {
            holder.bind(list[position])
        }

        fun setItem(list: List<AddToCartDoneRecommendationItemDataModel>){
            this.list = list
            notifyDataSetChanged()
        }
    }

    inner class RecommendationCarouselImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context).inflate(R.layout.add_to_cart_done_recommendation_carousel_image_item, parent, false))
        fun bind(dataModel: AddToCartDoneRecommendationItemDataModel){
            val recommendation = dataModel.recommendationItem
            itemView.findViewById<ImageView>(R.id.carousel_image)?.loadImageRounded(recommendation.imageUrl)
            if(!itemView.hasOnClickListeners()){
                itemView.setOnClickListener {
                    recommendationListener.onProductClick(
                            recommendation,
                            null,
                            adapterPosition
                    )
                }
            }
            itemView.findViewById<ImageView>(R.id.carousel_image).addOnImpressionListener(recommendation, object : ViewHintListener {
                override fun onViewHint() {
                    recommendationListener.onProductImpression(recommendation)
                }
            })
        }
    }

    inner class HorizontalMarginItemDecoration(context: Context, @DimenRes horizontalMarginInDp: Int) : RecyclerView.ItemDecoration() {

        private val horizontalMarginInPx: Int =
                context.resources.getDimension(horizontalMarginInDp).toInt()

        override fun getItemOffsets(
                outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
        ) {
            outRect.right = horizontalMarginInPx
            outRect.left = horizontalMarginInPx
        }

    }

    inner class ViewPager2PageTransformation : ViewPager2.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            val nextItemVisiblePx = itemView.resources.getDimension(R.dimen.viewpager_next_item_visible)
            val currentItemHorizontalMarginPx = itemView.resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
            val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
            page.apply {
                page.translationX = -pageTranslationX * position
                page.scaleY = 1 - (0.25f * abs(position))
                page.alpha = 0.25f + (1 - abs(position))
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