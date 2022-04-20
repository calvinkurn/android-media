package com.tokopedia.tokomember_seller_dashboard.view.animation

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TokomemberIntroTextVh

open class TokomemberIntroItemAnimator : DefaultItemAnimator() {

/*    override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
        if (holder is TokomemberIntroTextVh){
            setAnimation(holder)
        }
        return true
    }*/

    override fun canReuseUpdatedViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ): Boolean {
        return true
    }

    override fun animatePersistence(
        viewHolder: RecyclerView.ViewHolder,
        preInfo: ItemHolderInfo,
        postInfo: ItemHolderInfo
    ): Boolean {
        if (viewHolder is TokomemberIntroTextVh){
            setAnimation(viewHolder)
        }
        return true
    }

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        preInfo: ItemHolderInfo,
        postInfo: ItemHolderInfo
    ): Boolean {
        if (newHolder is TokomemberIntroTextVh){
            setAnimation(newHolder)
        }
        return true
    }

    override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
        if (holder is TokomemberIntroTextVh){
            setAnimation(holder)
        }
        return true
    }


    private fun setAnimation(viewHolder: RecyclerView.ViewHolder) {
            val animation: Animation =
                AnimationUtils.loadAnimation(viewHolder.itemView.context, R.anim.tm_dash_intro_benefit_left)
        viewHolder.itemView.startAnimation(animation)

        }
    }
