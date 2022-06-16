package com.tokopedia.tokomember_seller_dashboard.view.adapter

import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomember_seller_dashboard.view.adapter.factory.TokomemberIntroFactory
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TmIntroBenefitImageVh
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TmIntroTextVh

class TmIntroAdapter(private val visitableList: ArrayList<Visitable<*>>,
                     typeFactory: TokomemberIntroFactory) :
    BaseAdapter<TokomemberIntroFactory>(typeFactory, visitableList) {
    private var onAttach = true
    private var isAlterNateTextView = false
    private var isAlterNateImageView = false
    fun addItems(data: ArrayList<Visitable<*>>) {
        visitableList.clear()
        visitableList.addAll(data)
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewAttachedToWindow(holder)
        if (holder is TmIntroTextVh) {
            if (!isAlterNateTextView) {
                isAlterNateTextView = !isAlterNateTextView
                holder.setAnimationLeftToRight(holder.itemView, holder.adapterPosition,onAttach)
            } else {
                isAlterNateTextView = !isAlterNateTextView
                holder.setAnimationRightToLeft(holder.itemView, holder.adapterPosition,onAttach)
            }
        }
        if (holder is TmIntroBenefitImageVh) {
            if (!isAlterNateImageView) {
                isAlterNateImageView = !isAlterNateImageView
                holder.setAnimationLeftToRight(holder.itemView, holder.adapterPosition,onAttach)
            } else {
                isAlterNateImageView = !isAlterNateImageView
                holder.setAnimationRightToLeft(holder.itemView, holder.adapterPosition,onAttach)
            }
        }
    }

    override fun onAttachedToRecyclerView(@NonNull recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                onAttach = false
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        super.onAttachedToRecyclerView(recyclerView)
    }
}
