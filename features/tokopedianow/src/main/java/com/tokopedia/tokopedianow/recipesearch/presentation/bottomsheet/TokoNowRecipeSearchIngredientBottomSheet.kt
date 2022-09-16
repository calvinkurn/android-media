package com.tokopedia.tokopedianow.recipesearch.presentation.bottomsheet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.BottomSheetUtil.configureMaxHeight
import com.tokopedia.tokopedianow.databinding.BottomsheetTokopedianowRecipeSearchIngredientBinding
import com.tokopedia.tokopedianow.recipesearch.presentation.adapter.RecipeSearchIngredientAdapter
import com.tokopedia.tokopedianow.recipesearch.presentation.uimodel.RecipeSearchIngredientUiModel
import com.tokopedia.tokopedianow.recipesearch.presentation.viewholder.RecipeSearchIngredientViewHolder
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class TokoNowRecipeSearchIngredientBottomSheet: BottomSheetUnify() {

    companion object {
        const val EXTRA_SEARCH_BY_INGREDIENT_IDS = "EXTRA_SEARCH_BY_INGREDIENT_IDS"

        private val TAG = TokoNowRecipeSearchIngredientBottomSheet::class.simpleName

        fun newInstance(): TokoNowRecipeSearchIngredientBottomSheet {
            return TokoNowRecipeSearchIngredientBottomSheet()
        }
    }

    private var binding by autoClearedNullable<BottomsheetTokopedianowRecipeSearchIngredientBinding>()
    private var recipeIdsChecked: ArrayList<String> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureMaxHeight()
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun initView() {
        binding = BottomsheetTokopedianowRecipeSearchIngredientBinding.inflate(LayoutInflater.from(context))

        clearContentPadding = true
        isFullpage = false
        showKnob = true
        showCloseIcon = false
        isHideable = true

        setTitle(getString(R.string.tokopedianow_recipe_search_ingredient_title_bottomsheet))
        setChild(binding?.root)
        setUi(binding)
    }

    private fun setUi(binding: BottomsheetTokopedianowRecipeSearchIngredientBinding?) {
        binding?.apply {
            // add dummy data for testing purpose only
            val adapter = RecipeSearchIngredientAdapter(
                listener = recipeSearchIngredientCallback()
            )
            val recipes = listOf(
                RecipeSearchIngredientUiModel(
                    id = "123",
                    imgUrl = "https://cdn1-production-images-kly.akamaized.net/3hVTEoX_tJ0lFq3_q8NoQB0hmZI=/1280x720/smart/filters:quality(75):strip_icc():format(webp)/kly-media-production/medias/3133148/original/027517900_1589942784-Raw_Fresh_Meat_With_Rosemary.jpg",
                    title = "Daging Mentah",
                    isChecked = true
                ),
                RecipeSearchIngredientUiModel(
                    id = "124",
                    imgUrl = "https://cdn1-production-images-kly.akamaized.net/3hVTEoX_tJ0lFq3_q8NoQB0hmZI=/1280x720/smart/filters:quality(75):strip_icc():format(webp)/kly-media-production/medias/3133148/original/027517900_1589942784-Raw_Fresh_Meat_With_Rosemary.jpg",
                    title = "Daging Segar",
                    isChecked = false
                ),
                RecipeSearchIngredientUiModel(
                    id = "125",
                    imgUrl = "https://cdn1-production-images-kly.akamaized.net/3hVTEoX_tJ0lFq3_q8NoQB0hmZI=/1280x720/smart/filters:quality(75):strip_icc():format(webp)/kly-media-production/medias/3133148/original/027517900_1589942784-Raw_Fresh_Meat_With_Rosemary.jpg",
                    title = "Daging Aja",
                    isChecked = false
                ),
                RecipeSearchIngredientUiModel(
                    id = "123",
                    imgUrl = "https://cdn1-production-images-kly.akamaized.net/3hVTEoX_tJ0lFq3_q8NoQB0hmZI=/1280x720/smart/filters:quality(75):strip_icc():format(webp)/kly-media-production/medias/3133148/original/027517900_1589942784-Raw_Fresh_Meat_With_Rosemary.jpg",
                    title = "Daging Mentah",
                    isChecked = true
                ),
                RecipeSearchIngredientUiModel(
                    id = "124",
                    imgUrl = "https://cdn1-production-images-kly.akamaized.net/3hVTEoX_tJ0lFq3_q8NoQB0hmZI=/1280x720/smart/filters:quality(75):strip_icc():format(webp)/kly-media-production/medias/3133148/original/027517900_1589942784-Raw_Fresh_Meat_With_Rosemary.jpg",
                    title = "Daging Segar",
                    isChecked = false
                ),
                RecipeSearchIngredientUiModel(
                    id = "125",
                    imgUrl = "https://cdn1-production-images-kly.akamaized.net/3hVTEoX_tJ0lFq3_q8NoQB0hmZI=/1280x720/smart/filters:quality(75):strip_icc():format(webp)/kly-media-production/medias/3133148/original/027517900_1589942784-Raw_Fresh_Meat_With_Rosemary.jpg",
                    title = "Daging Aja",
                    isChecked = false
                ),
                RecipeSearchIngredientUiModel(
                    id = "123",
                    imgUrl = "https://cdn1-production-images-kly.akamaized.net/3hVTEoX_tJ0lFq3_q8NoQB0hmZI=/1280x720/smart/filters:quality(75):strip_icc():format(webp)/kly-media-production/medias/3133148/original/027517900_1589942784-Raw_Fresh_Meat_With_Rosemary.jpg",
                    title = "Daging Mentah",
                    isChecked = false
                ),
                RecipeSearchIngredientUiModel(
                    id = "124",
                    imgUrl = "https://cdn1-production-images-kly.akamaized.net/3hVTEoX_tJ0lFq3_q8NoQB0hmZI=/1280x720/smart/filters:quality(75):strip_icc():format(webp)/kly-media-production/medias/3133148/original/027517900_1589942784-Raw_Fresh_Meat_With_Rosemary.jpg",
                    title = "Daging Segar",
                    isChecked = false
                ),
                RecipeSearchIngredientUiModel(
                    id = "125",
                    imgUrl = "https://cdn1-production-images-kly.akamaized.net/3hVTEoX_tJ0lFq3_q8NoQB0hmZI=/1280x720/smart/filters:quality(75):strip_icc():format(webp)/kly-media-production/medias/3133148/original/027517900_1589942784-Raw_Fresh_Meat_With_Rosemary.jpg",
                    title = "Daging Aja",
                    isChecked = false
                ),
                RecipeSearchIngredientUiModel(
                    id = "123",
                    imgUrl = "https://cdn1-production-images-kly.akamaized.net/3hVTEoX_tJ0lFq3_q8NoQB0hmZI=/1280x720/smart/filters:quality(75):strip_icc():format(webp)/kly-media-production/medias/3133148/original/027517900_1589942784-Raw_Fresh_Meat_With_Rosemary.jpg",
                    title = "Daging Mentah",
                    isChecked = false
                ),
                RecipeSearchIngredientUiModel(
                    id = "124",
                    imgUrl = "https://cdn1-production-images-kly.akamaized.net/3hVTEoX_tJ0lFq3_q8NoQB0hmZI=/1280x720/smart/filters:quality(75):strip_icc():format(webp)/kly-media-production/medias/3133148/original/027517900_1589942784-Raw_Fresh_Meat_With_Rosemary.jpg",
                    title = "Daging Segar",
                    isChecked = false
                ),
                RecipeSearchIngredientUiModel(
                    id = "125",
                    imgUrl = "https://cdn1-production-images-kly.akamaized.net/3hVTEoX_tJ0lFq3_q8NoQB0hmZI=/1280x720/smart/filters:quality(75):strip_icc():format(webp)/kly-media-production/medias/3133148/original/027517900_1589942784-Raw_Fresh_Meat_With_Rosemary.jpg",
                    title = "Daging Aja",
                    isChecked = false
                ),
                RecipeSearchIngredientUiModel(
                    id = "123",
                    imgUrl = "https://cdn1-production-images-kly.akamaized.net/3hVTEoX_tJ0lFq3_q8NoQB0hmZI=/1280x720/smart/filters:quality(75):strip_icc():format(webp)/kly-media-production/medias/3133148/original/027517900_1589942784-Raw_Fresh_Meat_With_Rosemary.jpg",
                    title = "Daging Mentah",
                    isChecked = false
                ),
                RecipeSearchIngredientUiModel(
                    id = "124",
                    imgUrl = "https://cdn1-production-images-kly.akamaized.net/3hVTEoX_tJ0lFq3_q8NoQB0hmZI=/1280x720/smart/filters:quality(75):strip_icc():format(webp)/kly-media-production/medias/3133148/original/027517900_1589942784-Raw_Fresh_Meat_With_Rosemary.jpg",
                    title = "Daging Segar",
                    isChecked = false
                ),
                RecipeSearchIngredientUiModel(
                    id = "125",
                    imgUrl = "https://cdn1-production-images-kly.akamaized.net/3hVTEoX_tJ0lFq3_q8NoQB0hmZI=/1280x720/smart/filters:quality(75):strip_icc():format(webp)/kly-media-production/medias/3133148/original/027517900_1589942784-Raw_Fresh_Meat_With_Rosemary.jpg",
                    title = "Daging Aja",
                    isChecked = false
                )
            )
            adapter.submitList(recipes)
            // get how many items are checked
            recipes.filter { it.isChecked }.map { it.id }.toCollection(recipeIdsChecked)
            showHideSearchButton(this)

            tpSubtitle.text = getString(R.string.tokopedianow_recipe_search_ingredient_subtitle_bottomsheet)
            rvIngredients.layoutManager = LinearLayoutManager(context)
            rvIngredients.adapter = adapter
        }
    }

    private fun recipeSearchIngredientCallback() = object : RecipeSearchIngredientViewHolder.RecipeSearchIngredientListener {
        override fun onCheckRecipe(isChecked: Boolean, id: String) {
            binding?.apply {
                if (isChecked) {
                    recipeIdsChecked.add(id)
                } else {
                    recipeIdsChecked.remove(id)
                }
                showHideSearchButton(this)
            }
        }
    }

    private fun showHideSearchButton(binding: BottomsheetTokopedianowRecipeSearchIngredientBinding) {
        binding.apply {
            val isShown = recipeIdsChecked.size.isMoreThanZero()
            if (isShown) {
                ubSearchBtn.setOnClickListener {
                    clickSearchBtn()
                }
                ubSearchBtn.text = getString(R.string.tokopedianow_recipe_search_ingredient_button_bottomsheet, recipeIdsChecked.size)
                ubSearchBtn.show()
                cvSearchBtnBackground.show()
            } else {
                ubSearchBtn.hide()
                cvSearchBtnBackground.hide()
            }
        }
    }

    private fun clickSearchBtn() {
        val intent = Intent()
        intent.putStringArrayListExtra(EXTRA_SEARCH_BY_INGREDIENT_IDS, recipeIdsChecked)
        activity?.run {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}