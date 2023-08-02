package com.tokopedia.digital.home.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.databinding.LayoutDigitalHomeCategoryItemSubmenuBinding
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.media.loader.loadImage

class RechargeItemCategoryAdapter(
    var items: List<RechargeHomepageSections.Item>,
    val listener: RechargeHomepageItemListener
) : RecyclerView.Adapter<RechargeItemCategoryAdapter.DigitalItemSubmenuCategoryViewHolder>() {

    override fun onBindViewHolder(viewHolder: DigitalItemSubmenuCategoryViewHolder, position: Int) {
        viewHolder.bind(items[position], listener, position == items.size - 1)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        position: Int
    ): DigitalItemSubmenuCategoryViewHolder {
        val view = LayoutDigitalHomeCategoryItemSubmenuBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DigitalItemSubmenuCategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class DigitalItemSubmenuCategoryViewHolder(val binding: LayoutDigitalHomeCategoryItemSubmenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            element: RechargeHomepageSections.Item,
            onItemBindListener: RechargeHomepageItemListener,
            isLastItem: Boolean = false
        ) {
            with(binding) {
                binding.categoryImage.loadImage(element.mediaUrl)
                binding.categoryName.text = element.title
                root.setOnClickListener {
                    onItemBindListener.onRechargeSectionItemClicked(element)
                }

                if (isLastItem && element.title.equals(SEE_ALL_TITLE, true)) {
                    binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
                            ViewTreeObserver.OnGlobalLayoutListener {
                            override fun onGlobalLayout() {
                                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                                onItemBindListener.onRechargeAllCategoryShowCoachmark(binding.root)
                            }
                        })
                }
            }
        }

        companion object {
            private const val SEE_ALL_TITLE = "Semua Kategori"
        }
    }
}
