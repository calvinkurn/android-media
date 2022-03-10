package com.tokopedia.home_account.explicitprofile.features

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.home_account.explicitprofile.data.CategoriesDataModel
import com.tokopedia.home_account.explicitprofile.features.categories.CategoryFragment

class ExplicitProfilePageAdapter(
    private var categories: CategoriesDataModel,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val generatedFragments: MutableList<Fragment> = mutableListOf()

    override fun getItemCount(): Int {
        return categories.data.dataCategories.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = CategoryFragment.createInstance(
            categories.data.dataCategories[position].idCategory,
            categories.data.dataCategories[position].templateName
        )

        generatedFragments.add(fragment)
        return fragment
    }

    fun getFragments(): MutableList<Fragment> = generatedFragments

    fun getFragmentByPosition(position: Int): Fragment {
        return generatedFragments[position]
    }

    fun clearFragments() {
        notifyItemRangeRemoved(0, itemCount)
        generatedFragments.clear()
    }
}