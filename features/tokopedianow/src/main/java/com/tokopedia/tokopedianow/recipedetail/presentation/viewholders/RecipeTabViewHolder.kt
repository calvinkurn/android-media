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
) : AbstractViewHolder<RecipeTabUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_tokopedianow_recipe_tab

        private const val INDEX_BUY_INGREDIENT_TAB = 0
        private const val INDEX_HOW_TO_TAB = 1
        private const val UNSPECIFIED_HEIGHT = 0
    }

    private var binding: ItemTokopedianowRecipeTabBinding? by viewBinding()

    private var ingredientTab: TokoNowRecipeIngredientFragment? = null
    private var instructionTab: TokoNowRecipeInstructionFragment? = null

    private val analytics by lazy { recipeDetailView.getTracker() }
    private val tabAdapter by lazy { createTabAdapter() }

    init {
        setupViewPager()
    }

    override fun bind(tab: RecipeTabUiModel) {
        ingredientTab?.setItemList(tab.ingredient.items)
        instructionTab?.setItemList(tab.instruction.items)

        analytics.trackImpressionBuyIngredientsTab()
        analytics.trackImpressionHowToTab()
    }

    private fun setupViewPager() {
        val recipeTab = binding?.recipeTab
        val viewPager = binding?.viewPager
        val ingredientTabTitle = itemView.context.resources.getString(
            R.string.tokopedianow_recipe_ingredient_tab_title
        )
        val instructionTabTitle = itemView.context.resources.getString(
            R.string.tokopedianow_recipe_instruction_tab_title
        )

        recipeTab?.apply {
            addNewTab(ingredientTabTitle)
            addNewTab(instructionTabTitle)
            addOnTabSelectedListener(viewPager)
        }

        viewPager?.apply {
            adapter = tabAdapter
            registerPageChangeCallback(recipeTab)
            setPageTransformer { page, _ ->
                updatePagerHeightForChild(page, viewPager)
            }
        }
    }

    private fun createTabAdapter(): RecipeTabAdapter? {
        return recipeDetailView.getFragmentActivity()?.run {
            ingredientTab = TokoNowRecipeIngredientFragment.newInstance().apply {
                setRecipeDetailView(recipeDetailView)
            }
            instructionTab = TokoNowRecipeInstructionFragment.newInstance()

            RecipeTabAdapter(this).apply {
                addFragment(ingredientTab)
                addFragment(instructionTab)
            }
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

    private fun updatePagerHeightForChild(view: View, pager: ViewPager2) {
        val wMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
        val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(UNSPECIFIED_HEIGHT, View.MeasureSpec.UNSPECIFIED)
        view.measure(wMeasureSpec, hMeasureSpec)
        pager.layoutParams = pager.layoutParams.also { lp -> lp.height = view.measuredHeight }
    }
}