package com.tokopedia.tokopedianow.recipedetail.presentation.viewholders

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecipeTabBinding
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeTabAdapter
import com.tokopedia.tokopedianow.recipedetail.presentation.fragment.TokoNowRecipeIngredientFragment
import com.tokopedia.tokopedianow.recipedetail.presentation.fragment.TokoNowRecipeInstructionFragment
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeTabUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.view.RecipeDetailView
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.utils.view.binding.viewBinding

class RecipeTabViewHolder(
    itemView: View,
    private val recipeDetailView: RecipeDetailView
): AbstractViewHolder<RecipeTabUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_tokopedianow_recipe_tab

        private const val INDEX_BUY_INGREDIENT_TAB = 0
        private const val INDEX_HOW_TO_TAB = 1
    }

    private var binding: ItemTokopedianowRecipeTabBinding? by viewBinding()

    private val analytics by lazy { recipeDetailView.getTracker() }

    override fun bind(tab: RecipeTabUiModel) {
        recipeDetailView.getFragmentActivity()?.let {
            val recipeTab = binding?.recipeTab
            val viewPager = binding?.viewPager
            val ingredientTabTitle = itemView.context.resources.getString(
                R.string.tokopedianow_recipe_ingredient_tab_title)
            val instructionTabTitle = itemView.context.resources.getString(
                R.string.tokopedianow_recipe_instruction_tab_title)

            val ingredientFragment = TokoNowRecipeIngredientFragment.newInstance().apply {
                setRecipeDetailView(recipeDetailView)
                setItemList(tab.ingredient.items)
            }

            val instructionFragment = TokoNowRecipeInstructionFragment.newInstance().apply {
                setItemList(tab.instruction.items)
            }

            val recipeTabAdapter = RecipeTabAdapter(it).apply {
                addFragment(ingredientFragment)
                addFragment(instructionFragment)
            }

            recipeTab?.apply {
                addNewTab(ingredientTabTitle)
                addNewTab(instructionTabTitle)
                addOnTabSelectedListener(viewPager)
            }

            viewPager?.apply {
                adapter = recipeTabAdapter
                registerPageChangeCallback(recipeTab)
            }

            analytics.trackImpressionBuyIngredientsTab()
            analytics.trackImpressionHowToTab()
        }
    }

    private fun TabsUnify.addOnTabSelectedListener(viewPager: ViewPager2?) {
        getUnifyTabLayout().addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager?.setCurrentItem(tab.position, true)
                trackClickRecipeTab(tab.position)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun ViewPager2.registerPageChangeCallback(recipeTab: TabsUnify?) {
        registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                recipeTab?.getUnifyTabLayout()?.getTabAt(position)?.select()
            }
        })
    }

    private fun trackClickRecipeTab(position: Int) {
        when (position) {
            INDEX_BUY_INGREDIENT_TAB -> analytics.trackClickBuyIngredientsTab()
            INDEX_HOW_TO_TAB -> analytics.trackClickHowToTab()
        }
    }
}