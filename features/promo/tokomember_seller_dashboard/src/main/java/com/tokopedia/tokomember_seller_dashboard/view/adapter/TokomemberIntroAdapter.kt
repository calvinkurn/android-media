package com.tokopedia.tokomember_seller_dashboard.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomember_seller_dashboard.view.adapter.factory.TokomemberIntroFactory
import android.animation.ObjectAnimator

import android.animation.AnimatorSet
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TokomemberIntroBenefitImageVh
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TokomemberIntroTextVh

class TokomemberIntroAdapter(private val visitableList: ArrayList<Visitable<*>>,
                  private val typeFactory: TokomemberIntroFactory) :
    BaseAdapter<TokomemberIntroFactory>(typeFactory, visitableList) {
    var DURATION: Long = 1000
    private var ON_ATTACH = true
    var isAlterNateTextView = false
    var isAlterNateImageView = false
    fun addItems(data: ArrayList<Visitable<*>>) {
        visitableList.clear()
        visitableList.addAll(data)
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewAttachedToWindow(holder)

        if (holder is TokomemberIntroTextVh) {
            if (!isAlterNateTextView) {
                isAlterNateTextView = !isAlterNateTextView
                fromLeftToRight(holder.itemView, holder.adapterPosition)
            }
            else{
                fromRightToLeft(holder.itemView,holder.adapterPosition)
            }
        }
        if (holder is TokomemberIntroBenefitImageVh){
            if (!isAlterNateImageView) {
                isAlterNateImageView = !isAlterNateImageView
                fromRightToLeft(holder.itemView, holder.adapterPosition)
            } else{
                fromLeftToRight(holder.itemView, holder.adapterPosition)
            }
        }
    }

    override fun onAttachedToRecyclerView(@NonNull recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                ON_ATTACH = false
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        super.onAttachedToRecyclerView(recyclerView)
    }

    private fun fromLeftToRight(itemView: View, position: Int) {
        var i = position
        if (!ON_ATTACH) {
            i = -1
        }
        val notFirsItem = i == -1
        i += 1
        itemView.translationX = -(getDeviceWidth(itemView)).toFloat()
        itemView.alpha = 0f
        val animatorSet = AnimatorSet()
        val animatorTranslateX: ObjectAnimator =
            ObjectAnimator.ofFloat(itemView, "translationX", -(getDeviceWidth(itemView)), 0F)
        val animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1f)
        ObjectAnimator.ofFloat(itemView, "alpha", 0f).start()
        animatorTranslateX.startDelay = if (notFirsItem) DURATION else i * DURATION
        animatorTranslateX.duration = (if (notFirsItem) 2 else 1) * DURATION
        animatorSet.playTogether(animatorTranslateX, animatorAlpha)
        animatorSet.start()
    }

    private fun fromRightToLeft(itemView: View, position: Int) {
        var i = position
        if (!ON_ATTACH) {
            i = -1
        }
        val notFirsItem = i == -1
        i += 1
        itemView.translationX = (getDeviceWidth(itemView)+ itemView.x)
        itemView.alpha = 0f
        val animatorSet = AnimatorSet()
        val animatorTranslateX =
            ObjectAnimator.ofFloat(itemView, "translationX", getDeviceWidth(itemView)+ itemView.x, 0f)
        val animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1f)
        ObjectAnimator.ofFloat(itemView, "alpha", 0f).start()
        animatorTranslateX.startDelay = if (notFirsItem) DURATION else i * DURATION
        animatorTranslateX.duration = (if (notFirsItem) 2 else 1) * DURATION
        animatorSet.playTogether(animatorTranslateX, animatorAlpha)
        animatorSet.start()
    }

    private fun getDeviceWidth(itemView: View): Float {
        val metrics = itemView.context.resources.displayMetrics
        return metrics.widthPixels.toFloat()
    }
}

interface TokomemberIntroAdapterListener {
    fun onItemDisplayed(tokoIntroItem: Visitable<*>, position: Int)
}