package com.tokopedia.home_component.viewholders

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.os.Handler
import android.os.SystemClock.uptimeMillis
import android.view.*
import android.view.MotionEvent.ACTION_DOWN
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentDynamicIconBinding
import com.tokopedia.home_component.decoration.CommonSpacingDecoration
import com.tokopedia.home_component.listener.DynamicIconComponentListener
import com.tokopedia.home_component.model.DynamicIconComponent
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.home_component.visitable.DynamicIconComponentDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.UnifyMotion
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by Lukas on 1/8/21.
 */
class DynamicIconViewHolder(itemView: View, private val listener: DynamicIconComponentListener) :
    AbstractViewHolder<DynamicIconComponentDataModel>(itemView) {

    private var binding: HomeComponentDynamicIconBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_dynamic_icon
        private const val SCROLLABLE_ITEM = 5
    }

    private val adapter = DynamicIconAdapter(listener)
    private var iconRecyclerView: RecyclerView? = null

    override fun bind(element: DynamicIconComponentDataModel) {
        setupDynamicIcon(element)
        setupImpression(element)
    }

    private fun setupDynamicIcon(element: DynamicIconComponentDataModel) {
        val icons = element.dynamicIconComponent.dynamicIcon
        iconRecyclerView = itemView.findViewById(R.id.dynamic_icon_recycler_view)
        if (icons.isNotEmpty()) {
            adapter.submitList(element)
            adapter.updatePosition(adapterPosition)
            adapter.setType(element.type)
            if (iconRecyclerView?.itemDecorationCount == 0) {
                iconRecyclerView?.addItemDecoration(
                    CommonSpacingDecoration(
                        8f.toDpInt()
                    )
                )
            }
            iconRecyclerView?.adapter = adapter
            setupLayoutManager(
                isScrollItem = icons.size > SCROLLABLE_ITEM,
                spanCount = icons.size
            )
            iconRecyclerView?.clearOnScrollListeners()
            iconRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    listener.onIconScroll(adapterPosition)
                }
            })
        }
    }

    private fun setupImpression(element: DynamicIconComponentDataModel) {
        itemView.addOnImpressionListener(element) {
            if (!element.isCache) listener.onIconChannelImpressed(element, adapterPosition)
        }
    }

    private fun setupLayoutManager(isScrollItem: Boolean, spanCount: Int) {
        if (isScrollItem) {
            binding?.dynamicIconRecyclerView?.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        } else {
            binding?.dynamicIconRecyclerView?.layoutManager =
                GridLayoutManager(itemView.context, spanCount, GridLayoutManager.VERTICAL, false)
        }
    }

    internal inner class DynamicIconAdapter(private val listener: DynamicIconComponentListener) :
        RecyclerView.Adapter<DynamicIconItemViewHolder>() {
        private val categoryList = mutableListOf<DynamicIconComponent.DynamicIcon>()
        private var position: Int = 0
        private var type: Int = 1
        private var isCache: Boolean = false

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): DynamicIconItemViewHolder {
            return DynamicIconItemViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(DynamicIconItemViewHolder.LAYOUT, parent, false),
                listener
            )
        }

        override fun onBindViewHolder(holder: DynamicIconItemViewHolder, position: Int) {
            categoryList.getOrNull(position)?.let {
                holder.bind(
                    it,
                    categoryList.size > SCROLLABLE_ITEM,
                    this.position,
                    type,
                    isCache
                )
            }
        }

        override fun getItemCount(): Int {
            return categoryList.size
        }

        fun setType(type: Int) {
            this.type = type
        }

        fun updatePosition(position: Int) {
            this.position = position
        }

        fun submitList(list: DynamicIconComponentDataModel) {
            categoryList.clear()
            categoryList.addAll(list.dynamicIconComponent.dynamicIcon)
            this.isCache = list.isCache
        }
    }

    internal class DynamicIconItemViewHolder(
        itemView: View,
        private val listener: DynamicIconComponentListener
    ) : RecyclerView.ViewHolder(itemView) {
        var iconTvName: Typography? = null
        var iconImageView: ImageUnify? = null
        var iconContainer: CardUnify2? = null
        var iconContainerImage: CardUnify2? = null
        val longPressHandler = Handler()
        private var isLongPress = false
        private var scaleAnimator = ValueAnimator.ofFloat()

        var onLongPress = Runnable {
            if (itemView != null) {
                isLongPress = true
                itemView.performLongClick()
            }
        }

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.home_component_dynamic_icon_item
        }

        fun View.performTouchDown() = dispatchTouchEvent(
            MotionEvent.obtain(uptimeMillis(), uptimeMillis() + 700, ACTION_DOWN, 0f, 0f, 0)
        )

        private fun animateScaling(
            start: Float,
            end: Float,
            duration: Long,
            interpolator: TimeInterpolator
        ) {
            scaleAnimator = ValueAnimator.ofFloat()
            scaleAnimator.setFloatValues(start, end)
            scaleAnimator.removeAllListeners()
            scaleAnimator.removeAllUpdateListeners()
            scaleAnimator.addUpdateListener {
                iconContainerImage?.scaleX = it.animatedValue as Float
                iconContainerImage?.scaleY = it.animatedValue as Float
            }
            scaleAnimator.duration = duration
            scaleAnimator.interpolator = interpolator
            scaleAnimator.start()
        }

        fun bind(
            item: DynamicIconComponent.DynamicIcon,
            isScrollable: Boolean,
            parentPosition: Int,
            type: Int,
            isCache: Boolean
        ) {
            iconTvName = itemView.findViewById(R.id.dynamic_icon_typography)
            iconImageView = itemView.findViewById(R.id.dynamic_icon_image_view)
            iconContainer = itemView.findViewById(R.id.dynamic_icon_container)
            iconContainerImage = itemView.findViewById(R.id.dynamic_icon_image_container)

            iconTvName?.text = item.name
            iconImageView?.setImageUrl(item.imageUrl)
            iconContainer?.layoutParams = ViewGroup.LayoutParams(
                if (isScrollable) ViewGroup.LayoutParams.WRAP_CONTENT else ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            iconTvName?.minLines = 2
            iconContainer?.cardType = CardUnify2.TYPE_CLEAR
            iconContainer?.animateOnPress = CardUnify2.ANIMATE_OVERLAY
            iconContainerImage?.cardType = CardUnify2.TYPE_CLEAR

//            iconContainerImage?.animateOnPress = CardUnify2.ANIMATE_BOUNCE
            iconContainer?.setOnTouchListener { view, event ->
                iconContainerImage?.performTouchDown()

                when (event?.action) {
                    MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                        longPressHandler.removeCallbacks(onLongPress)
                        Handler().postDelayed(
                            {
                                animateScaling(0.8125f, 1.01f, UnifyMotion.T1, UnifyMotion.EASE_OUT)

                                scaleAnimator.addListener(object : Animator.AnimatorListener {
                                    override fun onAnimationRepeat(p0: Animator) {
                                    }

                                    override fun onAnimationCancel(p0: Animator) {
                                    }

                                    override fun onAnimationStart(p0: Animator) {
                                    }

                                    override fun onAnimationEnd(p0: Animator) {
                                        animateScaling(
                                            1.01f,
                                            1f,
                                            UnifyMotion.T1,
                                            UnifyMotion.EASE_IN_OUT
                                        )
                                    }
                                })
                            },
                            if (event.eventTime - event.downTime <= UnifyMotion.T1) UnifyMotion.T1 - (event.eventTime - event.downTime) else 0.toLong()
                        )

                        if (event.action == MotionEvent.ACTION_UP) {
                            if (!isLongPress) {
                                itemView.performClick()
                            }
                            isLongPress = false
                        }
                        true
                    }

                    MotionEvent.ACTION_DOWN -> {
                        longPressHandler.postDelayed(
                            onLongPress,
                            ViewConfiguration.getLongPressTimeout().toLong()
                        )

                        animateScaling(1f, 0.95f, UnifyMotion.T1, UnifyMotion.EASE_OUT)

                        true
                    }
                }

                false
            }
            itemView.setOnClickListener {
                listener.onClickIcon(item, parentPosition, adapterPosition, type)
            }

            iconContainer?.setCardBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_Background
                )
            )

            if (!isCache) {
                itemView.addOnImpressionListener(item) {
                    listener.onImpressIcon(
                        dynamicIcon = item,
                        iconPosition = adapterPosition,
                        parentPosition = parentPosition,
                        type = type,
                        view = itemView
                    )
                }
            }
        }
    }
}
